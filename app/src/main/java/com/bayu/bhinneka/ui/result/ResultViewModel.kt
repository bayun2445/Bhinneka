package com.bayu.bhinneka.ui.result

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bayu.bhinneka.data.repository.Repository
import com.bayu.bhinneka.helper.TFLiteHelper
import com.bayu.bhinneka.ml.ModelMobilenetv3smallV1
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

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
}