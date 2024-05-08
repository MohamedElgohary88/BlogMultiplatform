package org.example.blogmultiplatform.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.coroutines.launch
import org.example.blogmultiplatform.components.CategoryNavigationItems
import org.example.blogmultiplatform.components.OverflowSidePanel
import org.example.blogmultiplatform.models.ApiListResponse
import org.example.blogmultiplatform.models.Constants.POSTS_PER_PAGE
import org.example.blogmultiplatform.models.PostWithoutDetails
import org.example.blogmultiplatform.sections.HeaderSection
import org.example.blogmultiplatform.sections.MainSection
import org.example.blogmultiplatform.sections.PostsSection
import org.example.blogmultiplatform.sections.SponsoredPostsSection
import org.example.blogmultiplatform.utils.fetchLatestPosts
import org.example.blogmultiplatform.utils.fetchMainPosts
import org.example.blogmultiplatform.utils.fetchSponsoredPosts

@Page
@Composable
fun HomePage() {
    val scope = rememberCoroutineScope()
    val breakpoint = rememberBreakpoint()
    var overflowOpened by remember { mutableStateOf(false) }
    var mainPosts by remember { mutableStateOf<ApiListResponse>(ApiListResponse.Idle) }
    val sponsoredPosts = remember { mutableStateListOf<PostWithoutDetails>() }
    val latestPosts = remember { mutableStateListOf<PostWithoutDetails>() }
    var latestPostsToSkip by remember { mutableStateOf(0) }
    var showMoreLatest by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        fetchMainPosts(
            onSuccess = { mainPosts = it },
            onError = {}
        )
        fetchLatestPosts(
            skip = latestPostsToSkip,
            onSuccess = { response ->
                if (response is ApiListResponse.Success) {
                    latestPosts.addAll(response.data)
                    latestPostsToSkip += POSTS_PER_PAGE
                    if (response.data.size >= POSTS_PER_PAGE) showMoreLatest = true
                }
            },
            onError = {}
        )
        fetchSponsoredPosts(
            onSuccess = { response ->
                if (response is ApiListResponse.Success) {
                    sponsoredPosts.addAll(response.data)
                }
            },
            onError = {}
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (overflowOpened) {
            OverflowSidePanel(
                onMenuClose = { overflowOpened = false },
                content = { CategoryNavigationItems(vertical = true) }
            )
        }
        HeaderSection(
            breakpoint = breakpoint,
            onMenuOpen = { overflowOpened = true }
        )
        MainSection(breakpoint = breakpoint, posts = mainPosts)
        PostsSection(
            breakpoint = breakpoint,
            posts = latestPosts,
            title = "Latest Posts",
            showMoreVisibility = showMoreLatest,
            onShowMore = {
                scope.launch {
                    fetchLatestPosts(
                        skip = latestPostsToSkip,
                        onSuccess = { response ->
                            if (response is ApiListResponse.Success) {
                                if (response.data.isNotEmpty()) {
                                    if (response.data.size < POSTS_PER_PAGE) {
                                        showMoreLatest = false
                                    }
                                    latestPosts.addAll(response.data)
                                    latestPostsToSkip += POSTS_PER_PAGE
                                } else {
                                    showMoreLatest = false
                                }
                            }
                        },
                        onError = {}
                    )
                }
            },
            onClick = {}
        )
        SponsoredPostsSection(
            breakpoint = breakpoint,
            posts = sponsoredPosts,
            onClick = {}
        )
    }
}