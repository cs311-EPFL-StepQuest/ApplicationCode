package com.github.se.stepquest

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ProgressionPage() {
  val levelList = arrayListOf<String>("Current lvl", "Next lvl")
  var progress by remember { mutableStateOf(0.5f) }
  var showDialog by remember { mutableStateOf(false) }
  var dailyStepGoal by remember { mutableIntStateOf(5000) }
  var weeklyStepGoal by remember { mutableIntStateOf(35000) }
  Column(modifier = Modifier.fillMaxSize()) {
    Text(text = "Back", modifier = Modifier.padding(20.dp), fontSize = 20.sp)
    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp, 0.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
          Image(
              painter = painterResource(id = R.drawable.character),
              contentDescription = "Character",
              modifier = Modifier.size(200.dp, 250.dp).offset(0.dp, (-60).dp))
          LinearProgressIndicator(
              progress = progress,
              modifier = Modifier.fillMaxWidth().padding(20.dp, 0.dp).height(10.dp),
              color = colorResource(id = R.color.blueTheme),
              trackColor = colorResource(id = R.color.lightGrey))
          Row(
              horizontalArrangement = Arrangement.SpaceBetween,
              modifier = Modifier.offset(0.dp, 10.dp).fillMaxWidth()) {
                levelList.forEach { s -> Text(text = s, fontSize = 16.sp) }
              }
          BuildStats()
          Button(
              onClick = { showDialog = true },
              colors = ButtonDefaults.buttonColors(colorResource(id = R.color.blueTheme)),
              modifier =
                  Modifier.fillMaxWidth()
                      .height(72.dp)
                      .padding(vertical = 8.dp, horizontal = 16.dp),
              shape = RoundedCornerShape(8.dp)) {
                Text(text = "Set a new step goal", color = Color.White, fontSize = 24.sp)
              }
          if (showDialog) {
            SetStepGoalsDialog(
                onDismiss = { showDialog = false },
                onConfirm = { newDailyStepGoal, newWeeklyStepGoal ->
                  dailyStepGoal = newDailyStepGoal
                  weeklyStepGoal = newWeeklyStepGoal
                  showDialog = false
                })
          }
        }
  }
}

@Composable
fun BuildStats() {
  Box(modifier = Modifier.height(40.dp))
  BuildStatLine(icon = R.drawable.step_icon, title = "Daily steps", value = "3400/5000")
  Box(modifier = Modifier.height(20.dp))
  BuildStatLine(icon = R.drawable.step_icon, title = "Weekly steps", value = "7400/20000")
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
        modifier = Modifier.size(20.dp, 20.dp))
    Text(text = "$title: $value", fontSize = 16.sp, modifier = Modifier.offset(5.dp, 0.dp))
  }
}