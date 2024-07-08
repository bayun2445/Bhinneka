package com.bayu.bhinneka.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bayu.bhinneka.data.repository.Repository
import com.google.firebase.auth.FirebaseUser

class LoginViewModel: ViewModel() {

    private val repository = Repository()

    private val _currentUser = MutableLiveData<FirebaseUser?>()
    private val _message = MutableLiveData<String?>()

    val currentUser: LiveData<FirebaseUser?> = _currentUser
    val message: LiveData<String?> = _message

    init {
        _currentUser.value = repository.getCurrentUser()
    }

    fun signInWithGoogle(idToken: String) {
        repository.signInWithGoogle(idToken) { user ->
            _currentUser.value = user
        }
    }

    fun signInWithEmail(email: String, password: String) {
        repository.signInWithEmailAndPassword(email, password) { user, error ->
            _currentUser.value = user

            if (!error.isNullOrEmpty()) {
                _message.value = "Login berhasil!"
            } else {
                _message.value = error
            }
        }
    }

    fun getRole(): Boolean {
        return repository.getRole()
    }

}