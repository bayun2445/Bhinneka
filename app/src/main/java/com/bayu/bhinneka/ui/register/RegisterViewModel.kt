package com.bayu.bhinneka.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bayu.bhinneka.data.repository.Repository
import com.google.firebase.auth.FirebaseUser

class RegisterViewModel: ViewModel() {

    private val repository = Repository()

    private val _currentUser = MutableLiveData<FirebaseUser?>()
    private val _message = MutableLiveData<String?>()

    val currentUser: LiveData<FirebaseUser?> = _currentUser
    val message: LiveData<String?> = _message

    fun registerWithEmail(email: String, password: String) {
        repository.registerWithEmailAndPassword(email, password) { user, error ->
            _currentUser.value = user

            if (error.isNullOrEmpty()) {
                _message.value = error
            } else {
                _message.value = "Login berhasil!"
            }
        }
    }

    fun getRole(): Boolean {
        return repository.getRole()
    }
}