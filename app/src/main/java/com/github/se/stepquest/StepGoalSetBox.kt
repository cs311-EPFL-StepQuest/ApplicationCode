package com.github.se.stepquest

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun SetStepGoalsDialog(
    onDismiss: () -> Unit,
    onConfirm: (dailyStepGoal: Int, weeklyStepGoal: Int) -> Unit
) {
  val blueThemeColor = colorResource(id = R.color.blueTheme)
  var newDailyStepGoal by remember { mutableStateOf("") }
  val newWeeklyStepGoal by remember { mutableStateOf("") }

  Dialog(onDismissRequest = { onDismiss() }) {
    Surface(
        color = Color.White,
        border = BorderStroke(1.dp, Color.Black),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.padding(16.dp)) {
          Column(
              modifier = Modifier.padding(16.dp).fillMaxWidth(),
              horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically) {
                      Text(text = "Set New Step Goals", fontSize = 20.sp)
                      IconButton(onClick = { onDismiss() }, modifier = Modifier.padding(8.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                      }
                    }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Daily steps")
                Spacer(modifier = Modifier.height(2.dp))
                TextField(
                    modifier = Modifier.testTag("daily_steps_setter"),
                    value = newDailyStepGoal,
                    onValueChange = { newDailyStepGoal = it.filter { it.isDigit() }.take(5) },
                    label = { Text("Enter your daily step goal") },
                    placeholder = { Text("5000") },
                    keyboardOptions =
                        KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.NumberPassword, imeAction = ImeAction.Done),
                    keyboardActions =
                        KeyboardActions(
                            onDone = {
                              val (dailyStep, weeklyStep) =
                                  calculateStepGoals(newDailyStepGoal, newWeeklyStepGoal)
                              onConfirm(dailyStep, weeklyStep)
                              onDismiss()
                            }))
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                      val (dailyStep, weeklyStep) =
                          calculateStepGoals(newDailyStepGoal, newWeeklyStepGoal)
                      onConfirm(dailyStep, weeklyStep)
                      onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(blueThemeColor),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(horizontal = 4.dp)) {
                      Text(text = "Confirm")
                    }
              }
        }
  }
}

fun calculateStepGoals(newDailyStepGoal: String, newWeeklyStepGoal: String): Pair<Int, Int> {
  val dailyStep =
      newDailyStepGoal
          .filter { it.isDigit() }
          .take(5)
          .let {
            if (it.isBlank()) {
              5000 // Default value if blank
            } else {
              val parsedInput = it.toIntOrNull() ?: 0
              val roundedValue = (parsedInput + 249) / 250 * 250
              if (roundedValue < 1000) {
                1000
              } else {
                roundedValue
              }
            }
          }

  val weeklyStep = newWeeklyStepGoal.takeIf { it.isNotBlank() }?.toInt() ?: (dailyStep * 7)

  return Pair(dailyStep, weeklyStep)
}
