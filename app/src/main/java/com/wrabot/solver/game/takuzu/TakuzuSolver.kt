package com.wrabot.solver.game.takuzu

import com.wrabot.solver.grid.Grid

class TakuzuSolver(val grid: Grid<Cell>, private val values: Set<Char>) {
    fun solve() {
        val rows = (0 until grid.height).map { row -> grid.cells.filter { it.row == row } }
        val columns = (0 until grid.width).map { column -> grid.cells.filter { it.column == column } }
        do {
            var isModified = false
            grid.cells.filter { it.value == null }.forEach { cell ->
                cell.value = values.singleOrNull {
                    cell.value = it
                    // isValid
                    rows.allDistinct() && columns.allDistinct() &&
                        rows.checkCount() && columns.checkCount() &&
                        rows.checkTriples() && columns.checkTriples()
                }.also { if (it != null) isModified = true }
            }
        } while (isModified)
    }

    private fun List<List<Cell>>.allDistinct() = filter { row -> row.all { it.value != null } }
        .let { it.distinct().size == it.size }

    private fun List<List<Cell>>.checkCount() = all { it.maxCount() <= it.size / 2 }
    private fun List<List<Cell>>.checkTriples() = all { line -> line.windowed(3).all { it.maxCount() < 3 } }
    private fun List<Cell>.maxCount() = values.maxOf { value -> count { it.value == value } }
}
