package com.wrabot.solver.grids

open class Grid<T>(val height: Int, val width: Int, val cells: List<T>) {
    init {
        if (cells.size != height * width) throw IllegalArgumentException("invalid cells size")
    }

    fun isComplete() = cells.all { it != null }

    operator fun get(row: Int, column: Int) = cells.getOrNull(row * width + column)

    override fun toString() = cells.chunked(width).joinToString("\n") { row ->
        row.joinToString("") { it?.toString() ?: "." }
    }

    fun <U> toGrid(transform: (row: Int, column: Int, value: T) -> U) =
        Grid(height, width, cells.mapIndexed { index, value ->
            transform(index / width, index % width, value)
        })
}
