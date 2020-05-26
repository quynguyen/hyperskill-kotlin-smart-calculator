import java.util.*

fun main(args: Array<String>) {
    val scanner = Scanner(System.`in`)
    Math.cbrt(scanner.nextLong().toDouble()).toInt().let { print(it) }
}
