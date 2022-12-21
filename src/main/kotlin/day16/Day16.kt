package day16

import Day
import java.util.*
import java.util.regex.Pattern
import kotlin.Comparator
import kotlin.collections.ArrayDeque
import kotlin.math.max

object Day16 : Day {

    private val pattern = Pattern.compile("Valve ([A-Z][A-Z]) has flow rate=(\\d+); tunnels? leads? to valves? ([A-ZA-Z[,\\s]*]+)") //
    override fun problem1() {
        lateinit var first: Valve
        var potential: Int = 0
        val valves = javaClass.getResource("/day16.txt")!!.readText()
            .lines()
            .map {
                val matcher = pattern.matcher(it)
                assert(matcher.matches())
                Valve(matcher.group(1), matcher.group(2).toInt(), matcher.group(3).split(", "))
            }
            .apply {
                first = first()
            }
            .fold(mutableMapOf<String, Valve>()) { acc, valve ->
                potential += valve.flowRate
                acc[valve.name] = valve
                acc
            }

        val max = traverse(first, valves)
        println("max: $max")
    }

    private fun traverse(start: Valve, valves: Map<String, Valve>): Int {
        val queue = ArrayDeque<Target>()
        queue.add(Target(30, 0, buildSet {
            addAll(valves.filter { it.value.flowRate == 0 }.values)
        }, start, setOf()))
        var maxPressure = Integer.MIN_VALUE
        while (queue.isNotEmpty()) {
            val next = queue.removeFirst()
            if (next.time > 0) {
                val pressure = visit(next.time, next.accumulatedPressure, next.path, next.valve, valves, queue, next.visitedSinceOpening)
                maxPressure = max(maxPressure, pressure)
            }
        }
        return maxPressure
    }

    private fun visit(time: Int, accumulatedPressure: Int, opened: Set<Valve>, current: Valve, valves: Map<String, Valve>, queue: ArrayDeque<Target>, visitedSinceOpening: Set<Valve>): Int {
        if (opened.size == valves.size) return accumulatedPressure
        for (tunnel in current.tunnels) {
            val valve = valves[tunnel]
            if (valve != null && time > 0 && !visitedSinceOpening.contains(valve)) {
                queue.addLast(
                    Target(
                        time - 1,
                        accumulatedPressure,
                        opened,
                        valve,
                        buildSet {
                            addAll(visitedSinceOpening)
                            add(current)
                        }
                    )
                )
            }
        }
        if (!opened.contains(current)) {
            val openedTime = time - 1
            val nacc = accumulatedPressure + (openedTime * current.flowRate)
            if (openedTime > 0) {
                for (tunnel in current.tunnels) {
                    val valve = valves[tunnel]
                    if (valve != null) {
                        queue.addLast(
                            Target(
                                openedTime - 1,
                                nacc,
                                buildSet {
                                    addAll(opened)
                                    add(current)
                                },
                                valve,
                                setOf()
                            )
                        )
                    }
                }
            }
            return nacc
        }
        return accumulatedPressure
    }

    override fun problem2() {
        javaClass.getResource("/day16-test.txt")!!.readText()
    }
}

private data class Target(val time: Int, val accumulatedPressure: Int, val path: Set<Valve>, val valve: Valve, val visitedSinceOpening: Set<Valve>)

private data class Valve(val name: String, val flowRate: Int, val tunnels: List<String>)