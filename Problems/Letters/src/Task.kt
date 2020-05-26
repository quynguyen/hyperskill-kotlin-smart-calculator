fun main() {
    val letters = mutableMapOf<Int, String>()
    var order = 0
    do {
        val input = readLine()!!
        letters[++order] = input
    } while (input.toLowerCase().first() != 'z')
    print(letters[4])
}
