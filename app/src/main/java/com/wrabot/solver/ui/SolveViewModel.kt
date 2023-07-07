package com.wrabot.solver.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SolveViewModel : ViewModel() {
    var grid by mutableStateOf<Grid?>(null)

    fun solve() {
        val grid = grid ?: return
        val cells = grid.cells.toMutableList() ?: return
        cells.forEachIndexed { index, i ->
            if (i == null) {
                cells[index] = 0
                val zero = check(grid.size, cells)
                cells[index] = 1
                val one = check(grid.size, cells)
                when {
                    zero && one -> cells[index] = null
                    !zero && !one -> return // not consistent
                    zero -> cells[index] = 0
                }
            }
        }
        this.grid = grid.copy(cells = cells)
    }

    private fun check(size: Int, cells: List<Int?>): Boolean {
        val rows = cells.chunked(size)
        //if (rows.distinct().size != rows.size) return false
        val columns = rows.indices.map { c -> rows.map { row -> row[c] } }
        //if (columns.distinct().size != columns.size) return false
        (rows + columns).forEach { line ->
            if (line.count { it == 0 } > size / 2) return false
            if (line.count { it == 1 } > size / 2) return false
            line.windowed(3).forEach { group ->
                if (group.count { it == 0 } == 3) return false
                if (group.count { it == 1 } == 3) return false
            }
        }
        return true
    }
}
