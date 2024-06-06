package com.bayu.bhinneka.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bayu.bhinneka.data.model.Jajanan
import com.bayu.bhinneka.ui.login.LoginActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class Repository {
    private val database = Firebase.database.reference
    private val auth = FirebaseAuth.getInstance()

    fun getCurrentUser(): FirebaseUser? = auth.currentUser
    fun signInWithGoogle(idToken: String, user: (FirebaseUser?) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Google SignIn", "signInWithCredential:success")
                    user(auth.currentUser)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Goog leSignIn", "signInWithCredential:failure", it.exception)
                    user(null)
                }
            }
    }

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
    companion object {
        private const val CHILD_JAJANAN = "jajanan"
    }
}