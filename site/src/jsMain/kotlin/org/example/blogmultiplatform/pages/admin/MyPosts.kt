package org.example.blogmultiplatform.pages.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TransitionProperty
import com.varabyte.kobweb.compose.css.Visibility
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.modifiers.visibility
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.forms.Switch
import com.varabyte.kobweb.silk.components.forms.SwitchSize
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.browser.document
import kotlinx.coroutines.launch
import org.example.blogmultiplatform.components.AdminPageLayout
import org.example.blogmultiplatform.components.PostsView
import org.example.blogmultiplatform.components.SearchBar
import org.example.blogmultiplatform.models.ApiListResponse
import org.example.blogmultiplatform.models.Constants.POSTS_PER_PAGE
import org.example.blogmultiplatform.models.Constants.QUERY_PARAM
import org.example.blogmultiplatform.models.PostWithoutDetails
import org.example.blogmultiplatform.models.Theme
import org.example.blogmultiplatform.navigation.Screen
import org.example.blogmultiplatform.utils.Constants.FONT_FAMILY
import org.example.blogmultiplatform.utils.Constants.SIDE_PANEL_WIDTH
import org.example.blogmultiplatform.utils.Id
import org.example.blogmultiplatform.utils.deleteSelectedPosts
import org.example.blogmultiplatform.utils.fetchMyPosts
import org.example.blogmultiplatform.utils.isUserLoggedIn
import org.example.blogmultiplatform.utils.noBorder
import org.example.blogmultiplatform.utils.parseSwitchText
import org.example.blogmultiplatform.utils.searchPostsByTitle
import org.jetbrains.compose.web.css.ms
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Button
import org.w3c.dom.HTMLInputElement

@Page(routeOverride = "myposts")
@Composable
fun MyPostsPage() {
    isUserLoggedIn {
        MyPostsScreen()
    }
}

