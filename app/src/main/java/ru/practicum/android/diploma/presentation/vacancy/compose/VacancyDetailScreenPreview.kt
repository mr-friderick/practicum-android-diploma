package ru.practicum.android.diploma.presentation.vacancy.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import ru.practicum.android.diploma.domain.models.AddressModel
import ru.practicum.android.diploma.domain.models.EmployerModel
import ru.practicum.android.diploma.domain.models.ExperienceModel
import ru.practicum.android.diploma.domain.models.FilterAreaModel
import ru.practicum.android.diploma.domain.models.FilterIndustryModel
import ru.practicum.android.diploma.domain.models.SalaryModel
import ru.practicum.android.diploma.domain.models.VacancyDetailModel
import ru.practicum.android.diploma.presentation.theme.AppTheme
import ru.practicum.android.diploma.presentation.vacancy.viewmodel.VacancyDetailViewState

private val mockVacancy = VacancyDetailModel(
    id = "123",
    name = "Senior Android Developer",
    salary = SalaryModel(
        from = 250000,
        to = 400000,
        currency = "RUR"
    ),
    employer = EmployerModel(
        id = "1",
        name = "Яндекс",
        logo = "https://example.com/logo.png"
    ),
    address = AddressModel(
        city = "Москва",
        street = "Льва Толстого",
        building = "16",
        raw = "Москва, ул. Льва Толстого, д. 16"
    ),
    experience = ExperienceModel(
        id = "between3And6",
        name = "От 3 до 6 лет"
    ),
    description = "Разработка новых фич для мобильного приложения...",
    skills = listOf("Kotlin", "Coroutines", "Jetpack Compose", "Room"),
    contacts = null,
    schedule = null,
    employment = null,
    area = FilterAreaModel(
        id = 1,
        name = "Москва",
        parentId = 2,
        areas = emptyList()
    ),
    url = "",
    industry = FilterIndustryModel(
        id = 1,
        name = "Информационные технологии"
    )
)

@Preview(name = "Успешная загрузка")
@Composable
private fun VacancyDetailSuccessPreview() {
    AppTheme {
        VacancyDetailScreen(
            state = VacancyDetailViewState.VacancyDetail(mockVacancy),
            onBackClick = {}
        )
    }
}

@Preview(name = "Загрузка")
@Composable
private fun VacancyDetailLoadingPreview() {
    AppTheme {
        VacancyDetailScreen(
            state = VacancyDetailViewState.Loading,
            onBackClick = {}
        )
    }
}

@Preview(name = "Не найдено")
@Composable
private fun VacancyDetailNotFoundPreview() {
    AppTheme {
        VacancyDetailScreen(
            state = VacancyDetailViewState.NotFound,
            onBackClick = {}
        )
    }
}

@Preview(name = "Ошибка сервера")
@Composable
private fun VacancyDetailServerErrorPreview() {
    AppTheme {
        VacancyDetailScreen(
            state = VacancyDetailViewState.Error("Серверная ошибка"),
            onBackClick = {}
        )
    }
}

@Preview(name = "Нет интернета")
@Composable
private fun VacancyDetailNoInternetPreview() {
    AppTheme {
        VacancyDetailScreen(
            state = VacancyDetailViewState.NoInternet,
            onBackClick = {}
        )
    }
}
