package com.example.githatch.presentation.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.githatch.R
import com.example.githatch.data.model.repo.Repo
import com.example.githatch.databinding.ActivityHomeBinding
import com.example.githatch.databinding.ActivityRepoBinding
import com.example.githatch.presentation.detail.DetailActivity
import com.example.githatch.presentation.repo.RepoActivity

class HomeActivity: AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        binding.apply {
            btnBrowse.setOnClickListener {
                launchRepoActivity()
            }


        }
    }

    private fun launchRepoActivity() {
        val intent = Intent(this, RepoActivity::class.java)
        startActivity(intent)
    }



}