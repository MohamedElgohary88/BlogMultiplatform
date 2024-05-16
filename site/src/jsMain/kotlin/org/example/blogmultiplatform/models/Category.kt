package org.example.blogmultiplatform.models


enum class Category(override val color: String) : CategoryCommon {
    Technology(color = Theme.Green.hex),
    Programming(color = Theme.Yellow.hex),
    Design(color = Theme.Purple.hex)
}