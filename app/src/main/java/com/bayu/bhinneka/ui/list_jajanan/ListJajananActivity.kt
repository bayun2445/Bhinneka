package com.bayu.bhinneka.ui.list_jajanan

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = ViewModelProvider(this)[ListJajananViewModel::class.java]

        observeViewModel()

        binding.fabAdd.setOnClickListener {
            Intent(this@ListJajananActivity, AddJajananActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    private fun observeViewModel() {
        viewModel.getAllJajanan().observe(this) {
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
    }
}