package com.nowni.to_do.presentation.task.ui.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpandableSearchAppBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
) {
    var isSearchActive by remember { mutableStateOf(false) }

    AnimatedContent(
        targetState = isSearchActive, label = "SearchBarAnimation"
    ) { active ->
        when (active) {
            true -> SearchField(
                query = searchQuery,
                onQueryChange = onSearchQueryChange,
                onClose = {
                    onSearchQueryChange("")
                    isSearchActive = false
                })

            false -> DefaultAppBar(
                onSearchClick = { isSearchActive = true })
        }
    }


}

@Composable
fun SearchField(
    query: String, onQueryChange: (String) -> Unit, onClose: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(64.dp),
        tonalElevation = 30.dp
    ) {
        TextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .focusRequester(focusRequester),
            placeholder = { Text("Search task") },
            singleLine = true,
            leadingIcon = {
                IconButton(
                    onClick = {
                        keyboardController?.hide()
                        onClose()
                    }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null
                    )
                }
            })

    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultAppBar(
    onSearchClick: () -> Unit,
) {
    TopAppBar(title = { Text("T0-Do") }, actions = {
        IconButton(
            onClick = onSearchClick
        ) {
            Icon(
                imageVector = Icons.Default.Search, contentDescription = null
            )
        }
    })
}


