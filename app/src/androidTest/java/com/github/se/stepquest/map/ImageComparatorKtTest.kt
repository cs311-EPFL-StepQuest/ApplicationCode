package com.github.se.stepquest.map

import org.junit.Assert.*
import org.junit.Test

class ImageComparatorKtTest {
  @Test
  fun testEqualLocations() {
    val referenceLocation = LocationDetails(0.0, 0.0)
    val newImageLocation = LocationDetails(0.0, 0.0)
    val distance = compareImages(referenceLocation, newImageLocation)
    assertEquals(0f, distance)
  }

  @Test
  fun testFarLocations() {
    val referenceLocation = LocationDetails(0.0, 0.0)
    val newImageLocation = LocationDetails(0.0, 10.0)
    val distance = compareImages(referenceLocation, newImageLocation)
    assertEquals(0f, distance)
  }

  @Test
  fun testCloseLocations() {
    val referenceLocation = LocationDetails(0.0, 0.0)
    val newImageLocation = LocationDetails(0.0, 0.0001)
    val distance = compareImages(referenceLocation, newImageLocation)
    assertNotEquals(0f, distance)
  }
}
