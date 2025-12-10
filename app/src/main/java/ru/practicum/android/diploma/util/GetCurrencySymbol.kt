// GetCurrencySymbol.kt
package ru.practicum.android.diploma.util

object CurrencySymbolMapper {

    fun getCurrencySymbol(currencyCode: String?): String {
        if (currencyCode.isNullOrBlank()) return ""
        return currencySymbols[currencyCode.uppercase()] ?: currencyCode
    }

    private val currencySymbols: Map<String, String> = run {
        val map = mutableMapOf<String, String>()

        // Основные валюты
        map.apply {
            putAll(listOf("RUR", "RUB").associateWith { "₽" })
            put("USD", "$")
            put("EUR", "€")
            put("GBP", "£")
            putAll(listOf("JPY", "CNY").associateWith { "¥" })
            put("CHF", "Fr")
        }

        // Страны СНГ и Восточной Европы
        map.apply {
            put("UAH", "₴")
            put("KZT", "₸")
            put("BYN", "Br")
            put("KGS", "с")
            put("AMD", "֏")
            put("AZN", "₼")
            put("GEL", "₾")
            put("TJS", "SM")
            put("TRY", "₺")
            put("PLN", "zł")
        }

        // Скандинавские валюты
        map.apply {
            putAll(listOf("SEK", "NOK", "DKK", "ISK").associateWith { "kr" })
        }

        // Азиатские валюты
        map.apply {
            put("HKD", "HK$")
            put("SGD", "S$")
            put("INR", "₹")
            put("THB", "฿")
            put("VND", "₫")
            put("IDR", "Rp")
            put("MYR", "RM")
            put("PHP", "₱")
            put("TWD", "NT$")
        }

        // Ближний Восток
        map.apply {
            put("AED", "د.إ")
            put("ILS", "₪")
        }

        // Австралия и Океания
        map.apply {
            put("AUD", "A$")
            put("NZD", "NZ$")
        }

        // Канада
        map.apply {
            put("CAD", "C$")
        }

        map.toMap()
    }
}
