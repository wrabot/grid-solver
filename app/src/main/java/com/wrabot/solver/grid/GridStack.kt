package com.wrabot.solver.grid

import kotlinx.serialization.Serializable

@Serializable
data class GridStack<T>(var current: Grid<T>) {
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
        if (undo.isNotEmpty()) {
            redo.add(0, current)
            current = undo.removeLast()
        }
    }
    fun undoAll() {
        if (undo.isNotEmpty()) {
            current = undo.removeFirst()
            redo.addAll(0, undo)
            undo.clear()
        }
    }

    fun hasRedo() = redo.isNotEmpty()
    fun redo() {
        if (redo.isNotEmpty()) {
            undo.add(current)
            current = redo.removeLast()
        }
    }
    fun redoAll() {
        if (redo.isNotEmpty()) {
            current = redo.removeLast()
            undo.addAll(redo)
            redo.clear()
        }
    }
}
