package com.wrabot.solver.games

import com.wrabot.solver.grids.Grid
import com.wrabot.solver.grids.GridStack

abstract class Game(grid: Grid<Char?>, val values: Set<Char>) {
    val stack = GridStack(grid)

    override fun toString() = stack.current.toString()

    abstract fun solve() : Boolean
}
