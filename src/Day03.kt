import day3.MulCompiler

fun main() {
    fun part1(input: List<String>): Int = with(MulCompiler) {
        input.joinToString()
            .toCharArray()
            .forEach(::compile)
        return runMul()
    }

    fun part2(input: List<String>): Int = with(MulCompiler) {
        dosFeatureEnabled = true
        input.joinToString()
            .toCharArray()
            .forEach(::compile)
        return runMul()
    }

    val input = readInput("Day03")
    println("Part 1: ${part1(input)} shouldEqual 181345830")
    println("Part 2: ${part2(input)} shouldEqual 98729041")
}
