import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt")
    .readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * Make chunks of string list by given selector
 */
fun List<String>.chunkedBy(selector: (String) -> Boolean): List<List<String>> =
    fold(mutableListOf(mutableListOf<String>())) { acc, ele ->
        if (selector(ele)) {
            acc.add(mutableListOf())
        } else {
            acc.last().add(ele)
        }
        acc
    }


/**
 * Parses all numbers from string
 */
fun String.toNumbers(): List<Int> = Regex("\\d+").findAll(this).map { it.value.toInt() }.toList()
