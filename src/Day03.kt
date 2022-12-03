fun main() {
    fun findCommonItem(vararg blocks: String): Char {
        return blocks.fold(mutableMapOf<Char, Int>()) { acc, block ->
            for (item in block.toCharArray().distinct()) {
                acc[item] = (acc[item] ?: 0) + 1
            }
            acc
        }.filterValues { it == blocks.size }.keys.first()
    }

    fun toPriority(commonItem: Char): Int = when (commonItem) {
        in 'a'..'z' -> commonItem - 'a' + 1
        in 'A'..'Z' -> commonItem - 'A' + 27
        else -> 0
    }

    fun part1(input: List<String>): Int {
        return input
                .map { findCommonItem(*it.chunked(it.length/2).toTypedArray()) }
                .map { toPriority(it) }
                .sumOf { it }
    }

    fun part2(input: List<String>): Int {
        return input
                .chunked(3)
                .map{ findCommonItem(*it.toTypedArray()) }
                .map { toPriority(it) }
                .sumOf { it }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
