import kotlin.math.pow

fun main() {
    fun part1(input: List<String>): Long {
        var problems = input.asProblems()
        val mutations = problems.getMutations(2)
        var sum = 0L

        for (iteration in 0..mutations) {
            problems = problems.filterNot { (answer, inputs) ->
                isSolvable(
                    iteration = iteration,
                    answer = answer,
                    inputs = inputs,
                    numberOfFunctions = 2
                ).also { if (it) sum += answer }
            }.toMutableList()
        }

        return sum
    }

    fun part2(input: List<String>): Long {
        var sum = 0L
        var problems = input.asProblems()
        val mutations = problems.getMutations(numberOfFunctions = 3)

        for (iteration in 0..mutations) {
            problems = problems.filterNot { (answer, inputs) ->
                isSolvable(
                    numberOfFunctions = 3,
                    iteration = iteration,
                    answer = answer,
                    inputs = inputs
                ).also { if (it) sum += answer }
            }
        }

        return sum
    }

    val input = readInput("Day07")
    println("Part 1 : ${part1(input)} shouldEqual 267566105056")
    println("Part 2 : ${part2(input)} shouldEqual 116094961956019")
}

private fun isSolvable(iteration: Long, answer: Long, inputs: List<Long>, numberOfFunctions: Int): Boolean {
    var a = inputs.first()
    val functionChooser = iteration.asFunctionString(numberOfFunctions, inputs)
    for ((index, function) in functionChooser.withIndex()) {
        if (index == inputs.lastIndex) break
        a = when (function) {
            '0' -> a + inputs[index + 1]
            '1' -> a * inputs[index + 1]
            '2' -> "$a${inputs[index + 1]}".toLong()
            else -> throw Throwable("Bad function char $function")
        }
    }
    return a == answer
}

private fun Long.asFunctionString(
    numberOfFunctions: Int,
    inputs: List<Long>
) = buildString {
    val bitString = toString(numberOfFunctions)
    for (i in 0..<inputs.lastIndex - bitString.length) append("0")
    append(bitString)
}


private fun List<Pair<Long, List<Long>>>.getMutations(numberOfFunctions: Int) =
    numberOfFunctions.toDouble().pow(maxOf { it.second.lastIndex }).toLong() - 1

fun List<String>.asProblems() = map { line ->
    line.split(":").let { (answerString, inputsString) ->
        val answer = answerString.filter(Char::isDigit).toLong()
        val inputs = inputsString.trim().split(" ")
            .map { it.filter(Char::isDigit).toLong() }

        answer to inputs
    }
}