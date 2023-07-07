package com.wrabot.solver.ui

import android.graphics.Bitmap
import android.graphics.Rect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class RecognitionViewModel : ViewModel() {
    data class Symbol(val value: Int, val boundingBox: Rect) {
        override fun equals(other: Any?): Boolean = other is Symbol && value == other.value && boundingBox.intersect(other.boundingBox)
        override fun hashCode() = value
    }

    var symbols by mutableStateOf(emptyList<Symbol>())
    var grid by mutableStateOf<Grid?>(null)

    fun recognize(bitmap: Bitmap) {
        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
            .process(InputImage.fromBitmap(bitmap, 0))
            .addOnSuccessListener {
                symbols = it.textBlocks.flatMap { it.lines.flatMap { it.elements.flatMap { it.symbols } } }
                    .mapNotNull { Symbol(it.text.toIntOrNull() ?: return@mapNotNull null, it.boundingBox ?: return@mapNotNull null) }
                    .distinct()
                for (size in 3..12) {
                    val cells = arrayOfNulls<Int>(size * size)
                    symbols.forEach {
                        val rect = it.boundingBox
                        val r = rect.centerY() * size / bitmap.height
                        val c = rect.centerX() * size / bitmap.width
                        cells[r * size + c] = it.value
                    }
                    if (cells.count { it != null } == symbols.size) {
                        grid = Grid(size, cells.toList())
                        break
                    }
                }
            }
    }
}
