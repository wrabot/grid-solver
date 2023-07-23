package com.wrabot.solver.game.sudoku


data class Cell(val row: Int, val column: Int, val block: Int, val solutions: MutableSet<Char>) {
    fun get() = solutions.singleOrNull()
    fun isFound() = solutions.size == 1
    fun toCharOrNull() = solutions.singleOrNull()
    operator fun contains(value: Char) = value in solutions
    operator fun minus(solution: Char) = solutions.remove(solution)
    fun isNeighbour(cell: Cell) = when (row) {
        cell.row -> column != cell.column
        else -> column == cell.column || block == cell.block
    }
}
