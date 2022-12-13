import java.util.Queue
import java.util.LinkedList

enum class DIRECTION { UP, RIGHT, DOWN, LEFT }

fun main() {
    data class Point(val x: Int, val y: Int) {
        fun move(d: DIRECTION) = when (d) {
            DIRECTION.UP -> Point(x - 1 , y)
            DIRECTION.DOWN -> Point(x + 1, y)
            DIRECTION.LEFT -> Point(x, y - 1)
            DIRECTION.RIGHT -> Point(x, y + 1)
        }
    }

    fun findIndexes(map: List<List<Char>>, value: Char): List<Point> {
        val result: MutableList<Point> = mutableListOf()
        map.forEachIndexed { i, row ->
            row.forEachIndexed { j, col ->
                if (col == value) result.add(Point(i, j))
            }
        }
        return result
    }

    fun findIndex(map: List<List<Char>>, value: Char): Point? {
        map.forEachIndexed { i, row -> row.indexOf(value).also { j -> if (j != -1) return Point(i, j) } }
        return null
    }

    class Solution(input: List<String>) {
        val heightMap: List<List<Char>>

        init {
            this.heightMap = input.map { it.toCharArray().toList() }
        }

        fun result(from: Char, to: Char): Int {
            val startPoints = findIndexes(this.heightMap, from)
            val end = findIndex(this.heightMap, to)!!
            return startPoints.map { start -> findShortestPaths(start, end) }.filter { it != -1 }.min()
        }

        private fun findShortestPaths(start: Point, end: Point): Int {
            val travelMap = this.heightMap.map { MutableList(it.size) { -1 } }
            val queue: Queue<Point> = LinkedList<Point>()
            val (row, col) = listOf(this.heightMap.size, this.heightMap[0].size)

            fun movable(from: Point, to: Point): Boolean = when {
                to.x < 0 || to.x >= row || to.y < 0 || to.y >= col -> false
                from == end -> false
                heightMap[from.x][from.y] == 'S' -> true
                to == end && this.heightMap[from.x][from.y] == 'z' -> true
                else -> to != end && this.heightMap[to.x][to.y] - this.heightMap[from.x][from.y] <= 1
            }

            queue.add(start).run { travelMap[start.x][start.y] = 0 }

            while (queue.isNotEmpty()) {
                val cur = queue.remove()!!
                val step = travelMap[cur.x][cur.y] + 1

                arrayOf(DIRECTION.UP, DIRECTION.RIGHT, DIRECTION.DOWN, DIRECTION.LEFT)
                    .map { cur.move(it) }
                    .filter { next -> movable(cur, next)  }
                    .filter { next -> !(travelMap[next.x][next.y] != -1 && travelMap[next.x][next.y] < step) }
                    .forEach { next ->
                        travelMap[next.x][next.y] = step
                        if (!queue.contains(next)) queue.add(next)
                    }
            }

            return travelMap[end.x][end.y]
        }
    }

    fun part1(input: List<String>) = Solution(input).result('S', 'E')

    fun part2(input: List<String>) = Solution(input).result('a', 'E')

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 31)
    check(part2(testInput) == 29)

    val input = readInput("Day12")
    check(part1(input) == 408)
    println(part1(input))
    check(part2(input) == 399)
    println(part2(input))
}
