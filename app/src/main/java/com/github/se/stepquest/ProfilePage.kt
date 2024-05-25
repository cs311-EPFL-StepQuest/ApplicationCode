package com.github.se.stepquest

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.github.se.stepquest.ui.navigation.NavigationActions
import com.github.se.stepquest.ui.navigation.TopLevelDestination
import com.github.se.stepquest.viewModels.ProfilePageViewModel

@Composable
fun ProfilePageLayout(
    navigationActions: NavigationActions,
    userId: String,
    profilePicture: Uri?,
    context: Context,
    viewModel: ProfilePageViewModel = viewModel()
) {
    val blueThemeColor = colorResource(id = R.color.blueTheme)
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.initialize(userId, context)
    }

    Column(
        modifier = Modifier.padding(32.dp).fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(id = R.drawable.settings),
                contentDescription = "Settings",
                modifier = Modifier.size(30.dp),
            )
        }
        Text(text = "Profile", fontWeight = FontWeight.Bold, fontSize = 40.sp)
        Image(
            painter = rememberAsyncImagePainter(profilePicture),
            contentDescription = "Profile Picture",
            modifier = Modifier.size(200.dp).clip(RoundedCornerShape(100.dp))
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = state.username,
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp,
        )
        Text(
            text = "Total Steps: ${state.totalStepsMade}",
            fontSize = 24.sp,
            modifier = Modifier.padding(top = 16.dp)
        )
        Button(
            onClick = {
                navigationActions.navigateTo(TopLevelDestination(Routes.FriendsListScreen.routName))
            },
            colors = ButtonDefaults.buttonColors(blueThemeColor),
            modifier = Modifier.fillMaxWidth()
                .height(72.dp)
                .padding(vertical = 8.dp)
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Friends List", fontSize = 24.sp, color = Color.White)
        }
    }
}
