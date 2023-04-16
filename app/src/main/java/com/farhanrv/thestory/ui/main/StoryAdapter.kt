package com.farhanrv.thestory.ui.main

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.farhanrv.thestory.data.local.entity.StoryEntity
import com.farhanrv.thestory.databinding.StoriesItemBinding
import com.farhanrv.thestory.ui.detail.DetailActivity

class StoryAdapter : PagingDataAdapter<StoryEntity, StoryAdapter.ItemViewholder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewholder =
        ItemViewholder(
            StoriesItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    override fun onBindViewHolder(holder: StoryAdapter.ItemViewholder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    inner class ItemViewholder(private val binding: StoriesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: StoryEntity) = with(binding) {
            binding.tvItemTitle.text = item.name
            Glide.with(itemView.context)
                .load(item.photoUrl)
                .into(binding.ivItemImage)


            root.setOnClickListener {
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.ivItemImage, "image"),
                        Pair(binding.tvItemTitle, "title"),
                    )
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra("EXTRA", item)
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<StoryEntity>() {
            override fun areItemsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}