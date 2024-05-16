package com.example.androidapp.ui.screens.detail

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.androidapp.navigation.Screen
import com.example.androidapp.util.Constants.POST_ID_ARGUMENT
import org.example.blogmultiplatform.Constants

fun NavGraphBuilder.detailsRoute(
    onBackPress: () -> Unit
) {
    composable(
        route = Screen.Details.route,
        arguments = listOf(navArgument(name = POST_ID_ARGUMENT) {
            type = NavType.StringType
        })
    ) {
        val postId = it.arguments?.getString(POST_ID_ARGUMENT)
        DetailsScreen(
            url = "http://192.168.1.2:8080/posts/post?${POST_ID_ARGUMENT}=$postId&${Constants.SHOW_SECTIONS_PARAM}=false",
            onBackPress = onBackPress
        )
    }
}