package com.github.se.stepquest

import android.app.Activity.RESULT_OK
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.github.se.stepquest.ui.theme.StepQuestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StepQuestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    LoginPage()
                }
            }
        }
    }
}

@Composable
fun LoginPage() {
    val blueThemeColor = colorResource(id = R.color.blueTheme)

    fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {

        val response = result.idpResponse

        if (result.resultCode == RESULT_OK) {
            // TODO: navigate to main menu
        } else if (response != null) {
            throw Exception(response.error?.errorCode.toString())
        }
    }

    val signInLauncher =
        rememberLauncherForActivityResult(contract = FirebaseAuthUIActivityResultContract()) {
            onSignInResult(it)
        }

    val providers = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())

    val signInIntent =
        AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).build()

    Column(
        modifier = Modifier
            .padding(38.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Spacer(modifier = Modifier.height(100.dp))

        // Temporary until we have a logo
        val greyColor = Color(0xFF808080)
        Canvas(modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .size(200.dp)
            .padding(vertical = 16.dp), onDraw = {
            drawRect(color = greyColor, topLeft = Offset.Zero, size = Size(500f, 500f))
        })

        Spacer(modifier = Modifier.height(150.dp))

        // Log in Button
        Button(
            onClick = { signInLauncher.launch(signInIntent) },
            colors = ButtonDefaults.buttonColors(blueThemeColor),
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Log in", color = Color.White, fontSize = 24.sp)
        }

        Spacer(modifier = Modifier.height(25.dp))

        // New user button
        Button(
            onClick = { /* TODO: navigate to user creation */ },
            colors = ButtonDefaults.buttonColors(blueThemeColor),
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "New player", color = Color.White, fontSize = 24.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    StepQuestTheme { LoginPage() }
}
