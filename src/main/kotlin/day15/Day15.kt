package day15

import Day
import java.util.regex.Pattern
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

object Day15 : Day {
    private val pattern = Pattern.compile("Sensor at x=(\\d+), y=(\\d+): closest beacon is at x=(\\d+), y=(\\d+)")

    override fun problem1() {
        val targetRow = 2000000
        val beacons = mutableListOf<Int>()
        val input = javaClass.getResource("/day15.txt")!!.readText()
        input.lineSequence()
            .map { line ->
                val sx = line.removePrefix("Sensor at x=").takeWhile { it != ',' }.toInt()
                val sy = line.dropWhile { it != ',' }.removePrefix(", y=").takeWhile { it != ':' }.toInt()
                val bx = line.dropWhile { it != ':' }.removePrefix(": closest beacon is at x=").takeWhile { it != ',' }.toInt()
                val by = line.takeLastWhile { it != '=' }.toInt()
                val s = Point(sx, sy)
                val b = Point(bx, by)
                if (by == targetRow) {
                    beacons.add(bx)
                }
                val distance = s.distanceTo(b)
                Sensor(s, distance)
            }
            .fold(mutableListOf<Int>()) { acc, sensor ->
                val y = targetRow
                val yd = abs(sensor.coords.y - y)

                if (yd <= sensor.range) {
                    val l = abs(yd - sensor.range)
                    for (i in sensor.coords.x - l .. sensor.coords.x + l) {
                        acc.add(i)
                    }
                }
                acc
            }.sorted().toSet().filter {
                !beacons.contains(it)
            }.let {
                println("size: ${it.size}")
            }

    }

    override fun problem2() {

        val input = javaClass.getResource("/day15.txt")!!.readText()
        val sensors = input.lines()
            .map { line ->
                val sx = line.removePrefix("Sensor at x=").takeWhile { it != ',' }.toInt()
                val sy = line.dropWhile { it != ',' }.removePrefix(", y=").takeWhile { it != ':' }.toInt()
                val bx = line.dropWhile { it != ':' }.removePrefix(": closest beacon is at x=").takeWhile { it != ',' }.toInt()
                val by = line.takeLastWhile { it != '=' }.toInt()
                val s = Point(sx, sy)
                val b = Point(bx, by)
                val distance = s.distanceTo(b)
                Sensor(s, distance)
            }

        val ll = 0
        val ul = 4000000

        val points = sensors
            .runningFold(listOf<Point>()) { acc, sensor ->
                val points = mutableListOf<Point>()
                val yd = max(ll + 1, sensor.coords.y - sensor.range)
                val yu = min(ul - 1, sensor.coords.y + sensor.range)
                for (y in yd until sensor.coords.y) {
                    val distance = abs(sensor.coords.y - y)
                    val ydd = abs(sensor.range - distance)
                    if (ydd > 0) {
                        val dx = sensor.coords.x - ydd
                        if (dx in ll..ul) points.add(Point(sensor.coords.x - ydd, y-1))

                        val ux = sensor.coords.x + ydd
                        if (ux in ll..ul) points.add(Point(sensor.coords.x + ydd, y-1))

                    } else {
                        if (y - 1 in ll .. ul) points.add(Point(sensor.coords.x, y-1))
                    }
                }
                for (y in sensor.coords.y + 1 .. yu) {
                    val distance = abs(sensor.coords.y - y)
                    val ydd = abs(sensor.range - distance)
                    if (ydd > 0) {
                        val dx = sensor.coords.x - ydd
                        if (dx in ll..ul) points.add(Point(sensor.coords.x - ydd, y+1))

                        val ux = sensor.coords.x + ydd
                        if (ux in ll..ul) points.add(Point(sensor.coords.x + ydd, y+1))
                    } else {
                        if (y + 1 in ll .. ul) points.add(Point(sensor.coords.x, y+1))
                    }
                }
                if (sensor.coords.x - sensor.range - 1 in ll .. ul) points.add(Point(sensor.coords.x - sensor.range - 1, sensor.coords.y))
                if (sensor.coords.x + sensor.range + 1 in ll .. ul) points.add(Point(sensor.coords.x + sensor.range + 1, sensor.coords.y))
                points
            }
            .flatten()
            .toSet()

        val res = sensors.fold(points) { acc, sensor ->
            acc.filter { it.distanceTo(sensor.coords) > sensor.range }.toSet()
        }
        val ans: Long = res.first().x.toLong() * ul.toLong() + res.first().y.toLong()
        println(ans)

    }
}

data class Sensor(val coords: Point, val range: Int)

data class Point(val x: Int, val y: Int) {
    fun distanceTo(other: Point): Int {
        return abs(x - other.x) + abs(y - other.y)
    }
}