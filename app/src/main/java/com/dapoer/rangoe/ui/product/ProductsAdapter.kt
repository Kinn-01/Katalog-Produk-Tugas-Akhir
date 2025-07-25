package com.dapoer.rangoe.ui.product

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dapoer.rangoe.R
import com.dapoer.rangoe.data.retrofit.response.DataItem
import com.dapoer.rangoe.data.retrofit.response.LinksItem
import com.dapoer.rangoe.data.retrofit.response.Products
import com.dapoer.rangoe.databinding.ProductsCardBinding
import com.dapoer.rangoe.ui.detailProduct.DetailProductActivity

class ProductsAdapter(private val context: Context, private val products: Products) : ListAdapter<DataItem, ProductsAdapter.MyViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItem>() {
            override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ProductsCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val product = products.data?.get(position)
        if (product != null) {
            holder.bind(product, products.links)
        }
    }
    override fun getItemCount(): Int {
        return products.data?.size ?: 0
    }
    inner class MyViewHolder(private val binding: ProductsCardBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(product: DataItem, links: List<LinksItem?>?) {
            binding.nameTextView.text = product.title
            binding.descTextView.text = "Rp. ${product.price.toString()}"
            Glide.with(context)
                .load(product.thumbnail)
                .placeholder(R.drawable.linkyi_logo)
                .into(binding.profileImageView)

            binding.root.setOnClickListener {
                val intent = Intent(context, DetailProductActivity::class.java).apply {
                    putExtra("PRODUCT_ID", product.id)
                }
                context.startActivity(intent)
            }
        }
    }
}