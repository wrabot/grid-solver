package com.wrabot.solver.ui

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wrabot.solver.R
import com.wrabot.solver.game.Game
import com.wrabot.solver.game.Sudoku
import com.wrabot.solver.game.Takuzu
import com.wrabot.solver.grid.GridStack
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecognitionScreen(bitmap: Bitmap, viewModel: RecognitionViewModel = viewModel(), onRecognize: (Game) -> Unit) {
    LaunchedEffect(bitmap) { viewModel.recognize(bitmap) }
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth().wrapContentHeight().weight(1f).verticalScroll(rememberScrollState()).drawWithContent {
                drawContent()
                val thickness = 2.dp.toPx()
                viewModel.symbols.forEach {
                    val topLeft = Offset(it.boundingBox.left * size.width, it.boundingBox.top * size.height)
                    val size = Size(it.boundingBox.width() * size.width, it.boundingBox.height() * size.height)
                    drawRect(Color.Red, topLeft, size, style = Stroke(thickness))
                }
            }
        )
        Button(
            onClick = { coroutineScope.launch { sheetState.expand() } },
            enabled = viewModel.symbols.isNotEmpty()
        ) {
            Text(stringResource(R.string.next))
        }
    }
    if (sheetState.isVisible) {
        ModalBottomSheet(onDismissRequest = {}, sheetState = sheetState) {
            Text(stringResource(R.string.select_title), Modifier.padding(16.dp))
            Divider()
            Text(
                text = stringResource(R.string.select_sudoku),
                modifier = Modifier.fillMaxWidth().clickable {
                    onRecognize(Sudoku(GridStack(viewModel.createGrid(9, 9))))
                }.padding(16.dp)
            )
            listOf(8, 10, 12, 14).forEach {
                Divider()
                Text(
                    text = stringResource(R.string.select_tazuku, it),
                    modifier = Modifier.fillMaxWidth().clickable {
                        onRecognize(Takuzu(GridStack(viewModel.createGrid(it, it))))
                    }.padding(16.dp)
                )
            }
        }
    }
}
