package com.farhanrv.thestory.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.farhanrv.thestory.data.local.UserPreference
import com.farhanrv.thestory.data.local.database.StoryDatabase
import com.farhanrv.thestory.data.local.entity.StoryEntity
import com.farhanrv.thestory.data.network.ApiResource
import com.farhanrv.thestory.data.network.NetworkBoundResource
import com.farhanrv.thestory.data.network.NetworkDataSource
import com.farhanrv.thestory.data.network.StoryRemoteMediator
import com.farhanrv.thestory.data.network.api.ApiResponse
import com.farhanrv.thestory.data.network.api.ApiService
import com.farhanrv.thestory.data.network.request.LoginRequest
import com.farhanrv.thestory.data.network.request.SignupRequest
import com.farhanrv.thestory.data.network.response.FileUploadResponse
import com.farhanrv.thestory.data.network.response.ListStoryItemResponse
import com.farhanrv.thestory.data.network.response.LoginResponse
import com.farhanrv.thestory.data.network.response.SignupResponse
import com.farhanrv.thestory.model.StoriesList
import com.farhanrv.thestory.model.User
import com.farhanrv.thestory.utils.StoryListItemMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import okhttp3.MultipartBody
import okhttp3.RequestBody

@OptIn(ExperimentalPagingApi::class)
class AppRepository (
    private val networkDataSource: NetworkDataSource,
    private val databaseSource: StoryDatabase,
    private val preferences: UserPreference,
    private val apiService: ApiService
) {
    fun auth(auth: LoginRequest, result: (LoginResponse?) -> Unit) {
        networkDataSource.auth(auth) {
            result(it)
        }
    }

    fun register(auth: SignupRequest, result: (SignupResponse?) -> Unit) {
        networkDataSource.register(auth) {
            result(it)
        }
    }

    fun getAllStories(token: String): LiveData<PagingData<StoryEntity>> =
        Pager(
            config = PagingConfig(pageSize = 5),
            remoteMediator = StoryRemoteMediator(
                databaseSource,
                apiService,
                token
            ),
            pagingSourceFactory = { databaseSource.storyDao().getStories() }
        ).liveData

    fun getAllStoriesWithMap(token: String): Flow<ApiResource<List<StoriesList>>> =
        object : NetworkBoundResource<List<StoriesList>, List<ListStoryItemResponse>>() {
            override suspend fun emitFromNetwork(data: List<ListStoryItemResponse>): Flow<List<StoriesList>> =
                networkDataSource.getAllStoriesWithMap(token).map {
                    StoryListItemMapper.mapResponseToDomain(data)
                }

            override fun shouldFetch(): Boolean = true

            override suspend fun createCall(): Flow<ApiResponse<List<ListStoryItemResponse>>> =
                networkDataSource.getAllStoriesWithMap(token)
        }.asFlow()

    fun addNewStory(
        token: String,
        description: RequestBody,
        file: MultipartBody.Part,
        result: (FileUploadResponse?) -> Unit) {
        networkDataSource.addNewStory(token, description, file) {
            result(it)
        }
    }

    suspend fun saveUser(token: String, user: User) {
        preferences.saveUser(token, user)
    }

    suspend fun logoutUser() {
        preferences.logoutUser()
    }

    fun getToken() = flow {
        emit(preferences.getToken())
    }

}