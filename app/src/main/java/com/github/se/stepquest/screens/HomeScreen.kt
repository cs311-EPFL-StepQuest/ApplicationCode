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
import androidx.compose.runtime.mutableIntStateOf
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
import com.github.se.stepquest.data.model.ChallengeData
import com.github.se.stepquest.services.cacheUserInfo
import com.github.se.stepquest.services.getTopChallenge
import com.github.se.stepquest.services.getTopLeaderboard
import com.github.se.stepquest.services.getUserPlacement
import com.github.se.stepquest.services.getUserScore
import com.github.se.stepquest.services.getUsername
import com.github.se.stepquest.services.isOnline
import com.github.se.stepquest.services.someChallengesCompleted
import com.github.se.stepquest.ui.navigation.NavigationActions
import com.github.se.stepquest.ui.navigation.TopLevelDestination

@Composable
fun HomeScreen(navigationActions: NavigationActions, userId: String, context: Context) {

  // Added for testing purposes ------
  var leaderboard: List<Pair<String, Int>>? by remember { mutableStateOf(emptyList()) }
  var topChallenge: ChallengeData? by remember { mutableStateOf(null) }
  var showChallengeCompletionPopUp: Boolean by remember { mutableStateOf(false) }
  var userScore: Int by remember { mutableIntStateOf(0) }
  var username: String by remember { mutableStateOf("No name") }
  var currentPosition: Int? by remember { mutableStateOf(0) }
  LaunchedEffect(Unit) {
    getTopChallenge(userId) { receivedChallenge -> topChallenge = receivedChallenge }
    getTopLeaderboard(5) { topLeaderboard -> leaderboard = topLeaderboard }
    getUsername(userId) { cUsername ->
      username = cUsername
      getUserScore(cUsername) { score -> userScore = score }
      getUserPlacement(cUsername) { currentPlacement -> currentPosition = currentPlacement }
    }
  }

  // ---------------------------------

  val isOnline = isOnline(context)

  if (isOnline) getUsername(userId) { cacheUserInfo(context, userId, it) }

  someChallengesCompleted(userId) { result -> if (result) showChallengeCompletionPopUp = true }
  if (showChallengeCompletionPopUp)
      CongratulationDialog(
          titleText = "Challenges",
          mainText = "Congratulations! You have completed some challenges!",
          xpNumber = 100) {
            showChallengeCompletionPopUp = false
          }

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

          // Leaderboard
          Card(
              modifier =
                  Modifier.fillMaxWidth()
                      .padding(start = 25.dp, end = 25.dp, bottom = 30.dp)
                      .height(260.dp),
              colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Text(
                    text = "Leaderboard",
                    modifier = Modifier.padding(start = 18.dp, top = 14.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold)
                Column {
                  if (leaderboard == null) {
                    Text(
                        text = "Leaderboard is not accessible",
                        modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(top = 70.dp),
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold)
                  } else {
                    var i = 0
                    for (user in leaderboard!!) {
                      i++
                      Row(
                          modifier = Modifier.padding(top = 10.dp, start = 30.dp).fillMaxWidth(),
                          horizontalArrangement = Arrangement.Center,
                          verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "$i. ${user.first} :  ${user.second}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                modifier = Modifier.weight(1f))
                          }
                    }
                    Button(
                        onClick = {
                          navigationActions.navigateTo(
                              TopLevelDestination(Routes.Leaderboard.routName))
                        },
                        colors = ButtonDefaults.buttonColors(Color(0xFF0D99FF)),
                        modifier =
                            Modifier.padding(horizontal = 5.dp, vertical = 10.dp)
                                .height(40.dp)
                                .fillMaxWidth()
                                .align(Alignment.CenterHorizontally)) {
                          Text(
                              text = "Check the leaderboard",
                              fontSize = 16.sp,
                              modifier = Modifier.padding(0.dp))
                        }
                  }
                }
              }
          Card(
              modifier =
                  Modifier.fillMaxWidth()
                      .padding(start = 25.dp, end = 25.dp, top = 10.dp)
                      .height(100.dp),
              colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Text(
                    text = "Your current score is $userScore",
                    modifier = Modifier.padding(start = 20.dp, top = 15.dp),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold)
                Text(
                    text = "which makes you number $currentPosition",
                    modifier = Modifier.padding(start = 20.dp, top = 15.dp),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold)
              }
        }
      }
}
