package com.wrabot.solver.grid


open class Grid<T>(val height: Int, val width: Int, val cells: List<T>) {
    init {
        if (cells.size != height * width) throw IllegalArgumentException("invalid cells size")
    }

    operator fun get(row: Int, column: Int) = cells.getOrNull(row * width + column)

    override fun toString() = cells.chunked(width).run {
        val separator = List(width) { "─" }.joinToString("┼", "\n┠", "┨\n")
        val lineBold = List(width) { "━" }
        val top = lineBold.joinToString("┯", "┏", "┓\n")
        val bottom = lineBold.joinToString("┷", "\n┗", "┛")
        joinToString(separator, top, bottom) { row ->
            row.joinToString("│", "┃", "┃") { it?.toString() ?: " " }
        }
    }

    fun <U> toGrid(transform: (row: Int, column: Int, value: T) -> U) =
        Grid(height, width, cells.mapIndexed { index, value ->
            transform(index / width, index % width, value)
        })
}
