package com.github.se.stepquest.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.se.stepquest.R

/*
object Route {
    const val Home = "Home"
    const val Map = "Map"
    const val Progression = "Progression"
}

data class TopLevelDestination(val route: String, val textId: Int) {}


val TOP_LEVEL_DESTINATIONS =
    listOf(
        TopLevelDestination(Route.Home, R.string.tab_home),
        TopLevelDestination(Route.Map, R.string.tab_map),
        TopLevelDestination(Route.Progression, R.string.tab_progression)
    )
*/
@Preview(showBackground = true, widthDp = 393, heightDp = 808)
@Composable
fun MainScreen() {
  Scaffold(
      containerColor = Color(0xFF0D99FF),
      // Three main icons
      topBar = {
        Row(modifier = Modifier.height(100.dp).fillMaxWidth().padding(start = 15.dp, end = 15.dp)) {
          // Messages icon
          TextButton(onClick = { /*TODO*/}, modifier = Modifier.testTag("messages_button")) {
            Image(
                painter = painterResource(R.drawable.messages),
                modifier = Modifier.fillMaxHeight().size(50.dp),
                contentDescription = "messages_icon")
          }
          Spacer(Modifier.weight(1f))
          // Notifications icon
          TextButton(onClick = { /*TODO*/}, modifier = Modifier.testTag("notifications_button")) {
            Image(
                painter = painterResource(R.drawable.notification),
                modifier = Modifier.fillMaxHeight().size(50.dp),
                contentDescription = "notifications_icon")
          }
          // Profile icon
          TextButton(onClick = { /*TODO*/}, modifier = Modifier.testTag("profile_button")) {
            Image(
                painter = painterResource(R.drawable.profile),
                modifier = Modifier.fillMaxHeight().size(50.dp),
                contentDescription = "profile_icon")
          }
        }
      },
      bottomBar = {}) { innerPadding ->
        // Main content of the home screen
        Column(modifier = Modifier.padding(innerPadding).fillMaxWidth()) {

          // Start game button
          Button(
              onClick = { /*TODO*/},
              shape = RoundedCornerShape(20.dp),
              colors = ButtonDefaults.buttonColors(Color.White),
              modifier =
                  Modifier.fillMaxWidth()
                      .padding(start = 25.dp, end = 25.dp, top = 5.dp)
                      .height(70.dp)) {
                Text(
                    text = "Start Game",
                    color = Color.Black,
                    modifier = Modifier.padding(end = 70.dp),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold)
                Image(
                    painter = painterResource(R.drawable.play_icon),
                    contentDescription = "play_icon",
                    modifier = Modifier.size(40.dp))
              }
          // Challenges tab
          Card(
              modifier = Modifier.fillMaxWidth().padding(25.dp).height(190.dp),
              colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column {
                  Text(
                      text = "Challenges",
                      modifier = Modifier.padding(start = 18.dp, top = 14.dp),
                      fontSize = 20.sp,
                      fontWeight = FontWeight.Bold)
                  Column {
                    // Challenge ....

                    // Buttons
                    Spacer(modifier = Modifier.weight(1f))
                    Row {
                      // Accept button
                      Button(
                          onClick = { /*TODO*/},
                          colors = ButtonDefaults.buttonColors(Color(0xFF0D99FF)),
                          modifier =
                              Modifier.padding(start = 20.dp, top = 10.dp, bottom = 10.dp)
                                  .height(35.dp)
                                  .width(140.dp)) {
                            Text(
                                text = "Accept",
                                fontSize = 16.sp,
                                modifier = Modifier.padding(0.dp))
                          }

                      Spacer(modifier = Modifier.weight(1f))

                      // Reject button
                      Button(
                          onClick = { /*TODO*/},
                          colors = ButtonDefaults.buttonColors(Color.Gray),
                          modifier =
                              Modifier.padding(end = 20.dp, top = 10.dp)
                                  .height(35.dp)
                                  .width(140.dp)) {
                            Text(text = "Reject", color = Color.Black, fontSize = 16.sp)
                          }
                    }
                  }
                }
              }

          // Daily quests tab
          Card(
              modifier = Modifier.fillMaxWidth().padding(start = 25.dp, end = 25.dp).height(250.dp),
              colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Text(
                    text = "Daily Quests",
                    modifier = Modifier.padding(start = 18.dp, top = 14.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold)
                Column {
                  // Daily quests...
                }
              }
        }
      }
}
