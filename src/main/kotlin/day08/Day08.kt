package day08

import Day
import kotlin.math.max

object Day08 : Day {

    class Tree(
        val height: Int,
        var visibleL: Boolean? = null,
        var visibleR: Boolean? = null,
        var visibleT: Boolean? = null,
        var visibleB: Boolean? = null
    ) {
        var maxL:Int? = null
        var maxR:Int? = null
        var maxT:Int? = null
        var maxB:Int? = null
    }
    override fun problem1() {
        val grid = javaClass.getResource("/day08.txt")!!.readText().lines().map { it.chunked(1).map { c -> Tree(c.toInt()) } }
        var noVisible = 0
        for (i in 0 .. grid.lastIndex) {
            for (j in 0..grid[i].lastIndex) {
                val visible = determineVisibility(i, j, grid)
                if (visible) noVisible++
            }
        }

        println("visible trees: $noVisible")
    }

    private fun determineVisibility(i: Int, j: Int, grid: List<List<Tree>>): Boolean {
        return (checkUp(i, j, grid) ||
                checkDown(i, j, grid) ||
                checkLeft(i, j, grid) ||
                checkRight(i, j, grid))
    }

    private fun checkUp(i: Int, j: Int, grid: List<List<Tree>>): Boolean {
        val tree = grid[i][j]
        if (tree.visibleT != null) return tree.visibleT!!
        if (i == 0) {
            tree.visibleT = true
            tree.maxT = tree.height
            return true
        }

        checkUp(i-1, j, grid)
        val visible = grid[i-1][j].maxT!! < tree.height
        tree.visibleT = visible
        tree.maxT = max(tree.height, grid[i-1][j].maxT!!)
        return visible
    }

    private fun checkDown(i: Int, j: Int, grid: List<List<Tree>>): Boolean {
        val tree = grid[i][j]
        if (tree.visibleB != null) return tree.visibleB!!
        if (i == grid.lastIndex) {
            tree.visibleB = true
            tree.maxB = tree.height
            return true
        }

        checkDown(i+1, j, grid)
        val maxB = grid[i+1][j].maxB!!
        val visible = maxB < tree.height

        tree.visibleB = visible
        tree.maxB = max(maxB, tree.height)
        return visible
    }

    private fun checkLeft(i: Int, j: Int, grid: List<List<Tree>>): Boolean {
        val tree = grid[i][j]
        if (tree.visibleL != null) return tree.visibleL!!
        if (j == 0) {
            tree.visibleL = true
            tree.maxL = tree.height
            return true
        }

        checkLeft(i, j-1, grid)
        val maxL = grid[i][j-1].maxL!!
        val visible = tree.height > maxL
        tree.visibleL = visible
        tree.maxL = max(maxL, tree.height)
        return visible
    }

    private fun checkRight(i: Int, j: Int, grid: List<List<Tree>>): Boolean {
        val tree = grid[i][j]
        if (tree.visibleR != null) return tree.visibleR!!
        if (j == grid[i].lastIndex) {
            tree.visibleR = true
            tree.maxR = tree.height
            return true
        }

        checkRight(i, j+1, grid)
        val maxR = grid[i][j+1].maxR!!
        val visible = maxR < tree.height
        tree.visibleR = visible
        tree.maxR = max(maxR, tree.height)
        return visible
    }

    override fun problem2() {
        val max = javaClass.getResource("/day08.txt")!!.readText().lines().map { it.chunked(1).map { c -> c.toInt() } }.let { grid ->
            grid.mapIndexed { i, rows ->
                rows.mapIndexed { j, height ->
                    val l = (j - 1 downTo 0).map { grid[i][it] }.takeWhileInclusive { it < height }.count()
                    val r = (j + 1..rows.lastIndex).map { grid[i][it] }.takeWhileInclusive { it < height }.count()
                    val u = (i - 1 downTo 0).map { grid[it][j] }.takeWhileInclusive { it < height }.count()
                    val d = (i + 1..grid.lastIndex).map { grid[it][j] }.takeWhileInclusive { it < height }.count()
                    val sum = l * r * u * d
                    sum
                }
            }
        }.maxOf { row -> row.maxOf { it } }
        println("$max")
    }
}


fun <T> Iterable<T>.takeWhileInclusive(predicate: (T) -> Boolean): List<T> {
    val list = ArrayList<T>()
    for (item in this) {
        list.add(item)
        if (!predicate(item))
            break
    }
    return list
}