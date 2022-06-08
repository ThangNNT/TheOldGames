package com.nnt.theoldgames.screen.huntingsnake.playscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap
import kotlin.collections.LinkedHashSet

@HiltViewModel
class SnakePlayViewModel @Inject constructor() : ViewModel(){
    //Matrix [50,50]
    private val snakePositions = LinkedList<Pair<Int, Int>>()
    private val _snakeState = MutableStateFlow<LinkedList<Pair<Int, Int>>>(LinkedList())
    val snakeState: StateFlow<LinkedList<Pair<Int, Int>>> = _snakeState

    private val _foodPosition = MutableStateFlow(Pair(0,0))
    val foodPosition: StateFlow<Pair<Int, Int>> = _foodPosition

    private val snakeMap = LinkedHashSet<Int>()
    private val _snakeMaps = MutableStateFlow(snakeMap)
    val snakeMaps: StateFlow<LinkedHashSet<Int>> = _snakeMaps

    val boxPerRow = 50

    init {
        val head = Pair(25, 25)
        val neck = Pair (24, 25)
        addSnake(pair = head)
        addSnake(pair = neck)
        _snakeState.value = snakePositions
        _snakeMaps.value = snakeMap
        viewModelScope.launch {
            while (true) {
                delay(1000)
                runSnake()
            }
        }
    }
    private fun addSnake(index: Int = -1, pair: Pair<Int, Int>){
        if(index==-1){
            snakePositions.add(pair)
        }
        else {
            snakePositions.add(index, pair)
        }
        snakeMap.add(pair.convertToIndex())
    }

    private fun removeSnakeTail() {
        snakePositions.last.convertToIndex().let {
            snakeMap.remove(it)
        }
        snakePositions.removeLast()
    }

    private fun runSnake(){
        val snakeHead = snakePositions[0]
        val snakeNeck = snakePositions[1]
        when(getDirection(snakeHead, snakeNeck)){
            RunDirection.Up -> {
                addSnake(0, Pair(snakeHead.first, snakeHead.second-1))
                removeSnakeTail()
            }
            RunDirection.Down -> {
                addSnake(0, Pair(snakeHead.first, snakeHead.second+1))
                removeSnakeTail()
            }
            RunDirection.Left -> {
                addSnake(0, Pair(snakeHead.first-1, snakeHead.second))
                removeSnakeTail()
            }
            RunDirection.Right -> {
                addSnake(0, Pair(snakeHead.first+1, snakeHead.second))
                removeSnakeTail()
            }
        }
        _snakeMaps.value = snakeMap
        _snakeState.value = snakePositions
    }

    private fun getDirection(head: Pair<Int,Int>, neck: Pair<Int, Int>): RunDirection{
        when {
            head.first == neck.first && head.second+1==neck.second ->{
                return RunDirection.Up
            }
            head.first == neck.first && head.second-1==neck.second -> {
                return RunDirection.Down
            }
            head.second == neck.second && head.first+1==neck.first ->{
                return RunDirection.Left
            }
            head.first == neck.first && head.first-1==neck.first -> {
                return RunDirection.Right
            }
            else ->{
                return RunDirection.Right
            }
        }
    }

    enum class RunDirection {
        Up,
        Down,
        Left,
        Right
    }

    fun Int.convertIndexToCoordinate(): Pair<Int, Int> {
        return Pair(this % boxPerRow - 1, this / boxPerRow)
    }

    fun Pair<Int, Int>.convertToIndex(): Int {
        return this.first+ this.second*boxPerRow+1
    }
}