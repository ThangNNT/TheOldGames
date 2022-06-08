package com.nnt.theoldgames.screen.huntingsnake.playscreen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class SnakePlayViewModel @Inject constructor() : ViewModel(){
    val snakeCoordinate = mutableStateListOf<Pair<Int, Int>>()

    private val _foodPositionState = MutableStateFlow(Pair(0,0))
    val foodPositionState: StateFlow<Pair<Int, Int>> = _foodPositionState

    private val _shouldHideFood = mutableStateOf(false)
    val shouldHideFood: State<Boolean> = _shouldHideFood

    val boxPerRow = 50

    private val _score = mutableStateOf(0)
    val score: State<Int> = _score

    private val snakeSet = mutableSetOf<Int>()

    private var snakeRunJob: Job? = null

    val isDead = mutableStateOf(false)
    private var initSnakeLength = 2

    init {
        reset()
        viewModelScope.launch {
            while (true){
                _shouldHideFood.value = false
                delay(900)
                _shouldHideFood.value = true
                delay(100)
            }
        }
    }

    fun reset(){
        snakeRunJob?.cancel()
        snakeCoordinate.clear()
        snakeSet.clear()
        isDead.value = false
        val head = Pair(25, 25)
        val neck = Pair (24, 25)
        addSnake(pair = head)
        addSnake(pair = neck)
        for(i in 23 downTo 3){
            addSnake(pair = Pair(i, 25))
        }
        initSnakeLength = snakeCoordinate.size
        snakeRunJob = viewModelScope.launch(Dispatchers.Default) {
            delay(1000)
            while (true) {
                delay(100)
                runSnake()
            }
        }
        randomFood()
    }

    private fun getRandomPosition(): Pair<Int, Int>{
        val x = Random.nextInt(0, boxPerRow - 1)
        val y = Random.nextInt(0, boxPerRow - 1)
        return Pair(x, y)
    }

    private fun randomFood(){
        val foodPosition = getRandomPosition()
        if(isValidFoodPosition(foodPos = foodPosition)){
            _foodPositionState.value = foodPosition
        }
        else randomFood()
    }

    private fun isValidFoodPosition(foodPos: Pair<Int, Int>): Boolean {
        return !snakeSet.contains(foodPos.convertToIndex())
    }

    private fun addSnake(index: Int = -1, pair: Pair<Int, Int>){
        if(index==-1){
            snakeCoordinate.add(pair)
        }
        else {
            snakeCoordinate.add(index, pair)
        }
        snakeSet.add(pair.convertToIndex())

    }

    private fun removeSnakeTail() {
        snakeSet.remove(snakeCoordinate.last().convertToIndex())
        snakeCoordinate.removeLast()
    }

    private fun runSnake(){
        move()
    }

    /**
     * if direction null, the snake will move forward
     */
    fun move(direction: RunDirection? = null){
        val snakeHead = snakeCoordinate[0]
        val snakeNeck = snakeCoordinate[1]
        val currentDirection = getDirection(snakeHead, snakeNeck)
        if ((currentDirection == RunDirection.Down && direction == RunDirection.Up)
            || (currentDirection == RunDirection.Up && direction == RunDirection.Down)
            || (currentDirection == RunDirection.Left && direction == RunDirection.Right)
            || (currentDirection == RunDirection.Right && direction == RunDirection.Left)
        ) return
        when(direction ?: currentDirection){
            RunDirection.Up -> {
                val newHead = if(isReachTop(snakeHead, snakeNeck)){
                    Pair(snakeHead.first, boxPerRow-1)
                }
                else {
                    Pair(snakeHead.first, snakeHead.second-1)
                }
                if(isDead(newHead)){
                    die()
                    return
                }
                addSnake(0, newHead)
                removeSnakeTail()
            }
            RunDirection.Down -> {
                val newHead = if(isReachBottom(snakeHead, snakeNeck)){
                    Pair(snakeHead.first, 0)
                }
                else {
                    Pair(snakeHead.first, snakeHead.second+1)
                }
                if(isDead(newHead)){
                    die()
                    return
                }
                addSnake(0, newHead)
                removeSnakeTail()
            }
            RunDirection.Left -> {
                val newHead = if (isReachLeft(snakeHead, snakeNeck)) {
                    Pair(boxPerRow - 1, snakeHead.second)
                } else {
                    Pair(snakeHead.first - 1, snakeHead.second)
                }
                if(isDead(newHead)){
                    die()
                    return
                }
                addSnake(0, newHead)
                removeSnakeTail()
            }
            RunDirection.Right -> {
                val newHead = if(isReachRight(snakeHead, snakeNeck)){
                    Pair(0, snakeHead.second)
                }
                else {
                    Pair(snakeHead.first+1, snakeHead.second)
                }
                if(isDead(newHead)){
                    die()
                    return
                }
                addSnake(0, newHead)
                removeSnakeTail()
            }
        }
        val newHead = snakeCoordinate[0]
        if(newHead.isDuplicate(foodPositionState.value)){
            ateFood()
        }
    }

    private fun die(){
        isDead.value = true
        snakeRunJob?.cancel()
    }

    private fun ateFood(){
        randomFood()
        val snakeTail1 = snakeCoordinate.last()
        val snakeTail2 = snakeCoordinate[snakeCoordinate.size-2]
        when(getDirection(snakeTail1, snakeTail2)){
            RunDirection.Left -> {
                if(isReachRight(snakeTail1, snakeTail2)){
                    addSnake(pair = Pair(0, snakeTail1.second))
                }
                else {
                    addSnake(pair = Pair(snakeTail1.first+1, snakeTail1.second))
                }
            }
            RunDirection.Up -> {
                if(isReachBottom(snakeTail1, snakeTail2)){
                    addSnake(pair = Pair(snakeTail1.first, 0))
                }
                else addSnake(pair = Pair(snakeTail1.first, snakeTail1.second+1))
            }
            RunDirection.Right -> {
                if(isReachLeft(snakeTail1, snakeTail2)){
                    addSnake(pair = Pair(boxPerRow-1, snakeTail1.second))
                }
                addSnake(pair = Pair(snakeTail1.first-1, snakeTail1.second))
            }
            RunDirection.Down -> {
                if (isReachTop(snakeTail1, snakeTail2)) {
                    addSnake(pair = Pair(snakeTail1.first, boxPerRow-1))

                } else addSnake(pair = Pair(snakeTail1.first, snakeTail1.second-1))
            }
        }
        _score.value = (snakeCoordinate.size - initSnakeLength)*10
    }

    fun Pair<Int, Int>.isDuplicate(pair: Pair<Int, Int>): Boolean{
        return this.first == pair.first && this.second == pair.second
    }

    private fun isReachTop(head: Pair<Int, Int>, neck: Pair<Int, Int>): Boolean {
        return head.second == 0
    }
    private fun isReachBottom(head: Pair<Int, Int>, neck: Pair<Int, Int>): Boolean {
        return head.second == boxPerRow-1
    }
    private fun isReachLeft(head: Pair<Int, Int>, neck: Pair<Int, Int>): Boolean {
        return head.first == 0
    }
    private fun isReachRight(head: Pair<Int, Int>, neck: Pair<Int, Int>): Boolean {
        return head.first == boxPerRow-1
    }

    private fun isDead(head: Pair<Int, Int>): Boolean {
        return snakeSet.contains(head.convertToIndex())
    }

    private fun getDirection(head: Pair<Int,Int>, neck: Pair<Int, Int>): RunDirection {
        when {
            head.first == neck.first && (head.second+1==neck.second || (head.second == boxPerRow-1 && neck.second == 0)) ->{
                return RunDirection.Up
            }
            head.first == neck.first && (head.second-1==neck.second || (head.second == 0 && neck.second == boxPerRow-1)) -> {
                return RunDirection.Down
            }
            head.second == neck.second && (head.first+1==neck.first || (head.first == boxPerRow-1 && neck.first == 0)) ->{
                return RunDirection.Left
            }
            head.first == neck.first && (head.first-1==neck.first || (head.first == 0 && neck.first == boxPerRow-1)) -> {
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
        return this.first+ this.second*boxPerRow
    }
}