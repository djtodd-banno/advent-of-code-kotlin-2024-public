import Direction.*

typealias Board = List<List<Tile>>
typealias Position = Pair<Int, Int>
typealias Tile = Char

val test_input = listOf(
    "....#.....",
    ".........#",
    "..........",
    "..#.......",
    ".......#..",
    "..........",
    ".#..^.....",
    "........#.",
    "#.........",
    "......#...",
)

private data class BoardState(
    val position: Position,
    val nextPosition: Position,
    val direction: Direction,
    val visitedTiles: Map<Position, List<Direction>>,
    val nextTile: Tile,
    val obstacleOptions: List<Position>
) {
    constructor(startingPosition: Position) : this(
        position = startingPosition,
        direction = Up,
        nextPosition = startingPosition.u1j0,
        nextTile = EMPTY_TILE,
        visitedTiles = mapOf(startingPosition to listOf(Up)),
        obstacleOptions = listOf()
    )

    fun hasVisitedNext() = visitedTiles[nextPosition].contains(direction)
    fun hasVisited(position: Position) = visitedTiles.contains(position)

}

const val OUT_OF_BOUNDS = '_'
const val OBSTACLE = '#'
const val GUARD = '^'
const val EMPTY_TILE = '.'
fun main() {
    fun part1And2(input: List<String>) {
        val board = input.map { it.toCharArray().toList() }
        with(board) {
            val startingPosition = board.positionOf(GUARD)
            var boardState = BoardState(startingPosition)

            while (boardState.nextTile != OUT_OF_BOUNDS) {
                boardState = boardState.updateBoard()

                when (boardState.nextTile) {
                    OBSTACLE -> {
                        boardState = boardState.updateBoard(
                            direction = boardState.direction.turn()
                        )
                    }

                    else -> {
                        val obstruction = boardState.position stepIn boardState.direction
                        if (obstruction != startingPosition && !boardState.hasVisited(obstruction)) {
                            if (!isBoardEscapable(obstruction, boardState)) {
                                boardState = boardState.copy(
                                    obstacleOptions = boardState.obstacleOptions + obstruction
                                )
                            }
                        }
                        boardState = boardState.updateBoard(
                            position = obstruction
                        )
                    }
                }
            }
            println("Part 1: ${boardState.visitedTiles.keys.size} shouldEqual 4973")
            println("Part 2 : ${boardState.obstacleOptions.size} shouldEqual 1482")
        }
    }

    val input = readInput("Day06")
    part1And2(input)
}

enum class Direction {
    Up,
    Right,
    Down,
    Left;

    fun turn(): Direction = entries[(ordinal + 1) % 4]
}

context(Board)
infix fun Position.stepIn(
    direction: Direction
) = when (direction) {
    Up -> u1j0
    Right -> i0r1
    Down -> d1j0
    Left -> i0l1
}

context(Board)
infix fun Position.lookIn(
    direction: Direction
) = get(this stepIn direction)

context(Board)
private fun isBoardEscapable(
    obstruction: Position?,
    initialBoardState: BoardState,
): Boolean {
    var boardState = initialBoardState.updateBoard()
    while (!boardState.hasVisitedNext() &&
        boardState.nextTile != OUT_OF_BOUNDS
    ) {
        if (boardState.nextPosition == obstruction) {
            boardState = boardState.updateBoard(
                direction = boardState.direction.turn()
            )
        } else when (boardState.nextTile) {
            OBSTACLE -> {
                boardState = boardState.updateBoard(
                    direction = boardState.direction.turn()
                )
            }

            else -> {
                boardState = boardState.updateBoard(
                    position = boardState.position stepIn boardState.direction
                )
            }
        }

    }
    val escaped = boardState.nextTile == OUT_OF_BOUNDS
    val reachedLoop = boardState.hasVisitedNext()
    return !reachedLoop && escaped
}


context(Board)
private fun BoardState.updateBoard(position: Position? = null, direction: Direction? = null): BoardState {
    val pos = position ?: this.position
    val dir = direction ?: this.direction

    return copy(
        visitedTiles = visitedTiles + (pos to ((visitedTiles[pos] ?: emptyList()) + dir)),
        position = pos,
        direction = dir,
        nextTile = if (position != null || direction != null) pos lookIn dir else nextTile,
        nextPosition = if (position != null || direction != null) pos stepIn dir else nextPosition
    )
}