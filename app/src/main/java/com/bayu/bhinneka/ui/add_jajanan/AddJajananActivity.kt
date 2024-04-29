package com.bayu.bhinneka.ui.add_jajanan

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bayu.bhinneka.data.model.Jajanan
import com.bayu.bhinneka.data.model.Nutrition
import com.bayu.bhinneka.databinding.ActivityAddJajananBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AddJajananActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityAddJajananBinding.inflate(layoutInflater)
    }

    private lateinit var viewModel: AddJajananViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[AddJajananViewModel::class.java]
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnPost.setOnClickListener {
            postJajanan()
        }
    }

    private fun postJajanan() {

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

        viewModel.addNewJajanan(newJajanan)
    }
}