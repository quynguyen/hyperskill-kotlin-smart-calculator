import java.math.BigInteger
import java.util.*

fun main() {
    val (a, b, c, d) = (0 until 4).map { Scanner(System.`in`).run { BigInteger(readLine()) } }
    val r = -a * b + c - d
    println(r)
}
