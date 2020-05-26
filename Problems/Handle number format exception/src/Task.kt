import java.lang.NumberFormatException

fun parseCardNumber(cardNumber: String): Long {
    if (!"""^\d{4}\s\d{4}\s\d{4}\s\d{4}$""".toRegex().matches(cardNumber)) {
        throw NumberFormatException("Invalid format")
    }
    val number = cardNumber.replace(" ", "")
    return number.toLong()
}
