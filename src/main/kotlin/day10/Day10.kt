package day10

import Day

object Day10 : Day {
    override fun problem1() {
        javaClass.getResource("/day10.txt")!!.readText().execute()
            .mapIndexed { index, i ->
                (index + 1) * i
            }
            .filterIndexed { index, _ ->
                (index + 1).mod(40) == 20
            }
            .sum()
            .run { println("sum: $this") }

    }

    override fun problem2() {
        javaClass.getResource("/day10.txt")!!.readText().execute()
            .foldIndexed("") { index, acc, i ->
                val sprite = i - 1 until i + 2
                if (sprite.contains(index.mod(40))) "$acc█" else "$acc "
            }
            .chunked(40)
            .forEach { println(it) }
    }

    fun String.execute(): Sequence<Int> =
        this.lineSequence()
            .map {
                when {
                    it == "noop" -> listOf(0)
                    it.startsWith("addx") -> listOf(0, it.removePrefix("addx ").toInt())
                    else -> throw IllegalArgumentException("wtf is $it")
                }
            }
            .flatten()
            .runningFold(1) { acc, change ->
                (acc + change)
            }
}