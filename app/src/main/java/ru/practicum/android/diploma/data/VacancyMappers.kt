package ru.practicum.android.diploma.data

import ru.practicum.android.diploma.data.network.dto.AddressDto
import ru.practicum.android.diploma.data.network.dto.ContactsDto
import ru.practicum.android.diploma.data.network.dto.EmployerDto
import ru.practicum.android.diploma.data.network.dto.EmploymentDto
import ru.practicum.android.diploma.data.network.dto.ExperienceDto
import ru.practicum.android.diploma.data.network.dto.FilterAreaDto
import ru.practicum.android.diploma.data.network.dto.FilterIndustryDto
import ru.practicum.android.diploma.data.network.dto.PhoneDto
import ru.practicum.android.diploma.data.network.dto.SalaryDto
import ru.practicum.android.diploma.data.network.dto.ScheduleDto
import ru.practicum.android.diploma.data.network.dto.VacancyDetailDto
import ru.practicum.android.diploma.data.network.dto.VacancyDto
import ru.practicum.android.diploma.domain.models.AddressModel
import ru.practicum.android.diploma.domain.models.ContactsModel
import ru.practicum.android.diploma.domain.models.EmployerModel
import ru.practicum.android.diploma.domain.models.EmploymentModel
import ru.practicum.android.diploma.domain.models.ExperienceModel
import ru.practicum.android.diploma.data.localstorage.dto.FilterDto
import ru.practicum.android.diploma.domain.models.FilterAreaModel
import ru.practicum.android.diploma.domain.models.FilterIndustryModel
import ru.practicum.android.diploma.domain.models.FilterModel
import ru.practicum.android.diploma.domain.models.PhoneModel
import ru.practicum.android.diploma.domain.models.SalaryModel
import ru.practicum.android.diploma.domain.models.ScheduleModel
import ru.practicum.android.diploma.domain.models.VacancyDetailModel
import ru.practicum.android.diploma.domain.models.VacancyModel

fun VacancyDto.toModel(): VacancyModel {
    return VacancyModel(
        found = found,
        pages = pages,
        page = page,
        items = items.map { it.toModel() }
    )
}

fun VacancyDetailDto.toModel(): VacancyDetailModel {
    return VacancyDetailModel(
        id = id,
        name = name,
        description = description,
        salary = salary?.toModel(),
        address = address?.toModel(),
        experience = experience?.toModel(),
        schedule = schedule?.toModel(),
        employment = employment?.toModel(),
        contacts = contacts?.toModel(),
        employer = employer.toModel(),
        area = area.toModel(),
        skills = skills,
        url = url,
        industry = industry.toModel()
    )
}

fun SalaryDto.toModel(): SalaryModel {
    return SalaryModel(
        from = from,
        to = to,
        currency = currency
    )
}

fun AddressDto.toModel(): AddressModel {
    return AddressModel(
        city = city,
        street = street,
        building = building,
        raw = raw
    )
}

fun ExperienceDto.toModel(): ExperienceModel {
    return ExperienceModel(
        id = id,
        name = name
    )
}

fun ScheduleDto.toModel(): ScheduleModel {
    return ScheduleModel(
        id = id,
        name = name
    )
}

fun EmploymentDto.toModel(): EmploymentModel {
    return EmploymentModel(
        id = id,
        name = name
    )
}

fun ContactsDto.toModel(): ContactsModel {
    return ContactsModel(
        id = id,
        name = name,
        email = email,
        phones = phones.map { it.toModel() }
    )
}

fun PhoneDto.toModel(): PhoneModel {
    return PhoneModel(
        comment = comment,
        formatted = formatted
    )
}

fun EmployerDto.toModel(): EmployerModel {
    return EmployerModel(
        id = id,
        name = name,
        logo = logo
    )
}

fun FilterAreaDto.toModel(): FilterAreaModel {
    return FilterAreaModel(
        id = id,
        name = name,
        parentId = parentId,
        areas = areas.map { it.toModel() }
    )
}

fun FilterIndustryDto.toModel(): FilterIndustryModel {
    return FilterIndustryModel(
        id = id,
        name = name
    )
}

fun FilterDto.toModel(): FilterModel {
    return FilterModel(
        areaId = areaId,
        areaName = areaName,
        industryId = industryId,
        industryName = industryName,
        salary = salary,
        onlyWithSalary = onlyWithSalary
    )
}

fun FilterModel.toDto(): FilterDto {
    return FilterDto(
        areaId = areaId,
        areaName = areaName,
        industryId = industryId,
        industryName = industryName,
        salary = salary,
        onlyWithSalary = onlyWithSalary
    )
}
