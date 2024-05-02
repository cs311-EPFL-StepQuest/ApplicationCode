package com.github.se.stepquest.data.repository

import com.github.se.stepquest.data.model.NotificationData
import com.github.se.stepquest.screens.notificationRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class NotificationRepository {
  private val notificationCollection =
      FirebaseDatabase.getInstance().reference.child("notifications")

  fun getNotificationList(userUuid: String): DatabaseReference {
    return notificationCollection.child(userUuid)
  }

  fun removeNotification(userUuid: String, notificationUuid: String) {
    println("Remove userId: $userUuid; notificationId: $notificationUuid")
    notificationRepository
        .getNotificationList(userUuid)
        .child(notificationUuid)
        .addValueEventListener(
            object : ValueEventListener {
              override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (data in dataSnapshot.children) {
                  println("Data: ${data.value}")
                  data.ref.removeValue()
                }
              }

              override fun onCancelled(databaseError: DatabaseError) {}
            })
  }

  fun createNotification(userUuid: String, notificationData: NotificationData) {
    notificationCollection.child(userUuid).child(notificationData.uuid).setValue(notificationData)
  }
}
//    private var notificationList = listOf(
//        NotificationData("You achieved your daily goal!", Timestamp.now(), "notification1",
// false),
//        NotificationData("You received new friend request", Timestamp.now(), "notification2",
// true),
//        NotificationData("You received new challenge!", Timestamp.now(), "notification3", true))
