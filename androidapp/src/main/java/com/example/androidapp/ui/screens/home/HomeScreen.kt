package com.example.androidapp.ui.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidapp.components.PostCard
import com.example.androidapp.models.Post
import com.example.androidapp.util.RequestState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(posts: RequestState<List<Post>>) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Blog")
                },
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Drawer Icon"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Icon",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    ) {
        if (posts is RequestState.Success) {
            Column {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = it.calculateTopPadding())
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(posts.data.size) { post ->
                        PostCard(post = posts.data[post], onPostClick = {})
                    }
                }
            }
        }
    }
}