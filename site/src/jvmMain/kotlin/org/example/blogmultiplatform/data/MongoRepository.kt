package org.example.blogmultiplatform.data

import org.example.blogmultiplatform.models.Category
import org.example.blogmultiplatform.models.Newsletter
import org.example.blogmultiplatform.models.Post
import org.example.blogmultiplatform.models.PostWithoutDetails
import org.example.blogmultiplatform.models.User

interface MongoRepository {
    suspend fun addPost(post: Post): Boolean
    suspend fun readMyPosts(skip: Int, author: String): List<PostWithoutDetails>
    suspend fun subscribe(newsletter: Newsletter): String
    suspend fun searchPostsByCategory(category: Category, skip: Int): List<PostWithoutDetails>
    suspend fun updatePost(post: Post): Boolean
    suspend fun deleteSelectedPosts(ids: List<String>): Boolean
    suspend fun checkUserExistence(user: User): User?
    suspend fun searchPostsByTittle(query: String, skip: Int): List<PostWithoutDetails>
    suspend fun readSponsoredPosts(): List<PostWithoutDetails>
    suspend fun readPopularPosts(skip: Int): List<PostWithoutDetails>
    suspend fun readSelectedPost(id: String): Post
    suspend fun readLatestPosts(skip: Int): List<PostWithoutDetails>
    suspend fun readMainPosts(): List<PostWithoutDetails>
    suspend fun checkUserId(id: String): Boolean
}