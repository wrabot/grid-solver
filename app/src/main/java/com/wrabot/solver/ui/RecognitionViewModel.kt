package com.wrabot.solver.ui

import android.graphics.Bitmap
import android.graphics.RectF
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.wrabot.solver.grid.Grid

class RecognitionViewModel : ViewModel() {
    data class Symbol(val value: Char, val boundingBox: RectF) {
        override fun equals(other: Any?): Boolean = other is Symbol && value == other.value && boundingBox.intersect(other.boundingBox)
        override fun hashCode() = value.code
    }

    var symbols by mutableStateOf(emptyList<Symbol>())

    fun recognize(bitmap: Bitmap) {
        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
            .process(InputImage.fromBitmap(bitmap, 0))
            .addOnSuccessListener { text ->
                symbols = text.textBlocks.flatMap { block ->
                    block.lines.flatMap { line ->
                        line.elements.flatMap { it.symbols }
                    }
                }.mapNotNull { symbol ->
                    Symbol(
                        value = symbol.text.firstOrNull() ?: return@mapNotNull null,
                        boundingBox = symbol.boundingBox?.let {
                            RectF(
                                it.left.toFloat() / bitmap.width,
                                it.top.toFloat() / bitmap.height,
                                it.right.toFloat() / bitmap.width,
                                it.bottom.toFloat() / bitmap.height,
                            )
                        } ?: return@mapNotNull null
                    )
                }.distinct()
            }
    }

    fun createGrid(height: Int, width: Int): Grid<Char?> {
        val cells = arrayOfNulls<Char?>(height * width)
        symbols.forEach { symbol ->
            val rect = symbol.boundingBox
            val r = (rect.centerY() * height).toInt()
            val c = (rect.centerX() * width).toInt()
            cells[r * width + c] = symbol.value
        }
        return Grid(height, width, cells.toList())
    }
}
