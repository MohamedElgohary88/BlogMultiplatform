package org.example.blogmultiplatform.navigation

sealed class Screen(val route: String) {
    object AdminHome: Screen(route = "/admin/home")
    object AdminLogin: Screen(route = "/admin/login")
    object AdminCreate: Screen(route = "/admin/create")
    object AdminMyPosts: Screen(route = "/admin/myposts")
    object AdminSuccess: Screen(route = "/admin/success")
}