package com.example.androidapp.data

import com.example.androidapp.models.Post
import com.example.androidapp.util.RequestState
import kotlinx.coroutines.flow.Flow

interface MongoSyncRepository {
    fun configureTheRealm()
    fun readAllPosts(): Flow<RequestState<List<Post>>>
    suspend fun addSamplePost()
    fun searchPostsByTitle(query: String): Flow<RequestState<List<Post>>>
}