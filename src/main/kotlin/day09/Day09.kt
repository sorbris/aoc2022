package day09

import Day
import kotlin.math.abs

object Day09 : Day {

    override fun problem1() {
        val places = countPlaces(javaClass.getResource("/day09.txt")!!.readText(), 1)
        println("$places visited")
    }

    override fun problem2() {
        val places = countPlaces( javaClass.getResource("/day09.txt")!!.readText(), 9)
        println("problem2: $places")
    }

    private fun countPlaces(input: String, tails: Int): Int {
        return input.lineSequence()
            .map { it.split(" ") }
            .map { it.first() to it.last().toInt() }
            .map { cmd -> (1..cmd.second).map { cmd.first } }
            .flatten()
            .runningFold((0 .. tails).map { 0 to 0 }) { acc, action ->
                var (x, y) = acc.first()
                when (action) {
                    "U" -> x += 1
                    "D" -> x -= 1
                    "L" -> y -= 1
                    "R" -> y += 1
                }
                acc.drop(1).runningFold(x to y) { head, tail ->
                    val dx = head.first - tail.first
                    val dy = head.second - tail.second
                    if (abs(dx) > 1 || abs(dy) > 1) {
                        tail.first + Integer.signum(dx) to tail.second + Integer.signum(dy)
                    } else {
                        tail
                    }
                }
            }.map {
                it.last()
            }.toSet().size
    }
}