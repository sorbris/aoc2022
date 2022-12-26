package day17

import Day
import kotlin.math.max
import kotlin.math.min

object Day17 : Day {

    private val rocks = listOf(
        Rock(intArrayOf(1, 1, 1, 1), intArrayOf(0, 0, 0, 0)),
        Rock(intArrayOf(2, 3, 2), intArrayOf(-1, 0, -1)),
        Rock(intArrayOf(1, 1, 3), intArrayOf(0, 0, 0)),
        Rock(intArrayOf(4), intArrayOf(0)),
        Rock(intArrayOf(2, 2), intArrayOf(0, 0))
    )
    override fun problem1() {
        val chamber = IntArray(7) { 0 }
        var dropY = 4

        val pattern = javaClass.getResource("/day17-test.txt")!!.readText().map {
            if (it == '<') -1
            else 1
        }
        val winds = Winds(pattern)
        var maxY = 0
        for (i in 0..3) {
            dropRock(rocks[i.mod(rocks.size)], dropY, chamber, winds)
            maxY = chamber.fold(Integer.MIN_VALUE) { acc, h ->
                max(acc, h)
            }
            dropY = maxY + 4
        }
        println("max: $maxY")
    }

    private fun dropRock(rock: Rock, y: Int, chamber: IntArray, winds: Winds) {
        var ry = y
        var rx = 2
        var resting = false
        while (!resting) {
            val direction = winds.getNext()
            rx += direction
            rx = max(rx, 0)
            rx = min(rx, 7 - rock.width())
            resting = checkHitbox(rock, rx, ry - 1, chamber)
            --ry
            if (resting) {
                for (i in rock.heights.indices) {
                    chamber[rx + i] = ry + rock.heights[i]
                }
            }
        }
    }

    private fun checkHitbox(rock: Rock, x: Int, y: Int, chamber: IntArray): Boolean {
        for (i in rock.hitbox.indices) {
            if (y + rock.hitbox[i] == chamber[x + i]) {
                return true
            }
        }
        return false
    }

    override fun problem2() {

    }
}

data class Winds(val pattern: List<Int>, private var i: Int = 0) {
    fun getNext(): Int {
        val n = pattern[i++]
        i = i.mod(pattern.size)
        return n
    }
}
@Suppress("ArrayInDataClass")
private data class Rock(val heights: IntArray, val hitbox: IntArray) {
    fun width() = heights.size
}