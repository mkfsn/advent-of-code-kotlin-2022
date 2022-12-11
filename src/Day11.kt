fun main() {
    data class Decision(val to: Int, val item: Long)

    class Monkey(spec: List<String>) {
        val id: Int
        val items: MutableList<Long>
        val operateFn: (Long) -> Long
        val testFn: (Long) -> Boolean
        val dividedBy: Long
        val ifTrue: Int
        val ifFalse: Int
        var inspectCount = 0L

        init {
            id = spec[0].replace("Monkey ", "").trim(':').toInt()
            items = spec[1].replace("Starting items:", "").trimIndent()
                .split(", ").map { it.toLong() }.toMutableList()
            spec[2].replace("Operation: new = old", "").trimIndent()
                .split(" ").let {
                    operateFn = { old ->
                        when (it[0]) {
                            "*" -> if (it[1] == "old") old * old else old * it[1].toLong()
                            "+" -> if (it[1] == "old") old + old else old + it[1].toLong()
                            else -> old
                        }
                    }
                }
            dividedBy = spec[3].replace("Test: divisible by", "").trimIndent().toLong().apply {
                testFn = { it % this == 0L }
            }
            ifTrue = spec[4].replace("If true: throw to monkey", "").trimIndent().toInt()
            ifFalse = spec[5].replace("If false: throw to monkey", "").trimIndent().toInt()
        }

        fun inspect(dividedBy: Long = 3L): List<Decision> {
            val decisions: MutableList<Decision> = mutableListOf()
            this.items.forEach { item ->
                this.operateFn(item).div(dividedBy).let {
                    decisions.add(if (testFn(it)) Decision(this.ifTrue, it) else Decision(this.ifFalse, it))
                }
                this.inspectCount++
            }.also { this.items.clear() }
            return decisions
        }

        fun addItem(item: Long) = this.items.add(item)
    }

    fun part1(input: List<String>): Long {
        val monkeys = input.chunkedBy { it == "" }.map { Monkey(it) }
        repeat(20) {
            monkeys.forEach { monkey ->
                monkey.inspect(3L).forEach { decision ->
                    monkeys[decision.to].addItem(decision.item)
                }
            }
        }
        return monkeys.map { it.inspectCount }.sortedDescending().take(2).reduce(Long::times)
    }

    fun part2(input: List<String>): Long {
        val monkeys = input.chunkedBy { it == "" }.map { Monkey(it) }
        val upBound = monkeys.map { it.dividedBy }.reduce(Long::times)
        repeat(10000) {
            monkeys.forEach { monkey ->
                monkey.inspect(1L).forEach { decision ->
                    monkeys[decision.to].addItem(decision.item % upBound)
                }
            }
        }
        return monkeys.map { it.inspectCount }.sortedDescending().take(2).reduce(Long::times)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 10605L)
    check(part2(testInput) == 2713310158L)

    val input = readInput("Day11")
    check(part1(input) == 55930L) // [9, 228, 228, 225, 235, 11, 238, 21]
    println(part1(input))
    check(part2(input) == 14636993466L)
    println(part2(input))
}
