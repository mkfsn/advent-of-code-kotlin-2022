import java.util.LinkedList
import java.util.Queue

fun main() {
    fun getFirstStartOfPacketMarker(input: List<String>, distinctCharacters: Int): List<Int> {
        return input.map outer@{ text ->
            val queue: Queue<Char> = LinkedList(text.take(distinctCharacters - 1).toList())
            val dict = text
                .take(distinctCharacters - 1)
                .groupBy { it }.map { e -> e.key to e.value.size }
                .toMap()
                .toMutableMap()
            for ((i, ch) in text.drop(distinctCharacters - 1).withIndex()) {
                queue.add(ch)
                dict.merge(ch, 1, Int::plus)
                if (dict.filter { (_, v) -> v > 0 }.count() == distinctCharacters) {
                    return@outer i + distinctCharacters
                }
                dict.merge(queue.remove(), 1, Int::minus)
            }
            0
        }
    }

    fun part1(input: List<String>): List<Int> = getFirstStartOfPacketMarker(input, 4)

    fun part2(input: List<String>): List<Int> = getFirstStartOfPacketMarker(input, 14)


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == listOf(7, 5, 6, 10, 11))
    check(part2(testInput) == listOf(19, 23, 23, 29, 26))

    val input = readInput("Day06")
    println(part1(input)[0])
    println(part2(input)[0])
}
