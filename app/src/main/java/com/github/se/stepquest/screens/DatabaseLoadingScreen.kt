package com.github.se.stepquest.screens

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.PermissionChecker
import com.github.se.stepquest.Routes
import com.github.se.stepquest.ui.navigation.NavigationActions
import com.github.se.stepquest.ui.navigation.TopLevelDestination
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@Composable
fun DatabaseLoadingScreen(
    navigationActions: NavigationActions,
    startService: () -> Unit,
    userId: String,
    context : Context
) {
  val database = FirebaseDatabase.getInstance()
  var isNewPlayer by remember { mutableStateOf(false) }
  val databaseRef = database.reference
    val bodySensorsPermissionGranted = remember { mutableStateOf(false) }
    val launcherBodySensorsPermission =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            bodySensorsPermissionGranted.value = isGranted
        }
  databaseRef
      .child("users")
      .child(userId)
      .child("username")
      .addListenerForSingleValueEvent(
          object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
              val username = dataSnapshot.getValue(String::class.java)
              isNewPlayer = username == null
              if (isNewPlayer) {
                  if (PermissionChecker.checkSelfPermission(context, android.Manifest.permission.BODY_SENSORS) != PermissionChecker.PERMISSION_GRANTED) {
                      launcherBodySensorsPermission.launch(android.Manifest.permission.BODY_SENSORS)
                  }
                  navigationActions.navigateTo(TopLevelDestination(Routes.NewPlayerScreen.routName))
              } else {
                  if (PermissionChecker.checkSelfPermission(context, android.Manifest.permission.BODY_SENSORS) != PermissionChecker.PERMISSION_GRANTED) {
                      launcherBodySensorsPermission.launch(android.Manifest.permission.BODY_SENSORS)
                  }
                  startService()
                  navigationActions.navigateTo(TopLevelDestination(Routes.MainScreen.routName))
              }
            }

            override fun onCancelled(databaseError: DatabaseError) {
              // Handle cancellation
            }
          })
  Column(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Waiting for database...",
            modifier = Modifier.padding(32.dp),
            fontWeight = FontWeight.Bold)
      }
}
