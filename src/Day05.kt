import java.lang.IllegalStateException

fun main() {
    fun part1(input: List<String>): Int = with(input.toDay05Data()) {
        updates.filter { it.isSortedWith(ruleSorter) }
            .sumOf { it.medianValue }
    }


    fun part2(input: List<String>): Int = with(input.toDay05Data()) {
        updates.filterNot { it.isSortedWith(ruleSorter) }
            .map { it.sortedWith(ruleSorter) }
            .sumOf { it.medianValue }
    }

    val input = readInput("Day05")
    println("Part 1: ${part1(input)} shouldEqual 5762")
    println("Part 2: ${part2(input)} shouldEqual 4130")
}

private fun List<String>.toDay05Data(): Day05Data {
    val rules: MutableMap<Int, List<Int>> = mutableMapOf()
    val updates: MutableList<List<Int>> = mutableListOf()
    var addUpdates = false
    forEach { line ->
        when {
            line.isEmpty() -> addUpdates = !addUpdates
            addUpdates -> updates += line.split(",").map(String::toInt)
            !addUpdates -> rules += line.split("|").let {
                val page = it[0].toInt()
                page to (rules[page] ?: emptyList()) + it[1].toInt()
            }
        }
    }
    return Day05Data(
        rules = rules,
        updates = updates
    )
}

data class Day05Data(
    val rules: Map<Int, List<Int>>,
    val updates: List<List<Int>>
) {
    val ruleSorter = Comparator<Int> { leftPage, rightPage ->
        val leftLessThanRight = rules[leftPage]?.contains(rightPage) == true
        val rightLessThanLeft = rules[rightPage]?.contains(leftPage) == true
        when {
            leftLessThanRight && !rightLessThanLeft -> -1
            !leftLessThanRight && rightLessThanLeft -> 1
            else -> 0
        }
    }
}

val List<Int>.medianValue
    get():Int = get((size / 2))
