package com.example.androidapp.ui.screens.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidapp.data.MongoSync
import androidx.compose.runtime.State
import com.example.androidapp.models.Post
import com.example.androidapp.util.Constants.APP_ID
import com.example.androidapp.util.RequestState
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _allPosts: MutableState<RequestState<List<Post>>> =
        mutableStateOf(RequestState.Idle)
    val allPosts: State<RequestState<List<Post>>> = _allPosts

    init {
        viewModelScope.launch(Dispatchers.IO) {
            App.create(APP_ID).login(Credentials.anonymous())
            fetchAllPosts()
        }
    }

    private suspend fun fetchAllPosts() {
        viewModelScope.launch {
            MongoSync.readAllPosts().collectLatest {
                _allPosts.value = it
                Log.d("HomeViewModel", it.toString())
            }
        }
    }
}