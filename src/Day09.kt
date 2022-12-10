import kotlin.math.sign

fun main() {
    data class Action(val direction: Char, val steps: Int)

    data class Point(val x: Int, val y: Int)

    class Solution(input: List<String>, size: Int) {
        val actions: List<Action>
        val rope = MutableList(size) { Point(1, 1) }

        init {
            this.actions = input.map { it.split(" ").run { Action(this[0][0], this[1].toInt()) } }
        }

        fun result() =
            actions.fold(mutableListOf<Point>()) { tailHistory, action ->
                (1..action.steps).forEach { _ ->
                    rope.forEachIndexed { i, cur ->
                        rope[i] = if (i == 0) moveHead(action.direction, cur) else moveKnot(rope[i - 1], cur)
                    }
                    tailHistory.add(rope.last().copy())
                }
                tailHistory
            }.distinct().count()

        private fun moveHead(direction: Char, cur: Point): Point = when (direction) {
            'U' -> Point(cur.x, cur.y + 1)
            'D' -> Point(cur.x, cur.y - 1)
            'R' -> Point(cur.x + 1, cur.y)
            'L' -> Point(cur.x - 1, cur.y)
            else -> cur
        }

        private fun moveKnot(pre: Point, cur: Point): Point = run {
            val (dx, dy) = listOf(pre.x - cur.x, pre.y - cur.y)
            if (dx in -1..1 && dy in -1..1) cur else Point(cur.x + dx.sign, cur.y + dy.sign)
        }
    }

    fun part1(input: List<String>) = Solution(input, 2).result()

    fun part2(input: List<String>) = Solution(input, 10).result()

    // test if implementation meets criteria from the description, like:
    val testInput1 = readInput("Day09_test1")
    check(part1(testInput1) == 13)
    check(part2(testInput1) == 1)
    val testInput2 = readInput("Day09_test2")
    check(part2(testInput2) == 36)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}
