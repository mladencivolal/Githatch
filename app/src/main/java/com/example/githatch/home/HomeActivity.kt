package com.example.githatch.home

import android.content.Intent
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githatch.R
import com.example.githatch.api.Constants
import com.example.githatch.api.model.Author
import com.example.githatch.api.model.Repository
import com.example.githatch.author.AuthorActivity
import com.example.githatch.helpers.Helper
import com.example.githatch.repository.RepositoryActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.recyclerview
import kotlinx.android.synthetic.main.layout_item_repository.*
import kotlinx.android.synthetic.main.layout_item_repository.ivRepoAuthor
import kotlinx.android.synthetic.main.layout_sort_sheet.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity(), View.OnClickListener, HomeView,
    RepositoriesAdapter.OnItemClickListener, RepositoriesAdapter.OnLoadMoreListener {

    private lateinit var homePresenter: HomePresenterImplementation
    private lateinit var repositoriesAdapter: RepositoriesAdapter

    private lateinit var dialog: BottomSheetDialog
    private lateinit var bottomSortView: View

    private var orderBy: String = ""
    private var sortBy: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        ivSearch.setOnClickListener(this)
        fabSort.setOnClickListener(this)
        fabUp.setOnClickListener(this)

        homePresenter = HomePresenterImplementation(this)

        initRecyclerView()
        initSortSheet()

        etSearch.setText("Kotlin")
        ivSearch.performClick()
    }

    private fun initRecyclerView() {
        recyclerview.itemAnimator = DefaultItemAnimator()
        recyclerview.layoutManager = LinearLayoutManager(this)

        repositoriesAdapter = RepositoriesAdapter(true, recyclerview)
        repositoriesAdapter.onItemClickListener = this
        repositoriesAdapter.onLoadMoreListener = this

        recyclerview.adapter = repositoriesAdapter
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
            CoroutineScope(Dispatchers.Default).launch {
                homePresenter.onSortDialogCancel()
            }
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


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivSearch -> {
                showProgressBar()
                CoroutineScope(Dispatchers.Default).launch {
                    homePresenter.searchRepositories(etSearch.text.toString(), sortBy, orderBy)
                }
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
                homePresenter.onFilterApply()
                ivSearch.performClick()
            }
            R.id.lbClear -> {
                dialog.dismiss()
                CoroutineScope(Dispatchers.Default).launch {

                    homePresenter.onFilterClear()
                }
            }
            R.id.fabUp -> {
                recyclerview.smoothScrollToPosition(0)
            }
        }
    }

    override fun updateList(items: List<Repository>) {
        repositoriesAdapter.setLoaded()
        repositoriesAdapter.updateRepos(items)
    }

    override fun clearList() {
        repositoriesAdapter.clearItems()
    }

    override fun showList() {
        recyclerview.visibility = View.VISIBLE
    }

    override fun hideList() {
        recyclerview.visibility = View.GONE
    }

    override fun resetFilters() {
        resetSortFilters()
        resetOrderFilters()
    }

    override fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
        recyclerview.visibility = View.GONE
    }

    override fun hideProgressBar() {
        progressBar.visibility = View.GONE
        recyclerview.visibility = View.VISIBLE
    }

    override fun onItemClick(repo: Repository, v: View) {
        if (v.id == ivRepoAuthor.id) {
            launchAuthorActivity(repo.author)
        } else if (v.id == ivRepoLink.id) {
            launchBrowserActivity(repo)
        } else {
            launchRepoActivity(repo)
        }
    }

    override fun onLoadMore() {
        CoroutineScope(Dispatchers.Default).launch {
            homePresenter.onLoadMore()
        }
    }

    private fun launchAuthorActivity(author: Author) {
        val intent = Intent(this, AuthorActivity::class.java)
        intent.putExtra(Constants.AUTHOR_DETAILS, author)
        startActivity(intent)
    }

    private fun launchRepoActivity(repo: Repository) {
        val intent = Intent(this, RepositoryActivity::class.java)
        intent.putExtra(Constants.REPO_DETAILS, repo)
        startActivity(intent)
    }

    private fun launchBrowserActivity(repo: Repository) {
        val webpage: Uri = Uri.parse(repo.htmlUrl)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }
}