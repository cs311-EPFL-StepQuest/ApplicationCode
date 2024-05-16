package com.github.se.stepquest.screens

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
) {
  val database = FirebaseDatabase.getInstance()
  var isNewPlayer by remember { mutableStateOf(false) }
  val databaseRef = database.reference
  var permissionsGranted by remember { mutableStateOf(false) }
  val launcherPermission =
      rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
          permissions ->
        val bodySensorsGranted = permissions[android.Manifest.permission.BODY_SENSORS] ?: false
        val activityRecognitionGranted =
            permissions[android.Manifest.permission.ACTIVITY_RECOGNITION] ?: false
        permissionsGranted = bodySensorsGranted && activityRecognitionGranted
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
                if (!permissionsGranted) {
                  launcherPermission.launch(
                      arrayOf(
                          android.Manifest.permission.BODY_SENSORS,
                          android.Manifest.permission.ACTIVITY_RECOGNITION))
                }
                navigationActions.navigateTo(TopLevelDestination(Routes.NewPlayerScreen.routName))
              } else {
                if (!permissionsGranted) {
                  launcherPermission.launch(
                      arrayOf(
                          android.Manifest.permission.BODY_SENSORS,
                          android.Manifest.permission.ACTIVITY_RECOGNITION))
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
