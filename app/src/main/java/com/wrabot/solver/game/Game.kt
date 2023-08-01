package com.wrabot.solver.game

import com.wrabot.solver.grid.Grid
import com.wrabot.solver.grid.GridStack

abstract class Game(grid: Grid<Char?>, val values: Set<Char>) {
    val stack = GridStack(grid)

    override fun toString() = stack.current.toString()

    abstract fun solve() : Boolean
}
