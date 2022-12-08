import day01.Day01
import day02.Day02
import day03.Day03
import day04.Day04
import day05.Day05
import day06.Day06
import day07.Day07
import day08.Day08

private val all = listOf(Day01, Day02, Day03, Day04)
fun main(args: Array<String>) {

    val doAll = false
    if (doAll) {
        doAll()
    } else {
        Day08.problem1()
        Day08.problem2()
    }
}

private fun doAll() {
    all.forEach {
        it.problem1()
        it.problem2()
    }
}
