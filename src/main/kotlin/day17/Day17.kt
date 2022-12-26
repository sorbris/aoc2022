package day17

import Day
import java.util.Objects
import kotlin.math.max
import kotlin.math.min

object Day17 : Day {

    private val rocks = listOf(
        Rock(listOf(booleanArrayOf(true), booleanArrayOf(true), booleanArrayOf(true), booleanArrayOf(true))),
        Rock(listOf(booleanArrayOf(false, true, false), booleanArrayOf(true, true, true), booleanArrayOf(false, true, false))),
        Rock(listOf(booleanArrayOf(true, false, false), booleanArrayOf(true, false, false), booleanArrayOf(true, true, true))),
        Rock(listOf(booleanArrayOf(true, true, true, true))),
        Rock(listOf(booleanArrayOf(true, true), booleanArrayOf(true, true)))
    )
    override fun problem1() {
        val pattern = javaClass.getResource("/day17.txt")!!.readText().map {
            if (it == '<') -1
            else 1
        }
        val winds = Winds(pattern)
        val chamber = listOf(
            RockColumn(),
            RockColumn(),
            RockColumn(),
            RockColumn(),
            RockColumn(),
            RockColumn(),
            RockColumn(),
        )
        var y = 3

        val fulls = mutableListOf<Int>()
        for (i in 0..2021) {
            val index = i.mod(rocks.size)
            val (ny, fy) = dropRock(rocks, index, y, chamber, winds)
            y = ny + 3
            if (fy != -1) fulls.add(fy)
        }
        println("max: ${y - 3}")
    }

    private fun dropRock(rocks: List<Rock>, rockIndex: Int, y: Int, chamber: List<RockColumn>, winds: Winds, hashes: MutableList<Int>? = null): Pair<Int, Int> {
        val rock = rocks[rockIndex]
        var ry = y
        var rx = 2
        var resting = false
        if (hashes != null) {
            val hash = Objects.hash(winds.i, rockIndex, chamber.map { it[y - 3] })
            hashes.add(hash)
        }
        while (!resting) {
            val direction = winds.getNext()
            // first check if we are inside the bounds
            if (rx + direction >= 0 && rx + direction <= 6 - rock.cols.lastIndex) {
                // if still within three steps just move
                if (y - ry < 3) {
                    rx += direction
                } else {
                    var blocked = false
                    outer@ for (i in rock.cols.indices) {
                        for (j in rock.cols[i].indices) {
                            if (chamber[rx + direction + i][ry + j].and(rock.cols[i][j])) {
                                blocked = true
                                break@outer
                            }
                        }
                    }
                    if (!blocked) rx += direction

                }
            }

            // Now check if we will rest
            if (ry - 1 < 0) {
                resting = true
            } else if (y - ry >= 3){
                outer@for(i in rock.cols.indices) {
                    for (j in rock.cols[i].indices) {
                        if (chamber[rx + i][ry - 1 + j].and(rock.cols[i][j])) {
                            resting = true
                            break@outer
                        }
                    }
                }
            }

            if (resting) {
                for (i in rock.cols.indices) {
                    for (j in rock.cols[i].indices) {
                        chamber[rx + i][ry + j] = chamber[rx + i][ry + j] or rock.cols[i][j]
                    }
                }
            } else {
                ry--
            }
        }

        var found = -1
        for (i in rock.cols.first().indices) {
            if (chamber.all { it[i + ry] }) found = i + ry
        }
        return max(y - 3, ry + rock.cols.first().size) to found
    }

    override fun problem2() {
        val pattern = javaClass.getResource("/day17.txt")!!.readText().map {
            if (it == '<') -1
            else 1
        }
        val winds = Winds(pattern)
        var chamber = createChamber()
        var y = 3


        val hashes = mutableListOf<Int>()
        var saved = 0L

        var patternFound = false
        for (i in 0..2021) {
            val index = i.mod(rocks.size)
            val (ny, fy) = dropRock(rocks, index, y, chamber, winds, hashes)
            if (fy != -1) {
                saved += fy
                y = fy + 4
                chamber = createChamber()
            } else {
                y = ny + 3
            }

        }
        println("max: ${(saved + y) - 3}")



    }

    private fun createChamber() = mutableListOf(
        RockColumn(),
        RockColumn(),
        RockColumn(),
        RockColumn(),
        RockColumn(),
        RockColumn(),
        RockColumn(),
    )
}

class RockColumn {
    private val map = mutableMapOf<Int, Boolean>()

    operator fun get(index: Int) = map[index] ?: false
    operator fun set(index: Int, value: Boolean) {
        map[index] = value
    }
}

data class Winds(val pattern: List<Int>, var i: Int = 0) {
    fun getNext(): Int {
        val n = pattern[i++]
        i = i.mod(pattern.size)
        return n
    }
}
@Suppress("ArrayInDataClass")
private data class Rock(val cols: List<BooleanArray>) {
    fun width() = cols.size
}

private class RestingRock(val x: Int, val y: Int, rock: Rock)