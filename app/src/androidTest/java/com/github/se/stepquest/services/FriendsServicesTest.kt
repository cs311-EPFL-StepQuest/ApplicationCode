package com.github.se.stepquest.services

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.se.stepquest.screens.addUsername
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import com.google.firebase.firestore.util.Assert.fail
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FirebaseServiceTest {

  object FirebaseDatabaseInstance {
    val instance: FirebaseDatabase by lazy {
      val database = FirebaseDatabase.getInstance()
      database.useEmulator("10.0.2.2", 9000)
      database
    }
  }

  private lateinit var database: FirebaseDatabase

  @Before
  fun setup() {
    val context = InstrumentationRegistry.getInstrumentation().targetContext
    FirebaseApp.initializeApp(context)
    database = FirebaseDatabaseInstance.instance
    addUsername("currentUsername", "currentUsernameId", database)
    addUsername("friendName", "friendNameId", database)
  }

  @After
  fun cleanup() {
    database.reference.setValue(null)
  }

  @Test
  fun pendingFriendRequestTest() {
    val latch = CountDownLatch(1)
    sendFriendRequest("currentUsername", "friendName")
    deletePendingFriendRequest("friendName", database, "currentUsernameId")
    val friendRequestsRef =
        database.reference.child("users").child("friendNameId").child("pendingFriendRequests")
    friendRequestsRef.addListenerForSingleValueEvent(
        object : ValueEventListener {
          override fun onDataChange(snapshot: DataSnapshot) {
            try {
              val pendingRequests = snapshot.getValue<List<String>>()?.toMutableList()
              assert(pendingRequests == null)
            } finally {
              latch.countDown()
            }
          }

          override fun onCancelled(error: DatabaseError) {
            try {
              assert(false)
            } finally {
              latch.countDown()
            }
          }
        })
    if (!latch.await(10, TimeUnit.SECONDS)) { // Wait with timeout
      fail("Timeout waiting for database operation")
    }
  }

  /*@Test
  fun deleteFriendTest() {
    deleteFriend("currentUsername", "friendName", database, "uid")
  }*/
}
