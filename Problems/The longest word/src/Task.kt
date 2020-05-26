fun main() {
    readLine()!!.split(" ").maxBy { it.length }.let { print(it) }
}
