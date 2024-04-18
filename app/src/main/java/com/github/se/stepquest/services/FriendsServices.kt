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
                        .addOnFailureListener { e ->
                            // Handle failure if needed
                        }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // add code when failing to access database
                }
            })
    }
}