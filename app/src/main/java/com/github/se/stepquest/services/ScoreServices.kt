package com.github.se.stepquest.services

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

fun addPoints(
    username: String,
    points: Int,
    database: FirebaseDatabase = FirebaseDatabase.getInstance()
) {

    val userPointsRef = database.reference.child("leaderboard").child(username)

    userPointsRef.addListenerForSingleValueEvent(object : ValueEventListener {

        override fun onDataChange(snapshot: DataSnapshot) {

            val curr = snapshot.getValue(Int::class.java) ?: 0
            val newPoints = curr + points
            userPointsRef.setValue(newPoints)
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("ScoreServices", "Database error: ${error.message}")
        }

    })
}