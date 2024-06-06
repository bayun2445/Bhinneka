package com.bayu.bhinneka.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bayu.bhinneka.data.repository.Repository
import com.google.firebase.auth.FirebaseUser

class LoginViewModel: ViewModel() {

    private val repository = Repository()

    private val _currentUser = MutableLiveData<FirebaseUser?>()

    val currentUser: LiveData<FirebaseUser?> = _currentUser

    init {
        _currentUser.value = repository.getCurrentUser()
    }

    fun signInWithGoogle(idToken: String) {
        repository.signInWithGoogle(idToken) { user ->
            _currentUser.value = user
        }
    }

}