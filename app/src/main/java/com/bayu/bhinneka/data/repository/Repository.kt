package com.bayu.bhinneka.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bayu.bhinneka.data.model.Jajanan
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class Repository {
    private val database = Firebase.database.reference

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
                Log.e("Repository", "Failed to load jajananList", error.toException())
            }

        })

        return liveData
    }
    fun addNewJajanan(jajanan: Jajanan) {
        database.child(CHILD_JAJANAN).child(jajanan.name).setValue(jajanan)
    }

    fun updateJajanan(jajanan: Jajanan) {
        database.child(CHILD_JAJANAN).child(jajanan.name).setValue(jajanan)
    }

    fun deleteJajanan(jajanan: Jajanan) {
        database.child(CHILD_JAJANAN).child(jajanan.name).removeValue()
    }
    companion object {
        private const val CHILD_JAJANAN = "jajanan"
    }
}