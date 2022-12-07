fun main() {
    data class File(val name: String, val size: Int)

    class Dir {
        var parentDirectory: Dir? = null
        val files: MutableList<File> = mutableListOf<File>()
        val subDirectories: MutableMap<String, Dir> = mutableMapOf<String, Dir>()

        fun traverseSize(recorder: (Int) -> Unit): Int =
            listOf(
                this.files.sumOf { f -> f.size },
                this.subDirectories.values.sumOf { d -> d.traverseSize(recorder) }
            ).sum().apply { recorder(this) }

        fun goToParent(): Dir? = this.parentDirectory

        fun addFile(f: File) = this.files.add(f)

        fun addDirectory(name: String) =
            this.subDirectories?.put(name, Dir().apply { this.parentDirectory = this@Dir })

        fun goToSubDirectory(name: String): Dir = this.subDirectories[name]!!
    }

    fun parseCommands(input: List<String>): List<List<String>> =
        input.fold(mutableListOf<MutableList<String>>()) { acc, ele ->
            if (ele.first() == '$') {
                acc.add(mutableListOf())
            }
            acc.last().add(ele)
            acc
        }

    class Solution(input: List<String>) {
        var root: Dir?

        init {
            var cur: Dir? = null

            parseCommands(input).forEach { group ->
                when (group[0]) {
                    "$ cd /" -> cur = Dir()
                    "$ cd .." -> cur = cur?.goToParent()
                    "$ ls" -> group.drop(1).forEach { line ->
                        line.split(" ").run {
                            if (this[0] == "dir") {
                                cur?.addDirectory(this[1])
                            } else {
                                cur?.addFile(File(this[1], this[0].toInt()))
                            }
                        }
                    }
                    else -> cur = cur?.goToSubDirectory(group[0].substring("$ cd ".length))
                }
            }.run {
                while (cur?.parentDirectory != null) cur = cur?.goToParent()
            }

            this.root = cur
        }

        fun findSizes(): List<Int> =
            mutableListOf<Int>().apply { this@Solution.root?.traverseSize { this.add(it) } }

        fun findTotalSize(filter: (v: Int) -> Boolean): Int = this.findSizes().filter(filter).sum()
    }

    fun part1(input: List<String>) =
        Solution(input).findTotalSize { v -> v <= 100000 }

    fun part2(input: List<String>): Int =
        Solution(input).findSizes().run { this.filter { it >= this.max().minus(40000000) }.minOf { it } }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 95437)
    check(part2(testInput) == 24933642)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}
