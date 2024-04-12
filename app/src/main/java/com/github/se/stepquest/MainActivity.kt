package com.github.se.stepquest

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.github.se.stepquest.services.StepCounterService
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.se.stepquest.map.Map
import com.github.se.stepquest.ui.navigation.NavigationActions
import com.github.se.stepquest.ui.navigation.Route
import com.github.se.stepquest.ui.navigation.TopLevelDestination
import com.github.se.stepquest.ui.theme.StepQuestTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      StepQuestTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
          MyAppNavHost()
        }
      }
    }
  }
}

@Composable
fun MyAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Route.LOGIN
) {
  val navigationActions = remember(navController) { NavigationActions(navController) }
  NavHost(modifier = modifier, navController = navController, startDestination = startDestination) {
    composable(Route.LOGIN) { LoginPage(navigationActions) }
    composable(Route.MAP) { Map() }
  }
}

@Composable
fun LoginPage(navigationActions: NavigationActions) {
  var username by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }
  val blueThemeColor = colorResource(id = R.color.blueTheme)

  val context = LocalContext.current

  fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {

    val response = result.idpResponse

    if (result.resultCode == RESULT_OK) {
      println("Sign in successful!")
      // TODO: navigate to main menu

      // Start the step counter:
      ContextCompat.startForegroundService(context, Intent(context, StepCounterService::class.java))
      // TODO: move map to where it should be after main menu is ready, put here just for develope
      // purpose
      navigationActions.navigateTo(TopLevelDestination(Route.MAP))
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
        Spacer(modifier = Modifier.height(75.dp))

        // Temporary until we have a logo
        val greyColor = Color(0xFF808080)
        Canvas(
            modifier =
                Modifier.align(Alignment.CenterHorizontally).size(200.dp).padding(vertical = 16.dp),
            onDraw = {
              drawRect(color = greyColor, topLeft = Offset.Zero, size = Size(500f, 500f))
            })

        Spacer(modifier = Modifier.height(30.dp))

        // Login text
        Text(
            "Log In",
            fontSize = 25.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.align(Alignment.Start))

        Spacer(modifier = Modifier.height(3.dp))

        // Username box
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
          Text("Username", fontSize = 20.sp, modifier = Modifier.align(Alignment.Start))
          TextField(
              value = username,
              onValueChange = { username = it },
              shape = RoundedCornerShape(8.dp),
              singleLine = true,
              modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
              keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next))
        }
        // Password box
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
          Text("Password", fontSize = 20.sp, modifier = Modifier.align(Alignment.Start))
          TextField(
              value = password,
              onValueChange = { password = it },
              shape = RoundedCornerShape(8.dp),
              singleLine = true,
              modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
              keyboardOptions =
                  KeyboardOptions.Default.copy(
                      imeAction = ImeAction.Done, keyboardType = KeyboardType.Password),
              keyboardActions =
                  KeyboardActions(onDone = { /* TODO: same things as the Sign in button */}))
        }

        Column {
          // Sign in Button
          Button(
              onClick = { /* TODO: sign in using username + password */},
              colors = ButtonDefaults.buttonColors(blueThemeColor),
              modifier = Modifier.fillMaxWidth().height(72.dp).padding(vertical = 8.dp),
              shape = RoundedCornerShape(8.dp)) {
                Text(text = "Sign in", color = Color.White, fontSize = 24.sp)
              }

          // Forgot password and sign up buttons
          Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween) {
                TextButton(onClick = { /* TODO: handle forgot password */}) {
                  Text(
                      text = "Forgot password",
                      fontSize = 18.sp,
                      style = TextStyle(color = Color.Gray))
                }
                TextButton(onClick = { /* TODO: account creation w/o Google */}) {
                  Text(
                      text = "Sign up", fontSize = 18.sp, style = TextStyle(color = blueThemeColor))
                }
              }
        }

        Spacer(modifier = Modifier.height(50.dp))

        // Sign in with Google button
        Button(
            modifier = Modifier.size(65.dp),
            onClick = { signInLauncher.launch(signInIntent) },
            border = BorderStroke(1.dp, Color(0xFF000000)),
            colors = ButtonDefaults.buttonColors(Color.White),
            contentPadding = PaddingValues(12.dp),
            shape = CircleShape) {
              Image(
                  painter = painterResource(id = R.drawable.google),
                  contentDescription = "Sign in with google")
            }
      }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
  StepQuestTheme {
    val navController = rememberNavController()
    val navigationActions = remember(navController) { NavigationActions(navController) }
    LoginPage(navigationActions)
  }
}
