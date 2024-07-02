package com.bayu.bhinneka.ui.list_jajanan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bayu.bhinneka.data.model.Jajanan
import com.bayu.bhinneka.data.repository.Repository

class ListJajananViewModel: ViewModel() {

    private val repository = Repository()

    private val _isLoading = MutableLiveData<Boolean>()
    private val _isSuccessful = MutableLiveData<Boolean>()

    val isLoading: LiveData<Boolean> = _isLoading
    val isSuccessful: LiveData<Boolean> = _isSuccessful

    fun getAllJajanan(): LiveData<List<Jajanan>> {
        return repository.getAllJajanan()
    }
    fun deleteJajanan(jajanan: Jajanan) {
        _isLoading.value = true
        repository.deleteJajanan(jajanan) {
            _isLoading.value = false
            _isSuccessful.value = it
        }

        repository.deleteJajananImage("img-${jajanan.name}")
    }
}