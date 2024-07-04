package com.bayu.bhinneka.ui.result

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bayu.bhinneka.data.model.History
import com.bayu.bhinneka.data.model.Jajanan
import com.bayu.bhinneka.data.repository.Repository
import com.bayu.bhinneka.helper.TFLiteHelper

class ResultViewModel: ViewModel() {

    private val repository = Repository()
    private lateinit var tfLiteHelper: TFLiteHelper

    private val _isLoading = MutableLiveData<Boolean>()
    private val _isInitSuccessful = MutableLiveData<Boolean?>()
    private val _result = MutableLiveData<String?>()
    private val _output = MutableLiveData<String?>()
    private val _jajananResult = MutableLiveData<Jajanan?>()
    private val _imgPath = MutableLiveData<String?>()

    val isLoading: LiveData<Boolean> = _isLoading
    val isInitSuccessful: LiveData<Boolean?> = _isInitSuccessful
    val result: LiveData<String?> = _result
    val output: LiveData<String?> = _output
    val jajananResult: LiveData<Jajanan?> = _jajananResult
    val imgPath: LiveData<String?> = _imgPath


    fun init(context: Context) {
        _isLoading.value = true
        tfLiteHelper = TFLiteHelper(context)
        tfLiteHelper.init {
            _isInitSuccessful.value = it
        }
    }

    fun classifyImage(bitmap: Bitmap) {
        tfLiteHelper.classifyImage(bitmap)

        val resultArray = tfLiteHelper.showResult()
        _result.value = resultArray[0]
        _output.value = resultArray[1]
    }

    fun getJajanan(name: String) {
        _jajananResult.value = repository.getJajanan(name)
    }

    fun addNewHistory(history: History) {
        repository.addNewHistory(history)
        _isLoading.value = false
    }

    fun uploadImage(bitmap: Bitmap) {
        repository.uploadHistoryImage(bitmap) { _, path ->
            _imgPath.value = path
        }
    }
}