fun main() {
    val timerValue = readLine()!!.toInt()
    val timer = ByteTimer(timerValue)
    println(timer.time)
}

data class ByteTimer(val initTime: Int) {
    var time = initTime.coerceIn(-128, 127)
}
