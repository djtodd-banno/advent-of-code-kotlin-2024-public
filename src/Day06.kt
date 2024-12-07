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

    val part1Answer get() = visitedTiles.keys.size
    val part2Answer get() = obstacleOptions.size
}

const val OUT_OF_BOUNDS = '_'
const val OBSTACLE = '#'
const val GUARD = '^'
const val EMPTY_TILE = '.'
fun main() {
    fun part1And2(input: List<String>) : BoardState {
        val board = input.map { it.toCharArray().toList() }
        with(board) {
            val startingPosition = board.positionOf(GUARD)
            var boardState = BoardState(startingPosition)

            while (boardState.nextTile != OUT_OF_BOUNDS) when (boardState.nextTile) {
                OBSTACLE -> {
                    boardState = boardState.updateBoard(
                        direction = boardState.direction.turn()
                    )
                }

                else -> {
                    val obstruction = boardState.position stepIn boardState.direction
                    if (obstruction != startingPosition && !boardState.hasVisited(obstruction)) {
                        boardState = isBoardEscapable(boardState, obstruction )
                    }
                    boardState = boardState.updateBoard(
                        position = obstruction
                    )
                }
            }
            return boardState
        }
    }

    fun part1And2ReCursive(input: List<String>) : BoardState {
        val board = input.map { it.toCharArray().toList() }
        with(board) {
            val startingPosition = board.positionOf(GUARD)
            var boardState = BoardState(startingPosition)
            while (boardState.nextTile != OUT_OF_BOUNDS){
                boardState = isBoardEscapable(boardState, boardState.nextPosition.takeIf { it != startingPosition } )
            }

            return boardState
        }
    }
    val input = readInput("Day06")
    val boardState = part1And2(test_input)
    println("Part 1: ${boardState.part1Answer} shouldEqual 4973")
    println("Part 2 : ${boardState.part2Answer} shouldEqual 1482")
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
    initialBoardState: BoardState,
    obstruction: Position?,
): BoardState {
    var boardState = initialBoardState.copy()
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
    return if ((reachedLoop || !escaped) && obstruction != null)
        initialBoardState.copy(
            obstacleOptions = initialBoardState.obstacleOptions + obstruction
        ) else initialBoardState
}

context(Board)
private fun BoardState.updateBoard(position: Position): BoardState {
    return copy(
        visitedTiles = visitedTiles + (position to ((visitedTiles[position] ?: emptyList()) + direction)),
        position = position,
        nextTile = position lookIn direction,
        nextPosition = position stepIn direction
    )
}

context(Board)
private fun BoardState.updateBoard(direction: Direction): BoardState {
    return copy(
        visitedTiles = visitedTiles + (position to ((visitedTiles[position] ?: emptyList()) + direction)),
        direction = direction,
        nextTile = position lookIn direction,
        nextPosition = position stepIn direction
    )
}