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
fun GameBoardScreen(navController: NavController = rememberNavController()) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val width = 200.dp
        Text(text = "Game board")
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            modifier = Modifier
                .wrapContentHeight()
                .width(width),
            onClick = { navController.navigate(Destinations.HuntingSnake.route) }) {
            Text("Hunting Snake")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            modifier = Modifier
                .width(width)
                .wrapContentHeight(),
            onClick = { navController.navigate(Destinations.HuntingSnake.route) }) {
            Text("Racing boy")
        }
    }
}