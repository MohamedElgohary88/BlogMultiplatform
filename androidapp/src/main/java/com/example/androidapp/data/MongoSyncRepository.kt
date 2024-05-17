package com.example.androidapp.data

import com.example.androidapp.models.Category
import com.example.androidapp.models.post
import com.example.androidapp.util.RequestState
import kotlinx.coroutines.flow.Flow

interface MongoSyncRepository {
    fun configureTheRealm()
    fun readAllPosts(): Flow<RequestState<List<post>>>
    suspend fun addSamplePost()
    fun searchPostsByTitle(query: String): Flow<RequestState<List<post>>>
    fun searchPostsByCategory(category: Category): Flow<RequestState<List<post>>>
}