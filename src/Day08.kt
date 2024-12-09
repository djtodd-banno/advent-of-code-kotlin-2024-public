fun main() {
    fun part1(input: List<String>): Int {
        val board = input.map { it.toCharArray().toList() }
        with(board) {
            val antennas = getAntennaPositions()
            val possiblePositions = mutableListOf<Position>()
            antennas.forEach { (_, positions) ->
                for ((left, leftPosition) in positions.withIndex()) {
                    for (right in (left + 1)..positions.lastIndex) {
                        val rightPosition = positions[right]
                        val change = rightPosition - leftPosition
                        val behindLeftPosition = leftPosition - change
                        val frontRightPosition = rightPosition + change

                        if (board[behindLeftPosition] != OUT_OF_BOUNDS)
                            possiblePositions += behindLeftPosition

                        if (board[frontRightPosition] != OUT_OF_BOUNDS)
                            possiblePositions += frontRightPosition
                    }
                }
            }
            return possiblePositions.distinct().size
        }
    }

    fun part2(input: List<String>): Int {
        val board = input.map { it.toCharArray().toList() }
        with(board) {
            val antennas = getAntennaPositions()
            val possiblePositions = mutableListOf<Position>()
            antennas.forEach { (_, positions) ->
                possiblePositions += positions
                for ((left, leftPosition) in positions.withIndex()) {
                    for (right in (left + 1)..positions.lastIndex) {
                        val rightPosition = positions[right]
                        val change = rightPosition - leftPosition
                        var behindLeftPosition = leftPosition - change
                        var frontRightPosition = rightPosition + change

                        while (board[behindLeftPosition] != OUT_OF_BOUNDS) {
                            possiblePositions += behindLeftPosition
                            behindLeftPosition -= change
                        }

                        while (board[frontRightPosition] != OUT_OF_BOUNDS) {
                            possiblePositions += frontRightPosition
                            frontRightPosition += change
                        }
                    }
                }
            }
            return possiblePositions.distinct().size
        }
    }


    val input = readInput("Day08")
    println("Part 1: ${part1(input)} shouldEqual 285")
    println("Part 1: ${part2(input)} shouldEqual 34")

}

fun List<List<Char>>.forEachPosition(block: (position: Position, char: Char) -> Unit) {
    for ((i, chars) in withIndex())
        for ((j, char) in chars.withIndex()) {
            block(i to j, char)
        }
}

fun List<List<Char>>.getAntennaPositions(): Map<Char, List<Position>> {
    val antennas = mutableMapOf<Char, List<Position>>()
    forEachPosition { position, char ->
        if (char != EMPTY_TILE)
            antennas[char] = (antennas[char] ?: listOf()) + position
    }
    return antennas
}

operator fun Position.minus(position: Position): Position = first - position.first to second - position.second
operator fun Position.plus(position: Position): Position = first + position.first to second + position.second
