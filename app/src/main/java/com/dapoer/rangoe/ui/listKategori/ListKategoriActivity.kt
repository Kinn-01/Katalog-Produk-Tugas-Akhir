package com.dapoer.rangoe.ui.listKategori

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.dapoer.rangoe.R
import com.dapoer.rangoe.data.ViewModelFactory
import com.dapoer.rangoe.databinding.ActivityListKategoriBinding

class ListKategoriActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListKategoriBinding
    private lateinit var adapter: ListKategoriAdapter
    private val viewModel by viewModels<ListKategoriViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityListKategoriBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ListKategoriAdapter()

        binding.rvCategory.layoutManager = LinearLayoutManager(this)
        binding.rvCategory.adapter = adapter

        viewModel.categories.observe(this) { categories ->
            adapter.submitList(categories)
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(this) { error ->
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.fetchCategories()

        with(binding){
            topAppBar.setNavigationOnClickListener { finish() }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainKategori)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}