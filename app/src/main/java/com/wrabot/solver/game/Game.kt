package com.wrabot.solver.game

import com.wrabot.solver.grid.GridStack
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
sealed class Game {
    val id : String = UUID.randomUUID().toString()
    abstract val stack: GridStack<Char?>
    abstract val values: Set<Char>
    abstract fun solve()
}
