package com.github.se.stepquest.data.repository

import com.github.se.stepquest.data.model.NotificationData
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.firestore
import java.util.Stack

class NotificationRepository {
    //val userCollection = Firebase.firestore.collection("notifications")
    private var notificationList = listOf(NotificationData("You achieved your daily goal!", Timestamp.now(), "notification1", false),
        NotificationData("You received new friend request", Timestamp.now(), "notification2", true),
        NotificationData("You received new challenge!", Timestamp.now(), "notification3", true))
    fun getNotificationList(): List<NotificationData> {
        return notificationList
    }

    fun removeNotification(uuid: String) {
        val tempList = Stack<NotificationData>()
        notificationList.forEach { e ->
            if (e.uuid != uuid) {
                tempList.push(e)
            }
        }
        notificationList = tempList.toList()
    }
}