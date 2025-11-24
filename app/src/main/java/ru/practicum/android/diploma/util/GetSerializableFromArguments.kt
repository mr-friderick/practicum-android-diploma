package ru.practicum.android.diploma.util

import android.os.Build
import androidx.fragment.app.Fragment
import java.io.Serializable

/**
 * Экстеншен функция для получения детальной вакансии на фрагменте из бандл
 * Пригодится когда будем передавать вакансию на целевой фрагмент ( при нажатии на детальный фрагмент)
 */
@Suppress("DEPRECATION")
inline fun <reified T : Serializable> Fragment.getSerializableFromArguments(key: String): T? =
    arguments?.run {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializable(key, T::class.java)
            containsKey(key) && getSerializable(key)?.javaClass == T::class.java -> getSerializable(key) as? T
            else -> null
        }
    }
