package day07

import Day
import day07.Day07.Item.Dir
import day07.Day07.Item.File

object Day07 : Day {
    override fun problem1() {
        var pwd = Dir("/", null, mutableMapOf())
        val root = pwd
        javaClass.getResource("/day07.txt")!!.readText().lines().drop(1).forEach {
            when {
                it == "$ ls" -> {}
                it.startsWith("$ cd") -> {
                    val target = it.split(" ").last()
                    pwd = if (target == "..") {
                        pwd.parent!!
                    } else {
                        pwd.children[target] as Dir
                    }
                }
                it.startsWith("dir") -> {
                    val name = it.split(" ").last()
                    pwd.children[name] = Dir(name, pwd, mutableMapOf())
                }
                else -> {
                    val vals = it.split(" ")
                    pwd.children[vals.last()] = File(vals.last(), vals.first().toLong())
                }
            }
        }

        getAndSetSize(root)
        println("sum: ${targets.sumOf { it.totalSize }}")
    }

    private val targets: MutableList<Dir> = mutableListOf()
    fun getAndSetSize(item: Item): Long {
        return when (item) {
            is File -> {
                item.size
            }
            is Dir -> {
                val size = item.children.entries.fold(0L) { acc, entry ->
                    acc + getAndSetSize(entry.value)
                }
                item.totalSize = size
                if (item.totalSize < 100000L) {
                    targets.add(item)
                }
                return item.totalSize
            }
        }
    }

    override fun problem2() {
        var pwd = Dir("/", null, mutableMapOf())
        val root = pwd
        javaClass.getResource("/day07.txt")!!.readText().lines().drop(1).forEach {
            when {
                it == "$ ls" -> {}
                it.startsWith("$ cd") -> {
                    val target = it.split(" ").last()
                    pwd = if (target == "..") {
                        pwd.totalSize = pwd.children.values.fold(0L) { acc, entry ->
                            acc + when (entry) {
                                is Dir -> entry.totalSize
                                is File -> entry.size
                            }
                        }
                        pwd.parent!!.totalSize = pwd.parent!!.children.values.fold(0L) { acc, entry ->
                            acc + when (entry) {
                                is Dir -> entry.totalSize
                                is File -> entry.size
                            }
                        }
                        pwd.parent!!
                    } else {
                        pwd.children[target] as Dir
                    }
                }
                it.startsWith("dir") -> {
                    val name = it.split(" ").last()
                    pwd.children[name] = Dir(name, pwd, mutableMapOf())
                }
                else -> {
                    val vals = it.split(" ")
                    pwd.children[vals.last()] = File(vals.last(), vals.first().toLong())
                }
            }
        }


        setSizes(root)
        val unused = 70000000L - root.totalSize
        val target = 30000000L - unused

        val item = findTarget(target, root)
        println("totalSize = ${root.totalSize}")
        println("item: ${item.totalSize}")
    }

    fun findTarget(targetSize: Long, pwd: Dir): Dir {
        if (targetSize > pwd.totalSize) return pwd

        var best = pwd
        for (child in pwd.children.values) {
            if (child is Dir) {
                if (child.totalSize > targetSize) {
                    val childBest = findTarget(targetSize, child)
                    if (childBest.totalSize > targetSize && childBest.totalSize < best.totalSize) {
                        best = childBest
                    }
                }
            }
        }

        return best
    }

    fun setSizes(item: Item): Long {
        return when (item) {
            is File -> {
                item.size
            }
            is Dir -> {
                val size = item.children.entries.fold(0L) { acc, entry ->
                    acc + setSizes(entry.value)
                }
                item.totalSize = size
                return item.totalSize
            }
        }
    }

    sealed class Item {
        class File(val name: String, val size: Long) : Item()
        class Dir(val name: String, val parent: Dir?, val children: MutableMap<String, Item>, var totalSize: Long = 0) : Item()
    }
}