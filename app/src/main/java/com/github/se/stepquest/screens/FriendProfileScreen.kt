package com.github.se.stepquest.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.se.stepquest.R
import com.github.se.stepquest.Routes
import com.github.se.stepquest.ui.navigation.TopLevelDestination

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FriendProfileScreen() {
    val blueThemeColor = colorResource(id = R.color.blueTheme)
    var showDialog by remember { mutableStateOf(false) }
    // val user = Firebase.auth.currentUser
    Column(
        modifier = Modifier
            .padding(32.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(text = "Alice", fontWeight = FontWeight.Bold, fontSize = 40.sp)
        Image(
            painter = painterResource(id = R.drawable.dummypfp),
            contentDescription = "Profile Picture",
            modifier = Modifier.size(200.dp))
        Spacer(modifier = Modifier.height(80.dp))
        Button(
            onClick = {
                //TODO
            },
            colors = ButtonDefaults.buttonColors(blueThemeColor),
            modifier =
            Modifier
                .fillMaxWidth()
                .height(72.dp)
                .padding(vertical = 8.dp)
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Connect", fontSize = 24.sp, color = Color.White)
        }
        Button(
            onClick = {
                //TODO
            },
            colors = ButtonDefaults.buttonColors(blueThemeColor),
            modifier =
            Modifier
                .fillMaxWidth()
                .height(72.dp)
                .padding(vertical = 8.dp)
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Challenge", fontSize = 24.sp, color = Color.White)
        }
    }
}