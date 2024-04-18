package com.github.se.stepquest.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.github.se.stepquest.Routes
import com.github.se.stepquest.ui.navigation.NavigationActions
import com.github.se.stepquest.ui.navigation.TopLevelDestination

data class Friend(val name: String, val profilePictureUrl: String, val status: Boolean)

@Composable
fun FriendsListScreen(friendsList: List<Friend>, navigationActions: NavigationActions) {
  val blueThemeColor = colorResource(id = R.color.blueTheme)
  var showAddFriendScreen by remember { mutableStateOf(false) }
  if (showAddFriendScreen) {
    AddFriendScreen(onDismiss = { showAddFriendScreen = false })
  } else {
    Surface(color = Color.White, modifier = Modifier.fillMaxSize()) {
      Column(
          modifier = Modifier.padding(16.dp).fillMaxWidth(),
          horizontalAlignment = Alignment.CenterHorizontally) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically) {
                  Text(
                      text = "Back",
                      fontSize = 20.sp,
                      modifier =
                          Modifier.clickable {
                            navigationActions.navigateTo(
                                TopLevelDestination(Routes.FriendsListScreen.routName))
                          })
                }
            Text(text = "Friends", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { showAddFriendScreen = true },
                modifier = Modifier.padding(top = 8.dp).align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(blueThemeColor)) {
                  Text(text = "Add Friends", color = Color.White)
                }
            LazyColumn { items(friendsList) { friend -> FriendItem(friend = friend) } }
          }
    }
  }
}

@Composable
fun FriendItem(friend: Friend) {
  val blueThemeColor = colorResource(id = R.color.blueTheme)
  val backgroundColor = if (friend.status) blueThemeColor else Color.Gray
  val status = if (friend.status) "ONLINE" else "OFFLINE"
  Surface(
      color = backgroundColor,
      modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth().padding(horizontal = 8.dp),
      shape = MaterialTheme.shapes.medium) {
        Row(
            modifier = Modifier.padding(8.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {
              // Display friend's profile picture here if needed
              Text(
                  text = friend.name,
                  modifier = Modifier.weight(1f),
                  color = Color.White,
                  style = MaterialTheme.typography.body1)
              Text(text = status, color = Color.White, style = MaterialTheme.typography.body1)
            }
      }
}
