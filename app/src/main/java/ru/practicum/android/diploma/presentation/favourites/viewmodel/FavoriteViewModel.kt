package ru.practicum.android.diploma.presentation.favourites.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.favourites.FavoriteVacancyInteractor

class FavoriteViewModel(
    private val favoriteInteractor: FavoriteVacancyInteractor
) : ViewModel() {

    private val _state = MutableLiveData<FavoriteViewState>()
    val state: MutableLiveData<FavoriteViewState> = _state

    fun getAll() {
        viewModelScope.launch {
            favoriteInteractor.getAll()
                .catch {
                    _state.postValue(FavoriteViewState.Error)
                }
                .collect { vacancies ->
                    if (vacancies.isEmpty()) {
                        _state.postValue(FavoriteViewState.Empty)
                    } else {
                        _state.postValue(FavoriteViewState.Content(vacancies))
                    }
                }
        }
    }
}
