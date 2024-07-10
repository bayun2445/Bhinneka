package com.bayu.bhinneka.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bayu.bhinneka.data.repository.FirebaseRepository
import com.google.firebase.auth.FirebaseUser

class SplashViewModel : ViewModel() {

    private val repository = FirebaseRepository()

    private val _currentUser = MutableLiveData<FirebaseUser?>()

    val currentUser: LiveData<FirebaseUser?> = _currentUser
    init {
        _currentUser.value = repository.getCurrentUser()
    }

    fun getRole(): LiveData<String?> {
        return repository.getRole()
    }
}