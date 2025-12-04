package ru.practicum.android.diploma.domain.models

data class VacancyDetailModel(
    val id: String,
    val name: String,
    val description: String,
    val salary: SalaryModel?,
    val address: AddressModel?,
    val experience: ExperienceModel?,
    val schedule: ScheduleModel?,
    val employment: EmploymentModel?,
    val contacts: ContactsModel?,
    val employer: EmployerModel,
    val area: FilterAreaModel,
    val skills: List<String> = emptyList(),
    val url: String,
    val industry: FilterIndustryModel
)

data class SalaryModel(
    val from: Int?,
    val to: Int?,
    val currency: String?
)

data class AddressModel(
    val city: String,
    val street: String,
    val building: String,
    val raw: String
)

data class ExperienceModel(
    val id: String,
    val name: String
)

data class ScheduleModel(
    val id: String,
    val name: String
)

data class EmploymentModel(
    val id: String,
    val name: String
)

data class ContactsModel(
    val id: String,
    val name: String,
    val email: String,
    val phones: List<PhoneModel> = emptyList()
)

data class PhoneModel(
    val comment: String?,
    val formatted: String
)

data class EmployerModel(
    val id: String,
    val name: String,
    val logo: String
)
