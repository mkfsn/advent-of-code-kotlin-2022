fun main() {
    fun IntRange.covers(other: IntRange): Boolean =
        this.first <= other.first && this.last >= other.last

    fun IntRange.overlaps(other: IntRange): Boolean =
        other.contains(this.first) || other.contains(this.last)

    fun pairToRange(pair: String): IntRange {
        val (x, y) = pair.split("-").map { it.toInt() }
        return IntRange(x, y)
    }

    fun toRangePair(section: String): List<IntRange> =
        section.split(",").map { pairToRange(it) }

    fun part1(input: List<String>): Int {
        return input
            .map { toRangePair(it) }
            .map { it[0].covers(it[1]) || it[1].covers(it[0]) }
            .count { it }
    }

    fun part2(input: List<String>): Int {
        return input
            .map { toRangePair(it) }
            .map { it[0].overlaps(it[1]) || it[1].overlaps(it[0]) }
            .count { it }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
