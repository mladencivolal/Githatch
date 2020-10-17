package com.example.githatch.presentation.repo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githatch.R
import com.example.githatch.data.model.owner.Owner
import com.example.githatch.data.model.repo.Repo
import com.example.githatch.databinding.ActivityRepoBinding
import com.example.githatch.helpers.Helper
import com.example.githatch.presentation.detail.DetailActivity
import com.example.githatch.presentation.di.Injector
import com.example.githatch.presentation.owner.OwnerActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.layout_sort_sheet.view.*
import javax.inject.Inject


class RepoActivity : AppCompatActivity(), RepoAdapter.OnLoadMoreListener,
    RepoAdapter.OnItemClickListener, View.OnClickListener {
    @Inject
    lateinit var factory: RepoViewModelFactory
    private lateinit var viewmodel: RepoViewModel
    private lateinit var binding: ActivityRepoBinding
    private lateinit var adapter: RepoAdapter
    private lateinit var bottomSortView: View
    private lateinit var dialog: BottomSheetDialog
    private var searchTerm = ""
    private var orderBy: String = ""
    private var sortBy: String = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_repo)
        (application as Injector).createRepoSubComponent()
            .inject(this)

        viewmodel = ViewModelProvider(this, factory)
            .get(RepoViewModel::class.java)

        binding.etSearch.setText("Kotlin")

        initOnClickListeners()
        initOnActionListeners()
        initRecyclerView()
        initSortSheet()

        searchRepos(binding.etSearch.text.toString(), sortBy, orderBy)
    }

    private fun initOnActionListeners() {
        binding.apply {
            etSearch.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    searchTerm = binding.etSearch.text.toString()
                    searchRepos(searchTerm, sortBy, orderBy)
                    return@setOnEditorActionListener true
                } else {
                    return@setOnEditorActionListener false
                }
            }
            etSearch.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    searchTerm = binding.etSearch.text.toString()
                    searchRepos(searchTerm, sortBy, orderBy)
                    return@setOnEditorActionListener true
                } else {
                    return@setOnEditorActionListener false
                }
            }
            etSearch.setOnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    searchTerm = binding.etSearch.text.toString()
                    searchRepos(searchTerm, sortBy, orderBy)
                    Helper.hideKeyboard(this@RepoActivity)
                    return@setOnKeyListener true
                } else {
                    return@setOnKeyListener false
                }
            }
        }
    }

    private fun initOnClickListeners() {
        binding.apply {
            fabUp.setOnClickListener { binding.repoRecyclerview.scrollToPosition(0) }
            fabSort.setOnClickListener { dialog.show() }
        }
    }

    private fun initRecyclerView() {
        binding.repoRecyclerview.layoutManager = LinearLayoutManager(this)
        adapter = RepoAdapter(binding.repoRecyclerview, true)
        adapter.onLoadMoreListener = this
        adapter.onItemClickListener = this
        binding.repoRecyclerview.adapter = adapter
    }

    private fun initSortSheet() {
        bottomSortView = layoutInflater.inflate(R.layout.layout_sort_sheet, null)
        bottomSortView.apply {
            val clickables = listOf(lbStars, lbForks, lbUpdated, tvAsc, tvDesc, lbApply, lbClear)
            clickables.toTypedArray().forEach { it.setOnClickListener(this@RepoActivity) }
        }
        dialog = BottomSheetDialog(this)
        dialog.apply {
            setContentView(bottomSortView)
            setOnCancelListener {}
        }
    }

    private fun resetSortFilters() {
        bottomSortView.apply {
            lbStars.isSelected = false
            lbForks.isSelected = false
            lbUpdated.isSelected = false
        }
    }

    private fun resetOrderFilters() {
        bottomSortView.apply {
            tvAsc.isSelected = false
            tvDesc.isSelected = false
        }
    }

    private fun manageSortFilters() {
        resetSortFilters()

        when (sortBy) {
            Helper.SortBy.stars.name -> bottomSortView.lbStars.isSelected = true
            Helper.SortBy.forks.name -> bottomSortView.lbForks.isSelected = true
            Helper.SortBy.updated.name -> bottomSortView.lbUpdated.isSelected = true
        }
    }

    private fun manageOrderFilters() {
        resetOrderFilters()

        when (orderBy) {
            Helper.OrderBy.asc.name -> bottomSortView.tvAsc.isSelected = true
            Helper.OrderBy.desc.name -> bottomSortView.tvDesc.isSelected = true
        }
    }

    private fun searchRepos(searchPhrase: String, sortBy: String, orderBy: String) {
        binding.progressBar.visible(true)
        this.searchTerm = searchPhrase
        this.sortBy = sortBy
        this.orderBy = orderBy

        val responseLiveData = viewmodel.getRepos(searchPhrase, sortBy, orderBy)
        responseLiveData.observe(this, {
            if (it != null) {
                adapter.setList(it)
                binding.progressBar.visible(false)
                binding.promptLayout.visible(false)
            } else {
                binding.progressBar.visible(false)
            }
        })
    }

    override fun onLoadMore() {
        binding.progressBar.visible(true)
        val responseLiveData = viewmodel.loadMoreRepos()
        responseLiveData.observe(this, {
            if (it!!.size > 5) {
                adapter.updateList(it)
                adapter.setIsLoading(false)
                binding.progressBar.visible(false)
            }
        })
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.fabUp -> {
                binding.repoRecyclerview.scrollToPosition(0)
            }
            R.id.fabSort -> {
                dialog.show()
            }
            R.id.lbStars -> {
                sortBy = Helper.SortBy.stars.name
                manageSortFilters()
            }
            R.id.lbForks -> {
                sortBy = Helper.SortBy.forks.name
                manageSortFilters()
            }
            R.id.lbUpdated -> {
                sortBy = Helper.SortBy.updated.name
                manageSortFilters()
            }
            R.id.tvAsc -> {
                orderBy = Helper.OrderBy.asc.name
                manageOrderFilters()
            }
            R.id.tvDesc -> {
                orderBy = Helper.OrderBy.desc.name
                manageOrderFilters()
            }
            R.id.lbApply -> {
                dialog.dismiss()
                searchRepos(searchTerm, sortBy, orderBy)
            }
            R.id.lbClear -> {
                dialog.dismiss()
                resetOrderFilters()
                resetSortFilters()
                sortBy = ""
                orderBy = ""
                searchRepos(searchTerm, "", "")
            }
        }
    }

    override fun onItemClick(repo: Repo, view: View) {
        when (view.id) {
            R.id.ivRepoAuthor -> launchOwnerActivity(repo.owner)
            R.id.tvRepoName -> launchDetailActivity(repo)
        }
    }

    private fun View.visible(show:Boolean) {
        visibility = if(show) View.VISIBLE else View.GONE
    }

    private fun launchDetailActivity(repo: Repo) {
        val intent = Intent(this, DetailActivity::class.java).putExtra("repo", repo)
        startActivity(intent)
    }

    private fun launchOwnerActivity(owner: Owner) {
        val intent = Intent(this, OwnerActivity::class.java).putExtra("owner", owner)
        startActivity(intent)
    }
}



