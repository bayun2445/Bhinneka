package com.bayu.bhinneka.data.repository

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bayu.bhinneka.data.model.History
import com.bayu.bhinneka.data.model.Jajanan
import com.bayu.bhinneka.helper.generateId
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GetTokenResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.storage.storage
import java.io.ByteArrayOutputStream

class Repository {
    private val database = Firebase.database.reference
    private val auth = FirebaseAuth.getInstance()
    private val storage = Firebase.storage.reference

    // Authentications
    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    private fun getUID(): String = getCurrentUser()?.uid!!

    fun signInWithGoogle(
        idToken: String,
        user: (FirebaseUser?) -> Unit
    ) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("Google SignIn", "signInWithCredential:success")
                    user(getCurrentUser())
                } else {
                    Log.w("Google SignIn", "signInWithCredential:failure", it.exception)
                    user(null)
                }
            }
    }

    fun getRole(isAdmin: (Boolean) -> Unit) {

        database.child(CHILD_USERS).child(getUID()).child("role").get()
            .addOnSuccessListener {
                val role = it.getValue(String::class.java)

                if (role == "admin") {
                    isAdmin(true)
                } else {
                    isAdmin(false)
                }
            }
    }

    fun registerWithEmailAndPassword(
        email: String,
        password: String,
        result: (user: FirebaseUser?, error: String?) -> Unit,
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Email SignUp", "Success")
                    result(getCurrentUser(), null)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Email SignUp", "Failure:", it.exception)

                    when (it.exception) {
                        is FirebaseAuthInvalidCredentialsException -> {
                            result(
                                null,
                                "Email atau password salah!",
                            )
                        }
                        is FirebaseAuthUserCollisionException -> {
                            result(
                                null,
                                "Email sudah terdaftar untuk akun lain!",
                            )
                        }
                        else -> {
                            result(
                                null,
                                "Unknown error",
                            )
                        }
                    }
                }
            }
    }

    fun signInWithEmailAndPassword(
        email: String,
        password: String,
        result: (user: FirebaseUser?, error: String?) -> Unit,
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Email SignIn", "Success")
                    result(getCurrentUser(), null)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Email SignIn", "Failure:", it.exception)

                    when (it.exception) {
                        is FirebaseAuthInvalidCredentialsException -> {
                            result(
                                null,
                                "Email atau password salah!",
                            )
                        }

                        is FirebaseAuthInvalidUserException -> {
                            result(
                                null,
                                "Email belum terdaftar!",
                            )
                        }

                        else -> {
                            result(
                                null,
                                "Unknown error",
                            )
                        }
                    }
                }
            }
    }

    fun signOut() {
        auth.signOut()
    }


    // Database

    //--Jajanan
    fun getAllJajanan(): LiveData<List<Jajanan>> {
        val liveData = MutableLiveData<List<Jajanan>>()

        database.child(CHILD_JAJANAN).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val jajananList = mutableListOf<Jajanan>()
                for (child in snapshot.children) {
                    val jajanan = child.getValue(Jajanan::class.java)
                    if (jajanan != null) {
                        jajananList.add(jajanan)
                    }
                }

                liveData.value = jajananList
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Repository", "Failed to load jajananList: ", error.toException())
            }

        })

        return liveData
    }

    fun getJajanan(name: String): LiveData<Jajanan?> {
        val liveData = MutableLiveData<Jajanan?>()

        database.child(CHILD_JAJANAN).child(name).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d(TAG, snapshot.value.toString())
                val jajanan = snapshot.getValue(Jajanan::class.java)
                liveData.value = jajanan


//                val jajananList = mutableListOf<Jajanan>()
//                for (child in snapshot.children) {
//                    val jajanan = child.getValue(Jajanan::class.java)
//                    if (jajanan != null) {
//                        jajananList.add(jajanan)
//                    }
//                }
//
//                liveData.value = jajananList
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Repository", "Failed to load jajananList: ", error.toException())
            }
        })
//            .child(name).get()
//            .addOnSuccessListener {
//                val jajananData = it.getValue(Jajanan::class.java)
//                Log.d(TAG, it.value.toString())
//                Log.d(TAG, jajananData.toString())
//                jajanan = jajananData
//            }
//            .addOnFailureListener {
//                Log.e(TAG, it.message.toString())
//            }

        return liveData
    }

    fun addNewJajanan(jajanan: Jajanan, isSuccessful: (Boolean) -> Unit) {
        database.child(CHILD_JAJANAN).child(jajanan.name).setValue(jajanan)
            .addOnCompleteListener { task ->
                isSuccessful(task.isSuccessful)
            }
    }

    fun updateJajanan(jajanan: Jajanan, isSuccessful: (Boolean) -> Unit) {
        database.child(CHILD_JAJANAN).child(jajanan.name).setValue(jajanan)
            .addOnCompleteListener { task ->
                isSuccessful(task.isSuccessful)
            }
    }

    fun deleteJajanan(jajanan: Jajanan, isSuccessful: (Boolean) -> Unit) {
        database.child(CHILD_JAJANAN).child(jajanan.name).removeValue()
            .addOnCompleteListener { task ->
                isSuccessful(task.isSuccessful)
            }
    }

    //--History

    fun getAllHistory(): LiveData<List<History>?> {
        val liveData = MutableLiveData<List<History>>()

        database.child(CHILD_HISTORY).child(getUID()).orderByChild("timeStamp").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val historyList = mutableListOf<History>()
                Log.d(TAG, getUID())
                Log.d(TAG, snapshot.value.toString())
                for (child in snapshot.children) {
                    Log.d(TAG, child.value.toString())
                    val history = child.getValue(History::class.java)
                    if (history != null) {
                        historyList.add(history)
                    }
                }

                liveData.value = historyList.reversed()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "Failed to load history List: ", error.toException())
            }

        })

        return liveData
    }

    fun addNewHistory(history: History) {
        database.child(CHILD_HISTORY).child(getUID()).child(history.id).setValue(history)
    }

    //--Storage

    //--Jajanan

    fun uploadJajananImage(
        bitmap: Bitmap,
        name: String,
        result: (Boolean, String?) -> Unit
    ) {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val data = outputStream.toByteArray()

        val ref = storage.child(CHILD_STORAGE_ASSET).child(name)
        val uploadTask = ref.putBytes(data)

        uploadTask
            .continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                ref.downloadUrl
            }
            .addOnCompleteListener {
                result(it.isSuccessful, it.result.toString())
            }
    }

    fun deleteJajananImage(name: String) {
        storage.child(CHILD_STORAGE_ASSET).child(name)
            .delete()
    }

    //--History

    fun uploadHistoryImage(
        bitmap: Bitmap,
        result: (Boolean, String?) -> Unit
    ) {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val data = outputStream.toByteArray()

        val ref = storage.child(CHILD_STORAGE_HISTORY).child(getUID()).child(generateId(6))
        val uploadTask = ref.putBytes(data)

        uploadTask
            .continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                ref.downloadUrl
            }
            .addOnCompleteListener {
                result(it.isSuccessful, it.result.toString())
            }
    }

    companion object {
        private const val TAG = "Repository"
        private const val CHILD_USERS = "users"
        private const val CHILD_JAJANAN = "jajanan"
        private const val CHILD_HISTORY = "history"
        private const val CHILD_STORAGE_ASSET = "assets"
        private const val CHILD_STORAGE_HISTORY = "history_images"
    }
}