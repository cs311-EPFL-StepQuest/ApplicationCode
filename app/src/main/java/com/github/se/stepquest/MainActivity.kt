package com.github.se.stepquest

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.se.stepquest.ui.theme.StepQuestTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      StepQuestTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
          LoginPage()
        }
      }
    }
  }
}

@Composable
fun LoginPage() {
  val context = LocalContext.current
  var username by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }
  val blueThemeColor = colorResource(id = R.color.blueTheme)
  val googleSignInOptions = remember { GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(context.getString(R.string.default_web_client_id)).requestEmail().build() }
  val googleSignInClient = remember { GoogleSignIn.getClient(context, googleSignInOptions) }
  val auth = FirebaseAuth.getInstance()

  Column(modifier = Modifier
    .padding(15.dp)
    .fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    //Temporary until we have a logo
    val greyColor = Color(0xFF808080)
    Canvas(
      modifier = Modifier
        .align(Alignment.CenterHorizontally)
        .size(200.dp)
        .padding(vertical = 16.dp),
      onDraw = {
        drawRect(
          color = greyColor,
          topLeft = Offset.Zero,
          size = Size(500f, 500f)
        )
      }
    )
    //Login area
    //Username box
    Column(
      modifier = Modifier.fillMaxWidth(),
      horizontalAlignment = Alignment.Start
    ) {
      Text(
        "Username",
        modifier = Modifier.align(Alignment.Start)
      )
      TextField(
        value = username,
        onValueChange = { username = it },
        singleLine = true,
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 4.dp),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(onNext = { /* Handle next action */ })
      )
    }
    //Password box
    Column(
      modifier = Modifier.fillMaxWidth(),
      horizontalAlignment = Alignment.Start
    ) {
      Text(
        "Password",
        modifier = Modifier.align(Alignment.Start)
      )
      TextField(
        value = password,
        onValueChange = { password = it },
        singleLine = true,
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 4.dp),
        keyboardOptions = KeyboardOptions.Default.copy(
          imeAction = ImeAction.Done,
          keyboardType = KeyboardType.Password
        ),
        keyboardActions = KeyboardActions(onDone = { /* Handle login action */ })
      )
      //Forgot password and sign up buttons
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(
          text = "Forgot Password?",
          style = TextStyle(color = Color.Gray),
          modifier = Modifier.padding(start = 8.dp)
        )
        Text(
          text = "Sign Up",
          style = TextStyle(color = blueThemeColor),
          modifier = Modifier.padding(end = 8.dp)
        )
      }
    }
    // Confirm Button
    Button(
      onClick = {
        val signInIntent = googleSignInClient.signInIntent
        signInLauncher.launch(signInIntent)
      },
      colors = ButtonDefaults.buttonColors(blueThemeColor),
      modifier = Modifier
        .fillMaxWidth()
        .height(72.dp)
        .padding(vertical = 8.dp)
        .padding(horizontal = 16.dp),
      shape = RoundedCornerShape(8.dp)
    ) {
      Text(
        text = "Confirm",
        color = Color.White,
        fontSize = 24.sp
      )
    }
  }
}

@Composable
fun rememberGoogleSignInLauncher(onSuccess: (String) -> Unit): ActivityResultLauncher<Intent> {
  return rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
    try {
      val account = task.getResult(ApiException::class.java)!!
      val idToken = account.idToken!!
      onSuccess(idToken)
    } catch (e: ApiException) {
      // Handle sign-in failure
    }
  }
}

val signInLauncher = rememberGoogleSignInLauncher { idToken ->
  val credential = GoogleAuthProvider.getCredential(idToken, null)
  FirebaseAuth.getInstance().signInWithCredential(credential)
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  Text(text = "Hello $name!", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
  StepQuestTheme { LoginPage() }
}
