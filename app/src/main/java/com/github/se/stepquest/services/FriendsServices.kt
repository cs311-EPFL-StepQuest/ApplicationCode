package com.github.se.stepquest.services

import com.github.se.stepquest.Friend
import com.github.se.stepquest.IUserRepository
import com.github.se.stepquest.data.model.NotificationData
import com.github.se.stepquest.data.repository.INotificationRepository
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

fun deleteFriend(
    currentUsername: String,
    friendName: String,
    database: FirebaseDatabase = FirebaseDatabase.getInstance(),
    currentUserId: String? = FirebaseAuth.getInstance().currentUser?.uid
) {

  if (currentUserId == null) return

  // Retrieve friend's uid
  val usernamesRef = database.reference.child("usernames")
  usernamesRef.addListenerForSingleValueEvent(
      object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
          val uid = snapshot.child(friendName).getValue(String::class.java) ?: return

          // Remove friend from current user's list
          val curFriendsRefTotal =
              database.reference.child("users").child(currentUserId).child("numberOfFriends")
          curFriendsRefTotal.addListenerForSingleValueEvent(
              object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                  val numberOfFriends = dataSnapshot.getValue(Int::class.java) ?: 0
                  curFriendsRefTotal.setValue(numberOfFriends - 1)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                  // add code when failing to access database
                }
              })

          val curFriendsListRef =
              database.reference.child("users").child(currentUserId).child("friendsList")
          curFriendsListRef.addListenerForSingleValueEvent(
              object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                  val currentFriendsList: MutableList<Friend> =
                      dataSnapshot.getValue<List<Friend>>()?.toMutableList() ?: mutableListOf()
                  val friendToRemove = currentFriendsList.find { it.name == friendName }

                  if (friendToRemove == null) return

                  currentFriendsList.remove(friendToRemove)
                  curFriendsListRef.setValue(currentFriendsList)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                  // add code when failing to access database
                }
              })

          // Remove current user from friend's list
          val friendsRefTotal =
              database.reference.child("users").child(uid).child("numberOfFriends")
          friendsRefTotal.addListenerForSingleValueEvent(
              object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                  val numberOfFriends = dataSnapshot.getValue(Int::class.java) ?: 0
                  friendsRefTotal.setValue(numberOfFriends - 1)
                }

                override fun onCancelled(error: DatabaseError) {
                  // Handle database access failure
                }
              })

          val friendsListRef = database.reference.child("users").child(uid).child("friendsList")
          friendsListRef.addListenerForSingleValueEvent(
              object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                  val currentFriendsList: MutableList<Friend> =
                      dataSnapshot.getValue<List<Friend>>()?.toMutableList() ?: mutableListOf()
                  val friendToRemove = currentFriendsList.find { it.name == currentUsername }

                  if (friendToRemove == null) return

                  currentFriendsList.remove(friendToRemove)
                  friendsListRef.setValue(currentFriendsList)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                  // add code when failing to access database
                }
              })
        }

        override fun onCancelled(error: DatabaseError) {
          // Handle database access failure
        }
      })
}

fun sendFriendRequest(currentUsername: String, friendName: String) {
  val userRepository = IUserRepository()
  val notificationRepository = INotificationRepository()
  val database = FirebaseDatabase.getInstance()

  // Retrieve the new friend's uid
  val usernamesRef = database.reference.child("usernames")
  // Send notification
  val senderUserId = userRepository.getUid().toString()
  usernamesRef
      .child(friendName)
      .addListenerForSingleValueEvent(
          object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
              println("Snapshot: ${snapshot.value}")
              val receiverUserId = snapshot.value.toString()
              notificationRepository.createNotification(
                  receiverUserId,
                  NotificationData(
                      "You received new friend request from $currentUsername!",
                      LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")),
                      UUID.randomUUID().toString(),
                      receiverUserId,
                      senderUserId))
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
