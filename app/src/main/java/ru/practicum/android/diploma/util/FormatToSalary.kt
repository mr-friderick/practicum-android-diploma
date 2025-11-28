package ru.practicum.android.diploma.util

private const val CHUNK_SIZE = 3

/**
 * Форматирует число с разбиением на разряды
 * Пример: 1000000 -> "1 000 000"
 * Использовать : Для отображения зарплаты по ТЗ
 */
fun Int.formatToSalary(): String {
    return  this.toString()
        .reversed()
        .chunked(CHUNK_SIZE)
        .joinToString(" ")
        .reversed()

}
