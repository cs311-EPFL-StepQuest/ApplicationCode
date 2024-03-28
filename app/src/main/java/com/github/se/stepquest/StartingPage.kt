package com.github.se.stepquest

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.github.se.stepquest.ui.theme.StepQuestTheme

@Composable
fun StartPageLayout() {
    val blueThemeColor = colorResource(id = R.color.blueTheme)

    Column(
        modifier = Modifier.padding(15.dp).fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(64.dp)) {
        // Temporary until we have a logo
        val greyColor = Color(0xFF808080)
        Canvas(
            modifier =
            Modifier.align(Alignment.CenterHorizontally).size(200.dp).padding(vertical = 16.dp),
            onDraw = {
                drawRect(color = greyColor, topLeft = Offset.Zero, size = Size(500f, 500f))
            })
            //Log in button
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(blueThemeColor),
                modifier =
                Modifier.fillMaxWidth()
                    .height(72.dp)
                    .padding(vertical = 8.dp)
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Log in", color = Color.White, fontSize = 24.sp)
            }
            //New player button
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(blueThemeColor),
                modifier =
                Modifier.fillMaxWidth()
                    .height(72.dp)
                    .padding(vertical = 8.dp)
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "New Player", color = Color.White, fontSize = 24.sp)
            }
        }
}

@Preview(showBackground = true)
@Composable
fun StartingPagePreview() {
    StepQuestTheme { StartPageLayout() }
}