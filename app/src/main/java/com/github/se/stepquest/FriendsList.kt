package com.github.se.stepquest

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

// Step 1: Define a data model for a friend
data class Friend(val name: String, val profilePictureResId: Int)

// Step 2: Create a composable function to represent each friend item
@Composable
fun FriendItem(friend: Friend) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = friend.profilePictureResId),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(50.dp)
                .padding(8.dp)
                .clip(CircleShape)
        )
        Column {
            Text(text = friend.name, style = MaterialTheme.typography.h6)
        }
    }
}

// Step 3: Compose the list of friends using LazyColumn
@Composable
fun FriendsList(friends: List<Friend>) {
    LazyColumn {
        items(friends) { friend ->
            FriendItem(friend = friend)
        }
    }
}
