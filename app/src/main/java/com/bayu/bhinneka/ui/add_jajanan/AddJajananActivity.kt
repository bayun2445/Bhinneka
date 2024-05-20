package com.bayu.bhinneka.ui.add_jajanan

import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bayu.bhinneka.R
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
        setSupportActionBar(binding.topBarMenu)
        supportActionBar?.title = "Add Jajanan"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        viewModel = ViewModelProvider(this)[AddJajananViewModel::class.java]
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        observeViewModel()

        binding.btnPost.setOnClickListener {
            postJajanan()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_bar_menu, menu)
        menu?.findItem(R.id.menu_edit)?.setVisible(false)
        return true
    }

    private fun observeViewModel() {
        viewModel.isSuccessful.observe(this) {
            if (it) {
                Toast.makeText(this, "Berhasil Menambahkan", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show()
            }
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