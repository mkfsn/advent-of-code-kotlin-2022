fun main() {
    // Rock: A
    // Paper: B
    // Scissors: C

    val (rock, paper, scissors) = listOf(1, 2, 3);
    val (win, draw, loss) = listOf(6, 3, 0);

    fun part1(input: List<String>): Int {
        // Rock: X
        // Paper: Y
        // Scissors: Z

        val scores = hashMapOf(
            "A X" to draw + rock,
            "A Y" to win + paper,
            "A Z" to loss + scissors,
            "B X" to loss + rock,
            "B Y" to draw + paper,
            "B Z" to win + scissors,
            "C X" to win + rock,
            "C Y" to loss + paper,
            "C Z" to draw + scissors,
        )

        return input.map { scores.getValue(it) }.sumOf { it }
    }

    fun part2(input: List<String>): Int {
        // loss: X
        // draw: Y
        // win: Z

        val scores = hashMapOf(
            "A X" to loss + scissors,
            "A Y" to draw + rock,
            "A Z" to win + paper,
            "B X" to loss + rock,
            "B Y" to draw + paper,
            "B Z" to win + scissors,
            "C X" to loss + paper,
            "C Y" to draw + scissors,
            "C Z" to win + rock,
        )

        return input.map { scores.getValue(it) }.sumOf { it }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8+1+6)
    check(part2(testInput) == 12)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
