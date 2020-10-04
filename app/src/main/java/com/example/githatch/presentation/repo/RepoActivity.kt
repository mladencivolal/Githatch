package com.example.githatch.presentation.repo

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githatch.R
import com.example.githatch.data.model.repo.Repo
import com.example.githatch.databinding.ActivityRepoBinding
import com.example.githatch.helpers.Helper
import com.example.githatch.presentation.detail.DetailActivity
import com.example.githatch.presentation.di.Injector
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.layout_sort_sheet.view.*
import javax.inject.Inject

class RepoActivity : AppCompatActivity(), RepoAdapter.OnLoadMoreListener,
    RepoAdapter.OnItemClickListener, View.OnClickListener {
    @Inject
    lateinit var factory: RepoViewModelFactory
    private lateinit var repoViewModel: RepoViewModel
    private lateinit var binding: ActivityRepoBinding
    private lateinit var adapter: RepoAdapter
    private lateinit var bottomSortView: View
    private lateinit var dialog: BottomSheetDialog
    private var searchPhrase = ""
    private var orderBy: String = ""
    private var sortBy: String = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_repo)
        (application as Injector).createRepoSubComponent()
            .inject(this)

        repoViewModel = ViewModelProvider(this, factory)
            .get(RepoViewModel::class.java)

        binding.fabUp.setOnClickListener(this)
        binding.fabSort.setOnClickListener(this)
        binding.etSearch.setText("Kotlin")

        binding.etSearch.setOnEditorActionListener { view, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                searchPhrase = binding.etSearch.text.toString()
                searchRepos(searchPhrase, sortBy, orderBy)
                return@setOnEditorActionListener true
            } else {
                return@setOnEditorActionListener false
            }
        }

        binding.etSearch.setOnKeyListener { view, keyCode, event ->
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                searchPhrase = binding.etSearch.text.toString()
                searchRepos(searchPhrase, sortBy, orderBy)
                Helper.hideKeyboard(this)
                return@setOnKeyListener true
            } else {
                return@setOnKeyListener false
            }
        }

        initRecyclerView()
        initSortSheet()

        searchRepos(binding.etSearch.text.toString(), sortBy, orderBy)
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

        bottomSortView.lbStars.setOnClickListener(this)
        bottomSortView.lbForks.setOnClickListener(this)
        bottomSortView.lbUpdated.setOnClickListener(this)
        bottomSortView.tvAsc.setOnClickListener(this)
        bottomSortView.tvDesc.setOnClickListener(this)
        bottomSortView.lbApply.setOnClickListener(this)
        bottomSortView.lbClear.setOnClickListener(this)

        dialog = BottomSheetDialog(this)
        dialog.setContentView(bottomSortView)
        dialog.setOnCancelListener {

        }
    }

    private fun resetSortFilters() {
        bottomSortView.lbStars.isSelected = false
        bottomSortView.lbForks.isSelected = false
        bottomSortView.lbUpdated.isSelected = false
    }

    private fun resetOrderFilters() {
        bottomSortView.tvAsc.isSelected = false
        bottomSortView.tvDesc.isSelected = false
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
        binding.progressBar.visibility = View.VISIBLE
        this.searchPhrase = searchPhrase
        this.sortBy = sortBy
        this.orderBy = orderBy

        val responseLiveData = repoViewModel.getRepos(searchPhrase, sortBy, orderBy)
        responseLiveData.observe(this, {
            if (it != null) {
                adapter.setList(it)
                binding.progressBar.visibility = View.GONE
                binding.promptLayout.visibility = View.GONE
            } else {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(applicationContext, "No data available", Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onLoadMore() {
        binding.progressBar.visibility = View.VISIBLE
        val responseLiveData = repoViewModel.loadMoreRepos()
        responseLiveData.observe(this, {
            if (it != null) {
                adapter.updateList(it)
                adapter.setIsLoading(false)

                binding.progressBar.visibility = View.GONE
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
                searchRepos(searchPhrase, sortBy, orderBy)
            }
            R.id.lbClear -> {
                dialog.dismiss()
                resetOrderFilters()
                resetSortFilters()
                sortBy = ""
                orderBy = ""
                searchRepos(searchPhrase, "", "")
            }
        }
    }

    override fun onItemClick(repo: Repo, v: View) {
        when (v.id) {
            R.id.ivRepoLink -> {
                launchBrowserActivity(repo)
            }
            else -> {
                launchDetailActivity(repo)
            }
        }
    }

    private fun launchDetailActivity(repo: Repo) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("repo", repo)
        startActivity(intent)
    }

    private fun launchBrowserActivity(repo: Repo) {
        val webpage: Uri = Uri.parse(repo.htmlUrl)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        if (intent.resolveActivity(packageManager) != null) startActivity(intent)
    }
}



