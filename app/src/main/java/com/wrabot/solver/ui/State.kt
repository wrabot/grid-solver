package com.wrabot.solver.ui

import android.graphics.Bitmap
import com.wrabot.solver.data.Recents
import com.wrabot.solver.game.Game

sealed class State(private val ordinal: Int) : Comparable<State> {
    override fun compareTo(other: State) = ordinal.compareTo(other.ordinal)

    data object Select : State(0)
    data class Recognition(val bitmap: Bitmap) : State(1)
    data class Solve(val game: Game) : State(2) {
        init {
            Recents.add(game)
        }
    }
}
