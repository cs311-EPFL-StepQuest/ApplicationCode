package com.github.se.stepquest.screens

import android.annotation.SuppressLint
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.se.stepquest.Friend
import com.github.se.stepquest.R
import com.github.se.stepquest.UserRepository
import com.github.se.stepquest.data.model.NotificationData
import com.github.se.stepquest.data.repository.INotificationRepository
import com.github.se.stepquest.services.addFriend
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue

val notificationRepository = INotificationRepository()
var notificationList: MutableList<NotificationData> by mutableStateOf(mutableListOf())

@Composable
fun NotificationScreen(userRepository: UserRepository) {
  val uuid = userRepository.getUid()
  //  notificationRepository.createNotification(
  //      uuid!!, NotificationData("Hello2", "13:54", UUID.randomUUID().toString(), uuid, ""))
  //  notificationRepository.createNotification(
  //      uuid, NotificationData("Hello3", "13:55", UUID.randomUUID().toString(), uuid, "lsdiv"))
  //  notificationRepository.createNotification(
  //      uuid, NotificationData("Hello4", "13:56", UUID.randomUUID().toString(), uuid, ""))
  updateNotificationList(uuid)
  Column(modifier = Modifier.padding(0.dp, 30.dp)) {
    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
      Text("Notifications", fontSize = 20.sp, modifier = Modifier.testTag("Notifications title"))
    }
    Box(modifier = Modifier.height(40.dp))
    Divider(color = colorResource(id = R.color.blueTheme), thickness = 1.dp)
    NotificationList()
  }
}

@SuppressLint(
    "CoroutineCreationDuringComposition",
    "UnrememberedMutableState",
    "MutableCollectionMutableState")
@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun NotificationList() {
  LazyColumn {
    items(notificationList.size) { index -> BuildNotification(notificationList[index]) }
  }
}

@Composable
private fun BuildNotification(data: NotificationData?) {
  if (data == null) return
  Column {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth().padding(20.dp, 10.dp)) {
          Text(text = data.text)
          Row {
            Text(text = data.dateTime)
            Box(modifier = Modifier.width(20.dp))
            Icon(
                Icons.Default.Close,
                contentDescription = "Close",
                modifier =
                    Modifier.size(20.dp, 20.dp).clickable {
                      notificationRepository.removeNotification(data.userUuid, data.uuid)
                    })
          }
        }
    if (data.senderUuid.isNotEmpty()) {
      var friendName = ""
      var currentuserName = ""

      Row(
          horizontalArrangement = Arrangement.SpaceBetween,
          modifier = Modifier.fillMaxWidth().padding(20.dp, 10.dp)) {
            Button(
                onClick = {
                  val database = FirebaseDatabase.getInstance()

                  database.reference
                      .child("users")
                      .child(data.userUuid)
                      .child("username")
                      .addListenerForSingleValueEvent(
                          object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                              currentuserName = snapshot.getValue(String::class.java) ?: return

                              database.reference
                                  .child("users")
                                  .child(data.senderUuid)
                                  .child("username")
                                  .addListenerForSingleValueEvent(
                                      object : ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                          friendName =
                                              snapshot.getValue(String::class.java) ?: return

                                          println("Your name is $currentuserName")
                                          println("Their name is $friendName")

                                          addFriend(
                                              Friend(name = currentuserName),
                                              Friend(name = friendName))
                                        }

                                        override fun onCancelled(error: DatabaseError) {}
                                      })
                            }

                            override fun onCancelled(error: DatabaseError) {}
                          })

                  notificationRepository.removeNotification(data.userUuid, data.uuid)
                },
                content = { Text("Accept") },
                modifier = Modifier.fillMaxWidth().weight(1f).height(35.dp),
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.blueTheme)))
            Box(modifier = Modifier.width(20.dp))
            Button(
                onClick = { notificationRepository.removeNotification(data.userUuid, data.uuid) },
                content = { Text("Reject") },
                modifier = Modifier.fillMaxWidth().weight(1f).height(35.dp),
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.lightGrey)))
          }
      Divider(color = colorResource(id = R.color.blueTheme), thickness = 1.dp)
    }
  }
}

fun updateNotificationList(uuid: String?) {
  notificationRepository
      .getNotificationList(uuid!!)
      .addValueEventListener(
          object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
              val newNotificationList = arrayListOf<NotificationData>()
              notificationList = arrayListOf()
              for (postSnapshot in dataSnapshot.children) {
                val resultData = postSnapshot.getValue<NotificationData?>()
                if (resultData == null || resultData.isNull()) continue
                if (notificationList.contains(resultData)) continue
                newNotificationList.add(postSnapshot.getValue<NotificationData>()!!)
                println("Notification: ${postSnapshot.value}")
              }
              notificationList = newNotificationList
              println("Notifications list size: ${notificationList.size}")
            }

            override fun onCancelled(databaseError: DatabaseError) {
              println("Error: $databaseError")
            }
          })
}
