package org.example.blogmultiplatform.models

import org.example.blogmultiplatform.CategoryCommon

enum class Category(override val color: String) : CategoryComon {
    Technology(color = Theme.Green.hex),
    Programming(color = Theme.Yellow.hex),
    Design(color = Theme.Purple.hex)
}