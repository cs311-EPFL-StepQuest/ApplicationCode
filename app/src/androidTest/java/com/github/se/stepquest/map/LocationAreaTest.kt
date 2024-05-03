package com.github.se.stepquest.map

import androidx.compose.runtime.traceEventEnd
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.mockk.MockKSettings.relaxed
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class LocationAreaTest {

  private lateinit var locationArea: LocationArea
  private lateinit var firebaseAuth: FirebaseAuth
  private lateinit var mockRouteID: DataSnapshot
  private lateinit var mockRouteDataSnapshot: DataSnapshot
  private lateinit var mockRoutes: DatabaseReference
  private lateinit var mockDatabaseReference: DatabaseReference
  private lateinit var mockDatabase: FirebaseDatabase
  private lateinit var mockTask: Task<DataSnapshot>

  @Before
  fun setUp() {
    locationArea = LocationArea()
    locationArea.createArea(LocationDetails(0.0, 0.0), 1000.0)

    firebaseAuth = mockk()
    mockRouteID = mockk(relaxed = true)
    mockRouteDataSnapshot = mockk(relaxed = true)
    mockRoutes = mockk(relaxed = true)
    mockDatabaseReference = mockk(relaxed = true)
    mockDatabase = mockk(relaxed = true)
    mockTask = mockk(relaxed = true)
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
    val center = LocationDetails(1.0, 1.0)
    val radius = 1000.0
    val locationArea = LocationArea()
    locationArea.createArea(center, radius)

    assertEquals(center.latitude, locationArea.center.latitude, 0.0)
    assertEquals(center.longitude, locationArea.center.longitude, 0.0)
    assertEquals(radius, locationArea.radius, 0.0)
  }
  /*
    @Test
    fun routesAroundLocation_returnsRoutesWhenLocationIsInside() {
      val locationDetails = LocationDetails(0.0, 0.0)
      val location : LocationDetails = mockk()
      val googleMap : GoogleMap = mockk(relaxed = true)

      val mockRouteList = mutableListOf<LocationDetails>()
      val routeList = mutableListOf<LocationDetails>()
      val locationArea = LocationArea()
      locationArea.createArea(locationDetails)

      every { mockDatabase.reference } returns
          mockk {
            every { child(any()) } returns mockRoutes
          }
      every { mockRoutes.addListenerForSingleValueEvent(any()) } answers
              {
                val listener = arg<ValueEventListener>(0)
                listener.onDataChange(mockk {
                  every { children } returns listOf(mockRouteID)
                  every { mockRouteID.child(any()) } returns
                          mockk {
                            every { child(any()) } returns mockRouteDataSnapshot
                          }
                  every { mockRouteDataSnapshot.child("latitude") } returns
                          mockk {
                            every { getValue(Double::class.java) } returns 0.0
                          }
                  every { mockRouteDataSnapshot.child("longitude") } returns
                          mockk {
                            every { getValue(Double::class.java) } returns 0.0
                          }
              })
              }

      locationArea.routesAroundLocation(googleMap, locationDetails) { routeList.addAll(it) }

      assertTrue(routeList[0].latitude == 0.0)
    }
    */
}
