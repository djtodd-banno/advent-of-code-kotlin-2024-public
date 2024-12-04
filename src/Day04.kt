val testData = listOf(
    "MMMSXXMASM",
    "MSAMXMSMSA",
    "AMXSXMAAMM",
    "MSAMASMSMX",
    "XMASAMXAMM",
    "XXAMMXXAMA",
    "SMSMSASXSS",
    "SAXAMASAAA",
    "MAMMMXMMMM",
    "MXMXAXMASX",
)

fun main() {
    fun part1(input: List<String>): Int {
        return input.map { it.toCharArray().toList() }
            .mapOnWords { words -> words.count { it == XMAS } }
            .sum()
    }

    fun part2(input: List<String>): Int {

        return input.size
    }

    val input = readInput("Day04")
    println("Part 1: ${part1(input)} shouldEqual 2344")
    println("Part 1 test: ${part1(testData)} shouldEqual 18")
    println("Part 2: ${part2(input)} shouldEqual :shrug:")
    println("Part 2 test: ${part2(testData)} shouldEqual :shrug:")
}

const val XMAS = "XMAS"

fun <T> List<List<Char>>.mapOnWords(block: (words: List<String>) -> T): List<T> {
    val mapped = mutableListOf<T>()
    val matrix = this
    for (i in 0..matrix.lastIndex) {
        val row = matrix[i]
        for (j in 0..row.lastIndex) with(i to j) {
            mapped += block(
                listOfNotNull(
                    "${matrix[i0j0]}${matrix[u1j0]}${matrix[u2j0]}${matrix[u3j0]}",
                    "${matrix[i0j0]}${matrix[u1r1]}${matrix[u2r2]}${matrix[u3r3]}",
                    "${matrix[i0j0]}${matrix[i0r1]}${matrix[i0r2]}${matrix[i0r3]}",
                    "${matrix[i0j0]}${matrix[d1r1]}${matrix[d2r2]}${matrix[d3r3]}",
                    "${matrix[i0j0]}${matrix[d1j0]}${matrix[d2j0]}${matrix[d3j0]}",
                    "${matrix[i0j0]}${matrix[d1l1]}${matrix[d2l2]}${matrix[d3l3]}",
                    "${matrix[i0j0]}${matrix[i0l1]}${matrix[i0l2]}${matrix[i0l3]}",
                    "${matrix[i0j0]}${matrix[u1l1]}${matrix[u2l2]}${matrix[u3l3]}",
                )
            )
        }
    }
    return mapped
}

operator fun List<List<Char>>.get(pair: Pair<Int, Int>) = try {
    this[pair.first][pair.second]
} catch (e: Throwable) {
    'D'
}

context(List<List<*>>) val Pair<Int, Int>.i0j0 get() = first to second
context(List<List<*>>) val Pair<Int, Int>.i0r1 get() = first to second + 1
context(List<List<*>>) val Pair<Int, Int>.i0r2 get() = first to second + 2
context(List<List<*>>) val Pair<Int, Int>.i0r3 get() = first to second + 3
context(List<List<*>>) val Pair<Int, Int>.i0l1 get() = first to second - 1
context(List<List<*>>) val Pair<Int, Int>.i0l2 get() = first to second - 2
context(List<List<*>>) val Pair<Int, Int>.i0l3 get() = first to second - 3
context(List<List<*>>) val Pair<Int, Int>.d1j0 get() = first + 1 to second
context(List<List<*>>) val Pair<Int, Int>.d2j0 get() = first + 2 to second
context(List<List<*>>) val Pair<Int, Int>.d3j0 get() = first + 3 to second
context(List<List<*>>) val Pair<Int, Int>.u1j0 get() = first - 1 to second
context(List<List<*>>) val Pair<Int, Int>.u2j0 get() = first - 2 to second
context(List<List<*>>) val Pair<Int, Int>.u3j0 get() = first - 3 to second
context(List<List<*>>) val Pair<Int, Int>.d1r1 get() = first + 1 to second + 1
context(List<List<*>>) val Pair<Int, Int>.u1r1 get() = first - 1 to second + 1
context(List<List<*>>) val Pair<Int, Int>.d2r2 get() = first + 2 to second + 2
context(List<List<*>>) val Pair<Int, Int>.u2r2 get() = first - 2 to second + 2
context(List<List<*>>) val Pair<Int, Int>.d3r3 get() = first + 3 to second + 3
context(List<List<*>>) val Pair<Int, Int>.u3r3 get() = first - 3 to second + 3
context(List<List<*>>) val Pair<Int, Int>.d1l1 get() = first + 1 to second - 1
context(List<List<*>>) val Pair<Int, Int>.u1l1 get() = first - 1 to second - 1
context(List<List<*>>) val Pair<Int, Int>.d2l2 get() = first + 2 to second - 2
context(List<List<*>>) val Pair<Int, Int>.u2l2 get() = first - 2 to second - 2
context(List<List<*>>) val Pair<Int, Int>.d3l3 get() = first + 3 to second - 3
context(List<List<*>>) val Pair<Int, Int>.u3l3 get() = first - 3 to second - 3
