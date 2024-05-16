package com.github.se.stepquest

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.edit

private const val SHARED_PREF_NAME = "StepQuestPrefs"
private const val DAILY_STEP_GOAL_KEY = "daily_step_goal"
private const val WEEKLY_STEP_GOAL_KEY = "weekly_step_goal"

@Composable
fun ProgressionPage(user: UserRepository, context : Context) {
  val levelList = arrayListOf<String>("Current lvl", "Next lvl")
  var progress by remember { mutableStateOf(0.5f) }
  var showDialog by remember { mutableStateOf(false) }
    val sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
  var dailyStepsMade by remember { mutableStateOf(0) }
  var weeklyStepsMade by remember { mutableStateOf(0) }

  user.getSteps { steps -> dailyStepsMade = steps[0] }
  user.getSteps { steps -> weeklyStepsMade = steps[1] }

  var dailyStepGoal by remember { mutableIntStateOf(sharedPreferences.getInt(DAILY_STEP_GOAL_KEY, 5000)) }
  var weeklyStepGoal by remember { mutableIntStateOf(sharedPreferences.getInt(WEEKLY_STEP_GOAL_KEY, 35000)) }
  var dailyGoalAchieved by remember { mutableStateOf(dailyStepsMade > dailyStepGoal) }

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
          BuildStats(
              dailyStepsMade = dailyStepsMade,
              dailyStepGoal = dailyStepGoal,
              weeklyStepsMade = weeklyStepsMade,
              weeklyStepGoal = weeklyStepGoal)
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
            SetDailyGoalAchievedDialog(onConfirm = { dailyGoalAchieved = false })
          }
          if (showDialog) {
            SetStepGoalsDialog(
                onDismiss = { showDialog = false },
                onConfirm = { newDailyStepGoal, newWeeklyStepGoal ->
                  dailyStepGoal = newDailyStepGoal
                  weeklyStepGoal = newWeeklyStepGoal
                  showDialog = false
                    sharedPreferences.edit {
                        putInt(DAILY_STEP_GOAL_KEY, newDailyStepGoal)
                        putInt(WEEKLY_STEP_GOAL_KEY, newWeeklyStepGoal)
                        apply()
                    }
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

@Composable
fun SetDailyGoalAchievedDialog(onConfirm: () -> Unit) {
  val blueThemeColor = colorResource(id = R.color.blueTheme)

  Dialog(onDismissRequest = { onConfirm() }) {
    Surface(
        color = Color.White,
        border = BorderStroke(1.dp, Color.Black),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.padding(16.dp)) {
          Column(
              modifier = Modifier.padding(16.dp).fillMaxWidth(),
              horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Daily Step Goal", fontSize = 20.sp)
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Congratulations! You reached your daily step goal!",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.testTag("Achieved goal message"))
                Spacer(modifier = Modifier.height(15.dp))
                Text(text = "You receive 100 XP", textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = { onConfirm() },
                    colors = ButtonDefaults.buttonColors(blueThemeColor),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(horizontal = 4.dp).testTag("Confirm button")) {
                      Text(text = "Confirm")
                    }
              }
        }
  }
}

private fun SharedPreferences.edit(action: SharedPreferences.Editor.() -> Unit) {
    val editor = edit()
    action(editor)
    editor.apply()
}
