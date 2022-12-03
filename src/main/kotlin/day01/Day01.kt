package day01

import Day
import kotlin.math.max

object Day01 : Day {

    override fun problem1() {
        var max = Long.MIN_VALUE
        val content = javaClass.getResource("/day01.txt")!!.readText()
        val last = content.lines().fold(0L) { acc, row ->
            if (row.isBlank()) {
                max = max(max, acc)
                0
            } else {
                acc + row.toLong()
            }

        }
        max = max(max, last)


        println("max: $max")
    }

    override fun problem2() {
        val content = javaClass.getResource("/day01.txt")!!.readText()
        val elves = mutableListOf<Long>()
        val last = content.lines().fold(0L) { acc, row ->
            if (row.isBlank()) {
                elves.add(acc)
                0
            } else {
                acc + row.toLong()
            }
        }
        elves.add(last)
        val sum = elves.sorted().takeLast(3).sum()
        println("max 3: $sum")
    }
}