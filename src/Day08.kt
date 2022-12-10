fun main() {
    fun <T> rotate(table: MutableList<MutableList<T>>): MutableList<MutableList<T>> {
        val rows = table.size
        val cols = if (table.isNotEmpty()) table[0].size else 0
        return MutableList<MutableList<T>>(cols) { _ -> ArrayList(rows) }.apply {
            table.forEachIndexed { i, row -> row.forEachIndexed { j, col -> this[j].add(i, col) } }
        }
    }

    fun part1(input: List<String>): Int {
        var treeMap: MutableList<MutableList<Int>> =
            input.toMutableList().map { row -> row.map { it - '0' }.toMutableList() }.toMutableList()
        var visibleMap: MutableList<MutableList<Boolean>> =
            input.map { MutableList(it.length) { false } }.toMutableList()

        fun traverse(treeMap: List<List<Int>>, visibleMap: MutableList<MutableList<Boolean>>) {
            treeMap.forEachIndexed { i, row ->
                var (leftMax, rightMax) = listOf(-1, -1)
                var leftIncrements = MutableList(row.size) { -1 }
                var rightIncrements = MutableList(row.size) { -1 }

                row.forEachIndexed { j, v ->
                    leftIncrements[j] = leftMax
                    leftMax = if (leftMax < v) v else leftMax
                }
                row.asReversed().forEachIndexed { j, v ->
                    rightIncrements[row.size - j - 1] = rightMax
                    rightMax = if (rightMax < v) v else rightMax
                }

                row.forEachIndexed { j, v ->
                    if (v > leftIncrements[j] || v > rightIncrements[j]) visibleMap[i][j] = true
                }
            }
        }

        traverse(treeMap, visibleMap)
        treeMap = rotate(treeMap)
        visibleMap = rotate(visibleMap)
        traverse(treeMap, visibleMap)

        return visibleMap.sumOf { row -> row.count { it } }
    }

    fun part2(input: List<String>): Int {
        var treeMap: MutableList<MutableList<Int>> =
            input.toMutableList().map { row -> row.map { it - '0' }.toMutableList() }.toMutableList()
        var scoreMap: MutableList<MutableList<Int>> =
            input.map { MutableList(it.length) { 1 } }.toMutableList()

        fun countVisibleTrees(cur: Int, others: List<Int>) =
            others.indexOfFirst { it >= cur }.also {
                if (it == -1) return others.count { v -> v < cur }
                return it + 1
            }

        fun traverse(treeMap: List<List<Int>>, scoreMap: MutableList<MutableList<Int>>) {
            treeMap.forEachIndexed { i, row ->
                row.forEachIndexed { j, v ->
                    scoreMap[i][j] *= countVisibleTrees(v, (j - 1 downTo 0).map { k -> row[k] })
                    scoreMap[i][j] *= countVisibleTrees(v, (j + 1 until row.size).map { k -> row[k] })
                }
            }
        }

        traverse(treeMap, scoreMap)
        treeMap = rotate(treeMap)
        scoreMap = rotate(scoreMap)
        traverse(treeMap, scoreMap)

        return scoreMap.maxOfOrNull { row -> row.max() }!!
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 8)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
