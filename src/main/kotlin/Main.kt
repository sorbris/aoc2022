import day01.Day01
import day02.Day02
import day03.Day03
import day04.Day04
import day05.Day05

private val all = listOf(Day01, Day02, Day03, Day04)
fun main(args: Array<String>) {

    val doAll = false
    if (doAll) {
        doAll()
    } else {
        Day05.problem1()
        Day05.problem2()
    }
}

private fun doAll() {
    all.forEach {
        it.problem1()
        it.problem2()
    }
}
