import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.math.abs
import kotlin.math.absoluteValue

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

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

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