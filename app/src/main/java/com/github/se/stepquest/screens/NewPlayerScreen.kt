package com.github.se.stepquest.screens

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.github.se.stepquest.R
import com.github.se.stepquest.Routes
import com.github.se.stepquest.services.StepCounterService
import com.github.se.stepquest.services.setOnline
import com.github.se.stepquest.ui.navigation.NavigationActions
import com.github.se.stepquest.ui.navigation.TopLevelDestination
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

fun addUsername(username: String, firebaseAuth: FirebaseAuth, database: FirebaseDatabase) {
  val userId = firebaseAuth.currentUser?.uid
    var pair = Pair(userId, "")
  if (userId != null) {
    val databaseRef = database.reference
    databaseRef.addListenerForSingleValueEvent(
        object : ValueEventListener {
          override fun onDataChange(dataSnapshot: DataSnapshot) {
              pair = Pair(userId, username)
            databaseRef.child("users").child(userId).child("username").setValue(username)
            databaseRef
                .child("global")
                .child("usernames")
                .child(username)
                .setValue(pair)
          }

          override fun onCancelled(databaseError: DatabaseError) {
            // add code when failing to access database
          }
        })
  }
}

fun checkIfNewPlayer() {}

fun usernameIsAvailable(): Boolean {
    return true
}

@Composable
fun NewPlayerScreen(navigationActions: NavigationActions, context: Context) {

  var usernamePlayer by remember { mutableStateOf("") }

  val blueThemeColor = colorResource(id = R.color.blueTheme)

  fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {

    val response = result.idpResponse

    if (result.resultCode == Activity.RESULT_OK) {
      println("Sign in successful!")
      setOnline()
      context.startService(Intent(context, StepCounterService::class.java))
      navigationActions.navigateTo(TopLevelDestination(Routes.MainScreen.routName))
    } else if (response != null) {
      throw Exception(response.error?.errorCode.toString())
    } else {
      throw Exception("Sign in failed")
    }
  }

  val signInLauncher =
      rememberLauncherForActivityResult(contract = FirebaseAuthUIActivityResultContract()) {
        onSignInResult(it)
      }

  val providers = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())

  val signInIntent =
      AuthUI.getInstance()
          .createSignInIntentBuilder()
          .setAvailableProviders(providers)
          .setIsSmartLockEnabled(false)
          .build()

  Column(
      modifier = Modifier.fillMaxSize().padding(16.dp),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally) {
        TextField(
            value = usernamePlayer,
            onValueChange = { newInput ->
              // Remove spaces from the input
              val filteredInput = newInput.replace("\\s".toRegex(), "")
              // Limit the input to 25 characters
              usernamePlayer = filteredInput.take(25)
            },
            label = { Text("Username") },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions =
                KeyboardActions(
                    onDone = {
                      signInLauncher.launch(signInIntent)
                      val firebaseAuth = FirebaseAuth.getInstance()
                      val database = FirebaseDatabase.getInstance()
                      addUsername(usernamePlayer, firebaseAuth, database)
                    }),
            singleLine = true,
            modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
              signInLauncher.launch(signInIntent)
              val firebaseAuth = FirebaseAuth.getInstance()
              val database = FirebaseDatabase.getInstance()
              addUsername(usernamePlayer, firebaseAuth, database)
            },
            colors = ButtonDefaults.buttonColors(blueThemeColor),
            modifier = Modifier.fillMaxWidth().height(72.dp).padding(vertical = 8.dp),
            shape = RoundedCornerShape(8.dp)) {
              Text(text = "Sign in", color = Color.White, fontSize = 24.sp)
            }
      }
}