package com.bayu.bhinneka.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bayu.bhinneka.data.model.History
import com.bayu.bhinneka.data.repository.FirebaseRepository
import com.google.firebase.auth.FirebaseUser

class MainViewModel: ViewModel() {
    private val repository = FirebaseRepository()

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