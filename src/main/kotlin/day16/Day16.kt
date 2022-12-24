package day16

import Day
import java.util.*
import java.util.regex.Pattern
import kotlin.Comparator
import kotlin.collections.ArrayDeque
import kotlin.math.max
import kotlin.math.min

object Day16 : Day {

    private val pattern = Pattern.compile("Valve ([A-Z][A-Z]) has flow rate=(\\d+); tunnels? leads? to valves? ([A-ZA-Z[,\\s]*]+)") //
    override fun problem1() {
        val valves = javaClass.getResource("/day16.txt")!!.readText()
            .lines()
            .map {
                val matcher = pattern.matcher(it)
                assert(matcher.matches())
                Valve(matcher.group(1), matcher.group(2).toInt(), matcher.group(3).split(", "))
            }

        val valveMap = valves
            .fold(mutableMapOf<String, Valve>()) { acc, valve ->
                acc[valve.name] = valve
                acc
            }

        valves.forEach {
            getDistances(it, valveMap)
        }

        val nonZeroes = valves.filter { it.flowRate > 0 }.toSet()
        traverse(valves.find { it.name == "AA"}!!, nonZeroes, 30)
    }

    override fun problem2() {
        val valves = javaClass.getResource("/day16.txt")!!.readText()
            .lines()
            .map {
                val matcher = pattern.matcher(it)
                assert(matcher.matches())
                Valve(matcher.group(1), matcher.group(2).toInt(), matcher.group(3).split(", "))
            }

        val valveMap = valves
            .fold(mutableMapOf<String, Valve>()) { acc, valve ->
                acc[valve.name] = valve
                acc
            }

        valves.forEach {
            getDistances(it, valveMap)
        }

        val nonZeroes = valves.filter { it.flowRate > 0 }.toSet()
        traverse(valves.find { it.name == "AA"}!!, nonZeroes, 26, true)
    }

    private fun traverse(start: Valve, openable: Set<Valve>, time: Int, two: Boolean = false) {
        val keys = mutableMapOf<String, Long>()
        var k = 1L
        openable.forEachIndexed { i, v ->
            val key = k.shl(i)
            keys[v.name] = key
        }
        val scores = mutableMapOf<Long, Int>()
        val queue = ArrayDeque<Target>()
        queue.add(Target(start, time, 0, openable, emptySet()))
        var max = Integer.MIN_VALUE
        while (queue.isNotEmpty()) {
            val next = queue.removeFirst()
            val m = visit(next.valve, next.timeRemaining, next.accumulatedPressure, next.openable, queue, next.visited)
            val key = getKey(next.visited, next.valve, keys)
            max = max(m, max)
            scores[key] = max(m, scores[key] ?: Integer.MIN_VALUE)
        }
        if (two) {
            max = findBestCombo(scores)
        }
        println("max: $max")
    }

    private fun findBestCombo(scores: Map<Long, Int>): Int {
        var keys = scores.keys.toList()
        var max = Integer.MIN_VALUE
        for (i in keys.indices - 1) {
            val f = keys[i]
            for (j in i + 1 .. keys.lastIndex) {
                val s = keys[j]
                if (s and f == 0L) {
                    max = max(max, scores[f]!! + scores[s]!!)
                }
            }
        }
        return max
    }

    private fun getKey(visited: Set<Valve>, current: Valve, translation: Map<String, Long>): Long {
        var key = translation[current.name] ?: 0

        for (v in visited) {
            key = key or translation[v.name]!!
        }

        return key
    }

    private fun visit(current: Valve, time: Int, pressure: Int, openable: Set<Valve>, queue: ArrayDeque<Target>, visited: Set<Valve>): Int {
        for (valve in openable) {
            val timeToOpen = current.distances[valve.name]!! + 1

            if (timeToOpen < time) {
                val timeleft = time - timeToOpen
                queue.addLast(Target(valve, timeleft, pressure + timeleft * valve.flowRate, buildSet {
                    addAll(openable)
                    remove(valve)
                }, buildSet {
                    addAll(visited)
                    add(valve)
                }))
            }
        }
        return pressure
    }

    private fun getDistances(current: Valve, valves: Map<String, Valve>) {
        val visited = setOf(current)
        val queue = ArrayDeque<Triple<Valve, Set<Valve>, Int>>()
        for (key in current.tunnels) {
            val valve = valves[key]!!
            queue.add(Triple(valve, visited, 0))
        }
        while (queue.isNotEmpty()) {
            val next = queue.removeFirst()
            val distance = visit(next.first, next.second, next.third, queue, valves)
            val cd = current.distances[next.first.name] ?: Integer.MAX_VALUE
            current.distances[next.first.name] = min(distance, cd)
        }
    }

    private fun visit(valve: Valve, visited: Set<Valve>, distanceTraveled: Int, queue: ArrayDeque<Triple<Valve, Set<Valve>, Int>>, all: Map<String, Valve>): Int {
        val traveled = distanceTraveled + 1
        val updatedVisited = buildSet {
            addAll(visited)
            add(valve)
        }
        for (key in valve.tunnels) {
            val next = all[key]!!
            if (!updatedVisited.contains(next)) {
                queue.addLast(Triple(next, updatedVisited, traveled))
            }
        }
        return traveled
    }
}

private data class Target(val valve: Valve, val timeRemaining: Int, val accumulatedPressure: Int, val openable: Set<Valve>, val visited: Set<Valve>)

private data class Valve(val name: String, val flowRate: Int, val tunnels: List<String>, val distances: MutableMap<String, Int> = mutableMapOf())