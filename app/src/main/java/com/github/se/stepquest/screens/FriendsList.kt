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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.github.se.stepquest.Friend
import com.github.se.stepquest.R
import com.github.se.stepquest.Routes
import com.github.se.stepquest.ui.navigation.NavigationActions
import com.github.se.stepquest.ui.navigation.TopLevelDestination
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

@Composable
fun FriendsListScreen(
    navigationActions: NavigationActions,
    userId: String,
    testCurrentFriendsList: List<Friend> = emptyList()
) {
  val blueThemeColor = colorResource(id = R.color.blueTheme)
  var showAddFriendScreen by remember { mutableStateOf(false) }
  var showFriendProfile by remember { mutableStateOf(false) }
  var selectedFriend by remember { mutableStateOf<Friend?>(null) }
  var currentFriendsList by remember { mutableStateOf(testCurrentFriendsList.toMutableList()) }
  val database = FirebaseDatabase.getInstance()
  if (currentFriendsList.isEmpty()) {
    val friendsListRef = database.reference.child("users").child(userId).child("friendsList")
    friendsListRef.addListenerForSingleValueEvent(
        object : ValueEventListener {
          override fun onDataChange(dataSnapshot: DataSnapshot) {
            for (snapshot in dataSnapshot.getChildren()) {
              val friend = snapshot.getValue(Friend::class.java)
              if (friend != null) {
                currentFriendsList.add(friend)
              }
            }
          }

          override fun onCancelled(databaseError: DatabaseError) {
            // add code when failing to access database
          }
        })
  }
  if (showAddFriendScreen) {
    AddFriendScreen(onDismiss = { showAddFriendScreen = false }, userId)
  } else if (showFriendProfile) {
    FriendDialogBox(
        friend = selectedFriend!!,
        userId,
        onDismiss = {
          selectedFriend = null
          showFriendProfile = false
        })
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
                                TopLevelDestination(Routes.ProfileScreen.routName))
                          })
                }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Friends", fontWeight = FontWeight.Bold, fontSize = 40.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { showAddFriendScreen = true },
                colors = ButtonDefaults.buttonColors(blueThemeColor),
                modifier =
                    Modifier.fillMaxWidth()
                        .height(72.dp)
                        .padding(vertical = 8.dp)
                        .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(8.dp)) {
                  androidx.compose.material3.Text(
                      text = "Add Friends", fontSize = 24.sp, color = Color.White)
                }
            Spacer(modifier = Modifier.height(16.dp))
            if (currentFriendsList.isEmpty()) {
              Text(text = "No friends yet", fontWeight = FontWeight.Bold, fontSize = 24.sp)
            } else {
              LazyColumn {
                items(currentFriendsList) { friend ->
                  FriendItem(
                      friend = friend,
                      onClick = {
                        showFriendProfile = true
                        selectedFriend = friend
                      })
                }
              }
            }
          }
    }
  }
}

@Composable
fun FriendItem(friend: Friend, onClick: () -> Unit) {
  val blueThemeColor = colorResource(id = R.color.blueTheme)
  val backgroundColor = if (friend.status) blueThemeColor else Color.Gray
  val status = if (friend.status) "ONLINE" else "OFFLINE"
  Surface(
      color = backgroundColor,
      modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth().padding(horizontal = 16.dp),
      shape = MaterialTheme.shapes.medium,
      onClick = onClick) {
        Row(
            modifier = Modifier.padding(8.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {
              // Display friend's profile picture here if needed
              Text(
                  text = friend.name,
                  modifier = Modifier.weight(1f),
                  color = Color.White,
                  style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold))
              Text(
                  text = status,
                  color = Color.White,
                  style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold))
            }
      }
}
