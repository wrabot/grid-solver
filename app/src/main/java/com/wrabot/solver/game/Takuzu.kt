package com.wrabot.solver.game

import com.wrabot.solver.game.takuzu.Cell
import com.wrabot.solver.game.takuzu.TakuzuSolver
import com.wrabot.solver.grid.GridStack

class Takuzu(override val stack: GridStack<Char?>) : Game {
    override val values = setOf('0', '1')

    override fun solve(): Boolean {
        val grid = stack.current.toGrid { row, column, value -> Cell(row, column, value) }
        TakuzuSolver(grid, values).solve()
        stack.add(grid.toGrid { _, _, cell -> cell.value })
        return stack.current.cells.all { it != null }
    }

    override fun toString() = stack.current.toString()
}
