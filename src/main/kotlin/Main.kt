import day01.Day01
import day02.Day02
import day03.Day03
import day04.Day04
import day05.Day05
import day06.Day06
import day07.Day07
import day08.Day08
import day09.Day09
import day10.Day10
import day11.Day11
import day12.Day12
import day13.Day13
import day14.Day14
import day15.Day15
import day16.Day16
import day17.Day17
import java.util.concurrent.TimeUnit
import kotlin.system.measureTimeMillis

private val all = listOf(Day01, Day02, Day03, Day04, Day05, Day06, Day07, Day08, Day09, Day10, Day11, Day13, Day14, Day15)
fun main(args: Array<String>) {

    val doAll = false
    if (doAll) {
        doAll()
    } else {
        Day17.problem1()
        Day17.problem2()
    }
}

private fun doAll() {
    all.map {
        println("===${it.javaClass.simpleName}===")
        val time = measureTimeMillis {
            it.problem1()
            it.problem2()
        }
        it to time
    }.sortedBy { it.second }
        .also {
            println()
            println("=== Days ordered by execution time ===")
        }.forEachIndexed { index, pair ->
            println("${index + 1}. ${pair.first.javaClass.simpleName} - ${pair.second} ms")
        }
}