@Composable
fun MyPostsScreen() {
    val context = rememberPageContext()
    val breakpoint = rememberBreakpoint()
    val scope = rememberCoroutineScope()
    val selectedPosts = remember { mutableStateListOf<String>() }
    val myPosts = remember { mutableStateListOf<PostWithoutDetails>() }

    var postsToSkip by remember { mutableStateOf(0) }
    var showMoreVisibility by remember { mutableStateOf(false) }
    var selectableMode by remember { mutableStateOf(false) }
    var switchText by remember { mutableStateOf("Select") }

    val hasParams = remember(key1 = context.route) { context.route.params.containsKey(QUERY_PARAM) }
    val query = remember(key1 = context.route) { context.route.params[QUERY_PARAM] ?: "" }

    LaunchedEffect(context.route) {
        postsToSkip = 0
        if (hasParams) {
            (document.getElementById(Id.ADMIN_SEARCH_BAR) as HTMLInputElement).value =
                query.replace("%20", " ")
            searchPostsByTitle(
                query = query,
                skip = postsToSkip,
                onSuccess = {
                    if (it is ApiListResponse.Success) {
                        myPosts.clear()
                        myPosts.addAll(it.data)
                        postsToSkip += POSTS_PER_PAGE
                        showMoreVisibility = it.data.size >= POSTS_PER_PAGE
                    }
                },
                onError = {
                    println(it)
                }
            )
        } else {
            fetchMyPosts(
                skip = postsToSkip,
                onSuccess = {
                    if (it is ApiListResponse.Success) {
                        myPosts.clear()
                        myPosts.addAll(it.data)
                        postsToSkip += POSTS_PER_PAGE
                        showMoreVisibility = it.data.size >= POSTS_PER_PAGE
                    }
                },
                onError = {
                    println(it)
                }
            )
        }
    }
    AdminPageLayout {
        Column(
            modifier = Modifier
                .margin(topBottom = 50.px)
                .fillMaxSize()
                .padding(left = if (breakpoint > Breakpoint.MD) SIDE_PANEL_WIDTH.px else 0.px),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(
                        if (breakpoint > Breakpoint.MD) 30.percent
                        else 50.percent
                    ).margin(bottom = 24.px),
                contentAlignment = Alignment.Center
            ) {
                SearchBar(breakpoint = breakpoint, modifier = Modifier
                    .visibility(if (selectableMode) Visibility.Hidden else Visibility.Visible)
                    .transition(
                        CSSTransition(
                            property = TransitionProperty.All,
                            duration = 200.ms
                        )
                    ),
                    onEnterClick = {
                        val searchInput =
                            (document.getElementById(Id.ADMIN_SEARCH_BAR) as HTMLInputElement).value
                        if (searchInput.isNotEmpty()) {
                            context.router.navigateTo(Screen.AdminMyPosts.searchByTitle(query = searchInput))
                        } else {
                            context.router.navigateTo(Screen.AdminMyPosts.route)
                        }
                    },
                    onSearchIconClick = {}
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(90.percent)
                    .margin(bottom = 24.px),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Switch(
                        modifier = Modifier.margin(right = 8.px),
                        size = SwitchSize.LG,
                        checked = selectableMode,
                        onCheckedChange = {
                            selectableMode = it
                            if (!selectableMode) {
                                switchText = "Select"
                                selectedPosts.clear()
                            } else {
                                switchText = "0 Posts Selected"
                            }
                        }
                    )
                    SpanText(
                        modifier = Modifier.color(if (selectableMode) Colors.Black else Theme.HalfBlack.rgb),
                        text = switchText
                    )
                }
                Button(
                    attrs = Modifier
                        .height(54.px)
                        .padding(leftRight = 24.px)
                        .backgroundColor(Theme.Red.rgb)
                        .color(Colors.White)
                        .noBorder()
                        .borderRadius(r = 4.px)
                        .fontFamily(FONT_FAMILY)
                        .fontSize(14.px)
                        .fontWeight(FontWeight.Medium)
                        .visibility(if (selectedPosts.isNotEmpty()) Visibility.Visible else Visibility.Hidden)
                        .onClick {
                            scope.launch {
                                val result = deleteSelectedPosts(ids = selectedPosts)
                                if (result) {
                                    selectableMode = false
                                    switchText = "Select"
                                    postsToSkip -= selectedPosts.size
                                    selectedPosts.forEach { deletedPostId ->
                                        myPosts.removeAll {
                                            it.id == deletedPostId
                                        }
                                    }
                                    selectedPosts.clear()
                                }
                            }
                        }
                        .toAttrs()
                ) {
                    SpanText(text = "Delete")
                }
            }
            PostsView(
                breakpoint = breakpoint,
                posts = myPosts,
                selectableMode = selectableMode,
                onSelect = {
                    selectedPosts.add(it)
                    switchText = parseSwitchText(selectedPosts.toList())
                },
                onDeselect = {
                    selectedPosts.remove(it)
                    switchText = parseSwitchText(selectedPosts.toList())
                },
                showMoreVisibility = showMoreVisibility,
                onShowMore = {
                    scope.launch {
                        if (hasParams) {
                            searchPostsByTitle(
                                query = query,
                                skip = postsToSkip,
                                onSuccess = {
                                    if (it is ApiListResponse.Success) {
                                        if (it.data.isNotEmpty()) {
                                            myPosts.addAll(it.data)
                                            postsToSkip += POSTS_PER_PAGE
                                            showMoreVisibility = it.data.size >= POSTS_PER_PAGE
                                        } else {
                                            showMoreVisibility = false
                                        }
                                    }
                                },
                                onError = {
                                    println(it)
                                }
                            )
                        } else {
                            fetchMyPosts(
                                skip = postsToSkip,
                                onSuccess = {
                                    if (it is ApiListResponse.Success) {
                                        if (it.data.isNotEmpty()) {
                                            myPosts.addAll(it.data)
                                            postsToSkip += POSTS_PER_PAGE
                                            showMoreVisibility = it.data.size >= POSTS_PER_PAGE
                                        } else {
                                            showMoreVisibility = false
                                        }
                                    }
                                },
                                onError = {
                                    println(it)
                                }
                            )
                        }
                    }
                },
                onClick = { context.router.navigateTo(Screen.AdminCreate.passPostId(id = it)) }
            )
        }
    }
}