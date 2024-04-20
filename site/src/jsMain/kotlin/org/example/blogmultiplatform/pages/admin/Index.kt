package org.example.blogmultiplatform.pages.admin

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.core.Page
import org.example.blogmultiplatform.components.SidePanel
import org.example.blogmultiplatform.utils.Constants.PAGE_WIDTH
import org.example.blogmultiplatform.utils.isUserLoggedIn
import org.jetbrains.compose.web.css.px

@Page(routeOverride = "home")
@Composable
fun HomeScreen() {
    isUserLoggedIn {
        HomePage()
    }
}

@Composable
fun HomePage() {
    println("Admin Home Page")
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .maxWidth(PAGE_WIDTH.px)
        ) {
            SidePanel()
        }
    }
}