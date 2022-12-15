package day14

import Day
import kotlin.math.max

object Day14 : Day {
    override fun problem1() {
        val (ymax, grid) = parseGrid(javaClass.getResource("/day14.txt")!!.readText())

        var overflow = false
        var counter = 0
        while(!overflow) {

            overflow = addSand(500 to 0, grid, ymax)
            if (!overflow) counter++
        }
        println("overflow after: $counter")
    }

    override fun problem2() {
        val (ymax, grid) = parseGrid(javaClass.getResource("/day14.txt")!!.readText())

        var overflow = false
        var counter = 0
        while(!overflow) {

            overflow = addSand2(500 to 0, grid, ymax + 2)
            counter++
        }
        println("overflow after: $counter")
    }
}

private fun parseGrid(input: String): Pair<Int, MutableMap<Pair<Int, Int>, Char>> {
    val grid = mutableMapOf<Pair<Int, Int>, Char>()
    var ymax = Integer.MIN_VALUE
    input.lineSequence()
        .map { line ->
            val parts = line.split("->")
            val points = parts.map { pair ->
                val l = pair.split(",")
                val x = l.first().trim().toInt()
                val y = l.last().trim().toInt()
                ymax = max(ymax, y)
                x to y
            }
            points
        }
        .forEach {
            it.windowed(2, 1, true).map { list ->
                val dx = list.first().first - list.last().first
                val dy = list.first().second - list.last().second
                val xrange = if (dx > 0) {
                    list.last().first..list.first().first
                } else if (dx < 0) {
                    list.first().first..list.last().first
                } else {
                    null
                }
                val yrange = if (dy > 0) {
                    list.last().second..list.first().second
                } else if (dy < 0) {
                    list.first().second..list.last().second
                } else {
                    null
                }
                xrange?.let { range ->
                    for (x in range) {
                        grid[x to list.first().second] = '#'
                    }
                }
                yrange?.let { range ->
                    for (y in range) {
                        grid[list.first().first to y] = '#'
                    }
                }


            }
        }
    return ymax to grid
}

private fun addSand2(coord: Pair<Int, Int>, grid: MutableMap<Pair<Int, Int>, Char>, ymax: Int): Boolean {
    val (x, y) = coord


    return if (y + 1 == ymax) {
        grid[coord] = 'o'
        false
    } else if (grid[x to y + 1] != '#' && grid[x to y + 1] != 'o') {
        addSand2(x to y + 1, grid, ymax)
    } else {
        if (grid[x - 1 to y + 1] != '#' && grid[x - 1 to y + 1] != 'o') {
            addSand2(x - 1 to y + 1, grid, ymax)
        } else if (grid[x + 1 to y + 1] != '#' && grid[x + 1 to y + 1] != 'o') {
            addSand2(x + 1 to y + 1, grid, ymax)
        } else {
            grid[coord] = 'o'
            return x == 500 && y == 0
        }
    }
}

private fun addSand(coord: Pair<Int, Int>, grid: MutableMap<Pair<Int, Int>, Char>, ymax: Int): Boolean {
    // Overflow
    if (coord.second > ymax) return true

    val (x, y) = coord

    return if (grid[x to y + 1] != '#' && grid[x to y + 1] != 'o') {
        addSand(x to y + 1, grid, ymax)
    } else {
        if (grid[x - 1 to y + 1] != '#' && grid[x - 1 to y + 1] != 'o') {
            addSand(x - 1 to y + 1, grid, ymax)
        } else if (grid[x + 1 to y + 1] != '#' && grid[x + 1 to y + 1] != 'o') {
            addSand(x + 1 to y + 1, grid, ymax)
        } else {
            grid[coord] = 'o'
            false
        }
    }
}