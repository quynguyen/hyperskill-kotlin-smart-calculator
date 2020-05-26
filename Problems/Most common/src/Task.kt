fun main() {
    val words = mutableMapOf<String, Int>()
    var word = readLine()!!
    while (word != "stop") {
        words.merge(word, 1, Int::plus)
        word = readLine()!!
    }
    print(words.maxBy { it.value }?.key)
}
