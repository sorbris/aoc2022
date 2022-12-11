package day11

import Day

object Day11 : Day {
    override fun problem1() {

        val monkeys = javaClass.getResource("/day11.txt")!!.readText()
            .lines()
            .chunked(7)
            .map { rawMonkey ->
                val name = rawMonkey.first().removeSuffix(":")
                val items = rawMonkey[1].trim().removePrefix("Starting items: ").split(", ").map { it.toInt() }.toMutableList()
                val operation = rawMonkey[2].parseOperation()
                val test = parseTest(rawMonkey[3], rawMonkey[4], rawMonkey[5])
                Monkey(name, items, operation, test)
            }
        println("${monkeys.size} monkeys")
        for (i in 0..19) {
            for (monkey in monkeys) {
                while (monkey.items.isNotEmpty()) {
                    val item = monkey.items.removeFirst()
                    monkey.inspections++
                    val worry = monkey.operation(item) / 3
                    val target = monkey.decision(worry)
                    monkeys[target].items.add(worry)
                }
            }
        }
        val sortedMonkeys = monkeys.sortedByDescending { it.inspections }
        println("monkey business level: ${sortedMonkeys[0].inspections * sortedMonkeys[1].inspections}")
    }

    override fun problem2() {

    }
}

private fun parseTest(test: String, case1: String, case2: String): (Int) -> Int {
    val divider = test.trim().removePrefix("Test: divisible by ").toInt()
    val target1 = case1.trim().removePrefix("If true: throw to monkey ").toInt()
    val target2 = case2.trim().removePrefix("If false: throw to monkey ").toInt()
    return { input ->
        if (input.rem(divider) == 0) {
            target1
        } else {
            target2
        }
    }
}
private fun String.parseOperation(): (Int) -> Int {
    return trim().removePrefix("Operation: new = old ").let {
        val target = it.drop(1).trim()
        when {
            it.startsWith("+") -> {
                if (target == "old") {
                    { input -> input + input }
                } else {
                    { input -> input + target.toInt() }
                }
            }
            it.startsWith("*") -> {
                if (target == "old") {
                    { input -> input * input }
                } else {
                    { input -> input * target.toInt() }
                }
            }
            else -> throw IllegalArgumentException("cant parse: $this")
        }
    }
}
private class Monkey(val name: String, val items: MutableList<Int>, val operation: (Int) -> Int, val decision: (Int) -> Int, var inspections: Int = 0) {

}