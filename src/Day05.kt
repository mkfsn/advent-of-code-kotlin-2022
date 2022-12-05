import java.util.Stack

fun main() {
    class Solution(input: List<String>) {
        val supplyStacks: List<Stack<String>>
        val procedures: List<String>

        init {
            val (spec, procedures) = input.chunkedBy { ele -> ele == "" };
            val (indexes, configurations) = spec.asReversed().run { Pair(this.first(), this.drop(1)) }

            this.supplyStacks = indexes.toNumbers().map { Stack<String>() }
            this.procedures = procedures

            configurations.forEach { row ->
                row.chunked(4).map { it.replace(" ", "") }.forEachIndexed { i, v ->
                    if (v != "") this.supplyStacks[i].push(v.trim('[', ']'))
                }
            }
        }

        fun moveCrates(executor: (count: Int, from: Int, to: Int, stacks: List<Stack<String>>) -> Unit) = apply {
            procedures.forEach { procedure ->
                val (count, from, to) = procedure.toNumbers()
                executor(count, from, to, this.supplyStacks)
            }
        }

        fun toResult(): String = supplyStacks.joinToString(separator = "") { if (it.size == 0) "_" else it.last() }
    }

    fun part1(input: List<String>): String =
        Solution(input).moveCrates { count: Int, from: Int, to: Int, stacks: List<Stack<String>> ->
            (1..count).forEach { _ -> stacks[to - 1].push(stacks[from - 1].pop()) }
        }.toResult()

    fun part2(input: List<String>): String =
        Solution(input).moveCrates { count: Int, from: Int, to: Int, stacks: List<Stack<String>> ->
            val tmpStack = Stack<String>()
            (1..count).forEach { _ -> tmpStack.push(stacks[from - 1].pop()) }
            (1..count).forEach { _ -> stacks[to - 1].push(tmpStack.pop()) }
        }.toResult()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == "CMZ")
    check(part2(testInput) == "MCD")

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}
