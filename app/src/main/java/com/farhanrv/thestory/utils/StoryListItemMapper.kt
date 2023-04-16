package com.farhanrv.thestory.utils

import com.farhanrv.thestory.data.network.response.ListStoryItemResponse
import com.farhanrv.thestory.model.StoriesList

object StoryListItemMapper {
    fun mapResponseToDomain(input: List<ListStoryItemResponse>): List<StoriesList> {
        val imageList = ArrayList<StoriesList>()
        input.map {
            val image = StoriesList(
                photoUrl = it.photoUrl,
                createdAt = it.createdAt,
                name = it.name,
                description = it.description,
                lon = it.lon,
                lat = it.lat,
                id = it.id
            )
            imageList.add(image)
        }
        return imageList
    }
}