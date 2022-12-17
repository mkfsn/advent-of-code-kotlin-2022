import java.util.*

fun main() {
    fun decode(input: String): List<Any> {
        var groupStack = Stack<Int>()
        var numberQueue: Queue<Int> = LinkedList<Int>()
        var parts = mutableListOf<String>()

        val inner = input.substring(1, input.length - 1)
        inner.forEachIndexed { i, ch ->
            if (ch == '[') {
                groupStack.add(i)
            } else if (ch == ']') {
                val j = groupStack.pop()
                if (groupStack.isEmpty()) {
                    parts.add(inner.substring(j, i + 1))
                    return@forEachIndexed
                }
            }

            if (groupStack.isNotEmpty()) return@forEachIndexed

            if (ch != ',') numberQueue.add(i)

            if (ch == ',' || i == inner.length - 1) {
                if (numberQueue.isEmpty()) return@forEachIndexed
                val j = numberQueue.first()
                parts.add(inner.substring(j, if (ch == ',') i else i + 1))
                numberQueue.clear()
            }
        }

        return parts.map { if (it[0] == '[') decode(it) else it.toInt() }
    }

    fun inRightOrder(pair: Pair<List<Any>, List<Any>>): Int {
        pair.first.forEachIndexed { i, left ->
            if (i >= pair.second.size) return -1

            val right = pair.second[i]
            val res = when {
                left is Int && right is Int -> if (left < right) 1 else if (left > right) -1 else 0
                left is List<*> && right is List<*> -> inRightOrder(Pair(left as List<Any>, right as List<Any>))
                left is Int && right is List<*> -> inRightOrder(Pair(listOf(left), right as List<Any>))
                left is List<*> && right is Int -> inRightOrder(Pair(left as List<Any>, listOf(right)))
                else -> 0
            }

            if (res != 0) return res
        }

        return if (pair.first.size < pair.second.size) 1 else 0
    }

    fun part1(input: List<String>): Int =
        input.chunkedBy { it == "" }
            .map { inRightOrder(Pair(decode(it[0]), decode(it[1]))) }
            .mapIndexed { i, v -> Pair(i + 1, v) }
            .filter { it.second == 1 }
            .sumOf { it.first }

    fun part2(input: List<String>) =
        input.filter { it != "" }.run { this + listOf("[[2]]", "[[6]]") }.map { Pair(it, decode(it)) }
            .sortedWith { left, right -> inRightOrder(Pair(left.second, right.second)) }
            .asReversed()
            .run {
                val a = this.indexOfFirst { it.first == "[[2]]" } + 1
                var b = this.indexOfFirst { it.first == "[[6]]" } + 1
                a * b
            }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 140)

    val input = readInput("Day13")
    check(part1(input) == 5808)
    println(part1(input))
    check(part2(input) == 22713)
    println(part2(input))
}
