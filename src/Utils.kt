import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.math.abs

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/input/$name.txt").readText().trim().lines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')


fun List<String>.splitList(regex: String): Pair<List<String>, List<String>> {
    val aList = mutableListOf<String>()
    val bList = mutableListOf<String>()
    forEach {
        val split = it.trim().split(Regex(regex))
        if (split.size > 1) {
            aList += split[0]
            bList += split[1]
        }
    }
    return aList to bList
}

fun <T> Pair<List<String>, List<String>>.applyBoth(block: List<String>.() -> List<T>) = first.block() to second.block()

fun String.toNullableInt() = try {
    toInt()
} catch (e: NumberFormatException) {
    println("Failed to int on \"$this\"")
    null
}

infix fun Int.isNotSafeDelta(other: Int) = abs(this - other) !in 1..3

fun <T : Comparable<*>> List<T>.isSortedWith(comparator: Comparator<in T>): Boolean = noneIndexed { t, i ->
    i != 0 && comparator.compare(get(i - 1), t) > 0
}

inline fun <T> Iterable<T>.noneIndexed(predicate: (T, Int) -> Boolean): Boolean {
    var i = 0
    if (this is Collection && isEmpty()) return true
    for (element in this) if (predicate(element, i++)) return false
    return true
}

operator fun List<List<Char>>.get(pair: Pair<Int, Int>) = getViaPair(pair)
operator fun List<MutableList<Char>>.set(pair: Pair<Int, Int>, char: Char) {
    this[pair.first][pair.second] = char
}

fun List<List<Char>>.getViaPair(pair: Pair<Int, Int>) = try {
    this[pair.first][pair.second]
} catch (e: Throwable) {
    '_'
}

fun List<List<Char>>.positionOf(char: Char): Pair<Int, Int> {
    forEachIndexed { i, rows ->
        rows.forEachIndexed { j, c -> if (c == char) return i to j }
    }
    return (0 to 0)
}

val Pair<Int, Int>.i0j0 get() = first to second
val Pair<Int, Int>.i0r1 get() = first to second + 1
val Pair<Int, Int>.i0r2 get() = first to second + 2
val Pair<Int, Int>.i0r3 get() = first to second + 3
val Pair<Int, Int>.i0l1 get() = first to second - 1
val Pair<Int, Int>.i0l2 get() = first to second - 2
val Pair<Int, Int>.i0l3 get() = first to second - 3
val Pair<Int, Int>.d1j0 get() = first + 1 to second
val Pair<Int, Int>.d2j0 get() = first + 2 to second
val Pair<Int, Int>.d3j0 get() = first + 3 to second
val Pair<Int, Int>.u1j0 get() = first - 1 to second
val Pair<Int, Int>.u2j0 get() = first - 2 to second
val Pair<Int, Int>.u3j0 get() = first - 3 to second
val Pair<Int, Int>.d1r1 get() = first + 1 to second + 1
val Pair<Int, Int>.u1r1 get() = first - 1 to second + 1
val Pair<Int, Int>.d2r2 get() = first + 2 to second + 2
val Pair<Int, Int>.u2r2 get() = first - 2 to second + 2
val Pair<Int, Int>.d3r3 get() = first + 3 to second + 3
val Pair<Int, Int>.u3r3 get() = first - 3 to second + 3
val Pair<Int, Int>.d1l1 get() = first + 1 to second - 1
val Pair<Int, Int>.u1l1 get() = first - 1 to second - 1
val Pair<Int, Int>.d2l2 get() = first + 2 to second - 2
val Pair<Int, Int>.u2l2 get() = first - 2 to second - 2
val Pair<Int, Int>.d3l3 get() = first + 3 to second - 3
val Pair<Int, Int>.u3l3 get() = first - 3 to second - 3

fun <T> List<T>?.contains(item: T) = this?.contains(item) == true