package com.github.se.stepquest

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.github.se.stepquest.ui.theme.StepQuestTheme

@Composable
fun ProgressionPageLayout() {
  val blueThemeColor = colorResource(id = R.color.blueTheme)
  var showDialog by remember { mutableStateOf(false) }
  var dailyStepGoal by remember { mutableIntStateOf(5000) }
  var weeklyStepGoal by remember { mutableIntStateOf(35000) }

  Column(
      modifier = Modifier.padding(32.dp).fillMaxSize(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(64.dp)) {
        // Temporary until we have a real character
        Image(
            painter = painterResource(id = R.drawable.tempchar),
            contentDescription = "tempCharacter",
            modifier = Modifier.width(200.dp).height(200.dp))
        Text(text = "Daily Steps : 0/$dailyStepGoal")
        Text(text = "Weekly Steps : 0/$weeklyStepGoal")
        Button(
            onClick = { showDialog = true },
            colors = ButtonDefaults.buttonColors(blueThemeColor),
            modifier =
                Modifier.fillMaxWidth()
                    .height(72.dp)
                    .padding(vertical = 8.dp)
                    .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(8.dp)) {
              Text(text = "Set new step goals", color = Color.White, fontSize = 24.sp)
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

@Preview(showBackground = true)
@Composable
fun ProgressionPagePreview() {
  StepQuestTheme { ProgressionPageLayout() }
}
