package com.example.androidapp.models

import org.example.blogmultiplatform.CategoryCommon

enum class Category(override val color: String): CategoryCommon {
    Programming(color = ""),
    Technology(color = ""),
    Design(color = "")
}