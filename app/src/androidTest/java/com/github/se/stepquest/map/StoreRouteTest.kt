package com.github.se.stepquest.map

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class StoreRouteTest {
  private lateinit var storeRoute: StoreRoute

  @Before
  fun setup() {
    // Initialize StoreRoute
    storeRoute = StoreRoute()
  }

  @Test
  fun testRouteCreation() {
    val routeDetails =
        listOf(LocationDetails(1.0, 2.0), LocationDetails(3.0, 4.0), LocationDetails(5.0, 6.0))
    val checkpoints = listOf("Checkpoint 1", "Checkpoint 2")
    val route = StoreRoute.Route(routeDetails, checkpoints)
    assertEquals(routeDetails, route.route)
    assertEquals(checkpoints, route.checkpoints)
  }

  @Test
  fun testGlobalRouteCreation() {
    // Given
    val routeDetails =
        listOf(LocationDetails(1.0, 2.0), LocationDetails(3.0, 4.0), LocationDetails(5.0, 6.0))
    val checkpoints = listOf("Checkpoint 1", "Checkpoint 2")
    val userId = "testUserId"
    val globalRoute = StoreRoute.GlobalRoute(routeDetails, checkpoints, userId)
    assertEquals(routeDetails, globalRoute.route)
    assertEquals(checkpoints, globalRoute.checkpoints)
    assertEquals(userId, globalRoute.userid)
  }

  //    @Test
  //    fun TestaddRoute() {
  //        // Given
  //        val userId = "testUserId"
  //        val firebaseAuth = mockk<FirebaseAuth>(relaxed = true)
  //        val firebaseDatabase = mockk<FirebaseDatabase>(relaxed = true)
  //        every { firebaseAuth.currentUser } returns mockk { every { uid } returns "testUserId" }
  //
  //        val route = listOf(LocationDetails(1.0, 2.0), LocationDetails(3.0, 4.0))
  //        val checkpoints = listOf("Checkpoint 1", "Checkpoint 2")
  //
  //        // When
  //        storeRoute.addRoute(route, checkpoints)
  //
  //        // Then
  //        verify {
  //            firebaseDatabase.reference.child("routes").push().key.toString()
  //            firebaseDatabase.reference.child("routes").child(any())
  //
  // firebaseDatabase.reference.child("users").child(userId).child("new_route").child(any())
  //        }
  //    }
}
