package com.github.se.stepquest.map
// Map.kt
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.se.stepquest.R
import com.google.maps.android.compose.GoogleMap
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.materialIcon
import androidx.compose.ui.res.painterResource

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Map() {
    var showDialog by remember { mutableStateOf(false) }
    var checkpointTitle by remember { mutableStateOf("") }

    Scaffold(
        content = {
            Box(modifier = Modifier.fillMaxSize()) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize().testTag("GoogleMap"),
                )
                FloatingActionButton(
                    onClick = { showDialog = true },
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.BottomStart)
                ) {
                    //Icon(
                    //    imageVector = Icons.Default.Add,
                    //    contentDescription = "Add checkpoint"
                    //)
                    androidx.compose.material3.Text(
                        text = "Add Checkpoint",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(0.dp)
                    )
                }
            }
        },
        floatingActionButton = {
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Enter Checkpoint Title") },
                    text = {
                        TextField(
                            value = checkpointTitle,
                            onValueChange = { checkpointTitle = it },
                            label = { Text("Title") }
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                // Here you can handle adding the checkpoint to the map
                                val title = checkpointTitle
                                showDialog = false
                            }
                        ) {
                            Text("Confirm")
                        }
                    },
                    dismissButton = {
                        IconButton(
                            onClick = { showDialog = false },
                            modifier = Modifier.padding(8.dp)//.align()
                                //.align(Alignment.TopEnd)
                        ) {
                            Image(
                                painter = painterResource(R.drawable.transparent_x),
                                contentDescription = "Close"
                            )
                        }
                    }
                )
            }
        }
    )
}
