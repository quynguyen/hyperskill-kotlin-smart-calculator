import java.math.BigInteger
import java.util.*

fun main() {
    val (a, b, c) = (0 until 3).map {
        Scanner(System.`in`).run { BigInteger(readLine()) }
    }
    val numEqual = when {
        a == b && b == c && a == c -> 3
        a == b || b == c || a == c -> 2
        else -> 0
    }
    println(numEqual)
}
