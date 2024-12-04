fun main() {
    fun part1(input: List<String>): Int = input
        .map {
            it.split(Regex("\\s+")).map(String::toInt)
        }.filter { getFirstUnSafeIndex(it) == null }.size

    fun part2(input: List<String>): Int = input
        .map {
            it.split(Regex("\\s+")).map(String::toInt)
        }.filter { record ->
            var index: Int? = getFirstUnSafeIndex(record)
            while (index != null && index < record.size) {
                if (getFirstUnSafeIndex(record.toMutableList().apply { removeAt(index) }) != null)
                    index++
                else
                    return@filter true
            }
            index == null
        }.size

    val input = readInput("Day02")

    println("Part 1 answer: ${part1(input)}")
    println("Part 2 answer: ${part2(input)}")
}

fun getFirstUnSafeIndex(record: List<Int>): Int? {
    for (i in 2..record.lastIndex) {
        val level = record[i]
        val backOne = record[i - 1]
        val backTwo = record[i - 2]
        val cardinal = backOne.cardinal(level)
        val prevCardinal = backTwo.cardinal(backOne)

        if (cardinal != prevCardinal) return i - 2
        if (level isNotSafeDelta backOne) return i
        if (backOne isNotSafeDelta backTwo) return i - 2
    }
    return null
}
