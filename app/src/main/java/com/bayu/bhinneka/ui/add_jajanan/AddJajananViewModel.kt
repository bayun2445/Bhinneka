package com.bayu.bhinneka.ui.add_jajanan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bayu.bhinneka.data.model.Jajanan
import com.bayu.bhinneka.data.repository.Repository

class AddJajananViewModel: ViewModel() {

    private val repository = Repository()

    private val _isLoading = MutableLiveData<Boolean>()
    private val _isSuccessful = MutableLiveData<Boolean>()

    val isLoading: LiveData<Boolean> = _isLoading
    val isSuccessful: LiveData<Boolean> = _isSuccessful

    fun addNewJajanan(jajanan: Jajanan) {
        _isLoading.value = true
        return repository.addNewJajanan(jajanan) { isSuccess ->
            _isSuccessful.value = isSuccess
            _isLoading.value = false
        }
    }
}