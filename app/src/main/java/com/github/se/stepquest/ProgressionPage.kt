package com.github.se.stepquest

import SetStepGoalsDialog
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.se.stepquest.screens.CongratulationDialog
import com.github.se.stepquest.services.getCachedSteps
import com.github.se.stepquest.services.isOnline
import com.github.se.stepquest.viewModels.ProgressionPageViewModel

@Composable
fun ProgressionPage(
    user: UserRepository,
    context: Context,
    viewModel: ProgressionPageViewModel = viewModel()
) {
  val state by viewModel.state.collectAsState()
  var showDialog by remember { mutableStateOf(false) }

  LaunchedEffect(Unit) {
    viewModel.initialize(user, context) // Pass the UserRepository here
  }

  Column(modifier = Modifier.fillMaxSize()) {
    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp, 0.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
          Image(
              painter = painterResource(id = R.drawable.character),
              contentDescription = "Character",
              modifier =
                  Modifier.size(200.dp, 250.dp).offset(0.dp, (-60).dp).testTag("CharacterImage"))
          if (isOnline(context)) {
            BuildStats(
                dailyStepsMade = state.dailyStepsMade,
                dailyStepGoal = state.dailyStepGoal,
                weeklyStepsMade = state.weeklyStepsMade,
                weeklyStepGoal = state.weeklyStepGoal)
          } else {
            OfflineStats(context)
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
          if (state.dailyGoalAchieved) {
            CongratulationDialog(
                titleText = "Daily Step Goal",
                mainText = "Congratulations! You reached your daily step goal!",
                xpNumber = 100) {
                  viewModel.resetDailyGoalAchievement()
                }
          }
          if (showDialog) {
            SetStepGoalsDialog(
                onDismiss = { showDialog = false },
                onConfirm = { newDailyStepGoal, newWeeklyStepGoal ->
                  viewModel.updateGoals(newDailyStepGoal, newWeeklyStepGoal, context)
                  showDialog = false
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
  Box(modifier = Modifier.height(60.dp))
}

@Composable
fun OfflineStats(context: Context) {
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

@Composable
fun BuildStatLine(icon: Int, title: String, value: String) {
  Row(modifier = Modifier.fillMaxWidth().offset(20.dp, 0.dp)) {
    Image(
        painter = painterResource(id = icon),
        contentDescription = "Stat icon",
        modifier = Modifier.size(32.dp, 32.dp).testTag("$title icon"))
    Text(
        text = "$title: $value",
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.offset(8.dp, 0.dp).testTag("$title text"))
  }
}
