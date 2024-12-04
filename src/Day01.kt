import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val (a, b) = input
            .splitList("\\s+")
            .applyBoth {
                mapNotNull { it.toNullableInt() }
                    .sorted()
            }
        return a.zip(b) { i, j -> abs(i - j) }.sum()

    }

    fun part2(input: List<String>): Int {
        val (a, b) = input
            .splitList("\\s+")
            .applyBoth {
                mapNotNull { it.toNullableInt() }
            }
        return a.sumOf { it * b.count(it::equals) }
    }

    val input = readInput("Day01")

    val part1 = part1(input)
    println("Pat 1 answer: $part1 shouldEqual 1320851")
    val part2 = part2(input)
    println("Pat 2 answer: $part2 shouldEqual 26859182")
}
