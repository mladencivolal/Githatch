package com.example.githatch.repository

import RepositoryPresenterImplementation
import RepositoryView
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.example.githatch.R
import com.example.githatch.api.Constants
import com.example.githatch.api.model.Author
import com.example.githatch.api.model.Repository
import com.example.githatch.author.AuthorActivity
import com.example.githatch.helpers.Helper
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_repository.*

class RepositoryActivity : AppCompatActivity(), RepositoryView, ContributorsAdapter.OnItemClickListener {

    private var repoPresenterImplementation: RepositoryPresenterImplementation? = null
    private var contributorsAdapter: ContributorsAdapter? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repository)

        repoPresenterImplementation = RepositoryPresenterImplementation(this)

        initRecyclerView()

        populateWithIntentData()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun populateWithIntentData() {
        val repoData = intent.getParcelableExtra(Constants.REPO_DETAILS) as Repository

        tvTitle.text = repoData.name
        tvAuthor.text = Helper.textFormatter("Author ${repoData.author.login}", 6, "#FAFAFA")
        tvLang.text = Helper.textFormatter("Language ${repoData.language}", 8, "#33BBFF")
        tvDateCreated.text = Helper.textFormatter("Initial Commit ${Helper.dateFormatter(repoData.createdAt)}", 14, "#FAFAFA")
        tvDateUpdated.text = Helper.textFormatter("Last Update ${Helper.dateFormatter(repoData.updatedAt)}", 11, "#FAFAFA")
        tvWatch.text = Helper.numberFormatter(repoData.watchersCount)
        tvFork.text = Helper.numberFormatter(repoData.forksCount)
        tvIssue.text = Helper.numberFormatter(repoData.openIssues)

        tvDescription.text = repoData.description

        repoPresenterImplementation?.getContributors(repoData.author.login, repoData.name)

        tvLink.setOnClickListener{launchBrowserActivity(repoData)}
        ivProfile.setOnClickListener{launchAuthorActivity(repoData.author)}

        Picasso.get().load(repoData.author.avatarUrl)
            .placeholder(R.mipmap.ic_launcher)
            .error(R.mipmap.ic_launcher)
            .into(ivProfile)
    }

    private fun initRecyclerView() {
        recyclerview.itemAnimator = DefaultItemAnimator()
        recyclerview.layoutManager = GridLayoutManager(this, 4)
        ViewCompat.setNestedScrollingEnabled(recyclerview, false)

        contributorsAdapter = ContributorsAdapter()
        contributorsAdapter!!.onItemClickListener = this

        recyclerview.adapter = contributorsAdapter
    }

    override fun attachContributors(list: List<Author>) {
        contributorsAdapter?.updateList(list)
    }

    override fun showProgressBar() {
        recyclerview.visibility = View.INVISIBLE
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        recyclerview.visibility = View.VISIBLE
        progressBar.visibility = View.INVISIBLE
    }

    override fun onItemClick(owner: Author) {
        launchAuthorActivity(owner)
    }

    private fun launchAuthorActivity(author: Author) {
        val intent = Intent(this, AuthorActivity::class.java)
        intent.putExtra(Constants.AUTHOR_DETAILS, author)
        startActivity(intent)
    }

    private fun launchBrowserActivity (repo: Repository) {
        val webpage: Uri = Uri.parse(repo.htmlUrl)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }
}