package com.github.se.stepquest.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.github.se.stepquest.R
import com.github.se.stepquest.data.model.ChallengeData
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import com.github.se.stepquest.services.getChallenges
import com.google.firebase.database.FirebaseDatabase

@Composable
fun ChallengesScreen(userId: String, testChallenges: List<ChallengeData> = listOf()) {
    var challenges = testChallenges
    getChallenges(userId) { receivedChallenges -> challenges = receivedChallenges }
    Column {
        challenges.forEach { challenge ->
            ChallengeItem(challenge = challenge)
        }
    }
}

@Composable
fun ChallengeItem(challenge: ChallengeData) {
    val blueThemeColor = colorResource(id = R.color.blueTheme)
    Surface(
        color = blueThemeColor,
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.padding(8.dp).fillMaxWidth(),
            verticalAlignment = CenterVertically
        ) {
            // You can display additional challenge details here if needed
            Text(
                text = challenge.senderUsername,
                modifier = Modifier.weight(1f),
                color = Color.White,
            )
            Text(
                text = challenge.dateTime,
                color = Color.White,
            )
        }
    }
}