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

fun Int.cardinal(other: Int) = when (val dif = (this - other)) {
    0 -> 0
    else -> dif / dif.absoluteValue
}

infix fun Int.isNotSafeDelta(other: Int) = abs(this - other) !in 1..3