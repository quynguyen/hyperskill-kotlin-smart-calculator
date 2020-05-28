data class City(val name: String) {
    var population: Int = 0
        set(value) {
            field = value.coerceAtLeast(0).coerceAtMost(50_000_000)
        }
}
