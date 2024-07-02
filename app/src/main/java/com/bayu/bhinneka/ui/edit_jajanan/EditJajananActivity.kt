package com.bayu.bhinneka.ui.edit_jajanan

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
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.bayu.bhinneka.R
import com.bayu.bhinneka.data.model.Jajanan
import com.bayu.bhinneka.data.model.Nutrition
import com.bayu.bhinneka.databinding.ActivityEditJajananBinding
import com.bayu.bhinneka.helper.JAJANAN_PARCELABLE_EXTRA
import com.bumptech.glide.Glide

class EditJajananActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityEditJajananBinding.inflate(layoutInflater)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val selectedImage = it.data?.data as Uri
            binding.imgJajanan.setImageURI(selectedImage)
            binding.imgJajanan.visibility = View.VISIBLE
            isImageUpdated = true
        }
    }

    private val viewModel: EditJajananViewModel by viewModels()
    private lateinit var jajananIntent: Jajanan

    private var isImageUpdated: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        setSupportActionBar(binding.topBarMenu)
        supportActionBar?.title = getString(R.string.edit_jajanan)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (intent.hasExtra(JAJANAN_PARCELABLE_EXTRA)) {
            jajananIntent = intent.getParcelableExtra(JAJANAN_PARCELABLE_EXTRA)!!
        } else {
            Toast.makeText(this, "Jajanan Extra not found!", Toast.LENGTH_SHORT).show()
            finish()
        }

        loadJajanan()
        observeViewModel()

        binding.btnUpdate.setOnClickListener {
            if (isImageUpdated) {
                uploadJajananImage()
            } else {
                updateJajanan(null)
            }
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
                showToast("Edit berhasil!")
            } else {
                showToast("Edit gagal!")
            }

            finish()
        }

        viewModel.imagePath.observe(this) { path ->
            path?.let {
                updateJajanan(path)
            }
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun uploadJajananImage() {
        if (binding.imgJajanan.isVisible) {
            val bitmap = binding.imgJajanan.drawable.toBitmap()

            viewModel.uploadImage(bitmap, "img-${binding.txtName.text.toString()}")
        } else {
            updateJajanan(null)
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

            if (!jajananIntent.imagePath.isNullOrEmpty()) {
                Glide.with(this@EditJajananActivity)
                    .load(jajananIntent.imagePath)
                    .into(binding.imgJajanan)

                binding.imgJajanan.visibility = View.VISIBLE
            }
        }
    }

    private fun updateJajanan(imagePath: String?) {
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
                imagePath =  imagePath
            )
        }

        viewModel.updateJajanan(newJajanan)
    }
}