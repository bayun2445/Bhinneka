package com.bayu.bhinneka.ui.metabolic_rate

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bayu.bhinneka.R
import com.bayu.bhinneka.databinding.ActivityMetabolicBinding

class MetabolicPreferenceActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMetabolicBinding.inflate(layoutInflater)
    }
    private val viewModel: MetabolicPreferenceViewModel by viewModels()

    private var genderIndex = 0
    private var arrayExercise = arrayOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        setSupportActionBar(binding.topBarMenu)
        supportActionBar?.title = "Profil Kebutuhan Gizi"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel.init(this)

        prepareUI()
        loadUI()

    }

    private fun prepareUI() {
        arrayExercise = resources.getStringArray(R.array.exercise_items)
        val arrayExerciseAdapter = ArrayAdapter(this, R.layout.item_dropdown_exercise, arrayExercise)
        binding.tvExercise.setAdapter(arrayExerciseAdapter)

        binding.rdGenders.setOnCheckedChangeListener { _, index ->
            genderIndex = index
        }

        binding.btnSave.setOnClickListener {
            savePreference()
        }
    }

    private fun loadUI() {
        val preferenceMap = viewModel.getPreferences()

        if (preferenceMap["gender"] == 0) {
            binding.rdMale.isChecked = true
        } else {
            binding.rdFemale.isChecked = true
        }

        binding.txtAge.setText(preferenceMap["age"].toString())
        binding.txtWeight.setText(preferenceMap["weight"].toString())
        binding.txtHeight.setText(preferenceMap["height"].toString())
        binding.tvExercise.setText(arrayExercise[preferenceMap["exercisePreference"] as Int], false)
    }

    private fun savePreference() {
        viewModel.inputPreferences(
            gender = genderIndex,
            age = binding.txtAge.text.toString().toInt(),
            weight = binding.txtWeight.text.toString().toFloat(),
            height = binding.txtHeight.text.toString().toFloat(),
            exercisePreference = arrayExercise.indexOf(binding.tvExercise.text.toString())
        )

        val result = viewModel.getResult()
        binding.tvResult.text = buildString {
            append(result)
            append(" kkal")
        }
    }
}