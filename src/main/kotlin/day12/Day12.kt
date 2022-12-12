package day12

import Day

object Day12 : Day {
    override fun problem1() {
        find(false)
    }

    override fun problem2() {
        find(true)
    }

    private fun find(fromEnd: Boolean) {
        lateinit var start: Point

        val grid = javaClass.getResource("/day12.txt")!!.readText().lines().mapIndexed { x, line ->
            line.mapIndexed { y, char ->
                when (char) {
                    'S' -> Point(x = x, y = y, elevation = 0, endNode = fromEnd).also { if (!fromEnd) start = it }
                    'E' -> Point(x = x, y = y, elevation = 'z' - 'a', endNode = !fromEnd).also { if (fromEnd) start = it }
                    'a' -> Point(x = x, y = y, elevation = 0, endNode = fromEnd)
                    else -> Point(x = x, y = y, elevation = char - 'a')
                }
            }
        }

        val queue = ArrayDeque<Pair<Int, Point>>()
        queue.add(0 to start)
        var found = false
        while (queue.isNotEmpty() && !found) {
            found = evaluate(grid, queue) { point, next ->
                if (fromEnd) {
                    next.elevation - point.elevation >= -1
                } else {
                    point.elevation - next.elevation >= -1
                }
            }
        }

        println("found after: ${queue.first().first} steps")
    }
    private fun evaluate(grid: List<List<Point>>, queue: ArrayDeque<Pair<Int, Point>>, compareElevation: (Point, Point) -> Boolean): Boolean {
        val (steps, point) = queue.first()
        if (point.endNode) return true
        queue.removeFirst()
        if (point.visited) return false
        point.visited = true

        if (point.x > 0) {
            val next = grid[point.x - 1][point.y]
            if (!next.visited && compareElevation(point, next)) {
                queue.addLast(steps + 1 to next)
            }
        }
        if (point.x < grid.lastIndex) {
            val next = grid[point.x + 1][point.y]
            if (!next.visited && compareElevation(point, next)) {
                queue.addLast(steps + 1 to next)
            }
        }
        if (point.y > 0) {
            val next = grid[point.x][point.y - 1]
            if (!next.visited && compareElevation(point, next)) {
                queue.addLast(steps + 1 to next)
            }
        }
        if (point.y < grid[point.x].lastIndex) {
            val next = grid[point.x][point.y + 1]
            if (!next.visited && compareElevation(point, next)) {
                queue.addLast(steps + 1 to next)
            }
        }
        return false
    }
}

private data class Point(val x: Int, val y: Int, var visited: Boolean = false, val elevation: Int, val endNode: Boolean = false)