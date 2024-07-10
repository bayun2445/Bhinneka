package com.bayu.bhinneka.ui.main

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bayu.bhinneka.R
import com.bayu.bhinneka.data.model.History
import com.bayu.bhinneka.data.repository.PreferencesRepository
import com.bayu.bhinneka.databinding.ActivityMainBinding
import com.bayu.bhinneka.helper.IMAGE_EXTRA
import com.bayu.bhinneka.helper.ROLE_EXTRA
import com.bayu.bhinneka.helper.bitmapToFile
import com.bayu.bhinneka.helper.uriToFile
import com.bayu.bhinneka.ui.list_jajanan.ListJajananActivity
import com.bayu.bhinneka.ui.login.LoginActivity
import com.bayu.bhinneka.ui.metabolic_rate.MetabolicPreferenceActivity
import com.bayu.bhinneka.ui.result.ResultActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val viewModel: MainViewModel by viewModels()

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val selectedImage = it.data?.data as Uri
            val imgFile = uriToFile(selectedImage, this)
            val intent = Intent(this@MainActivity, ResultActivity::class.java)
            intent.putExtra(IMAGE_EXTRA, imgFile)
            startActivity(intent)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val capturedImage = it.data?.extras?.get("data") as Bitmap
            val imgFile = bitmapToFile(capturedImage, this)
            val intent = Intent(this@MainActivity, ResultActivity::class.java)
            intent.putExtra(IMAGE_EXTRA, imgFile)
            startActivity(intent)
        }
    }

    private val launcherRequestPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            startCameraIntent()
        } else {
            Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private var isAdmin = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        setSupportActionBar(binding.topBarMenu)
        supportActionBar?.title = getString(R.string.app_name)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        if (viewModel.getCurrentUser() == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        isAdmin = intent.getBooleanExtra(ROLE_EXTRA, false)
        binding.txtUsername.text = getString(R.string.welcome, viewModel.getCurrentUser()?.email)

        setPopUpMenu()

        viewModel.getAllHistory().observe(this) {
            if (!it.isNullOrEmpty()) {
                binding.txtNoHistory.visibility = View.GONE
                loadHistoryList(it)
            }
        }

        binding.rvListHistory.layoutManager = LinearLayoutManager(this)
    }

    private fun loadHistoryList(histories: List<History>) {
        val historyAdapter = HistoryListAdapter()
        historyAdapter.setList(histories)

        binding.rvListHistory.apply {
            adapter = historyAdapter
            setHasFixedSize(false)
            visibility = View.VISIBLE
        }
    }

    private fun setPopUpMenu() {
        val popupMenu = PopupMenu(
            this,
            binding.btnDetection,
            Gravity.CENTER_HORIZONTAL
        )

        popupMenu.inflate(R.menu.select_image_menu)
        popupMenu.menu.findItem(R.id.menu_camera)
        popupMenu.setForceShowIcon(true)

        popupMenu.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.menu_gallery -> {
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*"

                    val chooser = Intent.createChooser(intent, getString(R.string.choose_picture))

                    launcherIntentGallery.launch(chooser)
                    true
                }

                R.id.menu_camera -> {
                    startCameraIntent()
                    true
                }

                else -> {
                    true
                }
            }
        }

        binding.btnDetection.setOnClickListener {
            popupMenu.show()
        }

    }

    private fun startCameraIntent() {
        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            launcherIntentCamera.launch(intent)
        } else {
            launcherRequestPermission.launch(Manifest.permission.CAMERA)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_top_menu, menu)
        if (!isAdmin) {
            menu?.findItem(R.id.menu_edit)?.setVisible(false)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.menu_edit -> {
                startActivity(
                    Intent(this, ListJajananActivity::class.java)
                )

                true
            }

            R.id.menu_logout -> {
                logout()

                true
            }

            R.id.menu_pref -> {
                startActivity(
                    Intent(this, MetabolicPreferenceActivity::class.java)
                )

                true
            }
            else -> true
        }
    }

    private fun logout() {
        lifecycleScope.launch {
            val credentialManager = CredentialManager.create(this@MainActivity)
            val preferences = PreferencesRepository.getInstance(this@MainActivity)

            preferences.clearPreferences()
            viewModel.signOut()
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        }
    }

}
