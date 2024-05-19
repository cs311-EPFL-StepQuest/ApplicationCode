package com.github.se.stepquest.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.se.stepquest.Routes
import com.github.se.stepquest.activity.Quest
import com.github.se.stepquest.data.model.ChallengeData
import com.github.se.stepquest.services.cacheUserInfo
import com.github.se.stepquest.services.checkChallengesCompletion
import com.github.se.stepquest.services.getTopChallenge
import com.github.se.stepquest.services.getUsername
import com.github.se.stepquest.services.isOnline
import com.github.se.stepquest.ui.navigation.NavigationActions
import com.github.se.stepquest.ui.navigation.TopLevelDestination

@Composable
fun HomeScreen(navigationActions: NavigationActions, userId: String, context: Context) {

  // Added for testing purposes ------
  var quests: List<Quest> by remember { mutableStateOf(emptyList()) }
  var topChallenge: ChallengeData? by remember { mutableStateOf(null) }
  LaunchedEffect(Unit) {
    getTopChallenge(userId) { receivedChallenge -> topChallenge = receivedChallenge }

    // Simulated data for testing purposes
    val firstQuest = Quest("1", "0", "1000", "500", "Walk 1000 steps", "0")
    quests = quests.plus(firstQuest)
  }

  // ---------------------------------

  val isOnline = isOnline(context)

  if (isOnline) getUsername(userId) { cacheUserInfo(context, userId, it) }

  checkChallengesCompletion(userId)

  Scaffold(
      containerColor = Color(0xFF0D99FF),

      // Three main icons
      topBar = {
        Row(modifier = Modifier.height(100.dp).fillMaxWidth().padding(start = 15.dp, end = 15.dp)) {
          // Messages icon
          TextButton(onClick = { /*TODO*/}, modifier = Modifier.testTag("messages_button")) {
            Image(
                painter = painterResource(com.github.se.stepquest.R.drawable.messages),
                modifier = Modifier.fillMaxHeight().size(50.dp),
                contentDescription = "messages_icon")
          }
          Spacer(Modifier.weight(1f))
          // Notifications icon
          TextButton(
              onClick = {
                navigationActions.navigateTo(
                    TopLevelDestination(Routes.NotificationScreen.routName))
              },
              modifier = Modifier.testTag("notifications_button")) {
                Image(
                    painter = painterResource(com.github.se.stepquest.R.drawable.notification),
                    modifier = Modifier.fillMaxHeight().size(50.dp),
                    contentDescription = "notifications_icon")
              }
          // Profile icon
          TextButton(
              onClick = {
                navigationActions.navigateTo(TopLevelDestination(Routes.ProfileScreen.routName))
              },
              modifier = Modifier.testTag("profile_button")) {
                Image(
                    painter = painterResource(com.github.se.stepquest.R.drawable.profile),
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
              onClick = { /* start game when ready */},
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
                    painter = painterResource(com.github.se.stepquest.R.drawable.play_icon),
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
                    // Challenge
                    if (topChallenge == null) {
                      Text(
                          text = "No challenges available",
                          modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(top = 50.dp),
                          fontSize = 16.sp,
                          textAlign = TextAlign.Center,
                          fontWeight = FontWeight.Bold)
                    } else {
                      Row(
                          modifier = Modifier.padding(top = 20.dp, start = 30.dp).fillMaxWidth(),
                          horizontalArrangement = Arrangement.Center,
                          verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter =
                                    painterResource(
                                        com.github.se.stepquest.R.drawable.profile_challenges),
                                modifier = Modifier.size(40.dp).fillMaxHeight().fillMaxWidth(),
                                contentDescription = "profile_challenges")
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally) {
                                  Spacer(modifier = Modifier.width(10.dp))
                                  Text(text = "Main challenge", fontSize = 18.sp)
                                  Text(
                                      text =
                                          "${topChallenge!!.stepsToMake} steps until ${topChallenge!!.dateTime}!",
                                      fontSize = 18.sp,
                                      fontWeight = FontWeight.Bold)
                                }
                          }
                      Spacer(modifier = Modifier.weight(1f))
                      Button(
                          onClick = {
                            navigationActions.navigateTo(
                                TopLevelDestination(Routes.ChallengeScreen.routName))
                          },
                          colors = ButtonDefaults.buttonColors(Color(0xFF0D99FF)),
                          modifier =
                              Modifier.padding(horizontal = 5.dp, vertical = 10.dp)
                                  .height(40.dp)
                                  .fillMaxWidth()
                                  .align(Alignment.CenterHorizontally)) {
                            Text(
                                text = "Check active challenges",
                                fontSize = 16.sp,
                                modifier = Modifier.padding(0.dp))
                          }
                    }
                    // Buttons

                  }
                }
              }

          // Daily quests tab
          Card(
              modifier =
                  Modifier.fillMaxWidth()
                      .padding(start = 25.dp, end = 25.dp, bottom = 30.dp)
                      .height(250.dp),
              colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Text(
                    text = "Daily Quests",
                    modifier = Modifier.padding(start = 18.dp, top = 14.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold)
                Column {
                  if (quests.isEmpty()) {
                    Text(
                        text = "No daily quests available",
                        modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(top = 70.dp),
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold)
                  } else {
                    for (quest in quests) {
                      // Quest details
                      Row(
                          modifier = Modifier.padding(top = 40.dp, start = 30.dp).fillMaxWidth(),
                          horizontalArrangement = Arrangement.Center,
                          verticalAlignment = Alignment.CenterVertically) {
                            // Icon of a blue dot

                            Image(
                                painter =
                                    painterResource(
                                        com.github.se.stepquest.R.drawable.quest_not_finished),
                                modifier = Modifier.size(20.dp).fillMaxHeight().fillMaxWidth(),
                                contentDescription = "profile_challenges")
                            Row(modifier = Modifier.fillMaxWidth()) {
                              Spacer(modifier = Modifier.width(10.dp))
                              Text(
                                  text = "${quest.currentState}/${quest.questGoal}",
                                  fontSize = 18.sp)
                              Spacer(modifier = Modifier.padding(10.dp))
                              Text(
                                  text = quest.questDescription,
                                  fontSize = 18.sp,
                                  fontWeight = FontWeight.Bold)
                            }
                          }
                    }
                  }
                }
              }
        }
      }
}
