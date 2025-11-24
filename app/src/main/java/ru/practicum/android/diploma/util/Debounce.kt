package ru.practicum.android.diploma.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * Одна Extension Функция для Дебаунса всего.
 * Можно делать дебаунс нажатий, дебаунс поиска.
 *
 * @param delayMillis Временной интервал задержки ( пример: перед поиском или перед повторным нажатием на вакансию ).
 * @param coroutineScope Scope корутин
 * @param replaceWithLatest Следует ли использовать только последний параметр или нет ( если поиск - то при true
 * запрос будет сделан на основании только последних изменений)
 * @param action Действие ( передаём в лямбду функцию которую будем выполнять )
 */
fun <T> debounce(
    delayMillis: Long,
    coroutineScope: CoroutineScope,
    replaceWithLatest: Boolean = false,
    action: suspend (T) -> Unit
): (T) -> Unit {
    var job: Job? = null
    return { param: T ->
        coroutineScope.launch {
            try {
                // отменим предыдущую Job если надо
                if (job?.isActive == true && replaceWithLatest) {
                    job?.cancel()
                }
                // Старт новой Job
                job = launch {
                    delay(delayMillis)
                    action(param)
                }
            } catch (ignored: kotlinx.coroutines.CancellationException) {
                // Игнорируем отмену корутины - это нормальное поведение для debounce
            }
        }
    }
}
