package org.example.blogmultiplatform.sections

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import org.example.blogmultiplatform.components.PostPreview
import org.example.blogmultiplatform.models.ApiListResponse
import org.example.blogmultiplatform.models.PostWithoutDetails
import org.example.blogmultiplatform.models.Theme
import org.example.blogmultiplatform.utils.Constants.PAGE_WIDTH
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px

@Composable
fun MainSection(
    breakpoint: Breakpoint,
    posts: ApiListResponse
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .backgroundColor(Theme.Secondary.rgb),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .maxWidth(PAGE_WIDTH.px),
            contentAlignment = Alignment.Center
        ) {
            when (posts) {
                is ApiListResponse.Idle -> {}
                is ApiListResponse.Success -> {
                    MainPosts(breakpoint = breakpoint, posts = posts.data)
                }

                is ApiListResponse.Error -> {}
            }
        }
    }
}

@Composable
fun MainPosts(
    breakpoint: Breakpoint,
    posts: List<PostWithoutDetails>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(
                if (breakpoint > Breakpoint.MD) 80.percent
                else 90.percent
            )
            .margin(topBottom = 50.px)
    ) {
        if(breakpoint == Breakpoint.XL) {
            PostPreview(
                post = posts.first(),
                darkTheme = true,
                thumbnailHeight = 640.px
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth(80.percent)
                    .margin(left = 20.px)
            ) {
                posts.drop(1).forEach {
                    PostPreview(
                        post = it,
                        darkTheme = true,
                        vertical = false,
                        thumbnailHeight = 200.px,
                        titleMaxLines = 1
                    )
                }
            }
        } else if(breakpoint >= Breakpoint.LG) {
            Box(modifier = Modifier.margin(right = 10.px)) {
                PostPreview(
                    post = posts.first(),
                    darkTheme = true
                )
            }
            Box(modifier = Modifier.margin(left = 10.px)) {
                PostPreview(
                    post = posts[1],
                    darkTheme = true
                )
            }
        } else {
            PostPreview(
                post = posts.first(),
                darkTheme = true,
                thumbnailHeight = 640.px
            )
        }
    }
}