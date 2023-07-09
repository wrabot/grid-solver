package grids

import kotlin.math.max

@Suppress("SpellCheckingInspection")
class Takuzu(width: Int, cells: List<Int?>) : Grid(width, cells.map { Cell(it) }) {
    override fun solve() {
        val cells = cells.toMutableList()
        do {
            var isModified = false
            cells.forEachIndexed { index, i ->
                if (i.value == null) {
                    cells[index].value = 0
                    val zero = isValid()
                    cells[index].value = 1
                    val one = isValid()
                    when {
                        !(zero xor one) -> cells[index].value = null
                        zero -> cells[index].value = 0
                    }
                    isModified = isModified || cells[index].value != null
                }
            }
        } while (isModified)
    }

    override fun isValid() = rows.allDistinct() && columns.allDistinct() && (rows + columns)
        .all { it.maxCount() <= it.size / 2 && it.checkTriples() }

    private fun List<Cell>.checkTriples() = windowed(3).all { it.maxCount() < 3 }
    private fun List<Cell>.maxCount() = max(count { it.value == 0 }, count { it.value == 1 })
    private fun List<List<Cell>>.allDistinct() = filter { row -> row.all { it.value != null } }
        .let { it.distinct().size == it.size }
}
