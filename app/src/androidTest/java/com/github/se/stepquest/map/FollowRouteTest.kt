package com.github.se.stepquest.map

import org.junit.Test

class FollowRouteTest {

  @Test
  fun TestcreateTrackpoint() {
    val route =
        listOf(LocationDetails(0.0, 0.0), LocationDetails(0.0, 1.0), LocationDetails(0.0, 2.0))
    val followRoute = FollowRoute()
    val trackpoints = followRoute.createTrackpoint(route, 1.0)
    assert(trackpoints.size == 2)
    assert(trackpoints[0] == LocationDetails(0.0, 1.0))
    assert(trackpoints[1] == LocationDetails(0.0, 2.0))
  }
}
