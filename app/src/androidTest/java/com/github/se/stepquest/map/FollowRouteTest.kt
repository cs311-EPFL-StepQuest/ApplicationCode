package com.github.se.stepquest.map

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import org.junit.Before
import org.junit.Test

class FollowRouteTest {
  private lateinit var followRoute: FollowRoute
  private lateinit var context: Context

  @Before
  fun setup() {
    followRoute = FollowRoute()
    context = ApplicationProvider.getApplicationContext<Context>()
  }

  @Test
  fun testDrawRouteDetail_route() {
    // Mock dependencies
    val googleMap = mockk<GoogleMap>(relaxed = true)
    //        val context = mockk<Context>(relaxed = true)
    val clickedMarker = mockk<Marker>(relaxed = true)
    val routeDetails =
        RouteDetails(
            routeID = "1",
            routeDetails = listOf(LocationDetails(34.0, -118.0), LocationDetails(35.0, -119.0)),
            userID = "user123",
            checkpoints = listOf(Checkpoint("Checkpoint 1", LocationDetails(34.5, -118.5))))

    // Set expectations
    every { clickedMarker.title } returns "Route"
    every { clickedMarker.tag } returns routeDetails
    mockkStatic(BitmapDescriptorFactory::class)
    every { BitmapDescriptorFactory.defaultMarker(any()) } returns mockk()

    // Mocking setOnMarkerClickListener using slot
    val listenerSlot = slot<GoogleMap.OnMarkerClickListener>()
    every { googleMap.setOnMarkerClickListener(capture(listenerSlot)) } answers {}

    // Invoke function
    followRoute.drawRouteDetail(googleMap, context)
    // Invoke the lambda with the mocked marker
    assert(listenerSlot.isCaptured)
    listenerSlot.captured.onMarkerClick(clickedMarker)
  }
}
