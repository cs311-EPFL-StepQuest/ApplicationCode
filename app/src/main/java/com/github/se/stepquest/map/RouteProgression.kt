package com.github.se.stepquest.map

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteProgression(onDismiss: () -> Unit, routeLength: Float, numCheckpoints: Int) {
  var routeName by rememberSaveable { mutableStateOf("") }
  var routeID by rememberSaveable { mutableStateOf("") }
  var reward by rememberSaveable { mutableIntStateOf(0) }
  var extraKilometers by rememberSaveable { mutableIntStateOf(0) }
  var extraCheckpoints by rememberSaveable { mutableIntStateOf(0) }

  reward = (routeLength * 100).toInt()
  // Create a unique routeID (might find a better way)
  routeID = "route_${System.currentTimeMillis()}"
  extraKilometers = (routeLength / 10).toInt()
  extraCheckpoints = (numCheckpoints / 5).toInt()

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
                    verticalAlignment = Alignment.CenterVertically) {
                      // "End Route" title
                      Box(
                          modifier = Modifier.weight(1f),
                          contentAlignment = Alignment.CenterStart) {
                            Text(text = "End Route", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                          }
                      // "X" button to close window
                      Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterEnd) {
                        IconButton(onClick = { onDismiss() }, modifier = Modifier.padding(8.dp)) {
                          Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                      }
                    }
                // Input field for route name
                TextField(
                    value = routeName,
                    onValueChange = { routeName = it },
                    placeholder = { Text("Route name") },
                    shape = RoundedCornerShape(24.dp),
                    singleLine = true,
                    colors =
                        TextFieldDefaults.textFieldColors(
                            disabledTextColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent))
                Spacer(modifier = Modifier.height(20.dp))
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart,
                ) {
                  Text(
                      text = "Route length: $routeLength km",
                      fontSize = 16.sp,
                      fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(5.dp))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart,
                ) {
                  Text(
                      text = "Number of checkpoints: $numCheckpoints",
                      fontSize = 16.sp,
                      fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(5.dp))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart,
                ) {
                  Text(
                      text = "Reward: $reward points",
                      fontSize = 16.sp,
                      fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                  Text(
                      buildAnnotatedString {
                        withStyle(
                            style =
                                SpanStyle(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = Color.Red)) {
                              append(
                                  "$extraKilometers extra kilometers or $extraCheckpoints extra checkpoints for ")
                            }
                        withStyle(
                            style =
                                SpanStyle(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Red)) {
                              append("next reward")
                            }
                      })
                }
                Spacer(modifier = Modifier.height(20.dp))

                // Finish button
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                  Button(
                      onClick = {
                        saveRoute(
                            onDismiss, routeName, routeID, routeLength, numCheckpoints, reward)
                      },
                      enabled = routeName.isNotEmpty(),
                      colors = ButtonDefaults.buttonColors(Color(0xFF0D99FF)),
                      modifier = Modifier.height(35.dp).width(140.dp)) {
                        Text(text = "Finish", fontSize = 16.sp, modifier = Modifier.padding(0.dp))
                      }
                }
              }
        }
  }
}

fun saveRoute(
    onDismiss: () -> Unit,
    routeName: String,
    routeID: String,
    routeLength: Float,
    numCheckpoints: Int,
    reward: Int
) {
  // Save route to database
  onDismiss()
}
