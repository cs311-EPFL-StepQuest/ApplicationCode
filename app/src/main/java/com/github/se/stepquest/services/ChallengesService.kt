package com.github.se.stepquest.services

import com.github.se.stepquest.IUserRepository
import com.github.se.stepquest.data.model.ChallengeData
import com.github.se.stepquest.data.model.NotificationData
import com.github.se.stepquest.data.model.NotificationType
import com.github.se.stepquest.data.repository.INotificationRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

fun sendPendingChallenge(
    senderUsername: String,
    receiverUsername: String,
    challenge: ChallengeData
) {
  val userRepository = IUserRepository()
  val notificationRepository = INotificationRepository()
  val database = FirebaseDatabase.getInstance()
  val usernamesRef = database.reference.child("usernames")
  val senderUserId = userRepository.getUid().toString()
  usernamesRef
      .child(receiverUsername)
      .addListenerForSingleValueEvent(
          object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
              val uid = snapshot.getValue(String::class.java) ?: return
              val challengeRequestsRef =
                  database.reference
                      .child("users")
                      .child(uid)
                      .child("pendingChallenges")
                      .child(challenge.uuid)
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
                                senderUserId,
                                objectUuid = challenge.uuid,
                                type = NotificationType.CHALLENGE))
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

fun getPendingChallenge(userId: String, challengeUuid: String, callback: (ChallengeData?) -> Unit) {
  val database = FirebaseDatabase.getInstance()
  val challengeRef =
      database.reference
          .child("users")
          .child(userId)
          .child("pendingChallenges")
          .child(challengeUuid)
  challengeRef.addListenerForSingleValueEvent(
      object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
          val challenge = snapshot.getValue(ChallengeData::class.java)
          callback(challenge)
        }

        override fun onCancelled(error: DatabaseError) {}
      })
}

fun acceptChallenge(challenge: ChallengeData) {
  // initialize the challenge
  val database = FirebaseDatabase.getInstance()
  val firstUserRef =
      database.reference
          .child("users")
          .child(challenge.challengedUserUuid)
          .child("acceptedChallenges")
          .child(challenge.uuid)
  val secondUserRef =
      database.reference
          .child("users")
          .child(challenge.senderUserUuid)
          .child("acceptedChallenges")
          .child(challenge.uuid)
  firstUserRef.addListenerForSingleValueEvent(
      object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
          firstUserRef.setValue(challenge)
          secondUserRef.setValue(challenge)
        }

        override fun onCancelled(error: DatabaseError) {}
      })
}

fun getTopChallenge(userId: String, callback: (ChallengeData?) -> Unit) {
    val database = FirebaseDatabase.getInstance()
    val challengeRef = database.reference.child("users").child(userId).child("acceptedChallenges")
    challengeRef.addListenerForSingleValueEvent(
        object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val challenge = dataSnapshot.children.firstOrNull()?.getValue(ChallengeData::class.java)
                    callback(challenge)
                } else {
                    callback(null)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
}

fun getChallenges(userId: String, callback: (List<ChallengeData>) -> Unit) {
  val challenges: List<ChallengeData> = emptyList()
  val database = FirebaseDatabase.getInstance()
  val acceptedChallengesRef =
      database.reference.child("users").child(userId).child("acceptedChallenges")
  acceptedChallengesRef.addListenerForSingleValueEvent(
      object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
          for (snapshot in dataSnapshot.getChildren()) {
            val challenge = snapshot.getValue(ChallengeData::class.java)
            challenges.plus(challenge)
          }
          callback(challenges)
        }

        override fun onCancelled(error: DatabaseError) {}
      })
}
