package com.github.se.stepquest.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

data class Friend(
    val name: String,
    val profilePictureUrl: String,
    val status: Boolean
)

val fakeFriendsList = listOf(
    Friend("Alice", "https://example.com/alice.jpg", true),
    Friend("Bob", "https://example.com/bob.jpg", false),
    Friend("Charlie", "https://example.com/charlie.jpg", true),
    Friend("David", "https://example.com/david.jpg", false)
)

@Composable
fun FriendsListDialog(onDismiss: () -> Unit) {
  Dialog(onDismissRequest = { onDismiss() }) {
    Surface(
        color = Color.White,
        border = BorderStroke(1.dp, Color.Black),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.padding(16.dp)) {
          Column(
              modifier = Modifier.padding(16.dp).fillMaxWidth(),
              horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically) {
                      androidx.compose.material3.Text(text = "Friends", fontSize = 20.sp)
                      IconButton(onClick = { onDismiss() }, modifier = Modifier.padding(8.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                      }
                    }
              }
        }
  }
}
