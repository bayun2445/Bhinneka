package com.bayu.bhinneka.ui.list_jajanan

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bayu.bhinneka.R
import com.bayu.bhinneka.data.model.Jajanan
import com.bayu.bhinneka.databinding.ActivityListJajananBinding
import com.bayu.bhinneka.ui.add_jajanan.AddJajananActivity

class ListJajananActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityListJajananBinding.inflate(layoutInflater)
    }
    private lateinit var viewModel: ListJajananViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        setSupportActionBar(binding.topBarMenu)
        supportActionBar?.title = "Data Jajanan"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = ViewModelProvider(this)[ListJajananViewModel::class.java]

        binding.rvListJajanan.layoutManager = LinearLayoutManager(this)

        binding.fabAdd.setOnClickListener {
            Intent(this@ListJajananActivity, AddJajananActivity::class.java).also {
                startActivity(it)
            }
        }

        observeViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_bar_menu, menu)
        menu?.findItem(R.id.menu_edit)?.setVisible(false)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun observeViewModel() {
        viewModel.getAllJajanan().observe(this) {
            Log.d(
                "ListJajananActivity",
                it.toString()
            )
            it?.let {
                loadList(it)
            }
        }
    }

    private fun loadList(listJajanan: List<Jajanan>) {
        val adapter = JajananListAdapter()
        adapter.setList(listJajanan)

        adapter.setDeleteButtonOnCLickListener(
            object : JajananListAdapter.ItemClick {
                override fun deleteClick(position: Int) {
                    val selectedJajanan = listJajanan[position]
                    viewModel.deleteJajanan(selectedJajanan)
                }
            }
        )

        binding.rvListJajanan.adapter = adapter
        binding.rvListJajanan.setHasFixedSize(false)
    }
}