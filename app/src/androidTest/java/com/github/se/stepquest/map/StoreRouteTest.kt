package com.github.se.stepquest.map

import com.google.firebase.database.FirebaseDatabase
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class StoreRouteTest {
  private lateinit var storeRoute: StoreRoute
  private lateinit var database: FirebaseDatabase

  @Before
  fun setup() {
    // Initialize StoreRoute
    storeRoute = StoreRoute()
  }

  @Test
  fun testRouteCreation() {
    val routeDetails =
        listOf(LocationDetails(1.0, 2.0), LocationDetails(3.0, 4.0), LocationDetails(5.0, 6.0))
    val checkpoints =
        listOf(
            Checkpoint("Checkpoint 1", routeDetails[0]),
            Checkpoint("Checkpoint 2", routeDetails[1]))
    val route = StoreRoute.Route(routeDetails, checkpoints)
    assertEquals(routeDetails, route.route)
    assertEquals(checkpoints, route.checkpoints)
  }

  @Test
  fun testGlobalRouteCreation() {
    val routeDetails =
        listOf(LocationDetails(1.0, 2.0), LocationDetails(3.0, 4.0), LocationDetails(5.0, 6.0))
    val checkpoints =
        listOf(
            Checkpoint("Checkpoint 1", routeDetails[0]),
            Checkpoint("Checkpoint 2", routeDetails[1]))
    val userId = "testUserId"
    val globalRoute = StoreRoute.GlobalRoute(routeDetails, checkpoints, userId)
    assertEquals(routeDetails, globalRoute.route)
    assertEquals(checkpoints, globalRoute.checkpoints)
    assertEquals(userId, globalRoute.userid)
  }

  @Test
  fun testAddRoute() {
    val userId = "testUserId"
    val route = listOf(LocationDetails(1.0, 2.0), LocationDetails(3.0, 4.0))
    val checkpoints =
        listOf(Checkpoint("Checkpoint 1", route[0]), Checkpoint("Checkpoint 2", route[1]))
    val database = mockk<FirebaseDatabase>(relaxed = true)
    every { database.reference } returns mockk(relaxed = true)
    storeRoute.addRoute(userId, route, checkpoints)
  }
}
