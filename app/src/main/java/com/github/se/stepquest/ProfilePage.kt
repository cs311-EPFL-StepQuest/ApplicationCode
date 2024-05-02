package com.github.se.stepquest

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.github.se.stepquest.services.deleteFriend
import com.github.se.stepquest.ui.navigation.NavigationActions
import com.github.se.stepquest.ui.navigation.TopLevelDestination
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@Composable
fun ProfilePageLayout(navigationActions: NavigationActions) {
  val blueThemeColor = colorResource(id = R.color.blueTheme)
  val firebaseAuth = FirebaseAuth.getInstance()
  val userId = firebaseAuth.currentUser?.uid
  val profilePictureURL = firebaseAuth.currentUser?.photoUrl
  val database = FirebaseDatabase.getInstance()
  var totalStepsMade by remember { mutableStateOf(0) }
  val stepsRef = database.reference.child("users").child(userId!!).child("totalSteps")
  var username by remember { mutableStateOf("No name") }
  val usernameRef = database.reference.child("users").child(userId).child("username")
  stepsRef.addListenerForSingleValueEvent(
      object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
          totalStepsMade = dataSnapshot.getValue(Int::class.java) ?: 0
        }

        override fun onCancelled(databaseError: DatabaseError) {
          // add code when failing to access database
        }
      })

  usernameRef.addListenerForSingleValueEvent(
      object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
          username = dataSnapshot.getValue(String::class.java) ?: "No name"
        }

        override fun onCancelled(databaseError: DatabaseError) {
          // add code when failing to access database
        }
      })
  var showDialog by remember { mutableStateOf(false) }
  Column(
      modifier = Modifier.padding(32.dp).fillMaxSize(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
          Spacer(modifier = Modifier.weight(1f))
          Image(
              painter = painterResource(id = R.drawable.settings),
              contentDescription = "Settings",
              modifier = Modifier.size(30.dp))
        }
        Text(text = "Profile", fontWeight = FontWeight.Bold, fontSize = 40.sp)
        profilePictureURL?.let { uri ->
          Image(
              painter = rememberAsyncImagePainter(uri),
              contentDescription = "Profile Picture",
              modifier = Modifier.size(200.dp).clip(RoundedCornerShape(100.dp)))
        }
        Spacer(modifier = Modifier.height(16.dp))
        /*Image(
        painter = painterResource(id = R.drawable.dummypfp),
        contentDescription = "Profile Picture",
        modifier = Modifier.size(200.dp))*/
        Text(
            text = username,
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp,
        )
        Text(
            text = "Total Steps: $totalStepsMade",
            fontSize = 24.sp,
            modifier = Modifier.padding(top = 16.dp))
        ClickableText(
            text = AnnotatedString("Achievements: 5"), // Replace with actual number of achievements
            onClick = { deleteFriend("eliott", "Santhos", database, userId) },
            style = TextStyle(fontSize = 24.sp),
            modifier = Modifier.padding(top = 8.dp))
        Button(
            onClick = {
              navigationActions.navigateTo(TopLevelDestination(Routes.FriendsListScreen.routName))
            },
            colors = ButtonDefaults.buttonColors(blueThemeColor),
            modifier =
                Modifier.fillMaxWidth()
                    .height(72.dp)
                    .padding(vertical = 8.dp)
                    .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(8.dp)) {
              Text(text = "Friends List", fontSize = 24.sp, color = Color.White)
            }
      }
}
