package com.example.githatch.repository;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.githatch.R
import com.example.githatch.api.model.Author
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_item_contributor.view.*

class ContributorsAdapter : RecyclerView.Adapter<ContributorsAdapter.MyViewHolder>() {

    private var items: MutableList<Author> = mutableListOf()
    lateinit var onItemClickListener: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_item_contributor, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindData(items[position])
    }

    fun updateList(list: List<Author>) {
        this.items = list as MutableList<Author>
        notifyDataSetChanged()
    }


    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        fun bindData(owner: Author) {
            itemView.tvName.text = owner.login

            Picasso.get().load(owner.avatarUrl)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(itemView.ivAuthorProfile)
        }

        override fun onClick(view: View?) {
            onItemClickListener.onItemClick(items[adapterPosition])
        }
    }

    interface OnItemClickListener {
        fun onItemClick(owner: Author)
    }

}