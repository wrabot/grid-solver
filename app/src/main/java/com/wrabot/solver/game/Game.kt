package com.wrabot.solver.game

import com.wrabot.solver.grid.GridStack

sealed interface Game {
    val stack: GridStack<Char?>
    val values: Set<Char>
    fun solve(): Boolean
}
