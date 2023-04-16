package com.farhanrv.thestory.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "story_entity")
data class StoryEntity(

    @PrimaryKey
    val id: String,

    val name: String? = null,

    val description: String? = null,

    @ColumnInfo(name = "photo_url")
    val photoUrl: String? = null,

    @ColumnInfo(name = "created_at")
    val createdAt: String? = null,

    val lat: Double? = null,

    val lon: Double? = null,
) : Parcelable