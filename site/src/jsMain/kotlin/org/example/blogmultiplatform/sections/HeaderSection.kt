package org.example.blogmultiplatform.sections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.icons.fa.FaBars
import com.varabyte.kobweb.silk.components.icons.fa.FaXmark
import com.varabyte.kobweb.silk.components.icons.fa.IconSize
import com.varabyte.kobweb.silk.components.style.breakpoint.Breakpoint
import kotlinx.browser.document
import org.example.blogmultiplatform.components.CategoryNavigationItems
import org.example.blogmultiplatform.components.SearchBar
import org.example.blogmultiplatform.models.Category
import org.example.blogmultiplatform.models.Theme
import org.example.blogmultiplatform.navigation.Screen
import org.example.blogmultiplatform.utils.Constants.HEADER_HEIGHT
import org.example.blogmultiplatform.utils.Constants.PAGE_WIDTH
import org.example.blogmultiplatform.utils.Id
import org.example.blogmultiplatform.utils.Res
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.w3c.dom.HTMLInputElement

@Composable
fun HeaderSection(
    breakpoint: Breakpoint,
    onMenuOpen: () -> Unit,
    selectedCategory: Category? = null,
    logo: String = Res.Image.LOGO_HOME,
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
                .backgroundColor(Theme.Secondary.rgb)
                .maxWidth(PAGE_WIDTH.px),
            contentAlignment = Alignment.TopCenter
        ) {
            Header(
                breakpoint = breakpoint,
                onMenuOpen = onMenuOpen,
                logo = logo,
                selectedCategory = selectedCategory,
            )
        }
    }
}

@Composable
fun Header(
    breakpoint: Breakpoint,
    onMenuOpen: () -> Unit,
    selectedCategory: Category? = null,
    logo: String = Res.Image.LOGO_HOME,
) {
    val context = rememberPageContext()
    var fullSearchBarOpened by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth(if (breakpoint > Breakpoint.MD) 80.percent else 90.percent)
            .height(HEADER_HEIGHT.px),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (breakpoint <= Breakpoint.MD) {
            if (fullSearchBarOpened) {
                FaXmark(
                    modifier = Modifier
                        .margin(right = 24.px)
                        .color(Colors.White)
                        .cursor(Cursor.Pointer)
                        .onClick { fullSearchBarOpened = false },
                    size = IconSize.XL
                )
            }
            if (!fullSearchBarOpened) {
                FaBars(
                    modifier = Modifier
                        .margin(right = 24.px)
                        .color(Colors.White)
                        .cursor(Cursor.Pointer)
                        .onClick { onMenuOpen() },
                    size = IconSize.XL
                )
            }
        }
        if (!fullSearchBarOpened) {
            Image(
                modifier = Modifier
                    .margin(right = 50.px)
                    .width(if (breakpoint >= Breakpoint.SM) 100.px else 70.px)
                    .cursor(Cursor.Pointer)
                    .onClick { context.router.navigateTo(Screen.HomePage.route) },
                src = logo,
                description = "Logo Image"
            )
        }
        if (breakpoint >= Breakpoint.LG) {
            CategoryNavigationItems(selectedCategory = selectedCategory)
        }
        Spacer()
        SearchBar(
            breakpoint = breakpoint,
            fullWidth = fullSearchBarOpened,
            darkTheme = true,
            onEnterClick = {
                val query = (document.getElementById(Id.ADMIN_SEARCH_BAR) as HTMLInputElement).value
                context.router.navigateTo(Screen.SearchPage.searchByTitle(query = query))
            },
            onSearchIconClick = { fullSearchBarOpened = it }
        )
    }
}