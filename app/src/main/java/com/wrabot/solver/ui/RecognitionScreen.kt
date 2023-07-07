package com.wrabot.solver.ui

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.toComposeRect
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wrabot.solver.R

@Composable
fun RecognitionScreen(bitmap: Bitmap, viewModel: RecognitionViewModel = viewModel(), onRecognize: (Grid) -> Unit) {
    LaunchedEffect(bitmap) { viewModel.recognize(bitmap) }
    Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Image(bitmap.asImageBitmap(), contentDescription = null, Modifier.fillMaxWidth().wrapContentHeight().weight(1f).verticalScroll(rememberScrollState()).drawWithContent {
            drawContent()
            val thickness = 2.dp.toPx()
            scale(size.width / bitmap.width, Offset.Zero) {
                viewModel.symbols.forEach {
                    val rect = it.boundingBox.toComposeRect()
                    drawRect(Color.Red, rect.topLeft, rect.size, style = Stroke(thickness))
                }
            }
        })
        val grid = viewModel.grid
        Button(onClick = { onRecognize(grid!!) }, enabled = grid != null) {
            Text(stringResource(R.string.next))
        }
    }
}
