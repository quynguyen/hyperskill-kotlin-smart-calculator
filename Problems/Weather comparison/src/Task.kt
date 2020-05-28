data class City(val name: String) {
    var degrees: Int = 0
        get(): Int {
            return if (field >= -92 && field <= 57) {
                field
            } else when (name) {
                "Moscow" -> 5
                "Hanoi" -> 20
                else -> 30
            }
        }
}

fun main() {
    val first = readLine()!!.toInt()
    val second = readLine()!!.toInt()
    val third = readLine()!!.toInt()
    val firstCity = City("Dubai")
    firstCity.degrees = first
    val secondCity = City("Moscow")
    secondCity.degrees = second
    val thirdCity = City("Hanoi")
    thirdCity.degrees = third
    val cities = arrayOf(firstCity, secondCity, thirdCity)
    val dupe = cities.map { it.degrees }.toSet().size < cities.size
    val coldestCity = if (dupe) {
        "neither"
    } else {
        cities.minBy { it.degrees }!!.name
    }
    print(coldestCity)
}
