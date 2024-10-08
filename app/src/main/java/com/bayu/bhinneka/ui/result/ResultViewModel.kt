package com.bayu.bhinneka.ui.result

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bayu.bhinneka.data.model.History
import com.bayu.bhinneka.data.model.Jajanan
import com.bayu.bhinneka.data.repository.FirebaseRepository
import com.bayu.bhinneka.data.repository.PreferencesRepository
import com.bayu.bhinneka.data.repository.TFLiteInitiator

class ResultViewModel: ViewModel() {

    private val repository = FirebaseRepository()
    private lateinit var tfLiteHelper: TFLiteInitiator
    private lateinit var preferences: PreferencesRepository

    private val _isLoading = MutableLiveData<Boolean>()
    private val _isInitSuccessful = MutableLiveData<Boolean?>()
    private val _result = MutableLiveData<Array<String?>>()
    private val _imgPath = MutableLiveData<String?>()

    val isLoading: LiveData<Boolean> = _isLoading
    val isInitSuccessful: LiveData<Boolean?> = _isInitSuccessful
    val result: LiveData<Array<String?>> = _result
    val imgPath: LiveData<String?> = _imgPath


    fun init(context: Context) {
        _isLoading.value = true
        preferences = PreferencesRepository.getInstance(context)
        tfLiteHelper = TFLiteInitiator(context)
        tfLiteHelper.init {
            _isInitSuccessful.value = it
        }
    }

    private fun getCurrentUser() = repository.getCurrentUser()

    fun classifyImage(bitmap: Bitmap) {
        tfLiteHelper.classifyImage(bitmap)

        val resultArray = tfLiteHelper.showResult()
        _result.value = resultArray
    }

    fun getJajanan(name: String): LiveData<Jajanan?> {
        return repository.getJajanan(name)
    }

    fun addNewHistory(history: History) {
        if (getCurrentUser() != null) {
            repository.addNewHistory(history)
        }
        _isLoading.value = false
    }

    fun uploadImage(bitmap: Bitmap) {
        if (getCurrentUser() != null) {
            repository.uploadHistoryImage(bitmap) { _, path ->
                _imgPath.value = path
            }
        } else {
            _imgPath.value = "guest"
        }
    }

    fun getResult(): Float {
        return preferences.readResult()
    }
}