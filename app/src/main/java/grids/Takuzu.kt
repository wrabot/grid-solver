package grids

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

    override fun isValid(): Boolean {
        if (rows.distinct().size != rows.size) return false
        if (columns.distinct().size != columns.size) return false
        (rows + columns).forEach { line ->
            if (line.count { it.value == 0 } > width / 2) return false
            if (line.count { it.value == 1 } > width / 2) return false
            line.windowed(3).forEach { group ->
                if (group.count { it.value == 0 } == 3) return false
                if (group.count { it.value == 1 } == 3) return false
            }
        }
        return true
    }
}
