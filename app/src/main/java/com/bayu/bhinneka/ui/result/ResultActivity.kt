package com.bayu.bhinneka.ui.result

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.text.LineBreaker
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
import com.bayu.bhinneka.component.setResizableText
import com.bayu.bhinneka.data.model.History
import com.bayu.bhinneka.data.model.Jajanan
import com.bayu.bhinneka.databinding.ActivityResultBinding
import com.bayu.bhinneka.helper.IMAGE_EXTRA
import com.bayu.bhinneka.helper.generateId
import com.bayu.bhinneka.helper.toPercent
import com.bayu.bhinneka.ui.metabolic_rate.MetabolicPreferenceActivity
import java.io.File
import kotlin.math.roundToInt

class ResultActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityResultBinding.inflate(layoutInflater)
    }

    private var imgBitmap: Bitmap? = null
    private var matchScore: Float? = 0f

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

        binding.txtCalculate.setOnClickListener {
            startActivity(Intent(this, MetabolicPreferenceActivity::class.java))
        }

    }

    private fun observeViewModel() {
        viewModel.isInitSuccessful.observe(this) {
            imgBitmap?.let { bitmap ->
                viewModel.classifyImage(bitmap)
            }
        }

        viewModel.result.observe(this) { resultArray ->
            val name = resultArray[0]
            matchScore = resultArray[1]?.toFloat()
            binding.txtLabelResult.text = name
            binding.txtOutput.text = buildString {
                append("Tingkat kecocokan: ")
                append(matchScore?.toPercent())
            }
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

        viewModel.isLoading.observe(this) {
            if (it) {
                binding.cvLoading.visibility = View.VISIBLE
                binding.container.visibility = View.INVISIBLE
            } else {
                binding.cvLoading.visibility = View.GONE
                binding.container.visibility = View.VISIBLE
            }
        }
    }

    private fun saveHistory(name: String, imgPath: String?) {

        viewModel.getJajanan(name).observe(this) { jajanan ->
            Log.d(TAG, jajanan.toString())
            jajanan?.let {
                loadJajanan(it)
                loadPreferences()
            }
        }
        val historyId = generateId()

        val newHistory = History(
            id = historyId,
            timeStamp = System.currentTimeMillis(),
            resultJajananName = name,
            imgPath = imgPath,
            matchScore = matchScore
        )

        viewModel.addNewHistory(newHistory)

    }

    private fun loadPreferences() {
        binding.apply {
            val metabolicPercentage =
                txtCalorie.text.toString()
                    .replace("kkal", "")
                    .toFloat() / viewModel.getResult()

            if (viewModel.getResult() == 0f) {
                txtCalculate.visibility = View.VISIBLE
                llMetabolicNeed.visibility = View.GONE
            } else {
                txtMetabolicNeed.text = metabolicPercentage.toPercent(1)
                txtCalculate.visibility = View.GONE
                llMetabolicNeed.visibility = View.VISIBLE
            }

            txtMetabolicLabel.text = buildString {
                append("${ getString(R.string.akg) }: ")
                append(viewModel.getResult().roundToInt())
            }
        }
    }

    private fun loadJajanan(jajanan: Jajanan) {
        binding.apply {
            txtCarbs.text = buildString {
                append(jajanan.nutrition.carbs.toString())
                append("g")
            }
            txtProtein.text = buildString {
                append(jajanan.nutrition.protein.toString())
                append("g")
            }
            txtCalorie.text = buildString {
                append(jajanan.nutrition.calorie.toString())
                append("kkal")
            }
            txtFat.text = buildString {
                append(jajanan.nutrition.fat.toString())
                append("g")
            }
            txtNatrium.text = buildString {
                append(jajanan.nutrition.natrium.toString())
                append("mg")
            }
            txtKalium.text = buildString {
                append(jajanan.nutrition.kalium.toString())
                append("mg")
            }

            txtDescription.text = jajanan.description
            txtRecipe.text = jajanan.recipe
            txtDescription.setResizableText(jajanan.description, 4, true)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                txtDescription.justificationMode = LineBreaker.JUSTIFICATION_MODE_INTER_WORD
            }
            txtRecipe.setResizableText(jajanan.recipe, 4, true)
        }

    }

    override fun onResume() {
        super.onResume()
        loadPreferences()
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