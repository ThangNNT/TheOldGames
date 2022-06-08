package com.nnt.theoldgames.screen.huntingsnake.playscreen

data class SnakePlayScreenState(val snakePosition: List<Int>, val foodPosition: Int)
data class Coordinator(val x: Int, val y: Int, val index: Int)