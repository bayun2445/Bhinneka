package com.bayu.bhinneka.ui.edit_jajanan

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bayu.bhinneka.data.model.Jajanan
import com.bayu.bhinneka.data.repository.Repository

class EditJajananViewModel: ViewModel() {

    private val repository = Repository()

    private val _isLoading = MutableLiveData<Boolean>()
    private val _isSuccessful = MutableLiveData<Boolean>()
    private val _isUploadSuccessful = MutableLiveData<Boolean>()
    private val _imagePath = MutableLiveData<String>()

    val isLoading: LiveData<Boolean> = _isLoading
    val isSuccessful: LiveData<Boolean> = _isSuccessful
    val isUploadSuccessful: LiveData<Boolean> = _isUploadSuccessful
    val imagePath: LiveData<String> = _imagePath

    fun updateJajanan(jajanan: Jajanan) {
        _isLoading.value = true
        repository.updateJajanan(jajanan) {
            _isLoading.value = false
            _isSuccessful.value = it
        }
    }

    fun uploadImage(bitmap: Bitmap, name: String) {
        repository.uploadJajananImage(bitmap, name) { isSuccess, path ->
            _isUploadSuccessful.value = isSuccess
            path?.let {
                _imagePath.value = it
                Log.d("EditJajananViewModel", it)
            }
        }
    }
}