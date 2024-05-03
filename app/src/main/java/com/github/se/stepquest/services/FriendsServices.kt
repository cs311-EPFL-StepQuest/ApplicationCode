package com.github.se.stepquest.services

import com.github.se.stepquest.Friend
import com.github.se.stepquest.IUserRepository
import com.github.se.stepquest.data.model.NotificationData
import com.github.se.stepquest.data.repository.NotificationRepository
import com.github.se.stepquest.screens.NotificationScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

fun addFriend(friend: Friend) {
  val firebaseAuth = FirebaseAuth.getInstance()
  val database = FirebaseDatabase.getInstance()
  val userId = firebaseAuth.currentUser?.uid
  if (userId != null) {
    val friendsRefTotal = database.reference.child("users").child(userId).child("numberOfFriends")
    friendsRefTotal.addListenerForSingleValueEvent(
        object : ValueEventListener {
          override fun onDataChange(dataSnapshot: DataSnapshot) {
            val numberOfFriends = dataSnapshot.getValue(Int::class.java) ?: 0
            friendsRefTotal.setValue(numberOfFriends + 1)
          }

          override fun onCancelled(databaseError: DatabaseError) {
            // add code when failing to access database
          }
        })
    val friendsListRef = database.reference.child("users").child(userId).child("friendsList")
    friendsListRef.addListenerForSingleValueEvent(
        object : ValueEventListener {
          override fun onDataChange(dataSnapshot: DataSnapshot) {
            val currentFriendsList: MutableList<Friend> =
                dataSnapshot.getValue<List<Friend>>()?.toMutableList() ?: mutableListOf()
            currentFriendsList.add(friend)
            friendsListRef
                .setValue(currentFriendsList)
                .addOnSuccessListener {
                  // Handle success if needed
                }
                .addOnFailureListener {
                  // Handle failure if needed
                }
          }

          override fun onCancelled(databaseError: DatabaseError) {
            // add code when failing to access database
          }
        })
  }
}


fun sendFriendRequest(currentUsername: String, friendName: String) {
    val userRepository = IUserRepository()
    val notificationRepository = NotificationRepository()
    val database = FirebaseDatabase.getInstance()

  // Retrieve the new friend's uid
  val usernamesRef = database.reference.child("usernames")
    //Send notification
    val senderUserId = userRepository.getUid().toString()
    usernamesRef.child(friendName).addListenerForSingleValueEvent(object : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            println("Snapshot: ${snapshot.value}")
            val receiverUserId = snapshot.value.toString()
            notificationRepository.createNotification(senderUserId,
                NotificationData(
                    "You received new friend request from $currentUsername!",
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")),
                    UUID.randomUUID().toString(),
                    senderUserId,
                    receiverUserId))
        }

        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }

    })
  usernamesRef.addListenerForSingleValueEvent(
      object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
          val uid = snapshot.child(friendName).getValue(String::class.java) ?: return

          // Send the friend request
          val friendRequestsRef =
              database.reference.child("users").child(uid).child("pendingFriendRequests")
          friendRequestsRef.addListenerForSingleValueEvent(
              object : ValueEventListener {
                override fun onDataChange(requestsSnapshot: DataSnapshot) {
                  val pendingRequests =
                      requestsSnapshot.getValue<List<String>>()?.toMutableList() ?: mutableListOf()
                  if (currentUsername in pendingRequests) return
                  pendingRequests.add(currentUsername)
                  friendRequestsRef.setValue(pendingRequests)
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
