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
        lateinit var first: Valve

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
        traverse(valves.first(), nonZeroes)
    }

    private fun traverse(start: Valve, openable: Set<Valve>) {

        val queue = ArrayDeque<Target>()
        queue.add(Target(start, 30, 0, openable))
        var max = Integer.MIN_VALUE
        while (queue.isNotEmpty()) {
            val next = queue.removeFirst()
            val m = stuff(next.valve, next.timeRemaining, next.accumulatedPressure, next.opened, queue)
            max = max(max, m)
        }
        println("max: $max")
    }

    private fun stuff(current: Valve, time: Int, pressure: Int, openable: Set<Valve>, queue: ArrayDeque<Target>): Int {
        for (valve in openable) {
            val timeToOpen = current.distances[valve.name]!! + 1

            if (timeToOpen < time) {
                val timeleft = time - timeToOpen
                queue.addLast(Target(valve, timeleft, pressure + timeleft * valve.flowRate, buildSet {
                    addAll(openable)
                    remove(valve)
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


    override fun problem2() {
        javaClass.getResource("/day16-test.txt")!!.readText()
    }
}

private data class Target(val valve: Valve, val timeRemaining: Int, val accumulatedPressure: Int, val opened: Set<Valve>)

private data class Valve(val name: String, val flowRate: Int, val tunnels: List<String>, val distances: MutableMap<String, Int> = mutableMapOf())