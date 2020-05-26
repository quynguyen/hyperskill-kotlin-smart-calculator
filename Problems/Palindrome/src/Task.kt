fun main() {
    val s = readLine()!!
    for (index in 0..s.lastIndex / 2) {
        if (s[index] != s[s.lastIndex - index]) {
            println("no")
            return
        }
    }
    println("yes")
}
