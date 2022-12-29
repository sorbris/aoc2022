package day18

import Day
import kotlin.math.abs
import kotlin.math.max

object Day18 : Day {
    override fun problem1() {
        val points = javaClass.getResource("/day18.txt")!!.readText().lines().map {
            val coords = it.split(",")
            Point(coords[0].toInt(), coords[1].toInt(), coords[2].toInt())
        }
        val sides = mutableMapOf<Point, Int>()
        for (i in 0 until points.lastIndex) {
            val point = points[i]
            var count = 0
            for (j in i + 1..points.lastIndex) {
                val p2 = points[j]
                if (point.distanceTo(p2) == 1) {
                    sides[point] = (sides[point] ?: 0) + 1
                    sides[p2] = (sides[p2] ?: 0) + 1
                }
            }
        }
        val surface = points.fold(0) { acc, point ->

            acc + (6 - (sides[point] ?: 0))
        }
        println("surface: $surface")
    }

    override fun problem2() {
        var maxX = Integer.MIN_VALUE
        var maxY = Integer.MIN_VALUE
        var maxZ = Integer.MIN_VALUE
        val points = javaClass.getResource("/day18.txt")!!.readText().lines().map {
            val coords = it.split(",")
            val x = coords[0].toInt()
            val y = coords[1].toInt()
            val z = coords[2].toInt()
            maxX = max(maxX, x)
            maxY = max(maxY, y)
            maxZ = max(maxZ, z)
            Point(x, y, z)
        }
        val cube = Cube(maxX, maxY, maxZ)
        cube.setAll(points)
        val sum = cube.findOpen()
        println("sum: $sum")
    }
}

private class Cube(private val x: Int, private val y: Int, private val z: Int) {
    private val grid: Array<Array<BooleanArray>> = Array(x + 1) {
        Array(y + 1) {
            BooleanArray(z + 1)
        }
    }

    fun findOpen(): Int {
        val visited = mutableSetOf<Point>()
        var sum = 0
        for (xs in 0..x) {
            for (ys in 0..y) {
                val p1 = Point(xs, ys, 0)
                if (!visited.contains(p1) && !this[p1]) {
                    sum += evaluate(p1, visited)
                } else if (this[p1]) {
                    sum += 1
                }
                val p2 = Point(xs, ys, z)
                if (!visited.contains(p2) && !this[p2]) {
                    sum += evaluate(p2, visited)
                } else if (this[p2]) {
                    sum += 1
                }
            }
            for (zs in 0..z) {
                val p1 = Point(xs, 0, zs)
                if (!visited.contains(p1) && !this[p1]) {
                    sum += evaluate(p1, visited)
                } else if (this[p1]) {
                    sum += 1
                }
                val p2 = Point(xs, y, zs)
                if (!visited.contains(p2) && !this[p2]) {
                    sum += evaluate(p2, visited)
                } else if (this[p2]) {
                    sum += 1
                }
            }
        }
        for (ys in 0..y) {
            for (zs in 0..z) {
                val p1 = Point(0, ys, zs)
                if (!visited.contains(p1) && !this[p1]) {
                    sum += evaluate(p1, visited)
                } else if (this[p1]) {
                    sum += 1
                }
                val p2 = Point(x, ys, zs)
                if (!visited.contains(p2) && !this[p2]) {
                    sum += evaluate(p2, visited)
                } else if (this[p2]) {
                    sum += 1
                }
            }
        }
        return sum
    }

    private fun evaluate(point: Point, visited: MutableSet<Point>): Int {
        visited.add(point)
        var exposed = 0
        if (point.x > 0) {
            val p = point.copy(x = point.x - 1)
            if (this[p]) {
                exposed += 1
            } else if (!visited.contains(p)) {
                exposed += evaluate(p, visited)
            }
        }
        if (point.x < x) {
            val p = point.copy(x = point.x + 1)
            if (this[p]) {
                exposed += 1
            } else if (!visited.contains(p)) {
                exposed += evaluate(p, visited)
            }
        }
        if (point.y > 0) {
            val p = point.copy(y = point.y - 1)
            if (this[p]) {
                exposed += 1
            } else if (!visited.contains(p)) {
                exposed += evaluate(p, visited)
            }
        }
        if (point.y < y) {
            val p = point.copy(y = point.y + 1)
            if (this[p]) {
                exposed += 1
            } else if (!visited.contains(p)) {
                exposed += evaluate(p, visited)
            }
        }
        if (point.z > 0) {
            val p = point.copy(z = point.z - 1)
            if (this[p]) {
                exposed += 1
            } else if (!visited.contains(p)) {
                exposed += evaluate(p, visited)
            }
        }
        if (point.z < z) {
            val p = point.copy(z = point.z + 1)
            if (this[p]) {
                exposed += 1
            } else if (!visited.contains(p)) {
                exposed += evaluate(p, visited)
            }
        }
        return exposed
    }

    private fun checkLeft(point: Point, trapped: MutableSet<Point>, notTrapped: MutableSet<Point>) {

    }

    operator fun get(point: Point) = get(point.x, point.y, point.z)
    operator fun get(x: Int, y: Int, z: Int): Boolean = grid[x][y][z]
    operator fun set(x: Int, y: Int, z: Int, value: Boolean) {
        grid[x][y][z] = value
    }

    fun setAll(points: Collection<Point>) {
        points.forEach { (x, y, z) ->
            this[x, y, z] = true
        }
    }
}
private data class Point(val x: Int, val y: Int, val z: Int) {
    fun distanceTo(other: Point): Int {
        return abs(x - other.x) + abs(y - other.y) + abs(z - other.z)
    }
}