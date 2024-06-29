package com.bayu.bhinneka.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bayu.bhinneka.data.model.Jajanan
import com.bayu.bhinneka.data.repository.Repository
import com.google.firebase.auth.FirebaseUser

class MainViewModel: ViewModel() {
    private val repository = Repository()

    fun getCurrentUser(): FirebaseUser? {
        return repository.getCurrentUser()
    }

    fun signOut() {
        repository.signOut()
    }
}