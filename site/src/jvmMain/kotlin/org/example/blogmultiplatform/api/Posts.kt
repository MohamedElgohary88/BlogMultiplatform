package org.example.blogmultiplatform.api

import com.varabyte.kobweb.api.Api
import com.varabyte.kobweb.api.ApiContext
import com.varabyte.kobweb.api.data.getValue
import com.varabyte.kobweb.api.http.setBodyText
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import org.example.blogmultiplatform.data.MongoDB
import org.example.blogmultiplatform.models.ApiListResponse
import org.example.blogmultiplatform.models.ApiResponse
import org.example.blogmultiplatform.models.Constants.POST_ID_PARAM
import org.example.blogmultiplatform.models.Constants.QUERY_PARAM
import org.example.blogmultiplatform.models.Constants.SKIP_PARAM
import org.example.blogmultiplatform.models.Post
import org.litote.kmongo.id.ObjectIdGenerator


@Api(routeOverride = "addpost")
suspend fun addPost(context: ApiContext) {
    try {
        val post = context.req.body?.decodeToString()?.let { Json.decodeFromString<Post>(it) }
        val newPost = post?.copy(id = ObjectIdGenerator.newObjectId<String>().id.toHexString())
        context.res.setBodyText(
            newPost?.let {
                context.data.getValue<MongoDB>().addPost(it).toString()
            } ?: false.toString()
        )
    } catch (e: Exception) {
        context.res.setBodyText(Json.encodeToString(e.message))
    }
}

@Api(routeOverride = "readmyposts")
suspend fun readMyPosts(context: ApiContext) {
    try {
        val skip = context.req.params["skip"]?.toInt() ?: 0
        val author = context.req.params["author"] ?: ""
        val myPosts = context.data.getValue<MongoDB>().readMyPosts(
            skip = skip,
            author = author
        )
        context.res.setBodyText(
            Json.encodeToString(ApiListResponse.Success(data = myPosts))
        )
    } catch (e: Exception) {
        context.res.setBodyText(
            Json.encodeToString(
                ApiListResponse.Error(message = e.message.toString())
            )
        )
    }
}

@Api(routeOverride = "deleteselectedposts")
suspend fun deleteSelectedPosts(context: ApiContext) {
    try {
        val request =
            context.req.body?.decodeToString()?.let { Json.decodeFromString<List<String>>(it) }
        context.res.setBodyText(
            request?.let {
                context.data.getValue<MongoDB>().deleteSelectedPosts(ids = it).toString()
            } ?: "false"
        )
    } catch (e: Exception) {
        context.res.setBodyText(Json.encodeToString(e.message))
    }
}

@Api(routeOverride = "searchposts")
suspend fun searchPostsByTitle(context: ApiContext) {
    try {
        val query = context.req.params[QUERY_PARAM] ?: ""
        val skip = context.req.params[SKIP_PARAM]?.toInt() ?: 0
        val request = context.data.getValue<MongoDB>().searchPostsByTittle(
            query = query,
            skip = skip
        )
        context.res.setBodyText(
            Json.encodeToString(
                ApiListResponse.Success(
                    data = request
                )
            )
        )
    } catch (e: Exception) {
        context.res.setBodyText(
            Json.encodeToString(ApiListResponse.Error(message = e.message.toString()))
        )
    }
}

@Api(routeOverride = "readselectedpost")
suspend fun readSelectedPost(context: ApiContext) {
    val postId = context.req.params[POST_ID_PARAM]
    if (!postId.isNullOrEmpty()) {
        try {
            val selectedPost = context.data.getValue<MongoDB>().readSelectedPost(id = postId)
            context.res.setBodyText(
                Json.encodeToString(ApiResponse.Success(data = selectedPost))
            )
        } catch (e: Exception) {
            context.res.setBodyText(
                Json.encodeToString(ApiResponse.Error(message = e.message.toString()))
            )
        }
    } else {
        context.res.setBodyText(
            Json.encodeToString(ApiResponse.Error(message = "Selected Post does not exist."))
        )
    }
}
