package day11

import Day

object Day11 : Day {
    override fun problem1() {
        val (_, monkeys) = javaClass.getResource("/day11.txt")!!.readText().parseMonkeys()
        execute(monkeys, { this / 3}, 20)
    }

    override fun problem2() {
        val (mod, monkeys) = javaClass.getResource("/day11.txt")!!.readText().parseMonkeys()
        execute(monkeys, { this.mod(mod) }, 10000)
    }

    private fun execute(monkeys: List<Monkey>, reducer: Long.() -> Long, limit: Int) {
        for (i in 1..limit) {
            for (monkey in monkeys) {
                while (monkey.items.isNotEmpty()) {
                    val item = monkey.items.removeFirst()
                    monkey.inspections++
                    val worry = monkey.operation(item).reducer()
                    val target = monkey.decision(worry)
                    monkeys[target].items.add(worry)
                }
            }
        }
        val sortedMonkeys = monkeys.sortedByDescending { it.inspections }
        println("monkey business level: ${sortedMonkeys[0].inspections * sortedMonkeys[1].inspections}")
    }
}

private fun String.parseMonkeys(): Pair<Long, List<Monkey>> {
    var mod = 1L
    val monkeys = lines()
        .chunked(7)
        .map { rawMonkey ->
            val items = rawMonkey[1].trim().removePrefix("Starting items: ").split(", ").map { it.toLong() }.toMutableList()
            val operation = rawMonkey[2].parseOperation()
            val divider = rawMonkey[3].trim().removePrefix("Test: divisible by ").toLong()
            val test = parseTest(divider, rawMonkey[4], rawMonkey[5])
            mod *= divider
            Monkey(items, operation, test)
        }
    return mod to monkeys
}

private fun parseTest(divider: Long, case1: String, case2: String): (Long) -> Int {
    val target1 = case1.trim().removePrefix("If true: throw to monkey ").toInt()
    val target2 = case2.trim().removePrefix("If false: throw to monkey ").toInt()
    return { input ->
        if (input.rem(divider) == 0L) {
            target1
        } else {
            target2
        }
    }
}
private fun String.parseOperation(): (Long) -> Long {
    return trim().removePrefix("Operation: new = old ").let {
        val target = it.drop(1).trim()
        when {
            it.startsWith("+") -> {
                if (target == "old") {
                    { input -> input + input }
                } else {
                    { input -> input + target.toLong() }
                }
            }
            it.startsWith("*") -> {
                if (target == "old") {
                    { input -> input * input }
                } else {
                    { input -> input * target.toLong() }
                }
            }
            else -> throw IllegalArgumentException("cant parse: $this")
        }
    }
}
private class Monkey(val items: MutableList<Long>, val operation: (Long) -> Long, val decision: (Long) -> Int, var inspections: Long = 0)