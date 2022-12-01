fun main() {
    fun makeGroups(input: List<String>): List<Int> {
        return input.fold(mutableListOf(mutableListOf<String>())) { acc, ele ->
            if (ele == "") {
                acc.add(mutableListOf())
            } else {
                acc.last().add(ele)
            }
            acc
        }.map {
            nums -> nums.sumOf { it.toInt() }
        }
    }

    fun part1(input: List<String>): Int {
        return makeGroups(input).max()
    }

    fun part2(input: List<String>): Int {
        return makeGroups(input).sortedDescending().take(3).sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24000)
    check(part2(testInput) == 45000)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
