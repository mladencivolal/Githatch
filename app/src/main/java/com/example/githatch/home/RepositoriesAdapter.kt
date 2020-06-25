package com.example.githatch.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githatch.R
import com.example.githatch.api.model.Repository
import com.example.githatch.helpers.Helper
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_item_repository.view.*

class RepositoriesAdapter(val showImage: Boolean, recyclerview: RecyclerView) :
    RecyclerView.Adapter<RepositoriesAdapter.MyViewHolder>() {

    private var repos: MutableList<Repository> = mutableListOf()

    lateinit var onItemClickListener: OnItemClickListener
    lateinit var onLoadMoreListener: OnLoadMoreListener

    private var loading: Boolean = false

    init {
        if (recyclerview.layoutManager is LinearLayoutManager) {
            val linearLayoutManager = recyclerview.layoutManager as LinearLayoutManager

            recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val totalItemCount = linearLayoutManager.itemCount
                    val lastVisibleItem = linearLayoutManager.findLastCompletelyVisibleItemPosition()

                    if(!loading && totalItemCount - 1 <= lastVisibleItem && lastVisibleItem > repos.size - 5) {
                        onLoadMoreListener.onLoadMore()
                        loading = true;
                    }
                }
            })
        }
    }

    fun updateRepos(repos: List<Repository>) {
        repos.forEach { this.repos.add(it) }

        notifyDataSetChanged()
    }

    fun clearItems() {
        this.repos.clear()
        notifyDataSetChanged()
    }

    fun setLoaded() {
        loading = false
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
            itemView.ivRepoAuthor.setOnClickListener(this)
            itemView.ivRepoLink.setOnClickListener (this)
        }

        fun bindData(repo: Repository) {
            itemView.tvRepoName.text = repo.name
            itemView.tvRepoName.isSelected = true
            itemView.tvAuthorName.text = Helper.textFormatter("Author: ${repo.author.login}", 8, "#FAFAFA")
            itemView.tvWatch.text = Helper.numberFormatter(repo.watchersCount)
            itemView.tvIssue.text = Helper.numberFormatter(repo.openIssues)
            itemView.tvFork.text = Helper.numberFormatter(repo.forksCount)

            if (showImage) {
                Picasso.get().load(repo.author.avatarUrl)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(itemView.ivRepoAuthor)
            } else {
                itemView.ivRepoAuthor.visibility = View.GONE
                itemView.ivRepoLink.visibility = View.GONE
            }
        }

        override fun onClick(v: View?) {
            onItemClickListener.onItemClick(repos[adapterPosition], v!!)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_item_repository, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindData(repos[position])
    }

    override fun getItemCount(): Int = repos.size


    interface OnItemClickListener {
        fun onItemClick(repo: Repository, v: View)
    }

    interface OnLoadMoreListener {
        fun onLoadMore()
    }
}