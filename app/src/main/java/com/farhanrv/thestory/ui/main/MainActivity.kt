package com.farhanrv.thestory.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.paging.LoadState
import com.farhanrv.thestory.R
import com.farhanrv.thestory.databinding.ActivityMainBinding
import com.farhanrv.thestory.ui.addstory.AddStoryActivity
import com.farhanrv.thestory.ui.auth.login.LoginActivity
import com.farhanrv.thestory.ui.map.ShowMapActivity
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModel()
    private val rvAdapter = StoryAdapter()
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intentExtra = intent.getStringExtra(EXTRA_LOGIN)

        viewModel.getToken().observe(this) { data ->
            if (intentExtra != null) {
                setupStories(intentExtra)
                token = intentExtra
            } else if (data != "" && data != null) {
                setupStories(data)
                token = data
            } else {
                navigateToLogin()
            }
        }

        binding.fab.setOnClickListener {
            val intent = Intent(this@MainActivity, AddStoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setupStories(token: String) {
        with(binding.rvStory) {
            setHasFixedSize(true)
            adapter = rvAdapter.withLoadStateHeaderAndFooter(
                header = LoadingStateAdapter { rvAdapter.retry() },
                footer = LoadingStateAdapter { rvAdapter.retry() }
            )
        }
        viewModel.getAllStories(token).observe(this) { data ->
            rvAdapter.submitData(lifecycle, data)
        }
        rvAdapter.addLoadStateListener {
            binding.apply {
                rvStory.isVisible = it.source.refresh is LoadState.NotLoading

                //not found
                rvStory.isVisible = !(it.source.refresh is LoadState.NotLoading &&
                        it.append.endOfPaginationReached &&
                        rvAdapter.itemCount < 1)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_logout -> {
                viewModel.logoutUser()
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.menu_map -> {
                val intent = Intent(this@MainActivity, ShowMapActivity::class.java)
                intent.putExtra(ShowMapActivity.EXTRA_TOKEN, token)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val EXTRA_LOGIN = "logged_in"
    }
}