import java.util.*

fun main(args: Array<String>) {
    val scanner = Scanner(System.`in`)
    val (a, b) = scanner.run { Pair(nextInt(), nextInt()) }
    if (b == 0) {
        println("Division by zero, please fix the second argument!")
        return
    }
    println(a / b)
}
