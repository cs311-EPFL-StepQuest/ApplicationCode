package com.github.se.stepquest.map

fun compareCheckpoints(
    referenceLocation: LocationDetails,
    newImageLocation: LocationDetails,
    treshold: Float = 10f
): Float {
  val distance = calculateDistance(referenceLocation, newImageLocation)
  if (distance >= treshold) {
    return -1f
  }
  return distance
}

/*
    * Button to take a picture near a checkpoint (to be inserted later)
Box(modifier = Modifier.fillMaxWidth()) {
                    IconButton(
                        onClick = {
                          val checkpointLocation = //get checkpoint location//
                          val pictureLocation = //get current location//
                          val distance = compareCheckpoints(checkpointLocation, pictureLocation)
                          if (distance == -1f) {
                            //'display too far from location' message//
                          } else {
                            calculate reward
                          }
                        },
                        modifier = Modifier.size(70.dp)) {
                          Icon(
                              painterResource(R.drawable.camera_icon),
                              contentDescription = "camera_icon",
                              modifier = Modifier.size(50.dp))
                        }
                  }
 */
