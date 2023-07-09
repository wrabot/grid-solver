package grids

abstract class Grid(val width: Int, val cells: List<Cell>) {
    data class Cell(var value: Int?)

    val rows = cells.chunked(width)
    val columns = (0 until width).map { c -> rows.map { it[c] } }

    override fun toString() = rows.joinToString("\n") { row ->
        row.joinToString("") { it.value?.toString() ?: "." }
    }

    abstract fun solve()

    abstract fun isValid(): Boolean
}
