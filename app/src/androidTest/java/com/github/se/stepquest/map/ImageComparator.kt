package com.github.se.stepquest.map

import android.graphics.drawable.Icon
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap

// At the moment the points are awarded based on the distance between the two images
// The closer the images are the more points are awarded
fun compareImages(referenceLocation: LocationDetails, reference: ImageBitmap, newImageLocation: LocationDetails, newImage: ImageBitmap): Float {
    val distance = calculateDistance(referenceLocation, newImageLocation)
    if(distance >= 10f) {
        return 0f
    }
    return 100-(distance*10)
}
