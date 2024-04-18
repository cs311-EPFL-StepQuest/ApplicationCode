package com.github.se.stepquest

import android.text.format.DateFormat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Date

interface UserRepository {
  fun getUid(): String?

  fun getSteps(): List<Int>
}

class IUserRepository : UserRepository {
  val firebaseAuth = FirebaseAuth.getInstance()
  val userId = firebaseAuth.currentUser?.uid
  val database = FirebaseDatabase.getInstance()

  override fun getUid(): String? {
    return this.userId
  }

  override fun getSteps(): List<Int> {
    var list = listOf(0, 0)
    val d = Date()
    val s: CharSequence = DateFormat.format("MMMM d, yyyy ", d.getTime())
    val stepsRef = database.reference.child("users").child(userId!!).child("dailySteps $s")
    stepsRef.addListenerForSingleValueEvent(
        object : ValueEventListener {
          override fun onDataChange(dataSnapshot: DataSnapshot) {
            list =
                listOf(
                    dataSnapshot.getValue(Int::class.java) ?: 0,
                    dataSnapshot.getValue(Int::class.java) ?: 0)
          }

          override fun onCancelled(databaseError: DatabaseError) {
            // add code when failing to access database
          }
        })
    return list
  }
}

class TestUserRepository : UserRepository {
  private val uid = "testuid"
  private val dailyStepsMade = 2500
  private val weeklyStepsMade = 6500

  override fun getUid(): String {
    return this.uid
  }

  override fun getSteps(): List<Int> {
    return listOf(dailyStepsMade, weeklyStepsMade)
  }
}
