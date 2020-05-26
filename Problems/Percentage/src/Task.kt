import java.math.BigInteger
import java.util.*

fun main() {
    val s = Scanner(System.`in`)
    val (a, b) = Pair(s.nextBigInteger(), s.nextBigInteger())
    val total = a + b
    /*
     * Typically, with real-life math, to get a percentage we'd do: (top/bottom) x 100
     * But, in computational math, we'll do this instead: (top x 100) / bottom
     * Reason:
     *   The result of top/bottom is less than 1, when the top is smaller than the bottom.
     *   -- When that less-than-1 result is converted an Integer, that result becomes 0.
     *   -- Multiplying 0 by 100, gives us 0, instead of the percentage we're after.
     * That's a problematic loss of precision, when we're looking to calculate a percentage.
     * To avoid that loss of precission, we apply the multiplication first: (top x 100) / bottom
     */
    var a1 = BigInteger("1")                                //1
    val (aTimes100, bTimes100) = arrayOf(a, b).map { it * 100.toBigInteger() }
    val (aPercent, _) = aTimes100.divideAndRemainder(total)
    val (bPercent, _) = bTimes100.divideAndRemainder(total)
    println("$aPercent% $bPercent%")
}
