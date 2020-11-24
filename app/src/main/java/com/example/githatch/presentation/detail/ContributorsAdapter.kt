package com.example.githatch.presentation.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githatch.R
import com.example.githatch.data.model.owner.Owner
import com.example.githatch.databinding.LayoutItemContributorBinding

class ContributorsAdapter : RecyclerView.Adapter<ContributorsAdapter.MyViewHolder>() {

    private var items: MutableList<Owner> = mutableListOf()
    lateinit var onItemClickListener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: LayoutItemContributorBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.layout_item_contributor,
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) =
        holder.bind(items[position])

    fun updateList(list: List<Owner>?) {
        this.items = list as MutableList<Owner>
        notifyDataSetChanged()
    }

    inner class MyViewHolder(val binding: LayoutItemContributorBinding) :
        RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(owner: Owner) {
            binding.owner = owner
            Glide.with(binding.ivAuthorProfile.context)
                .load(owner.avatarUrl)
                .into(binding.ivAuthorProfile)
        }

        override fun onClick(view: View?) =
            onItemClickListener.onItemClick(items[bindingAdapterPosition])
    }

    interface OnItemClickListener {
        fun onItemClick(owner: Owner)
    }
}