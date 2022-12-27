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
            y = dropRock(rocks, index, y, chamber, winds) + 3
        }
        println("max: ${y - 3}")
    }

    private fun dropRock(rocks: List<Rock>, rockIndex: Int, y: Int, chamber: List<RockColumn>, winds: Winds, hashes: MutableList<Pair<Int, Int>>? = null): Int {
        val rock = rocks[rockIndex]
        var ry = y
        var rx = 2
        var resting = false

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

        val res = max(y - 3, ry + rock.cols.first().size)
        if (hashes != null) {
            val hash = Objects.hash(winds.i, rockIndex, chamber.map { it[y - 3] })
            hashes.add(hash to res)
        }
        if (hashes != null && rockIndex == 0)
        println("total: ${hashes.size}, rockIndex: $rockIndex, wind: ${winds.i}, height: $res")
        return res
    }

    override fun problem2() {
        val pattern = javaClass.getResource("/day17.txt")!!.readText().map {
            if (it == '<') -1
            else 1
        }
        val winds = Winds(pattern)
        val chamber = createChamber()
        var y = 3


        val hashes = mutableListOf<Pair<Int, Int>>()

        for (i in 0 until  10000) {
            val index = i.mod(rocks.size)
            y = dropRock(rocks, index, y, chamber, winds, hashes) + 3
        }

        var start = -1
        var step = -1
        var height = -1
//        outer@for (i in IntProgression.fromClosedRange(hashes.lastIndex, 1, -5)) {
//            val target = hashes[i].first
//
//            for (j in IntProgression.fromClosedRange(i - 5, 0, -5)) {
//                if (hashes[j].first == target) {
//                    start = i
//                    step = i - j
//                    height = hashes[i].second - hashes[j].second
//                    break@outer
//                }
//            }
//        }
        outer@for (i in IntProgression.fromClosedRange(0, hashes.lastIndex, 5)) {
            for (j in IntProgression.fromClosedRange(i + 5, hashes.lastIndex, 5)) {
                val target = hashes[i].first
                if (hashes[j].first == target) {
                    start = i
                    step = j - i
                    height = hashes[j].second - hashes[i].second
                    break@outer
                }
            }
        }



//        outer@for (i in hashes.lastIndex downTo 1) {
//            val target = hashes[i].first
//            for (j in i - 1 downTo 0) {
//                if (hashes[j].first == target) {
//                    start = i
//                    step = i - j
//                    height = hashes[i].second - hashes[j].second
//                    break@outer
//                }
//            }
//        }

        println("start: $start")
        println("step: $step")
        println("height: $height")

        val l = 1000000000000L - start
        var rockCount: Long = hashes[start - 1].second.toLong()
        val d = l/step.toLong()
        println("calculated = ${rockCount + (d*height)}")



        var steps: Long = start.toLong()
        while (steps + step < 1000000000000L - 1) {

            steps += step
            rockCount += height
        }

        val left = 1000000000000L - 1 - steps

        println("steps left: $left")
        println("count so far: $rockCount")
        println("diff = ${1514285714288 - rockCount}")

        println("startheight: ${hashes[start].second}")
        println("startheight2: ${hashes[start + left.toInt()].second}")
        val hd = hashes[start + left.toInt()].second - hashes[start].second
        println("heightdiff = $hd")
        rockCount += (hashes[start + left.toInt()].second - hashes[start].second)
        println("after addition: $rockCount")
        println("diff = ${1514285714288 - rockCount}")
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
private data class Rock(val cols: List<BooleanArray>)