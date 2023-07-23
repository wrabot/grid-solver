import com.wrabot.solver.game.sudoku.SudokuSolver
import com.wrabot.solver.grid.Grid
import org.junit.Assert
import org.junit.Test
import java.io.File

class SodukuSolverTests {
    //@Test
    fun filter() {
        val writers = SudokuSolver.Level.values().associateWith { File("tests/$it.txt").bufferedWriter() }
        val counters = mutableMapOf<SudokuSolver.Level, Int>()
        File("tests/sudoku.csv").bufferedReader().lineSequence().drop(1)
            .forEachIndexed<String> { index, line ->
                if (index % 10000 == 0) println(index)
                val (input) = line.split(",")
                val solver = createSudokuSolver(input.map { it.takeIf { it != '0' } })
                val level = solver.solve()
                with(writers[level]!!) {
                    var count = counters.getOrDefault(level, 0)
                    if (level == SudokuSolver.Level.Other || count++ < 1000) {
                        counters[level] = count
                        write(line)
                        write("\n")
                    } else {
                        flush()
                    }
                }
            }
        writers.values.forEach {
            it.flush()
            it.close()
        }
    }

    @Test
    fun testSolveNeighbour() = testSolve(readLinesFromResource("Neighbour.txt"))

    @Test
    fun testSolveSegment() = testSolve(readLinesFromResource("Segment.txt"))

    @Test
    fun testSolveSubset() = testSolve(readLinesFromResource("Subset.txt"))

    @Test
    fun testSolveXWing() = testSolve(readLinesFromResource("XWing.txt"))

    @Test
    fun testSolveYWing() = testSolve(readLinesFromResource("YWing.txt"))

    private fun testSolve(tests: Sequence<String>) = tests.forEachIndexed { index, line ->
        val (input, output) = line.split(",")
        val solver = createSudokuSolver(input.map { it.takeIf { it != '0' } })
        solver.solve()
        val result = solver.grid.cells.joinToString("") { it.solutions.singleOrNull().toString() }
        Assert.assertEquals("error on $index", output, result)
    }

    private fun createSudokuSolver(cells: List<Char?>) =
        SudokuSolver(3, 3, Grid(9, 9, cells), ('1'..'9').toSet())

    private fun readLinesFromResource(name: String) =
        javaClass.getResourceAsStream(name)!!.bufferedReader().lineSequence()
}
