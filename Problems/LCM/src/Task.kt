import java.util.*

fun main() {
    val s = Scanner(System.`in`)
    val (a, b) = Pair(s.nextBigInteger(), s.nextBigInteger())
    val gcd = a.gcd(b)
    val result = a * b / gcd
    println(result)
}
