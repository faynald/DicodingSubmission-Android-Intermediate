package com.farhanrv.thestory.data.network

import android.util.Log
import com.farhanrv.thestory.data.network.api.ApiResponse
import kotlinx.coroutines.flow.*

abstract class NetworkBoundResource<ResultType, RequestType> {

    private var result: Flow<ApiResource<ResultType>> = flow {
        emit(ApiResource.Loading())
        if (shouldFetch()) {
            emit(ApiResource.Loading())
            when (val apiResponse = createCall().first()) {
                is ApiResponse.Success -> {
                    emitAll(emitFromNetwork(apiResponse.data).map { ApiResource.Success(it) })
                    Log.i("NetworkBoundResource", "ApiResponse.Success")
                }
                is ApiResponse.Empty -> {
                    Log.e("NetworkBoundResource", "ApiResponse.Empty")
                }
                is ApiResponse.Error -> {
                    onFetchFailed()
                    emit(ApiResource.Error(apiResponse.errorMessage))
                }
            }
        } else {
            Log.e("Internet Connection", "False")
        }
    }

    protected open fun onFetchFailed() {}

    protected abstract suspend fun emitFromNetwork(data: RequestType): Flow<ResultType>

    protected abstract fun shouldFetch(): Boolean

    protected abstract suspend fun createCall(): Flow<ApiResponse<RequestType>>

    fun asFlow(): Flow<ApiResource<ResultType>> = result
}