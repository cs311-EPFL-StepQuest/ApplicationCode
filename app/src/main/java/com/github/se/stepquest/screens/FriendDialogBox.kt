package com.github.se.stepquest.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.github.se.stepquest.Friend
import com.github.se.stepquest.R

@Composable
fun FriendDialogBox(friend: Friend, onDismiss: () -> Unit) {
  val blueThemeColor = colorResource(id = R.color.blueTheme)
  val profilePictureURL = friend.profilePicture
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
                    IconButton(onClick = { onDismiss() }, modifier = Modifier.padding(8.dp)) {
                      Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                  }
              Text(text = friend.name, fontWeight = FontWeight.Bold, fontSize = 40.sp)
              Spacer(modifier = Modifier.height(2.dp))
              profilePictureURL?.let { uri ->
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = "Profile Picture",
                    modifier = Modifier.size(200.dp).clip(RoundedCornerShape(100.dp)))
              }
              Spacer(modifier = Modifier.height(16.dp))
              Button(
                  onClick = { /*Handle connection*/},
                  colors = ButtonDefaults.buttonColors(blueThemeColor),
                  modifier =
                      Modifier.fillMaxWidth()
                          .height(72.dp)
                          .padding(vertical = 8.dp)
                          .padding(horizontal = 16.dp),
                  shape = RoundedCornerShape(8.dp)) {
                    Text(text = "Connect", fontSize = 24.sp, color = Color.White)
                  }
              Button(
                  onClick = { /*Handle connection*/},
                  colors = ButtonDefaults.buttonColors(blueThemeColor),
                  modifier =
                      Modifier.fillMaxWidth()
                          .height(72.dp)
                          .padding(vertical = 8.dp)
                          .padding(horizontal = 16.dp),
                  shape = RoundedCornerShape(8.dp)) {
                    Text(text = "Challenge", fontSize = 24.sp, color = Color.White)
                  }
            }
      }
}
