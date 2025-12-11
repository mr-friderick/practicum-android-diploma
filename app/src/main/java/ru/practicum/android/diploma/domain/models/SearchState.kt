package ru.practicum.android.diploma.domain.models

sealed interface SearchState<out T> {
    data class Success<T>(val data: T) : SearchState<T>
    object NotFound : SearchState<Nothing>
    object NoInternet : SearchState<Nothing>
    data class Error(val message: String) : SearchState<Nothing>
}
