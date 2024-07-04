package com.bayu.bhinneka.ui.add_jajanan

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bayu.bhinneka.R
import com.bayu.bhinneka.data.model.Jajanan
import com.bayu.bhinneka.data.model.Nutrition
import com.bayu.bhinneka.databinding.ActivityAddJajananBinding

class AddJajananActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityAddJajananBinding.inflate(layoutInflater)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val selectedImage = it.data?.data as Uri
            binding.imgJajanan.setImageURI(selectedImage)
            binding.imgJajanan.visibility = View.VISIBLE
        }
    }

    private lateinit var viewModel: AddJajananViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        setSupportActionBar(binding.topBarMenu)
        supportActionBar?.title = "Add Jajanan"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = ViewModelProvider(this)[AddJajananViewModel::class.java]

        observeViewModel()

        binding.btnPost.setOnClickListener {
            uploadJajananImage()
        }

        binding.btnUploadImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"

            val chooser = Intent.createChooser(intent, getString(R.string.choose_picture))

            launcherIntentGallery.launch(chooser)
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

    private fun observeViewModel() {
        viewModel.isSuccessful.observe(this) {
            if (it) {
                Toast.makeText(this, "Berhasil Menambahkan", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.imagePath.observe(this) {
            if (!it.isNullOrEmpty()) {
                postJajanan(it)
            }
        }
    }

    private fun uploadJajananImage() {
        if (binding.imgJajanan.isVisible) {
            val bitmap = binding.imgJajanan.drawable.toBitmap()

            viewModel.uploadImage(bitmap, "img-${binding.txtName.text.toString()}")
        } else {
            postJajanan(null)
        }
    }

    private fun postJajanan(imagePath: String?) {

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
                ),
                imagePath = imagePath
            )
        }

        viewModel.addNewJajanan(newJajanan)
    }
}