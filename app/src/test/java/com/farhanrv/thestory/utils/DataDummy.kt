package com.farhanrv.thestory.utils

import com.farhanrv.thestory.data.local.entity.StoryEntity

object DataDummy {

    fun generateDummyListStory(): List<StoryEntity> {
        val items = arrayListOf<StoryEntity>()

        for (i in 0 until 10) {
            val story = StoryEntity(
                id = "story-Wvj1hSanFLeKOqMX",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1678431014734_gh45DBNF.jpg",
                createdAt = "2023-03-09T10:25:18.598Z",
                name = "Farhan",
                description = "Lorem Ipsum Dolor Sit Amet",
                lon = -10.402,
                lat = -15.612
            )

            items.add(story)
        }

        return items
    }

    fun generateToken(): String =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXBtY00wbEp5MUVXSFZRSWsiLCJpYXQiOjE2Nzg0MzA2MTR9.kvB5Ola8C4CfguL9ip8SoBPzQU8PnZBFL1L_53pWPwg"
}