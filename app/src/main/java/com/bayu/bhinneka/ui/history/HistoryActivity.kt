package com.bayu.bhinneka.ui.history

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
import com.bayu.bhinneka.databinding.ActivityHistoryBinding
import com.bayu.bhinneka.helper.HISTORY_EXTRA
import com.bayu.bhinneka.helper.toPercent
import com.bumptech.glide.Glide
import kotlin.math.roundToInt

class HistoryActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityHistoryBinding.inflate(layoutInflater)
    }
    private val viewModel: HistoryViewModel by viewModels()

    private lateinit var historyIntent: History
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        setSupportActionBar(binding.topBarMenu)
        supportActionBar?.title = "Riwayat"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (intent.hasExtra(HISTORY_EXTRA)) {
            historyIntent = intent.getParcelableExtra(HISTORY_EXTRA)!!
        } else {
            Toast.makeText(this, "History Extra not found!", Toast.LENGTH_SHORT).show()
            finish()
        }

        viewModel.init(this)

        viewModel.getJajanan(historyIntent.resultJajananName).observe(this) { jajanan ->
            Log.d("HistoryActivity", jajanan.toString())
            jajanan?.let {
                loadJajanan(it)
                loadPreferences()
                binding.container.visibility = View.VISIBLE
                binding.cvLoading.visibility = View.GONE
            }
        }


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
            Glide.with(this@HistoryActivity)
                .load(historyIntent.imgPath)
                .into(imgResult)

            txtLabelResult.text = jajanan.name
            txtOutput.text = buildString {
                append("Tingkat kecocokan: ")
                append(historyIntent.matchScore?.toPercent(2))
            }
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.common_top_menu, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}