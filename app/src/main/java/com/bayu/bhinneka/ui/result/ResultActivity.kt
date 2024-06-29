package com.bayu.bhinneka.ui.result

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bayu.bhinneka.R
import com.bayu.bhinneka.databinding.ActivityResultBinding
import com.bayu.bhinneka.helper.IMAGE_EXTRA
import java.io.File

class ResultActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityResultBinding.inflate(layoutInflater)
    }

    private var imgBitmap: Bitmap? = null

    private val viewModel: ResultViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        setSupportActionBar(binding.topBarMenu)
        supportActionBar?.title = getString(R.string.prediction_result)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (intent.hasExtra(IMAGE_EXTRA)) {
            val intentImageResult = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.extras?.getSerializable(IMAGE_EXTRA, File::class.java) as File
            } else {
                intent.extras?.getSerializable(IMAGE_EXTRA) as File
            }

            Log.w(TAG, intentImageResult.toString())
            imgBitmap = BitmapFactory.decodeFile(intentImageResult.path)
            binding.imgResult.setImageBitmap(imgBitmap)
        } else {
            Toast.makeText(this, "Error: no image passed", Toast.LENGTH_SHORT).show()
            finish()
        }

        viewModel.init(this)

        observeViewModel()

//        viewModel.initModel(this)
//        viewModel.classifyImageData(imgBitmap!!)
//
//        viewModel.result.observe(this) {
//            binding.txtLabelResult.text = it?.get(0)
//            binding.txtOutput.text = it?.get(1)
//        }


    }

    private fun observeViewModel() {
        viewModel.isSuccessful.observe(this) {
            imgBitmap?.let { bitmap -> viewModel.classifyImage(bitmap) }
        }

        viewModel.result.observe(this) {
            binding.txtLabelResult.text = it
        }

        viewModel.output.observe(this) {
            binding.txtOutput.text = it
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.common_top_menu, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    companion object {
        private val TAG = ResultActivity::class.java.simpleName
    }
}