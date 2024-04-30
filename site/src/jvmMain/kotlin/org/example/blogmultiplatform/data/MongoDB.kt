package org.example.blogmultiplatform.data

import com.varabyte.kobweb.api.data.add
import com.varabyte.kobweb.api.init.InitApi
import com.varabyte.kobweb.api.init.InitApiContext
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitLast
import org.example.blogmultiplatform.models.Constants.POSTS_PER_PAGE
import org.example.blogmultiplatform.models.Post
import org.example.blogmultiplatform.models.PostWithoutDetails
import org.example.blogmultiplatform.models.User
import org.example.blogmultiplatform.util.Constants.DATABASE_NAME
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.toList
import org.litote.kmongo.descending
import org.litote.kmongo.eq
import org.litote.kmongo.`in`
import org.litote.kmongo.reactivestreams.KMongo
import org.litote.kmongo.reactivestreams.getCollection
import org.litote.kmongo.regex

@InitApi
fun initMongoDB(context: InitApiContext) {
    System.setProperty(
        "org.litote.mongo.test.mapping.service",
        "org.litote.kmongo.serialization.SerializationClassMappingTypeService"
    )
    context.data.add(MongoDB(context))
}

class MongoDB(private val context: InitApiContext) : MongoRepository {
    private val client = KMongo.createClient()
    private val database = client.getDatabase(DATABASE_NAME)
    private val userCollection = database.getCollection<User>()
    private val postCollection = database.getCollection<Post>()

    override suspend fun addPost(post: Post): Boolean {
        return postCollection.insertOne(post).awaitFirst().wasAcknowledged()
    }

    override suspend fun readMyPosts(skip: Int, author: String): List<PostWithoutDetails> {
        return postCollection
            .withDocumentClass(PostWithoutDetails::class.java)
            .find(PostWithoutDetails::author eq author)
            .sort(descending(PostWithoutDetails::date))
            .skip(skip)
            .limit(POSTS_PER_PAGE)
            .toList()
    }

    override suspend fun deleteSelectedPosts(ids: List<String>): Boolean {
        return postCollection
            .deleteMany(Post::id `in` ids)
            .awaitLast()
            .wasAcknowledged()
    }

    override suspend fun checkUserExistence(user: User): User? {
        return try {
            userCollection
                .find(
                    and(
                        User::username eq user.username,
                        User::password eq user.password
                    )
                ).awaitFirst()
        } catch (e: Exception) {
            context.logger.error(e.message.toString())
            null
        }
    }

    override suspend fun searchPostsByTittle(query: String, skip: Int): List<PostWithoutDetails> {
        val regexQuery = query.toRegex(RegexOption.IGNORE_CASE)
        return postCollection
            .withDocumentClass(PostWithoutDetails::class.java)
            .find(PostWithoutDetails::title regex regexQuery)
            .sort(descending(PostWithoutDetails::date))
            .skip(skip)
            .limit(POSTS_PER_PAGE)
            .toList()
    }

    override suspend fun readSelectedPost(id: String): Post {
        return postCollection.find(Post::id eq id).toList().first()
    }

    override suspend fun checkUserId(id: String): Boolean {
        return try {
            val documentCount = userCollection.countDocuments(User::id eq id).awaitFirst()
            documentCount > 0
        } catch (e: Exception) {
            context.logger.error(e.message.toString())
            false
        }
    }


}