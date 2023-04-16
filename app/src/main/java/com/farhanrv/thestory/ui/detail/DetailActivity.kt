package com.farhanrv.thestory.ui.detail

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.farhanrv.thestory.data.local.entity.StoryEntity
import com.farhanrv.thestory.databinding.ActivityDetailBinding
import com.farhanrv.thestory.model.StoriesList

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val story = intent.getParcelableExtra<StoryEntity>("EXTRA")
        story?.let { data ->
            binding.tvItemTitle.text = data.name
            binding.tvItemDescription.text = data.description
            Glide.with(this@DetailActivity)
                .load(data.photoUrl)
                .into(binding.ivItemImage)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}