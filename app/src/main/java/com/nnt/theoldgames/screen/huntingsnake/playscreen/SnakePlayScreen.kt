package com.nnt.theoldgames.screen.huntingsnake

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.nnt.theoldgames.screen.huntingsnake.playscreen.SnakePlayViewModel

@ExperimentalFoundationApi
@Composable
fun SnakePlayScreen(navController: NavController = rememberNavController()){
    val viewModel = hiltViewModel<SnakePlayViewModel>()
    val screenWithDp = LocalContext.current.resources.configuration.screenWidthDp
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        SnakePlayGround(viewModel, screenWithDp, rememberLazyListState())
    }
}

@ExperimentalFoundationApi
@Composable
fun SnakePlayGround(
    viewModel: SnakePlayViewModel,
    screenWidthDp: Int,
    listState: LazyListState,
    margin: Int = 16
) {
    val boxPerRow = viewModel.boxPerRow
    val snake = viewModel.snakeMaps.collectAsState().value
    val boxSize = ((screenWidthDp - margin * 2) / boxPerRow).dp
    LazyVerticalGrid(
        cells = GridCells.Fixed(boxPerRow),
        state = listState,
        modifier = Modifier
            .padding(margin.dp)
            .border(width = 2.dp, Color.Black)
    ) {
        items(boxPerRow*boxPerRow){ index ->
            if(snake.contains(index)){
                Box(modifier = Modifier.size(boxSize).background(Color.Black))
            }
            else {
                Box(modifier = Modifier.size(boxSize))
            }

        }
    }
}