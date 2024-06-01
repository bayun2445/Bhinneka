package com.bayu.bhinneka.ui.edit_jajanan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bayu.bhinneka.data.model.Jajanan
import com.bayu.bhinneka.data.repository.Repository

class EditJajananViewModel: ViewModel() {

    private val repository = Repository()

    private val _isLoading = MutableLiveData<Boolean>()
    private val _isSuccessful = MutableLiveData<Boolean>()

    val isLoading: LiveData<Boolean> = _isLoading
    val isSuccessful: LiveData<Boolean> = _isSuccessful

    fun updateJajanan(jajanan: Jajanan) {
        _isLoading.value = true
        repository.updateJajanan(jajanan) {
            _isLoading.value = false
            _isSuccessful.value = it
        }
    }
}