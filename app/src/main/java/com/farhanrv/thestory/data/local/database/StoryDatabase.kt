package com.farhanrv.thestory.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.farhanrv.thestory.data.local.entity.RemoteKeys
import com.farhanrv.thestory.data.local.entity.StoryEntity

@Database(
    entities = [StoryEntity::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class StoryDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}