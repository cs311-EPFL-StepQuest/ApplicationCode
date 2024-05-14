package com.github.se.stepquest.services

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.se.stepquest.IUserRepository
import com.github.se.stepquest.data.repository.INotificationRepository
import com.github.se.stepquest.screens.addUsername
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import com.google.firebase.initialize
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock

@RunWith(AndroidJUnit4::class)
class FirebaseServiceTest {

  private lateinit var database: FirebaseDatabase

  private lateinit var requestsRef: DatabaseReference

  @Before
  fun setup() {
    val context = InstrumentationRegistry.getInstrumentation().targetContext
    val options = FirebaseOptions.Builder()
      .setApplicationId("1:316177260128:android:d6da82112d5626348d2d05")
      .setDatabaseUrl("http://127.0.0.1:4000/database")
      .setProjectId("stepquest-4de5e")
      .build()
    FirebaseApp.initializeApp(context)

    database = Firebase.database("http://127.0.0.1:4000/")
    addUsername("currentUsername", "currentUsernameId", database)
    addUsername("friendName", "friendNameId", database)
  }

  @Test
  fun sendFriendRequestTest() {
    sendFriendRequest("currentUsername", "friendName")
    val friendRequestsRef =
      database.reference.child("users").child("friendNameId").child("pendingFriendRequests")
    friendRequestsRef.addListenerForSingleValueEvent(
      object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
          val pendingRequests = snapshot.getValue<List<String>>()?.toMutableList()
          if (pendingRequests != null) {
            assert(true)
          } else {
            assert(false)
          }
        }

        override fun onCancelled(error: DatabaseError) {
          assert(false)
        }

      }
    )
  }

  @Test
  fun deletePendingFriendRequestTest() {

    requestsRef = mockk(relaxed = true)

    every { database.reference } returns
        mockk {
          every { child(any()) } returns
              mockk {
                every { child(any()) } returns mockk { every { child(any()) } returns requestsRef }
              }
        }

    deletePendingFriendRequest("friendName", database, "currentUsername")
  }

  @Test
  fun deleteFriendTest() {
    deleteFriend("currentUsername", "friendName", database, "uid")
  }
}
