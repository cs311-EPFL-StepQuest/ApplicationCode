package com.github.se.stepquest.services

import com.github.se.stepquest.Friend
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

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

  val database = FirebaseDatabase.getInstance()

  // Retrieve the new friend's uid
  val usernamesRef = database.reference.child("usernames")
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

fun deletePendingFriendRequest(friendName: String) {

  val database = FirebaseDatabase.getInstance()
  val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

  val pendingRequestsRef =
      database.reference.child("users").child(userId).child("pendingFriendRequests")
  pendingRequestsRef.addListenerForSingleValueEvent(
      object : ValueEventListener {
        override fun onDataChange(requestsSnapshot: DataSnapshot) {
          val pendingRequests =
              requestsSnapshot.getValue<List<String>>()?.toMutableList() ?: mutableListOf()
          if (friendName !in pendingRequests) return
          pendingRequests.remove(friendName)
          pendingRequestsRef.setValue(pendingRequests)
        }

        override fun onCancelled(error: DatabaseError) {
          // Handle access failure
        }
      })
}
