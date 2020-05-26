fun main() {
    val vowels = listOf('a', 'e', 'i', 'o', 'u').associateWith { it - 'a' + 1 }
    val letter = readLine()!!.toLowerCase().first()
    println(vowels[letter] ?: 0)
}
