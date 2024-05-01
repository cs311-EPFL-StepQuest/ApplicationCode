package com.github.se.stepquest.map

import com.google.android.gms.maps.model.LatLng
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class LocationAreaTest {

  private lateinit var locationArea: LocationArea

  @Before
  fun setUp() {
    locationArea = LocationArea(LatLng(0.0, 0.0), 1000.0)
  }

  @Test
  fun createArea_setsCenterAndRadius() {
    val locationDetails = LocationDetails(1.0, 1.0)
    locationArea.createArea(locationDetails)
    assertEquals(LatLng(1.0, 1.0), locationArea.center)
    assertEquals(1000.0, locationArea.radius, 0.0)
  }

  @Test
  fun checkInsideArea_returnsTrueWhenLocationIsInside() {
    val locationDetails = LocationDetails(0.0, 0.0)
    assertTrue(locationArea.checkInsideArea(locationDetails))
  }

  @Test
  fun checkInsideArea_returnsFalseWhenLocationIsOutside() {
    val locationDetails = LocationDetails(10.0, 10.0)
    assertFalse(locationArea.checkInsideArea(locationDetails))
  }

  @Test
  fun locationArea_initialization_setsCorrectValues() {
    val center = LatLng(1.0, 1.0)
    val radius = 1000.0
    val locationArea = LocationArea(center, radius)

    assertEquals(center, locationArea.center)
    assertEquals(radius, locationArea.radius, 0.0)
  }
  /*
    @Test
    fun routesAroundLocation_returnsRoutesWhenLocationIsInside() {
      val locationDetails = LocationDetails(0.0, 0.0)
      val mockDataSnapshot = mockk<DataSnapshot>()
      val mockRootDatabaseReference = mockk<DatabaseReference>()
      val mockDatabaseReference = mockk<DatabaseReference>()
      val mockDatabase = mockk<FirebaseDatabase>()

      every { mockDatabase.reference } returns mockRootDatabaseReference
      every { mockRootDatabaseReference.child("routes") } returns mockDatabaseReference
      val mockTask = mockk<Task<DataSnapshot>>()
      every { mockDatabaseReference.get() } returns mockTask
      every { mockTask.isSuccessful } returns true
      every { mockTask.result } returns mockDataSnapshot
      every { mockDataSnapshot.children } returns listOf(mockDataSnapshot)
      every { mockDataSnapshot.getValue(StoreRoute.Route::class.java) } returns StoreRoute.Route(
        listOf(locationDetails), emptyList()
      )

      val routes = locationArea.routesAroundLocation(mockk<GoogleMap>(), locationDetails)
      assertTrue(routes.isNotEmpty())
    }

  */
}
