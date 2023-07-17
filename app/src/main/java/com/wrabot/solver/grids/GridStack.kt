package com.wrabot.solver.grids

import java.util.LinkedList

class GridStack<T>(initial: Grid<T>) {
    var current = initial
        private set

    private val undo = LinkedList<Grid<T>>()
    private val redo = LinkedList<Grid<T>>()

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
