package day03

import Day

object Day03 : Day {
    override fun problem1() {
        var sums = 0
        javaClass.getResource("/day03.txt")!!.readText().lines().asSequence().map { it.toCharArray() }
            .forEach { array ->
                val half = array.size/2
                val matches = mutableSetOf<Char>()
                for (i in array.size/2..array.lastIndex) {
                    val char = array[i]
                    for (j in 0 until half) {
                        if (array[j] == char) {
                            matches.add(char)
                            break
                        }
                    }
                }

                for (char in matches) {
                    sums += if (char.isLowerCase()) char - '`' else char - '@' + 26
                }
            }
        println("sum $sums")
    }

    override fun problem2() {
        val badges = mutableListOf<Char>()
        javaClass.getResource("/day03.txt")!!.readText().lines().asSequence().windowed(3, 3, true).forEach {  members ->
            for (c in members[0]) {
                if (members[1].contains(c, ignoreCase = false) && members[2].contains(c, ignoreCase = false)) {
                    badges.add(c)
                    break
                }
            }
        }
        val sum = badges.fold(0) {acc, char ->
            acc + if (char.isLowerCase()) { char - '`' } else { char - '@' + 26 }
        }
        println("Sum: $sum")
    }
}