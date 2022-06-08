package com.nnt.theoldgames.screen.gameboard

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.nnt.theoldgames.navigation.Destinations

@Preview
@Composable
fun HuntingSnake(navController: NavController = rememberNavController()){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val width = 200.dp
        Text(text = "Snake")
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = "Player: Player one")
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            modifier = Modifier
                .wrapContentHeight()
                .width(width),
            onClick = { navController.navigate(Destinations.SnakePlayScreen.route) }) {
            Text("Play now")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            modifier = Modifier
                .width(width)
                .wrapContentHeight(),
            onClick = { navController.navigate(Destinations.HuntingSnake.route) }) {
            Text("Player name")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            modifier = Modifier
                .width(width)
                .wrapContentHeight(),
            onClick = { navController.navigate(Destinations.HuntingSnake.route) }) {
            Text("Instruction")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            modifier = Modifier
                .width(width)
                .wrapContentHeight(),
            onClick = { navController.navigate(Destinations.HuntingSnake.route) }) {
            Text("High score")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            modifier = Modifier
                .width(width)
                .wrapContentHeight(),
            onClick = { navController.navigate(Destinations.HuntingSnake.route) }) {
            Text("Back")
        }
    }
}