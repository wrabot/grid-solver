package com.wrabot.solver.game.sudoku

import com.wrabot.solver.grid.Grid


class SudokuSolver(val grid: Grid<Cell>, private val values: Set<Char>) {
    constructor(horizontalGroupSize: Int, verticalGroupSize: Int, grid: Grid<Char?>, values: Set<Char>) :
        this(
            grid.toGrid { row, column, value ->
                Cell(
                    row = row,
                    column = column,
                    block = (row / verticalGroupSize) * verticalGroupSize + (column / horizontalGroupSize),
                    solutions = if (value != null) mutableSetOf(value) else values.toMutableSet()
                )
            },
            values
        )

    enum class Level { None, Neighbour, Subset, Subset2, Segment, XWing, YWing, Other }

    fun solve(): Level {
        var level = Level.None
        while (grid.cells.any { it.solutions.size > 1 }) {
            level = level.coerceAtLeast(Level.Neighbour)
            if (updateNeighbours() > 0) continue
            level = level.coerceAtLeast(Level.Subset)
            if (updateSubsets() > 0) continue
            level = level.coerceAtLeast(Level.Segment)
            if (updateSegments() > 0) continue
            level = level.coerceAtLeast(Level.XWing)
            if (updateXWings() > 0) continue
            level = level.coerceAtLeast(Level.YWing)
            if (updateYWings() > 0) continue
            level = Level.Other
            break // no modification not solved
        }
        return level
    }

    private val range = 0 until grid.height
    private val rows = range.map { row -> grid.cells.filter { it.row == row } }
    private val columns = range.map { column -> grid.cells.filter { it.column == column } }
    private val blocks = range.map { block -> grid.cells.filter { it.block == block } }
    private val groups = rows + columns + blocks
    private val segments = (rows + columns).flatMap { group ->
        group.map { it.block }.distinct().map { block ->
            Pair(group.filter { it.block != block }, blocks[block].filter { it !in group })
        }
    }

    private fun updateNeighbours() = grid.cells.sumOf { cell ->
        val value = cell.get() ?: return@sumOf 0
        grid.cells.filter { it.isNeighbour(cell) }.remove(value)
    }.log("updateNeighbours")

    private fun updateSubsets() = groups.sumOf { group ->
        val notFoundCells = group.filter { !it.isFound() }
        values.sumOf { value ->
            val (with, without) = notFoundCells.partition { value in it }
            val set = without.flatMap { it.solutions }.toSet()
            if (set.size == without.size) with.remove(set) else 0
        }
    }.log("updateSubsets")

    private fun updateSegments() = segments.sumOf { (a, b) ->
        values.sumOf { it.removeInIfNotFoundIn(a, b) + it.removeInIfNotFoundIn(b, a) }
    }.log("updateSegments")

    private fun Char.removeInIfNotFoundIn(removeIn: List<Cell>, ifNotFoundIn: List<Cell>) =
        if (this !in ifNotFoundIn) removeIn.remove(this) else 0

    // todo check

    private fun updateSubsets2() = groups.sumOf { group ->
        val notFoundCells = group.filter { !it.isFound() }
        values.sumOf { value ->
            notFoundCells.findSubset(setOf(value))?.let { set ->
                group.filter { (it.solutions - set).isNotEmpty() }.remove(set)
            } ?: 0
        }
    }.log("updateSubsets")

    private fun List<Cell>.findSubset(set: Set<Char>): Set<Char>? {
        if (set.size == count()) return null
        if (count { (it.solutions - set).isEmpty() } == set.size) return set
        (filter { it.solutions.intersect(set).isNotEmpty() }.flatMap { it.solutions }.distinct() - set)
            .forEach { candidate -> findSubset(set + candidate)?.let { return it } }
        return null
    }

    private fun updateXWings() =
        values.sumOf { rows.updateXWing(it, columns) + columns.updateXWing(it, rows) }

    private fun List<List<Cell>>.updateXWing(value: Char, groups: List<List<Cell>>) = asSequence()
        .filter { group -> group.count { value in it } == 2 }
        .map { group ->
            Pair(group, Pair(group.indexOfFirst { value in it }, group.indexOfLast { value in it }))
        }.groupBy { it.second }.filter { it.value.size == 2 }.map {
            (groups[it.key.first] + groups[it.key.second] - it.value.first().first - it.value.last().first).remove(value)
        }.sum().log("updateXWings")

    private fun updateYWings(): Int {
        val pairs = grid.cells.filter { it.solutions.size == 2 }
        return pairs.sumOf { pincer ->
            val hinges = grid.cells.filter { (it.row == pincer.row || it.column == pincer.column) && it.block != pincer.block }
                .filter { it.solutions.size in 2..3 && it.solutions.intersect(pincer.solutions).size == it.solutions.size - 1 }
            hinges.sumOf { hinge ->
                val solution = (hinge.solutions - pincer.solutions).first()
                pairs.filter { it.block == hinge.block && it.row != hinge.row && it.column != hinge.column }.filter {
                    solution in it.solutions && it.solutions.intersect(pincer.solutions).size == 1 && it.solutions.intersect(hinge.solutions).size == hinge.solutions.size - 1
                }.sumOf { blockPincer ->
                    val list = (blocks[hinge.block] - hinge).filter { it.row == pincer.row || it.column == pincer.column }.toMutableList()
                    if (hinge.solutions.size == 2) list += blocks[pincer.block].filter { it.row == blockPincer.row || it.column == blockPincer.column }
                    list.remove(blockPincer.solutions.intersect(pincer.solutions).first())
                }
            }
        }.log("updateYWings")
    }
}
