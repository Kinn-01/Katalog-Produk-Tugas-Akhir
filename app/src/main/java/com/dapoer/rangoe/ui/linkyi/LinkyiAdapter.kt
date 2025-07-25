package com.dapoer.rangoe.ui.linkyi

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dapoer.rangoe.R
import com.dapoer.rangoe.data.retrofit.response.DataItem
import com.dapoer.rangoe.data.retrofit.response.Links
import com.dapoer.rangoe.data.retrofit.response.LinksDataItem
import com.dapoer.rangoe.databinding.LinkyiCardBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class LinkyiAdapter(
    private val context: Context,
    private val links: Links,
    private val viewModel: LinkyiViewModel,
    private val refreshListener: FragmentRefreshListener
) : ListAdapter<DataItem, LinkyiAdapter.MyViewHolder>(DIFF_CALLBACK) {

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
        val binding = LinkyiCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val link = links.data?.get(position)
        if (link != null) {
            holder.bind(link, links.data)
        }
    }
    override fun getItemCount(): Int {
        return links.data?.size ?: 0
    }
    inner class MyViewHolder(private val binding: LinkyiCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(link: LinksDataItem?, links: List<LinksDataItem?>?) {
            binding.linksTextView.text = link?.name

            val intent = Intent(context, DetailLinkyiActivity::class.java)
            intent.putExtra(DetailLinkyiActivity.EXTRA_ID, link?.id)
            val intentHead = Intent(context, DetailHeadlineActivity::class.java)
            intentHead.putExtra(DetailHeadlineActivity.EXTRA_ID, link?.id)

            binding.root.setOnClickListener {
                if (link?.type == "link"){
                    context.startActivity(intent)
                }else{
                    context.startActivity(intentHead)
                }
            }
            binding.root.setOnLongClickListener {
                MaterialAlertDialogBuilder(context)
                    .setTitle(context.getString(R.string.delete_dialog))
                    .setMessage(context.getString(R.string.supporting_text_linkyi))
                    .setNegativeButton(context.getString(R.string.decline)) { dialog, which ->
                        dialog.dismiss()
                    }
                    .setPositiveButton(context.getString(R.string.accept)) { dialog, which ->
                        viewModel.deleteLinkyi(link?.id!!)
                        Toast.makeText(context, "Tautan berhasil dihapus", Toast.LENGTH_SHORT).show()
                        refreshListener.onRefresh()
                    }
                    .show()
                true
            }
        }
    }
}