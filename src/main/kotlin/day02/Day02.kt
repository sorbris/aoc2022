package day02

import Day

object Day02 : Day {
    override fun problem1() {
        val results = javaClass.getResource("/day02.txt")!!.readText().lines().map {
            val moves = it.split(" ")
            score(ops1[moves[0]]!!, ops1[moves[1]]!!)
        }
        val score = results.fold(0) { acc, points ->
            acc + points
        }
        println("score is: $score")
    }

    private val ops1 = mapOf("A" to 0, "B" to 1, "C" to 2, "X" to 0, "Y" to 1, "Z" to 2)

    private fun score(opMove: Int, myMove: Int): Int {
        return if (opMove == myMove) {
            myMove + 1 + 3
        } else if ((opMove + 1).mod(3) == myMove) {
            myMove + 1 + 6
        } else {
            myMove + 1
        }
    }

    override fun problem2() {
        val results = javaClass.getResource("/day02.txt")!!.readText().lines().map {
            val moves = it.split(" ")
            val opMove = ops2[moves[0]]!!
            val myMove = (opMove + ops2[moves[1]]!!).mod(3)
            score(opMove, myMove)
        }
        val score = results.fold(0) { acc, points ->
            acc + points
        }
        println("score is: $score")
    }
    private val ops2 = mapOf("A" to 0, "B" to 1, "C" to 2, "X" to 2, "Y" to 0, "Z" to 1)
}