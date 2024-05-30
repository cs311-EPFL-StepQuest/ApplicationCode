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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.se.stepquest.R
import com.github.se.stepquest.Routes
import com.github.se.stepquest.services.fetchFriendsListFromDatabase
import com.github.se.stepquest.services.getFriendsLeaderboard
import com.github.se.stepquest.services.getTopLeaderboard
import com.github.se.stepquest.services.getUserPlacement
import com.github.se.stepquest.services.getUserScore
import com.github.se.stepquest.services.getUsername
import com.github.se.stepquest.ui.navigation.NavigationActions
import com.github.se.stepquest.ui.navigation.TopLevelDestination

/** Screen displaying the global and friend score leaderboards. */
@Composable
fun Leaderboards(userId: String, navigationActions: NavigationActions) {
  val blueThemeColor = colorResource(id = R.color.blueTheme)
  var leaderboard: List<Pair<String, Int>>? by remember { mutableStateOf(emptyList()) }
  var friendsLeaderboard: List<Pair<String, Int>>? by remember { mutableStateOf(emptyList()) }
  var userScore: Int by remember { mutableIntStateOf(0) }
  var currentPosition: Int? by remember { mutableStateOf(0) }

  LaunchedEffect(Unit) {
    getTopLeaderboard(10) { topLeaderboard -> leaderboard = topLeaderboard }
    fetchFriendsListFromDatabase(userId) { friendsList ->
      if (friendsList != null) {
        getFriendsLeaderboard(friendsList) { topFriendsLeaderboard ->
          friendsLeaderboard = topFriendsLeaderboard
        }
      }
    }
    getUsername(userId) { cUsername ->
      getUserScore(cUsername) { score -> userScore = score }
      getUserPlacement(cUsername) { currentPlacement -> currentPosition = currentPlacement }
    }
  }

  Surface(color = Color.White, modifier = Modifier.fillMaxSize()) {
    LazyColumn(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally) {
          item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically) {
                  Text(
                      text = "Back",
                      fontSize = 20.sp,
                      modifier =
                          Modifier.clickable {
                            navigationActions.navigateTo(
                                TopLevelDestination(Routes.HomeScreen.routName))
                          })
                }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Leaderboard", fontWeight = FontWeight.Bold, fontSize = 40.sp)
            Spacer(modifier = Modifier.height(16.dp))
          }

          item {
            // General Leaderboard Card
            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp).height(370.dp),
                colors = CardDefaults.cardColors(containerColor = blueThemeColor)) {
                  Column(
                      modifier = Modifier.padding(16.dp),
                      horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "General Leaderboard",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(16.dp))
                        if (leaderboard!!.isEmpty()) {
                          Text(
                              text = "Not available",
                              color = Color.White,
                              fontSize = 16.sp,
                              fontWeight = FontWeight.Bold)
                        } else {
                          LazyColumn {
                            items(leaderboard!!.size) { index ->
                              val user = leaderboard!![index]
                              var uScore = user.second.toString()
                              if (user.second > 99999) {
                                uScore = "+99999"
                              }
                              Text(
                                  text = "${index + 1}. ${user.first} : $uScore",
                                  color = Color.White,
                                  fontSize = 16.sp,
                                  fontWeight = FontWeight.Bold,
                                  modifier = Modifier.padding(vertical = 4.dp))
                            }
                          }
                        }
                      }
                }
          }

          item {
            // Friends Leaderboard Card
            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp).height(300.dp),
                colors = CardDefaults.cardColors(containerColor = blueThemeColor)) {
                  Column(
                      modifier = Modifier.padding(16.dp),
                      horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Friends Leaderboard",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(16.dp))
                        if (friendsLeaderboard!!.isEmpty()) {
                          Text(
                              text = "Not available",
                              color = Color.White,
                              fontSize = 16.sp,
                              fontWeight = FontWeight.Bold)
                        } else {
                          LazyColumn {
                            items(friendsLeaderboard!!.size) { index ->
                              val user = friendsLeaderboard!![index]
                              Text(
                                  text = "${index + 1}. ${user.first} :  ${user.second}",
                                  color = Color.White,
                                  fontSize = 16.sp,
                                  fontWeight = FontWeight.Bold,
                                  modifier = Modifier.padding(vertical = 4.dp))
                            }
                          }
                        }
                      }
                }
          }
        }
  }
}
