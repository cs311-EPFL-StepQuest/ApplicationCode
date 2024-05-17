package com.github.se.stepquest.screens

import android.app.Activity
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.github.se.stepquest.R
import com.github.se.stepquest.Routes
import com.github.se.stepquest.services.getCachedInfo
import com.github.se.stepquest.services.isOnline
import com.github.se.stepquest.services.setOnline
import com.github.se.stepquest.ui.navigation.NavigationActions
import com.github.se.stepquest.ui.navigation.TopLevelDestination

@Composable
fun LoginScreen(navigationActions: NavigationActions, context: Context) {
  val blueThemeColor = colorResource(id = R.color.blueTheme)
  val isOnline = isOnline(context)
  var showError by remember { mutableStateOf(false) }

  fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {

    val response = result.idpResponse

    if (result.resultCode == Activity.RESULT_OK) {
      println("Sign in successful!")
      setOnline()
      navigationActions.navigateTo(TopLevelDestination(Routes.DatabaseLoadingScreen.routName))
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
      modifier = Modifier.padding(38.dp).fillMaxSize(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Spacer(modifier = Modifier.height(100.dp))

        // Temporary until we have a logo
        val greyColor = Color(0xFF808080)
        Canvas(
            modifier =
                Modifier.align(Alignment.CenterHorizontally)
                    .size(200.dp)
                    .padding(vertical = 16.dp)
                    .testTag("App logo"),
            onDraw = {
              drawRect(color = greyColor, topLeft = Offset.Zero, size = Size(500f, 500f))
            })

        Spacer(modifier = Modifier.height(150.dp))

        if (isOnline) {
          Button(
              onClick = { signInLauncher.launch(signInIntent) },
              colors = ButtonDefaults.buttonColors(blueThemeColor),
              modifier = Modifier.fillMaxWidth().height(72.dp).padding(vertical = 8.dp),
              shape = RoundedCornerShape(8.dp)) {
                Text(text = "Authenticate", color = Color.White, fontSize = 24.sp)
              }
        } else {
          Button(
              onClick = {
                val cacheCheck = getCachedInfo(context)

                if (cacheCheck != null) {
                  navigationActions.navigateTo(TopLevelDestination(Routes.MainScreen.routName))
                } else {
                  showError = true
                }
              },
              colors = ButtonDefaults.buttonColors(blueThemeColor),
              modifier = Modifier.fillMaxWidth().height(72.dp).padding(vertical = 8.dp),
              shape = RoundedCornerShape(8.dp)) {
                Text(text = "Enter the app", color = Color.White, fontSize = 24.sp)
              }
        }

        if (showError) {
          Text(
              text = "Internet connection required.",
              modifier = Modifier.fillMaxWidth(),
              color = Color.Red)
        }
      }
}
