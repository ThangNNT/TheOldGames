package com.nnt.theoldgames

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.nnt.theoldgames.navigation.Destinations
import com.nnt.theoldgames.screen.gameboard.GameBoardScreen
import com.nnt.theoldgames.screen.gameboard.HuntingSnake
import com.nnt.theoldgames.screen.huntingsnake.SnakePlayScreen
import com.nnt.theoldgames.ui.theme.TheOldGamesTheme

@ExperimentalUnitApi
@ExperimentalFoundationApi
@Composable
fun App() {
    TheOldGamesTheme {
        val systemUiController = rememberSystemUiController()
        val nav = rememberNavController()
        NavigationGraph(nav)
    }
}

@ExperimentalUnitApi
@ExperimentalFoundationApi
@Composable
fun NavigationGraph(navController: NavHostController){
    NavHost(navController = navController, startDestination = Destinations.GameBoard.route){
        composable(route = Destinations.GameBoard.route){
            GameBoardScreen(navController)
        }

        composable(route = Destinations.HuntingSnake.route){
            HuntingSnake(navController)
        }
        composable(route = Destinations.SnakePlayScreen.route){
            SnakePlayScreen(navController)
        }
    }
}