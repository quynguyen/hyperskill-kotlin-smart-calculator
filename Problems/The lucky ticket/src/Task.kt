fun main() {
    val s = readLine()!!
    val start = s.substring(0..2).sumBy { it.toInt() }
    val end = s.substring(s.lastIndex - 2, s.length).sumBy { it.toInt() }
    val r = if (s.length >= 3 && start == end) "Lucky" else "Regular"
    println(r)
}
