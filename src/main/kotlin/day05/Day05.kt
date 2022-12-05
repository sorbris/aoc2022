package day05

import Day
import java.util.regex.Pattern

object Day05 : Day {
    override fun problem1() {
        val stacks = mutableMapOf<Int, MutableList<Char>>()
        val moves = mutableListOf<Move>()
        javaClass.getResource("/day05.txt")!!.readText().lines().forEach {
            if (it.startsWith("move")) {
                parseMove(it)?.let { move ->
                    moves.add(move)
                }
            } else if (it.isNotEmpty()) {
                parseRow(it, stacks)
            }
        }
        for (move in moves) {
            for (i in 1..move.count) {
                val c = stacks[move.from]!!.removeLast()
                stacks[move.to]!!.add(c)
            }
        }
        val sb = StringBuilder()
        stacks.entries.sortedWith { o1, o2 -> o1.key.compareTo(o2.key) }.forEach {
            sb.append(it.value.last())
        }
        println(sb.toString())
    }

    private fun parseRow(input: String, stacks: MutableMap<Int, MutableList<Char>>) {
        val chars = input.toCharArray()
        var index = 0
        var stack = 1
        while (index < chars.lastIndex) {
            if (chars[index] == '[') {
                val crate = chars[++index]
                (stacks[stack] ?: mutableListOf()).let {
                    it.add(0, crate)
                    return@let it
                }.apply {
                    stacks[stack] = this
                }
                index += 3
            } else {
                index += 4
            }
            stack++
        }
    }

    private val movePattern = Pattern.compile("move (\\d+) from (\\d+) to (\\d+)")
    private fun parseMove(input: String): Move? {
        val matcher = movePattern.matcher(input)
        if (matcher.matches()) {
            return Move(matcher.group(1).toInt(), matcher.group(2).toInt(), matcher.group(3).toInt())
        }
        return null
    }
    data class Move(val count: Int, val from: Int, val to: Int)

    override fun problem2() {
        val stacks = mutableMapOf<Int, MutableList<Char>>()
        val moves = mutableListOf<Move>()
        javaClass.getResource("/day05.txt")!!.readText().lines().forEach {
            if (it.startsWith("move")) {
                parseMove(it)?.let { move ->
                    moves.add(move)
                }
            } else if (it.isNotEmpty()) {
                parseRow(it, stacks)
            }
        }
        for (move in moves) {
            val l = stacks[move.from]!!
            stacks[move.from] = l.dropLast(move.count).toMutableList()
            stacks[move.to]!!.addAll(l.takeLast(move.count))
        }
        val sb = StringBuilder()
        stacks.entries.sortedWith { o1, o2 -> o1.key.compareTo(o2.key) }.forEach {
            sb.append(it.value.last())
        }
        println(sb.toString())
    }
}