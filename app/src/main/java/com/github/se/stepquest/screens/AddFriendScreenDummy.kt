package com.github.se.stepquest.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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

@Composable
fun AddFriendScreen(onDismiss: () -> Unit) {
  val blueThemeColor = colorResource(id = R.color.blueTheme)
  var searchQuery by remember { mutableStateOf("") }
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
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Back",
                        fontSize = 20.sp,
                        modifier = Modifier.clickable { onDismiss() })
                    Spacer(modifier = Modifier.weight(0.3f))
                    IconButton(
                        onClick = {
                          onDismiss()
                        },
                        modifier = Modifier.padding(8.dp)) {
                          Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                  }
              Text(
                  text = "Friends",
                  fontWeight = FontWeight.Bold,
                  fontSize = 20.sp,
              )
              Spacer(modifier = Modifier.height(16.dp))
              Text(
                  text = "Search for friends",
                  fontSize = 16.sp,
                  modifier = Modifier.padding(top = 16.dp))
              Spacer(modifier = Modifier.height(16.dp))
              TextField(
                  value = searchQuery,
                  onValueChange = { searchQuery = it },
                  placeholder = { Text("Search for friends") },
                  modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp))
            }
      }
}
