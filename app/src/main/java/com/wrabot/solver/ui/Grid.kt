package com.wrabot.solver.ui

data class Grid(val size: Int, val cells: List<Int?>) {
    override fun toString() = cells.toList().chunked(size).joinToString("\n") {
        it.joinToString("") { it?.toString() ?: "." }
    }
}
