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

    private val _isSuccessful = MutableLiveData<Boolean?>()
    private val _result = MutableLiveData<String?>()
    private val _output = MutableLiveData<String?>()

    val isSuccessful: LiveData<Boolean?> = _isSuccessful
    val result: LiveData<String?> = _result
    val output: LiveData<String?> = _output


    fun init(context: Context) {
        tfLiteHelper = TFLiteHelper(context)
        tfLiteHelper.init {
            _isSuccessful.value = it
        }
    }

    fun classifyImage(bitmap: Bitmap) {
        tfLiteHelper.classifyImage(bitmap)

        val resultArray = tfLiteHelper.showResult()
        _result.value = resultArray[0]
        _output.value = resultArray[1]
    }

    fun getJajanan(name: String): LiveData<Jajanan?> {
        return repository.getJajanan(name)
    }

    fun addNewHistory(history: History) {
        repository.addNewHistory(history)
    }
}