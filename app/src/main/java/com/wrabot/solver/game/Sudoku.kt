package com.wrabot.solver.game

import com.wrabot.solver.game.sudoku.SudokuSolver
import com.wrabot.solver.game.sudoku.toString
import com.wrabot.solver.grid.GridStack
import kotlinx.serialization.Serializable


@Serializable
data class Sudoku(
    override val stack: GridStack<Char?>,
    private val horizontalGroupSize: Int = 3,
    private val verticalGroupSize: Int = 3
) : Game() {
    override val values = (1..horizontalGroupSize * verticalGroupSize)
        .map { it.toString(Character.MAX_RADIX).first() }.toSet()

    override fun solve() {
        val solver = SudokuSolver(horizontalGroupSize, verticalGroupSize, stack.current, values)
        solver.solve()
        stack.add(solver.grid.toGrid { _, _, cell -> cell.toCharOrNull() })
    }

    override fun toString() = stack.current.cells.toString(horizontalGroupSize, verticalGroupSize)
}

