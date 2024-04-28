package org.example.blogmultiplatform.styles

import com.varabyte.kobweb.compose.css.CSSTransition
import com.varabyte.kobweb.compose.css.TransitionProperty
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.transition
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.silk.components.style.ComponentStyle
import org.example.blogmultiplatform.models.Theme
import org.example.blogmultiplatform.utils.Id
import org.jetbrains.compose.web.css.ms

val NavigationItemStyle by ComponentStyle {
    cssRule(" > #${Id.SVG_PARENT} > #${Id.VECTOR_ICON}") {
        Modifier
            .transition(CSSTransition(property = TransitionProperty.All, duration = 300.ms))
            .styleModifier {
                property("stroke", Theme.White.hex)
            }
    }
    cssRule(":hover > #${Id.SVG_PARENT} > #${Id.VECTOR_ICON}") {
        Modifier
            .styleModifier {
                property("stroke", Theme.Primary.hex)
            }
    }
    cssRule(" > #${Id.NAVIGATION_TEXT}") {
        Modifier
            .transition(CSSTransition(property = TransitionProperty.All, duration = 300.ms))
            .color(Theme.White.rgb)
    }
    cssRule(":hover > #${Id.NAVIGATION_TEXT}") {
        Modifier.color(Theme.Primary.rgb)
    }
}