package com.nnt.theoldgames.screen.huntingsnake

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.nnt.theoldgames.screen.huntingsnake.playscreen.SnakePlayViewModel

@ExperimentalUnitApi
@ExperimentalFoundationApi
@Composable
fun SnakePlayScreen(navController: NavController = rememberNavController()){
    val viewModel = hiltViewModel<SnakePlayViewModel>()
    val screenWithDp = LocalContext.current.resources.configuration.screenWidthDp
    val score = viewModel.score.value
    Box(modifier = Modifier.fillMaxSize()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(horizontalArrangement = Arrangement.End) {
                Text("Score: ")
                Text(text = "$score")
            }
            SnakePlayGround(viewModel, screenWithDp)
            Spacer(modifier = Modifier.height(24.dp))
            Controller {
                viewModel.move(it)
            }
        }
    }
    viewModel.isDead.value.apply {
        if(this){
            DeadDialog(onReplay = {viewModel.reset()}, onExit = {
                viewModel.isDead.value = false
                navController.popBackStack() })
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun SnakePlayGround(
    viewModel: SnakePlayViewModel,
    screenWidthDp: Int,
    margin: Int = 16
) {
    val boxPerRow = viewModel.boxPerRow
    val snake = viewModel.snakeCoordinate
    val boxSize = ((screenWidthDp - margin * 2) / boxPerRow)
    val foodPos = viewModel.foodPositionState.collectAsState().value
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .padding(top = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size((boxPerRow * boxSize).dp)
                    .border(2.dp, Color.Blue)
            )
            Box(
                modifier = Modifier
                    .size(boxSize.dp)
                    .offset(x = (foodPos.first * boxSize).dp, y = (foodPos.second * boxSize).dp)
                    .background(Color.Blue, shape = CircleShape)
            )
            snake.forEach {
                Box(
                    modifier = Modifier
                        .size(boxSize.dp)
                        .offset(x = (it.first * boxSize).dp, y = (it.second * boxSize).dp)
                        .background(Color.Blue, shape = RoundedCornerShape(2.dp))
                ) {
                }
            }
        }
    }
}

@ExperimentalFoundationApi
@Preview
@Composable
fun Controller(onMove: (direction: SnakePlayViewModel.RunDirection)-> Unit = {}){
    val buttonWidth = 60.dp
    LazyVerticalGrid(cells = GridCells.Fixed(3), modifier = Modifier.width(180.dp)){
        items(9){ index ->
            when(index){
                1 -> {
                    Button(onClick = {onMove.invoke(SnakePlayViewModel.RunDirection.Up)}) {
                        Text(text = "^", modifier = Modifier.width(buttonWidth), textAlign = TextAlign.Center)
                    }
                }
                3 -> {
                    Button(onClick = {onMove.invoke(SnakePlayViewModel.RunDirection.Left)}) {
                        Text(text = "<", modifier = Modifier.width(buttonWidth), textAlign = TextAlign.Center)
                    }
                }
                5 -> {
                    Button(onClick = {onMove.invoke(SnakePlayViewModel.RunDirection.Right)}) {
                        Text(text = ">", modifier = Modifier.width(buttonWidth), textAlign = TextAlign.Center)
                    }
                }
                7 -> {
                    Button(onClick = {onMove.invoke(SnakePlayViewModel.RunDirection.Down)}) {
                        Text(text = "v", modifier = Modifier.width(buttonWidth), textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}

@ExperimentalUnitApi
@Composable
fun DeadDialog(onReplay: ()-> Unit, onExit: ()-> Unit){
    val buttonWith= 80.dp
    Dialog(onDismissRequest = { onReplay.invoke()}){
        Column(
            modifier = Modifier
                .padding(bottom = 100.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Game over", color = Color.White, fontSize = TextUnit(20f, TextUnitType.Sp))
            Spacer(modifier = Modifier.height(100.dp))
            Row {
                Button(onClick = { onReplay.invoke() }) {
                    Text(text = "Replay", modifier = Modifier.width(buttonWith), textAlign = TextAlign.Center)
                }
                Spacer(modifier = Modifier.width(20.dp))
                Button(onClick = {
                    onExit.invoke() }) {
                    Text(text = "Exit", modifier = Modifier.width(buttonWith), textAlign = TextAlign.Center)
                }
            }
        }
    }
}