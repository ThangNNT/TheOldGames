package com.nnt.theoldgames.navigation

sealed class Destinations (val route: String){
    object GameBoard: Destinations(route = "game_board")
    object HuntingSnake: Destinations(route = "hunting_snake")
    object SnakePlayScreen: Destinations(route = "snake_play_screen")
}
