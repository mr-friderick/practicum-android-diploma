package ru.practicum.android.diploma.util

fun getCurrencySymbol(currencyCode: String?): String {
    if (currencyCode.isNullOrBlank()) return ""

    return when (currencyCode.uppercase()) {
        "RUR", "RUB" -> "₽"  // Российский рубль
        "USD" -> "$"         // Доллар США
        "EUR" -> "€"         // Евро
        "GBP" -> "£"         // Фунт стерлингов
        "JPY" -> "¥"         // Японская иена
        "CNY" -> "¥"         // Китайский юань
        "CHF" -> "Fr"        // Швейцарский франк
        "UAH" -> "₴"         // Украинская гривна
        "KZT" -> "₸"         // Казахстанский тенге
        "BYN" -> "Br"        // Белорусский рубль
        "KGS" -> "с"         // Киргизский сом
        "AMD" -> "֏"         // Армянский драм
        "AZN" -> "₼"         // Азербайджанский манат
        "GEL" -> "₾"         // Грузинский лари
        else -> currencyCode // Если символ не найден, возвращаем код
    }
}
