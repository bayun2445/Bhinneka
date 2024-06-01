package com.bayu.bhinneka.ui.edit_jajanan

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bayu.bhinneka.R
import com.bayu.bhinneka.data.model.Jajanan
import com.bayu.bhinneka.data.model.Nutrition
import com.bayu.bhinneka.databinding.ActivityEditJajananBinding

class EditJajananActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityEditJajananBinding.inflate(layoutInflater)
    }

    private lateinit var viewModel: EditJajananViewModel
    private lateinit var jajananIntent: Jajanan
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        setSupportActionBar(binding.topBarMenu)
        supportActionBar?.title = getString(R.string.edit_jajanan)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = ViewModelProvider(this)[EditJajananViewModel::class.java]

        if (intent.hasExtra("jajanan")) {
            jajananIntent = intent.getParcelableExtra("jajanan")!!
        } else {
            Toast.makeText(this, "No extra included", Toast.LENGTH_SHORT).show()
            finish()
        }

        loadJajanan()

        binding.btnUpdate.setOnClickListener {
            updateJajanan()
        }
    }

    private fun loadJajanan() {
        jajananIntent.apply {
            binding.txtName.setText(name)
            binding.txtDescription.setText(description)
            binding.txtRecipe.setText(recipe)

            binding.txtCalorie.setText(nutrition.calorie.toString())
            binding.txtProtein.setText(nutrition.protein.toString())
            binding.txtCarbs.setText(nutrition.carbs.toString())
            binding.txtNatrium.setText(nutrition.natrium.toString())
            binding.txtFat.setText(nutrition.fat.toString())
            binding.txtKalium.setText(nutrition.kalium.toString())
        }
    }

    private fun updateJajanan() {
        var newJajanan: Jajanan

        binding.apply {
            newJajanan = Jajanan(
                name = txtName.text.toString(),
                description = txtDescription.text.toString(),
                recipe = txtRecipe.text.toString(),
                nutrition = Nutrition(
                    carbs = txtCarbs.text.toString().toDouble(),
                    calorie = txtCalorie.text.toString().toDouble(),
                    fat = txtFat.text.toString().toDouble(),
                    protein = txtProtein.text.toString().toDouble(),
                    natrium = txtNatrium.text.toString().toDouble(),
                    kalium = txtKalium.text.toString().toDouble()
                )
            )
        }

        viewModel.updateJajanan(newJajanan)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}