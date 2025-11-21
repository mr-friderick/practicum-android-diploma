package ru.practicum.android.diploma.util

import android.os.Build
import androidx.fragment.app.Fragment
import java.io.Serializable

/**
Временный класс-заглушка пока не создана модель
После создания модели удалить
*/

data class Vacancy(

val id : Int
): Serializable



/**
Экстеншен функция для получения детальной вакансии на фрагменте из бандл
Пригодится когда будем передавать вакансию на целевой фрагмент ( при нажатии )
*/

fun Fragment.getVacancyFromArguments(): Vacancy? {
return arguments.let { bundle ->
    if (Build.VERSION.SDK_INT >= 33) {
        bundle?.getSerializable(Constants.VACANCY, Vacancy::class.java)

    } else {
        bundle?.getSerializable(Constants.VACANCY) as Vacancy
    }



}
}



