package com.github.se.stepquest.services

import android.text.format.DateFormat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Date

class FriendsService {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    fun onCreate() {
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
    }

    private fun addFriend(friend: Friend) {
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
                        val currentFriendsList = dataSnapshot.getValue(String()::class.java) ?: ""
                        val newFriendsList =
                        friendsListRef.setValue(totalSteps)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // add code when failing to access database
                    }
                })
        }
    }
}