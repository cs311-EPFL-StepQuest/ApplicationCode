package com.github.se.stepquest.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.se.stepquest.R
import com.github.se.stepquest.Routes
import com.github.se.stepquest.data.model.ChallengeData
import com.github.se.stepquest.services.getChallenges
import com.github.se.stepquest.ui.navigation.NavigationActions
import com.github.se.stepquest.ui.navigation.TopLevelDestination

@Composable
fun ChallengesScreen(
    userId: String,
    navigationActions: NavigationActions,
    testChallenges: List<ChallengeData> = listOf()
) {
  var challenges by remember { mutableStateOf(testChallenges) }
  getChallenges(userId) { receivedChallenges -> challenges = receivedChallenges }
  Surface(color = Color.White, modifier = Modifier.fillMaxSize()) {
    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally) {
          Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.Start,
              verticalAlignment = Alignment.CenterVertically) {
                androidx.compose.material.Text(
                    text = "Back",
                    fontSize = 20.sp,
                    modifier =
                        Modifier.clickable {
                          navigationActions.navigateTo(
                              TopLevelDestination(Routes.HomeScreen.routName))
                        })
              }
          Spacer(modifier = Modifier.height(16.dp))
          androidx.compose.material.Text(
              text = "Challenges", fontWeight = FontWeight.Bold, fontSize = 40.sp)
          Spacer(modifier = Modifier.height(16.dp))
          Column { challenges.forEach { challenge -> ChallengeItem(challenge = challenge) } }
        }
  }
}

@Composable
fun ChallengeItem(challenge: ChallengeData) {
  val blueThemeColor = colorResource(id = R.color.blueTheme)
  Surface(
      color = blueThemeColor,
      modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth().padding(horizontal = 16.dp),
      shape = MaterialTheme.shapes.medium) {
        Column(modifier = Modifier.padding(8.dp)) {
          Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = CenterVertically) {
            Text(
                text = "Sender: ${challenge.senderUsername}",
                color = Color.White,
            )
            Spacer(modifier = Modifier.width(100.dp))
            Text(
                text = "Challenged: ${challenge.challengedUsername}",
                color = Color.White,
            )
          }
          Spacer(modifier = Modifier.height(16.dp))
          Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = CenterVertically,
              horizontalArrangement = Arrangement.Center) {
                Text(
                    text = "Message: ${challenge.type.messageText}",
                    color = Color.White,
                )
              }
          Spacer(modifier = Modifier.height(16.dp))
          Row(
              modifier = Modifier.fillMaxWidth(),
              verticalAlignment = CenterVertically,
              horizontalArrangement = Arrangement.Center // Aligns items horizontally to the center
              ) {
                Text(
                    text = "End Date: ${challenge.dateTime}",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                )
              }
        }
      }
}
