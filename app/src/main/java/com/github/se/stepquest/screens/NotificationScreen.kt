package com.github.se.stepquest.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.se.stepquest.R
import com.github.se.stepquest.data.model.NotificationData
import com.github.se.stepquest.data.repository.NotificationRepository
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat

val notificationRepository = NotificationRepository()

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun NotificationScreen() {
    Column (modifier = Modifier.padding(0.dp, 30.dp)) {
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            Text("Notifications", fontSize = 20.sp)
        }
        Box(modifier = Modifier.height(40.dp))
        Divider(color = colorResource(id = R.color.blueTheme), thickness = 1.dp)
        NotificationList()
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun NotificationList() {
    val itemList = notificationRepository.getNotificationList()
    LazyColumn {
        items(itemList.size) {
            index -> BuildNotification(itemList[index])
        }
    }
}

@Composable
private fun BuildNotification(data: NotificationData) {
    Column {
        Row (horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 10.dp)) {
            Text(text = data.title)
            Row {
                Text(text = timeStampFormat(data.dateTime))
                Box(modifier = Modifier.width(20.dp))
                Icon(Icons.Default.Close, contentDescription = "Close", modifier = Modifier
                    .size(20.dp, 20.dp)
                    .clickable { notificationRepository.removeNotification(data.uuid) })
            }
        }
        if (data.showButtons)
            Row (horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 10.dp)) {
            Button(onClick = { /*TODO*/ }, content = {Text("Accept")}, modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .height(35.dp),
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.blueTheme)))
            Box(modifier = Modifier.width(20.dp))
            Button(onClick = { /*TODO*/ }, content = {Text("Reject")}, modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .height(35.dp),
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.lightGrey)))
        }
        Divider(color = colorResource(id = R.color.blueTheme), thickness = 1.dp)
    }

}

private fun timeStampFormat(data: Timestamp): String {
    val sfd: SimpleDateFormat =  SimpleDateFormat("HH:mm")
    return sfd.format(data.toDate())
}