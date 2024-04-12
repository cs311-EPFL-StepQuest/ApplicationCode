package com.github.se.stepquest

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Preview(
    showSystemUi = true,
    showBackground = true
)
@Composable
fun ProgressionPage() {
    val levelList = arrayListOf<String>("Current lvl", "Next lvl")
    val progress = 0.5f
    var showDialog by remember { mutableStateOf(false) }
    var dailyStepGoal by remember { mutableIntStateOf(5000) }
    var weeklyStepGoal by remember { mutableIntStateOf(35000) }
    Column (
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Back",
            modifier = Modifier.padding(20.dp),
            fontSize = 20.sp
        )
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp, 0.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.character),
                contentDescription = "Character",
                modifier = Modifier
                    .size(200.dp, 250.dp)
                    .offset(0.dp, (-60).dp)
            )
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp, 0.dp)
                    .height(10.dp),
                color = colorResource(id = R.color.blueTheme),
                trackColor = colorResource(id = R.color.lightGrey)
            )
            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .offset(0.dp, 10.dp)
                    .fillMaxWidth()
            ) {
                levelList.forEach {s -> Text(
                    text = s,
                    fontSize = 16.sp
                )}

            }
            Box(modifier = Modifier.height(40.dp))
            BuildStats(icon = R.drawable.step_icon, title = "Daily steps", value = "3400/5000")
            Box(modifier = Modifier.height(20.dp))
            BuildStats(icon = R.drawable.step_icon, title = "Weekly steps", value = "7400/20000")
            Box(modifier = Modifier.height(20.dp))
            BuildStats(icon = R.drawable.boss_icon, title = "Bosses defeated", value = "24")
            Box(modifier = Modifier.height(60.dp))
            Button(
                onClick = {showDialog = true},
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.blueTheme)),
                modifier =
                Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .padding(vertical = 8.dp)
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Set a new step goal", color = Color.White, fontSize = 24.sp)
            }
            if (showDialog) {
                SetStepGoalsDialog(
                    onDismiss = { showDialog = false },
                    onConfirm = { newDailyStepGoal, newWeeklyStepGoal ->
                        dailyStepGoal = newDailyStepGoal
                        weeklyStepGoal = newWeeklyStepGoal
                        showDialog = false
                    })
            }
        }
    }

    
}

@Composable
fun BuildStats(icon: Int, title: String, value: String) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .offset(20.dp, 0.dp)
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = "Stat icon",
            modifier = Modifier.size(20.dp, 20.dp)
        )
        Text(
            text = "$title: $value",
            fontSize = 16.sp,
            modifier = Modifier.offset(5.dp, 0.dp)
        )
    }
}

@Composable
fun HomeScreen(){
    BuildDefaultScreen(name = "Home")
}
@Composable
fun MapScreen() {
    BuildDefaultScreen(name = "Map")
}

@Composable
fun BuildDefaultScreen(name:String){
    Column (modifier = Modifier
        .fillMaxSize()
        .wrapContentSize(Alignment.Center)){
        Text(name)
    }
}

@Composable
fun BuildNavigationBar(
    navigationController: NavHostController,
) {
    val screens = listOf(Routes.HomeScreen,
        Routes.MapScreen,
        Routes.ProgressionScreen,)
    val navigationBackStack by navigationController.currentBackStackEntryAsState()
    val currentPage = navigationBackStack?.destination?.route

    NavigationBar (
        containerColor = Color.White,
        contentColor = Color.Black
    ){
        screens.forEach { screen ->
            NavigationBarItem(
                label = {Text(text = screen.title, fontSize = 16.sp)},
                selected = currentPage == screen.routName,
                onClick = {
                    navigationController.navigate(screen.routName){
                        popUpTo(navigationController.graph.findStartDestination().id){
                            saveState = true
                        }
                    }


                },
                icon = { Icons.Filled.Home },
                colors = NavigationBarItemDefaults.colors(
                    unselectedTextColor = Color.Black, selectedTextColor = colorResource(id = R.color.blueTheme)
                ),
            )}
    }
}

@Composable
fun BuildNavigationPage(navigationController: NavHostController){
    NavHost(navigationController,
        startDestination = Routes.HomeScreen.routName,){
        composable(Routes.HomeScreen.routName){
            HomeScreen()
        }
        composable(Routes.MapScreen.routName){
            MapScreen()
        }
        composable(Routes.ProgressionScreen.routName){
            ProgressionPage()
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BuildPage() {
        Greeting()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting(){
    val navigationController:NavHostController = rememberNavController()
    val bottomBarHeight = 70.dp
    val bottomBarOffset = remember {
        mutableStateOf(0f)
    }
    Scaffold (bottomBar = {
        BuildNavigationBar(navigationController = navigationController,
        )
    }){ paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues)
        ){
            BuildNavigationPage(navigationController = navigationController)
        }
    }

}

@Composable
fun SetStepGoalsDialog(
    onDismiss: () -> Unit,
    onConfirm: (dailyStepGoal: Int, weeklyStepGoal: Int) -> Unit
) {
    val blueThemeColor = colorResource(id = R.color.blueTheme)
    var newDailyStepGoal by remember { mutableStateOf("") }
    val newWeeklyStepGoal by remember { mutableStateOf("") }

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            color = Color.White,
            border = BorderStroke(1.dp, Color.Black),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.padding(16.dp)) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Set New Step Goals", fontSize = 20.sp)
                    IconButton(onClick = { onDismiss() }, modifier = Modifier.padding(8.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Daily steps")
                Spacer(modifier = Modifier.height(2.dp))
                TextField(
                    modifier = Modifier.testTag("daily_steps_setter"),
                    value = newDailyStepGoal,
                    onValueChange = { newDailyStepGoal = it.filter { it.isDigit() }.take(5) },
                    label = { Text("Enter your daily step goal") },
                    placeholder = { Text("5000") },
                    keyboardOptions =
                    KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.NumberPassword, imeAction = ImeAction.Done),
                    keyboardActions =
                    KeyboardActions(
                        onDone = {
                            val dailyStep =
                                newDailyStepGoal
                                    .filter { it.isDigit() }
                                    .take(5)
                                    .let {
                                        if (it.isBlank()) {
                                            5000 // Default value if blank
                                        } else {
                                            val parsedInput = it.toIntOrNull() ?: 0
                                            val roundedValue = (parsedInput + 249) / 250 * 250
                                            if (roundedValue < 1000) {
                                                1000
                                            } else {
                                                roundedValue
                                            }
                                        }
                                    }
                            val weeklyStep =
                                newWeeklyStepGoal.takeIf { it.isNotBlank() }?.toInt()
                                    ?: (dailyStep * 7)
                            onConfirm(dailyStep, weeklyStep)
                            onDismiss()
                        })
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        val dailyStep =
                            newDailyStepGoal
                                .filter { it.isDigit() }
                                .take(5)
                                .let {
                                    if (it.isBlank()) {
                                        5000 // Default value if blank
                                    } else {
                                        val parsedInput = it.toIntOrNull() ?: 0
                                        val roundedValue = (parsedInput + 249) / 250 * 250
                                        if (roundedValue < 1000) {
                                            1000
                                        } else {
                                            roundedValue
                                        }
                                    }
                                }
                        val weeklyStep =
                            newWeeklyStepGoal.takeIf { it.isNotBlank() }?.toInt() ?: (dailyStep * 7)
                        onConfirm(dailyStep, weeklyStep)
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(blueThemeColor),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(horizontal = 4.dp)) {
                    Text(text = "Confirm")
                }
            }
        }
    }
}
