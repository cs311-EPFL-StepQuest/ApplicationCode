package com.github.se.stepquest.services

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FriendsServicesTest {

  @get:Rule val composeTestRule = createComposeRule()

  private lateinit var database: FirebaseDatabase
  private lateinit var requestsListRef: DatabaseReference

  @Before
  fun setup() {

    database = mockk(relaxed = true)
    requestsListRef = mockk(relaxed = true)

    every { database.reference } returns
        mockk {
          every { child(any()) } returns
              mockk {
                every { child(any()) } returns
                    mockk { every { child(any()) } returns requestsListRef }
              }
        }
  }

  @Test
  fun testDeletePendingRequest() {

    val friendsList = mutableListOf("friendName")

    every { requestsListRef.addListenerForSingleValueEvent(any()) } answers
        {
          val listener = arg<ValueEventListener>(0)
          listener.onDataChange(
              mockk { every { getValue<List<String>>()?.toMutableList() } returns friendsList })
        }

    sendFriendRequest("friendName", "testUserId")

    deletePendingFriendRequest("friendName", database, "testUserId")
  }
}
