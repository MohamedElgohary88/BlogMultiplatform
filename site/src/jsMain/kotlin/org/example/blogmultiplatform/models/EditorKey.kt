package org.example.blogmultiplatform.models

import org.example.blogmultiplatform.utils.Res

enum class EditorKey(
    val icon: String,
) {
    Bold(icon = Res.Icon.bold),
    Italic(icon = Res.Icon.italic),
    Link(icon = Res.Icon.link),
    Title(icon = Res.Icon.title),
    Subtitle(icon = Res.Icon.subtitle),
    Quote(icon = Res.Icon.quote),
    Code(icon = Res.Icon.code),
    Image(icon = Res.Icon.image),
}