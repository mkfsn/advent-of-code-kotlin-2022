fun main() {
    data class Instruction(val operator: String, val value: Int)

    class Solution(input: List<String>) {
        val instructions: List<Instruction>

        init {
            this.instructions = input.map {
                it.split(" ").run { Instruction(this[0], if (this.size == 1) 0 else this[1].toInt()) }
            }
        }

        fun calculateSignalStrengths(): Int {
            fun addSignalStrengths(signalStrengths: MutableList<Int>, cycle: Int, register: Int) {
                if ((cycle - 20) % 40 == 0) signalStrengths.add(cycle * register)
            }

            var (cycle, register) = listOf(1, 1)
            return this.instructions.fold(mutableListOf<Int>()) { signalStrengths, instruction ->
                addSignalStrengths(signalStrengths, cycle, register)
                when (instruction.operator) {
                    "noop" -> cycle += 1
                    "addx" -> {
                        cycle += 1
                        addSignalStrengths(signalStrengths, cycle, register)
                        register += instruction.value
                        cycle += 1
                    }
                }
                signalStrengths
            }.sum()
        }

        fun render(): String {
            fun addPixel(pixels: MutableList<Char>, cycle: Int, register: Int) =
                pixels.add(if ((cycle - 1) % 40 in register - 1..register + 1) '#' else '.')

            var (cycle, register) = listOf(1, 1)
            return this.instructions.fold(mutableListOf<Char>()) { pixels, instruction ->
                addPixel(pixels, cycle, register)
                when (instruction.operator) {
                    "noop" -> cycle += 1
                    "addx" -> {
                        cycle += 1
                        addPixel(pixels, cycle, register)
                        register += instruction.value
                        cycle += 1
                    }
                }
                pixels
            }.chunked(40).joinToString("\n") { it.joinToString("") }
        }
    }

    fun part1(input: List<String>): Int = Solution(input).calculateSignalStrengths()

    fun part2(input: List<String>): String = Solution(input).render()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 13140)
    check(part2(testInput) == """
##..##..##..##..##..##..##..##..##..##..
###...###...###...###...###...###...###.
####....####....####....####....####....
#####.....#####.....#####.....#####.....
######......######......######......####
#######.......#######.......#######.....
        """.trimIndent()
    )

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}
