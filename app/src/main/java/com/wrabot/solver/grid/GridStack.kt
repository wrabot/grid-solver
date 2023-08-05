package com.wrabot.solver.grid

import kotlinx.serialization.Serializable

@Serializable
class GridStack<T>(var current: Grid<T>) {
    private val undo = mutableListOf<Grid<T>>()
    private val redo = mutableListOf<Grid<T>>()

    fun add(grid: Grid<T>) {
        if (grid == current) return
        undo.add(current)
        redo.clear()
        current = grid
    }

    fun hasUndo() = undo.isNotEmpty()
    fun undo() {
        if (undo.isNotEmpty()) current = undo.removeLast()
    }

    fun hasRedo() = redo.isNotEmpty()
    fun redo() {
        if (redo.isNotEmpty()) current = redo.removeFirst()
    }
}
