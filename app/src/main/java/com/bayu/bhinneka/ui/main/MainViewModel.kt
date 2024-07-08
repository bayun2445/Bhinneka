package com.bayu.bhinneka.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bayu.bhinneka.data.model.History
import com.bayu.bhinneka.data.repository.Repository
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot

class MainViewModel: ViewModel() {
    private val repository = Repository()

    fun getCurrentUser(): FirebaseUser? {
        return repository.getCurrentUser()
    }


    fun signOut() {
        repository.signOut()
    }

    fun getAllHistory(): LiveData<List<History>?> {
        return repository.getAllHistory()
    }
}