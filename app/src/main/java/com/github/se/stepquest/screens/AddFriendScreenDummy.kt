package com.github.se.stepquest.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.se.stepquest.R

val dummyUserList = listOf("Alice", "Bob", "Charlie", "Charles")

@Composable
fun AddFriendScreen(onDismiss: () -> Unit, onSecondScreenDismiss: () -> Unit) {
  val blueThemeColor = colorResource(id = R.color.blueTheme)
  var searchQuery by remember { mutableStateOf("") }
  val searchResults = dummyUserList.filter { it.startsWith(searchQuery, ignoreCase = true) }

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
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Back",
                        fontSize = 20.sp,
                        modifier = Modifier.clickable { onDismiss() })
                    Spacer(modifier = Modifier.weight(0.3f))
                    IconButton(
                        onClick = {
                          onDismiss()
                          onSecondScreenDismiss()
                        },
                        modifier = Modifier.padding(8.dp)) {
                          Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                  }
              Text(
                  text = "Friends",
                  fontWeight = FontWeight.Bold,
                  fontSize = 20.sp,
              )
              Spacer(modifier = Modifier.height(16.dp))
              Text(
                  text = "Search for friends",
                  fontSize = 16.sp,
                  modifier = Modifier.padding(top = 16.dp))
              Spacer(modifier = Modifier.height(16.dp))
              TextField(
                  value = searchQuery,
                  onValueChange = { searchQuery = it },
                  placeholder = { Text("Search for friends") },
                  modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp))
              Spacer(modifier = Modifier.height(16.dp))
              if (searchQuery.isNotBlank()) {
                if (searchResults.isNotEmpty()) {
                  LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(searchResults) { UserItem(name = it) }
                  }
                } else {
                  Text(
                      text = "No users were found.",
                      color = Color.Red,
                      modifier = Modifier.padding(horizontal = 16.dp))
                }
              }
            }
      }
}

@Composable
fun UserItem(name: String) {
  val blueThemeColor = colorResource(id = R.color.blueTheme)
  var isExpanded by remember { mutableStateOf(false) }

  Surface(
      color = blueThemeColor,
      modifier = Modifier.padding(vertical = 6.dp, horizontal = 16.dp).fillMaxWidth(),
      shape = RoundedCornerShape(12.dp)) {
        Column(
            modifier =
                Modifier.run {
                      if (!isExpanded) {
                        clickable { isExpanded = true }
                      } else {
                        this
                      }
                    }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth()) {
              Row(
                  horizontalArrangement = Arrangement.SpaceBetween,
                  modifier = Modifier.fillMaxWidth()) {
                    Text(text = name, color = Color.White, fontSize = 20.sp)
                    if (isExpanded) {
                      Icon(
                          imageVector = Icons.Default.Close,
                          contentDescription = "Close",
                          modifier = Modifier.clickable { isExpanded = false },
                          tint = Color.White)
                    }
                  }
              if (isExpanded) {
                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "Send a friend request",
                    color = Color.White,
                    modifier =
                        Modifier.clickable { /* TODO: send friend request */}.padding(end = 16.dp))
              }
            }
      }
}
