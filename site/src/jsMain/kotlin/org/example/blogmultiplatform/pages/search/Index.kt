package org.example.blogmultiplatform.pages.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.coroutines.launch
import org.example.blogmultiplatform.components.CategoryNavigationItems
import org.example.blogmultiplatform.components.OverflowSidePanel
import org.example.blogmultiplatform.models.ApiListResponse
import org.example.blogmultiplatform.models.Category
import org.example.blogmultiplatform.models.Constants.CATEGORY_PARAM
import org.example.blogmultiplatform.models.Constants.POSTS_PER_PAGE
import org.example.blogmultiplatform.models.PostWithoutDetails
import org.example.blogmultiplatform.sections.HeaderSection
import org.example.blogmultiplatform.sections.PostsSection
import org.example.blogmultiplatform.utils.Constants.FONT_FAMILY
import org.example.blogmultiplatform.utils.Res
import org.example.blogmultiplatform.utils.searchPostsByCategory
import org.jetbrains.compose.web.css.px

@Page(routeOverride = "query")
@Composable
fun SearchPage() {
    val context = rememberPageContext()
    val hasCategoryParam = remember(key1 = context.route) {
        context.route.params.containsKey(CATEGORY_PARAM)
    }
    val breakpoint = rememberBreakpoint()
    val scope = rememberCoroutineScope()
    var overflowOpened by remember { mutableStateOf(false) }
    val searchedPosts = remember { mutableStateListOf<PostWithoutDetails>() }
    var postsToSkip by remember { mutableStateOf(0) }
    var showMorePosts by remember { mutableStateOf(false) }
    val value = remember(key1 = context.route) {
        if (hasCategoryParam) {
            context.route.params.getValue(CATEGORY_PARAM)
        } else {
            ""
        }
    }

    LaunchedEffect(key1 = context.route) {
        postsToSkip = 0
        if (hasCategoryParam) {
            searchPostsByCategory(
                category = Category.valueOf(value),
                skip = postsToSkip,
                onSuccess = { response ->
                    if (response is ApiListResponse.Success) {
                        searchedPosts.clear()
                        searchedPosts.addAll(response.data)
                        postsToSkip += POSTS_PER_PAGE
                        if (response.data.size >= POSTS_PER_PAGE) showMorePosts = true
                    }
                },
                onError = {

                }
            )
        }
    }
    Column(
        modifier = Modifier.fillMaxWidth(),
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
            selectedCategory = Category.valueOf(value),
            logo = Res.Image.LOGO,
            onMenuOpen = { overflowOpened = true }
        )
        if (hasCategoryParam) {
            SpanText(
                modifier = Modifier
                    .fillMaxWidth()
                    .textAlign(TextAlign.Center)
                    .margin(top = 100.px, bottom = 40.px)
                    .fontFamily(FONT_FAMILY)
                    .fontSize(36.px),
                text = value
            )
        }
        PostsSection(
            breakpoint = breakpoint,
            posts = searchedPosts,
            showMoreVisibility = showMorePosts,
            onShowMore = {
                scope.launch {
                    searchPostsByCategory(
                        category = Category.valueOf(value),
                        skip = postsToSkip,
                        onSuccess = { response ->
                            if (response is ApiListResponse.Success) {
                                if (response.data.isNotEmpty()) {
                                    if (response.data.size < POSTS_PER_PAGE) {
                                        showMorePosts = false
                                    }
                                    searchedPosts.addAll(response.data)
                                    postsToSkip += POSTS_PER_PAGE
                                } else {
                                    showMorePosts = false
                                }
                            }
                        },
                        onError = {}
                    )
                }
            },
            onClick = {

            }
        )
    }
}