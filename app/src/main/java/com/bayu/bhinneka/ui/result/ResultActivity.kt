package com.bayu.bhinneka.ui.result

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bayu.bhinneka.R
import com.bayu.bhinneka.databinding.ActivityResultBinding
import com.bayu.bhinneka.helper.IMAGE_URI_EXTRA

class ResultActivity : AppCompatActivity() {

    private var image: Bitmap? = null

    private val binding by lazy {
        ActivityResultBinding.inflate(layoutInflater)
    }

    private val viewModel: ResultViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.prediction_result)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (intent.data != null) {
            val intentImageResult = intent.getByteArrayExtra(IMAGE_URI_EXTRA)
            if (intentImageResult != null) {
                image = BitmapFactory.decodeByteArray(
                    intentImageResult,
                    0,
                    intentImageResult.size
                )
            }
        }

        image?.let {
            binding.imgResult.setImageBitmap(it)
        }



    }
}