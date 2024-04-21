package com.github.se.stepquest.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.se.stepquest.R
import com.github.se.stepquest.ui.navigation.NavigationActions

@Composable
fun NewPlayerScreen(navigationActions: NavigationActions) {
    var username by remember { mutableStateOf("") }

    val blueThemeColor = colorResource(id = R.color.blueTheme)

    val textFieldFocusRequester = remember { androidx.compose.ui.focus.FocusRequester() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { /*Sign in with google*/ }),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(textFieldFocusRequester)
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        textFieldFocusRequester.requestFocus()
                    }
                }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(blueThemeColor),
            modifier = Modifier.fillMaxWidth().height(72.dp).padding(vertical = 8.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "Sign in",
                color = Color.White,
                fontSize = 24.sp
            )
        }
    }
}