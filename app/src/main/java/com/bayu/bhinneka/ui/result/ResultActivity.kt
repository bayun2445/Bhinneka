package com.bayu.bhinneka.ui.result

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bayu.bhinneka.R
import com.bayu.bhinneka.data.model.History
import com.bayu.bhinneka.data.model.Jajanan
import com.bayu.bhinneka.databinding.ActivityResultBinding
import com.bayu.bhinneka.helper.IMAGE_EXTRA
import com.bayu.bhinneka.helper.generateId
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

    }

    private fun observeViewModel() {
        viewModel.isInitSuccessful.observe(this) {
            imgBitmap?.let { bitmap ->
                viewModel.classifyImage(bitmap)
            }
        }

        viewModel.result.observe(this) { name ->
            binding.txtLabelResult.text = name
            if (name != null) {
                viewModel.getJajanan(name)
                imgBitmap?.let {
                    viewModel.uploadImage(it)
                }
            }
        }

        viewModel.imgPath.observe(this) {
            saveHistory(
                binding.txtLabelResult.text.toString(),
                it
            )
        }

        viewModel.jajananResult.observe(this) { jajanan ->
            jajanan?.let {
                loadJajanan(it)
                Log.d(TAG, jajanan.toString())
            }
        }

        viewModel.isLoading.observe(this) {
            if (it) {
                binding.cvLoading.visibility = View.VISIBLE
            } else {
                binding.cvLoading.visibility = View.GONE
            }
        }

        // Removable
        viewModel.output.observe(this) {
            binding.txtOutput.text = it
        }
    }

    private fun saveHistory(name: String, imgPath: String?) {
        val historyId = generateId()

        val newHistory = History(
            id = historyId,
            timeStamp = System.currentTimeMillis(),
            resultJajananName = name,
            imgPath = imgPath
        )

        viewModel.addNewHistory(newHistory)
    }

    private fun loadJajanan(jajanan: Jajanan) {
        // Todo: set update UI to load jajanan
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