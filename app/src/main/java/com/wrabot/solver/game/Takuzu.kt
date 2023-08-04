package com.wrabot.solver.game

import com.wrabot.solver.game.takuzu.Cell
import com.wrabot.solver.game.takuzu.TakuzuSolver
import com.wrabot.solver.grid.GridStack

class Takuzu(override val stack: GridStack<Char?>) : Game {
    override val values = setOf('0', '1')

    override fun solve() {
        val solver = TakuzuSolver(stack.current.toGrid(::Cell), values)
        solver.solve()
        stack.add(solver.grid.toGrid { _, _, cell -> cell.value })
    }

    override fun toString() = stack.current.toString()
}
