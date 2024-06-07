package com.bayu.bhinneka.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bayu.bhinneka.data.repository.Repository
import com.google.firebase.auth.FirebaseUser

class RegisterViewModel: ViewModel() {

    private val repository = Repository()

    private val _currentUser = MutableLiveData<FirebaseUser?>()

    val currentUser: LiveData<FirebaseUser?> = _currentUser

    fun registerWithEmail(email: String, password: String) {
        repository.registerWithEmailAndPassword(email, password) { user, error ->
            _currentUser.value = user
        }
    }
}