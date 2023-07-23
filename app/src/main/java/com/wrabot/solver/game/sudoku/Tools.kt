package com.wrabot.solver.game.sudoku

fun List<Char?>.toString(horizontalGroupSize: Int, verticalGroupSize: Int) = run {
    val width = horizontalGroupSize * verticalGroupSize
    val separatorSimple = List(width) { "─" }.chunked(horizontalGroupSize) { it.joinToString("┼") }
        .joinToString("╂", "\n┠", "┨\n")
    val lineBold = List(width) { "━" }
    val separatorBold = lineBold.chunked(horizontalGroupSize) { it.joinToString("┿") }
        .joinToString("╋", "\n┣", "┫\n")
    val top = lineBold.chunked(horizontalGroupSize) { it.joinToString("┯") }
        .joinToString("┳", "┏", "┓\n")
    val bottom = lineBold.chunked(horizontalGroupSize) { it.joinToString("┷") }
        .joinToString("┻", "\n┗", "┛")
    chunked(width).chunked(verticalGroupSize) { verticalGroup ->
        verticalGroup.joinToString(separatorSimple) { row ->
            row.chunked(horizontalGroupSize) { horizontalGroup ->
                horizontalGroup.joinToString("│") { it?.toString() ?: " " }
            }.joinToString("┃", "┃", "┃")
        }
    }.joinToString(separatorBold, top, bottom)
}

operator fun List<Cell>.contains(solution: Char) = any { solution in it }

fun List<Cell>.remove(solutions: Set<Char>) = solutions.sumOf { remove(it) }
fun List<Cell>.remove(solution: Char) = count { it - solution }

private const val log = false

fun Int.log(message: String) = apply { if (log) println("$message $this") }
