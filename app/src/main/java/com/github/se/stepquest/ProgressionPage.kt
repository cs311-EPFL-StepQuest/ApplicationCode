package com.github.se.stepquest

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.se.stepquest.screens.CongratulationDialog
import com.github.se.stepquest.services.cacheDailyWeeklySteps
import com.github.se.stepquest.services.cacheStepGoals
import com.github.se.stepquest.services.getCachedStepInfo
import com.github.se.stepquest.services.getCachedSteps
import com.github.se.stepquest.services.isOnline
import kotlinx.coroutines.delay

@Composable
fun ProgressionPage(user: UserRepository, context: Context) {
  var progress by remember { mutableFloatStateOf(0.5f) }
  var showDialog by remember { mutableStateOf(false) }
  val stepList = getCachedStepInfo(context)
  var dailyStepsMade by remember { mutableIntStateOf(stepList["dailySteps"] ?: 0) }
  var weeklyStepsMade by remember { mutableIntStateOf(stepList["weeklySteps"] ?: 0) }

  LaunchedEffect(Unit) {
    while (true) {
      user.getSteps { steps -> dailyStepsMade = steps[0] }
      user.getSteps { steps -> weeklyStepsMade = steps[1] }
      delay(100)
    }
  }
  cacheDailyWeeklySteps(context, dailyStepsMade, weeklyStepsMade)

  var dailyStepGoal by remember { mutableIntStateOf(stepList["dailyStepGoal"] ?: 5000) }
  var weeklyStepGoal by remember { mutableIntStateOf(stepList["weeklyStepGoal"] ?: 35000) }
  var dailyGoalAchieved by remember { mutableStateOf(dailyStepsMade > dailyStepGoal) }
  val levelList = arrayListOf<String>("Current week progression", "$weeklyStepsMade steps")

  @Composable
  fun onlineStats() {
    BuildStats(
        dailyStepsMade = dailyStepsMade,
        dailyStepGoal = dailyStepGoal,
        weeklyStepsMade = weeklyStepsMade,
        weeklyStepGoal = weeklyStepGoal)
  }

  @Composable
  fun offlineStats() {
    Box(modifier = Modifier.height(40.dp))
    BuildStatLine(
        icon = R.drawable.step_icon,
        title = "Steps taken since offline",
        value = getCachedSteps(context).toString())
    Box(modifier = Modifier.height(20.dp))
    Row(modifier = Modifier.fillMaxWidth().offset(20.dp, 0.dp)) {
      Text(
          text = "(Go online to retrieve past step counts)",
          fontSize = 16.sp,
          modifier = Modifier.offset(5.dp, 0.dp))
    }
    Box(modifier = Modifier.height(60.dp))
  }

  Column(modifier = Modifier.fillMaxSize()) {
    Text(text = "Back", modifier = Modifier.padding(20.dp), fontSize = 20.sp)
    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp, 0.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
          Image(
              painter = painterResource(id = R.drawable.character),
              contentDescription = "Character",
              modifier =
                  Modifier.size(200.dp, 250.dp).offset(0.dp, (-60).dp).testTag("CharacterImage"))
          LinearProgressIndicator(
              progress = progress,
              modifier =
                  Modifier.fillMaxWidth().padding(20.dp, 0.dp).height(10.dp).testTag("ProgressBar"),
              color = colorResource(id = R.color.blueTheme),
              trackColor = colorResource(id = R.color.lightGrey))
          Row(
              horizontalArrangement = Arrangement.SpaceBetween,
              modifier = Modifier.offset(0.dp, 10.dp).fillMaxWidth()) {
                levelList.forEach { s -> Text(text = s, fontSize = 16.sp) }
              }
          if (isOnline(context)) {
            onlineStats()
          } else {
            offlineStats()
          }
          Button(
              onClick = { showDialog = true },
              colors = ButtonDefaults.buttonColors(colorResource(id = R.color.blueTheme)),
              modifier =
                  Modifier.fillMaxWidth()
                      .height(72.dp)
                      .padding(vertical = 8.dp, horizontal = 16.dp)
                      .testTag("SetNewGoalButton"),
              shape = RoundedCornerShape(8.dp)) {
                Text(text = "Set a new step goal", color = Color.White, fontSize = 24.sp)
              }
          if (dailyGoalAchieved) {
            CongratulationDialog(
                titleText = "Daily Step Goal",
                mainText = "Congratulations! You reached your daily step goal!",
                xpNumber = 100) {
                  dailyGoalAchieved = false
                }
          }
          if (showDialog) {
            SetStepGoalsDialog(
                onDismiss = { showDialog = false },
                onConfirm = { newDailyStepGoal, newWeeklyStepGoal ->
                  dailyStepGoal = newDailyStepGoal
                  weeklyStepGoal = newWeeklyStepGoal
                  showDialog = false
                  cacheStepGoals(context, newDailyStepGoal, newWeeklyStepGoal)
                })
          }
        }
  }
}

@Composable
fun BuildStats(dailyStepsMade: Int, dailyStepGoal: Int, weeklyStepsMade: Int, weeklyStepGoal: Int) {
  Box(modifier = Modifier.height(40.dp))
  BuildStatLine(
      icon = R.drawable.step_icon, title = "Daily steps", value = "$dailyStepsMade/$dailyStepGoal")
  Box(modifier = Modifier.height(20.dp))
  BuildStatLine(
      icon = R.drawable.step_icon,
      title = "Weekly steps",
      value = "$weeklyStepsMade/$weeklyStepGoal")
  Box(modifier = Modifier.height(20.dp))
  BuildStatLine(icon = R.drawable.boss_icon, title = "Bosses defeated", value = "24")
  Box(modifier = Modifier.height(60.dp))
}

@Composable
fun BuildStatLine(icon: Int, title: String, value: String) {
  Row(modifier = Modifier.fillMaxWidth().offset(20.dp, 0.dp)) {
    Image(
        painter = painterResource(id = icon),
        contentDescription = "Stat icon",
        modifier = Modifier.size(20.dp, 20.dp).testTag("$title icon"))
    Text(
        text = "$title: $value",
        fontSize = 16.sp,
        modifier = Modifier.offset(5.dp, 0.dp).testTag("$title text"))
  }
}
