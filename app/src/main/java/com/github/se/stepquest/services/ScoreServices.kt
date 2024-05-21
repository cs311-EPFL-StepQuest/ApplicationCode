package com.github.se.stepquest.services

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

fun getTopLeaderboard(callback: (List<Pair<String, Int>>?) -> Unit) {
  val database = FirebaseDatabase.getInstance()
  val leaderboardRef = database.reference.child("leaderboard")
  leaderboardRef
      .orderByValue()
      .limitToLast(5)
      .addListenerForSingleValueEvent(
          object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
              val leaderboard = mutableListOf<Pair<String, Int>>()
              for (snapshot in dataSnapshot.getChildren()) {
                val username = snapshot.key ?: continue
                val score = snapshot.getValue(Int::class.java) ?: continue
                leaderboard.add(Pair(username, score))
              }
                leaderboard.sortByDescending { it.second }
                callback(leaderboard)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null)
            }
          })
}

fun getFriendsLeaderboard(callback: (List<Pair<String, Int>>?) -> Unit) {}

fun getUserScore(username: String, callback: (Int) -> Unit) {
    val database = FirebaseDatabase.getInstance()
    val leaderboardRef = database.reference.child("leaderboard").child(username)
    leaderboardRef
        .addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val score = dataSnapshot.getValue(Int::class.java) ?: 0
                    callback(score)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(0)
                }
            })
}
