package com.wrabot.solver.game

import com.wrabot.solver.grid.GridStack

abstract class Game(val stack: GridStack<Char?>, val values: Set<Char>) {
    override fun toString() = stack.current.toString()

    abstract fun solve(): Boolean
}
