package com.bayu.bhinneka.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions
import com.google.firebase.ml.modeldownloader.DownloadType
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.common.TensorOperator
import org.tensorflow.lite.support.common.TensorProcessor
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp
import org.tensorflow.lite.support.label.TensorLabel
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.util.Collections
import kotlin.math.min


class TFLiteInitiator(
    private val context: Context
) {

    private lateinit var model: Interpreter
    private lateinit var inputImageBuffer: TensorImage

    private lateinit var outputProbabilityBuffer: TensorBuffer
    private lateinit var probabilityProcessor: TensorProcessor

    private var imageSizeX: Int = 0
    private var imageSizeY: Int = 0
    private var labels: List<String> = listOf()

    fun init(isSuccessful: (Boolean) -> Unit) {
        val conditions = CustomModelDownloadConditions.Builder()
            .build()

        FirebaseModelDownloader.getInstance()
            .getModel(MODEL_NAME, DownloadType.LOCAL_MODEL_UPDATE_IN_BACKGROUND, conditions)
            .addOnSuccessListener { model ->

                val modelFile = model.file
                val opt = Interpreter.Options()
                if (modelFile != null) {
                    this.model = Interpreter(modelFile, opt)
                    Log.d(TAG, "Downloaded")
                    isSuccessful(true)
                } else {
                    Log.e(TAG, "Failed")
                    isSuccessful(false)
                }
            }
    }

    fun classifyImage(bitmap: Bitmap?) {
        val imageTensorIndex = 0
        val imageShape: IntArray =
            model.getInputTensor(imageTensorIndex).shape() // {1, height, width, 3}
        imageSizeY = imageShape[1]
        imageSizeX = imageShape[2]
        val imageDataType: DataType = model.getInputTensor(imageTensorIndex).dataType()

        val probabilityTensorIndex = 0
        val probabilityShape: IntArray =
            model.getOutputTensor(probabilityTensorIndex).shape() // {1, NUM_CLASSES}
        val probabilityDataType: DataType =
            model.getOutputTensor(probabilityTensorIndex).dataType()

        inputImageBuffer = TensorImage(imageDataType)
        outputProbabilityBuffer =
            TensorBuffer.createFixedSize(probabilityShape, probabilityDataType)
        probabilityProcessor = TensorProcessor.Builder().add(getPreprocessNormalizeOp()).build()

        inputImageBuffer = loadImage(bitmap!!)

        model.run(inputImageBuffer.buffer, outputProbabilityBuffer.buffer.rewind())
    }

    fun showResult(): Array<String?> {
        try {
            labels = FileUtil.loadLabels(context, "labels.txt")
        } catch (e: Exception) {
            e.printStackTrace()
            return arrayOf()
        }
        val labeledProbability =
            TensorLabel(labels, probabilityProcessor.process(outputProbabilityBuffer))
                .mapWithFloatValue
        val maxValueInMap = (Collections.max(labeledProbability.values))
        var resultLabel: String? = null
        for ((key, value) in labeledProbability) {
            if (value == maxValueInMap) {
                resultLabel = key
            }
        }

        return arrayOf(resultLabel, labeledProbability.getValue(resultLabel).toString())
    }

    private fun loadImage(bitmap: Bitmap): TensorImage {
        inputImageBuffer.load(bitmap)

        val cropSize = min(bitmap.width, bitmap.height)
        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeWithCropOrPadOp(cropSize, cropSize))
            .add(ResizeOp(imageSizeX, imageSizeY, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
            .add(getPreprocessNormalizeOp())
            .build()

        return imageProcessor.process(inputImageBuffer)
    }



    private fun getPreprocessNormalizeOp(): TensorOperator {
        return NormalizeOp(IMAGE_MEAN, IMAGE_STD)
    }


    companion object {
        private const val TAG = "TFLiteHelper"
        private const val MODEL_NAME = "MobileNet"

        private const val PROBABILITY_MEAN = 0.0f
        private const val PROBABILITY_STD = 255.0f

        private const val IMAGE_MEAN = 0.0f
        private const val IMAGE_STD = 1.0f
    }
}