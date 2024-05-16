package com.example.androidapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.androidapp.models.Category
import com.example.androidapp.ui.screens.category.CategoryScreen
import com.example.androidapp.ui.screens.category.CategoryViewModel
import com.example.androidapp.ui.screens.category.categoryRoute
import com.example.androidapp.ui.screens.detail.DetailsScreen
import com.example.androidapp.ui.screens.detail.detailsRoute
import com.example.androidapp.ui.screens.home.HomeScreen
import com.example.androidapp.ui.screens.home.HomeViewModel
import com.example.androidapp.ui.screens.home.homeRoute
import org.example.blogmultiplatform.Constants.SHOW_SECTIONS_PARAM

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        homeRoute(
            onCategorySelect = { category ->
                navController.navigate(Screen.Category.passCategory(category))
            },
            onPostClick = { postId ->
                navController.navigate(Screen.Details.passPostId(postId))
            }
        )
        categoryRoute(
            onBackPress = { navController.popBackStack() },
            onPostClick = { postId ->
                navController.navigate(Screen.Details.passPostId(postId))
            }
        )
        detailsRoute(onBackPress = { navController.popBackStack() })
    }
}