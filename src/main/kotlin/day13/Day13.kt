package day13

import Day
import java.lang.StringBuilder

object Day13 : Day {
    override fun problem1() {
        javaClass.getResource("/day13.txt")!!.readText().lineSequence()
            .filter { it.isNotBlank() }
            .chunked(2)
            .map {pair ->
                compare(Parser(pair.first()).parseElement(), Parser(pair.last()).parseElement())
            }
            .mapIndexed { index, i ->
                if (i == -1) index + 1 else 0
            }
            .sum().let {
                println("sum: $it")
            }
    }

    override fun problem2() {

        val list = javaClass.getResource("/day13.txt")!!.readText().lineSequence()
            .filter { it.isNotBlank() }
            .map {
                Parser(it).parseElement()
            }.toMutableList()
        val p1 = Parser("[[2]]").parseElement()
        val p2 = Parser("[[6]]").parseElement()
        list.add(p1)
        list.add(p2)
        list.sortWith { lhs, rhs ->
            compare(lhs, rhs)
        }
        val i1 = list.indexOf(p1) + 1
        val i2 = list.indexOf(p2) + 1
        println("Value = ${i1*i2}")
    }

    private fun compare(lhs: Element, rhs: Element): Int {
        if (lhs is NumberElement && rhs is NumberElement) {
            return lhs.value.compareTo(rhs.value)
        }
        val ll = if (lhs is ListElement) lhs else ListElement(listOf(lhs))
        val rl = if (rhs is ListElement) rhs else ListElement(listOf(rhs))
        for (i in ll.elements.indices) {
            if (i > rl.elements.lastIndex) return 1
            val le = ll.elements[i]
            val re = rl.elements[i]
            val comparison = compare(le, re)
            if (comparison != 0) return comparison
        }
        if (ll.elements.size < rl.elements.size) return -1
        return 0
    }
}

data class Parser(
    val input: String,
) {
    private var index: Int = 0

    fun parseElement(): Element {
        return if (input[index] == '[') {
            parseList()
        } else {
            parseNumber()
        }
    }

    private fun parseList(): ListElement {
        assert(input[index++] == '[')
        val elements = mutableListOf<Element>()
        while (input[index] != ']') {
            elements.add(parseElement())
        }
        index++
        if (index < input.lastIndex && input[index] == ',') index++
        return ListElement(elements)
    }

    private fun parseNumber(): NumberElement {
        val sb = StringBuilder()
        while (input[index] != ',' && input[index] != ']') {
            sb.append(input[index++])
        }
        if (input[index] == ',') index++
        return NumberElement(sb.toString().toInt())
    }
}

sealed class Element
data class ListElement(val elements: List<Element>) : Element()
data class NumberElement(val value: Int) : Element()
