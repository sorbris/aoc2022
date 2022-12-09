package day09

import Day
import kotlin.math.abs

object Day09 : Day {
    override fun problem1() {
        val places = mutableSetOf<Pair<Int, Int>>()
        javaClass.getResource("/day09.txt")!!.readText().lineSequence()
            .map { it.split(" ") }
            .map { it.first() to it.last().toInt() }
            .fold((0 to 0) to (0 to 0)) { acc, action ->
                val (head, tail) = acc
                var (hx, hy) = head
                var (tx, ty) = tail
                val (dir, steps) = action
                for (i in 1..steps) {
                    when (dir) {
                        "U" -> hx += 1
                        "D" -> hx -= 1
                        "L" -> hy -= 1
                        "R" -> hy += 1
                    }
                    val adjusted = adjustPosition(hx to hy, tx to ty)
                    tx = adjusted.first
                    ty = adjusted.second
                    places.add(tx to ty)
                }

                (hx to hy) to (tx to ty)
            }
        println("${places.size} visited")
    }

    private fun adjustPosition(head: Pair<Int, Int>, tail: Pair<Int, Int>): Pair<Int, Int> {
        val dx = head.first - tail.first
        val dy = head.second - tail.second
        return if (abs(dx) > 1 || abs(dy) > 1) {
            tail.first + Integer.signum(dx) to tail.second + Integer.signum(dy)
        } else {
            tail
        }
    }

    override fun problem2() {
        val places = javaClass.getResource("/day09.txt")!!.readText().lineSequence()
            .map { it.split(" ") }
            .map { it.first() to it.last().toInt() }
            .map { cmd -> (1..cmd.second).map { cmd.first } }
            .flatten()
            .runningFold((1 .. 10).map { 0 to 0 }) { acc, action ->
                var (x, y) = acc.first()
                when (action) {
                    "U" -> x += 1
                    "D" -> x -= 1
                    "L" -> y -= 1
                    "R" -> y += 1
                }
                acc.drop(1).runningFold(x to y) { head, tail ->
                    adjustPosition(head, tail)
                }
            }.map {
                it.last()
            }.toSet().size

        println("problem2: $places")
    }
}