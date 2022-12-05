package day04

import Day

object Day04 : Day {
    override fun problem1() {
        var count = 0
        javaClass.getResource("/day04.txt")!!.readText().lines().asSequence()
            .map {
                val pairs = it.split(",")
                val ranges = pairs.map {
                    val indices = it.split("-")
                    return@map (indices.first().toInt())..indices.last().toInt()
                }
                return@map ranges
            }.forEach {
                assert(it.size == 2)
                if (it.first().contains(it.last())) count++
                else if (it.last().contains(it.first())) count++
            }

        println("count : $count")
    }

    private fun IntRange.contains(other: IntRange): Boolean {
        if (first <= other.first && last >= other.last) {
            return true
        }
        return false
    }

    private fun IntRange.overlaps(other: IntRange): Boolean {
        if (other.isEmpty()) return false
        if (contains(other.start)) return true
        if (contains(other.last)) return true
        return false
    }

    override fun problem2() {
        var count = 0
        javaClass.getResource("/day04.txt")!!.readText().lines().asSequence()
            .map {
                val pairs = it.split(",")
                val ranges = pairs.map {
                    val indices = it.split("-")
                    return@map (indices.first().toInt())..indices.last().toInt()
                }
                return@map ranges
            }.forEach {
                assert(it.size == 2)
                if (it.first().overlaps(it.last())) count++
                else if (it.last().overlaps(it.first())) count++
            }

        println("count : $count")
    }
}