package com.github.se.stepquest.services

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.getValue
import io.mockk.every
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FriendsServicesTest {

  @get:Rule val mockkRule = MockKRule(this)

  private lateinit var firebaseAuth: FirebaseAuth
  private lateinit var database: FirebaseDatabase
  private lateinit var requestsListRef: DatabaseReference
  private lateinit var user: FirebaseUser

  @Test
  fun testDeletePendingRequest() {

    firebaseAuth = mockk()
    database = mockk(relaxed = true)
    requestsListRef = mockk(relaxed = true)
    user = mockk()

    every { firebaseAuth.currentUser } returns user

    every { database.reference } returns
        mockk {
          every { child(any()) } returns
              mockk {
                every { child(any()) } returns
                    mockk { every { child(any()) } returns requestsListRef }
              }
        }

    deletePendingFriendRequest("friendName", database, "testUserId")
  }
}
