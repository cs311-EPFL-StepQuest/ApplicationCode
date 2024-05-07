package com.github.se.stepquest.services

import com.github.se.stepquest.IUserRepository
import com.github.se.stepquest.activity.Challenge
import com.github.se.stepquest.data.model.ChallengeData
import com.github.se.stepquest.data.model.NotificationData
import com.github.se.stepquest.data.repository.INotificationRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

fun sendPendingChallenge(senderUsername : String, receiverUsername : String, challenge: ChallengeData) {
    val userRepository = IUserRepository()
    val notificationRepository = INotificationRepository()
    val database = FirebaseDatabase.getInstance()
    val usernamesRef = database.reference.child("usernames")
    val senderUserId = userRepository.getUid().toString()
    usernamesRef.child(receiverUsername).addListenerForSingleValueEvent(
        object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val uid = snapshot.getValue(String::class.java) ?: return
                val challengeRequestsRef = database.reference.child("users").child(uid).child("pendingChallenges").child(challenge.uuid)
                challengeRequestsRef.addListenerForSingleValueEvent(
                    object : ValueEventListener {
                        override fun onDataChange(requestsSnapshot: DataSnapshot) {
                            if (requestsSnapshot.value == null) {
                                challengeRequestsRef.setValue(challenge)
                                println("Snapshot: ${snapshot.value}")
                                val receiverUserId = snapshot.value.toString()
                                val challengeType = challenge.type
                                val messageText = challengeType.messageText
                                notificationRepository.createNotification(
                                    receiverUserId,
                                    NotificationData(
                                        "$senderUsername sent you the following challenge : $messageText",
                                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")),
                                        UUID.randomUUID().toString(),
                                        receiverUserId,
                                        senderUserId)
                                )
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Handle access failure
                        }
                    })
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle access failure
            }
        })
}