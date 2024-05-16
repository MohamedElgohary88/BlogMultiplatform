package com.example.androidapp.navigation

import com.example.androidapp.util.Constants.CATEGORY_ARGUMENT
import com.example.androidapp.util.Constants.POST_ID_ARGUMENT
import com.example.androidapp.models.Category as PostCategory

sealed class Screen(val route: String) {
    object Home : Screen(route = "home_screen")
    object Category : Screen(route = "category_screen/{${CATEGORY_ARGUMENT}}") {
        fun passCategory(category: PostCategory) = "category_screen/${category.name}"
    }
    object Details : Screen(route = "details_screen/{${POST_ID_ARGUMENT}}") {
        fun passPostId(id: String) = "details_screen/${id}"
    }
}