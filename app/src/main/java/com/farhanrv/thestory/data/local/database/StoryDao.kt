package com.farhanrv.thestory.data.local.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.farhanrv.thestory.data.local.entity.StoryEntity

@Dao
interface StoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(vararg story: StoryEntity)

    @Query("SELECT * FROM story_entity")
    fun getStories(): PagingSource<Int, StoryEntity>

    @Query("DELETE FROM story_entity")
    fun deleteAll()
}