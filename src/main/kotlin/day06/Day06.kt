package day06

import Day

object Day06 : Day {
    override fun problem1() {
        val array = javaClass.getResource("/day06.txt")!!.readText().toCharArray()
        for (i in 0..array.lastIndex - 4) {
            val a = array[i]
            val b = array[i+1]
            val c = array[i+2]
            val d = array[i+3]
            val set = mutableSetOf(a, b, c, d)
            if (set.size == 4) {
                println("found at ${i + 4}")
                break
            }

        }
    }
    override fun problem2() {
        val array = javaClass.getResource("/day06.txt")!!.readText().toCharArray()

        for (i in 0..array.lastIndex - 4) {
            val set = mutableSetOf<Char>()
            for (j in 0..13) {
                set.add(array[i + j])
            }
            if (set.size == 14) {
                println("found at ${i + 14}")
                break
            }

        }
    }
}