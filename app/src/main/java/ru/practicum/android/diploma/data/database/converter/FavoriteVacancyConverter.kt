package ru.practicum.android.diploma.data.database.converter

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.practicum.android.diploma.data.database.entity.FavoriteVacancyEntity
import ru.practicum.android.diploma.domain.models.AddressModel
import ru.practicum.android.diploma.domain.models.ContactsModel
import ru.practicum.android.diploma.domain.models.EmployerModel
import ru.practicum.android.diploma.domain.models.EmploymentModel
import ru.practicum.android.diploma.domain.models.ExperienceModel
import ru.practicum.android.diploma.domain.models.FilterAreaModel
import ru.practicum.android.diploma.domain.models.FilterIndustryModel
import ru.practicum.android.diploma.domain.models.SalaryModel
import ru.practicum.android.diploma.domain.models.ScheduleModel
import ru.practicum.android.diploma.domain.models.VacancyDetailModel

class FavoriteVacancyConverter(
    private val gson: Gson
) {
    fun map(model: VacancyDetailModel): FavoriteVacancyEntity {
        return FavoriteVacancyEntity(
            id = model.id,
            name = model.name,
            description = model.description,
            salary = gson.toJson(model.salary),
            address = gson.toJson(model.address),
            experience = gson.toJson(model.experience),
            schedule = gson.toJson(model.schedule),
            employment = gson.toJson(model.employment),
            contacts = gson.toJson(model.employment),
            employer = gson.toJson(model.employer),
            area = gson.toJson(model.area),
            skills = gson.toJson(model.skills),
            url = model.url,
            industry = gson.toJson(model.employment)
        )
    }

    fun map(entity: FavoriteVacancyEntity): VacancyDetailModel {
        return VacancyDetailModel(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            salary = gson.fromJson(entity.salary, SalaryModel::class.java),
            address = gson.fromJson(entity.address, AddressModel::class.java),
            experience = gson.fromJson(entity.experience, ExperienceModel::class.java),
            schedule = gson.fromJson(entity.schedule, ScheduleModel::class.java),
            employment = gson.fromJson(entity.employment, EmploymentModel::class.java),
            contacts = gson.fromJson(entity.contacts, ContactsModel::class.java),
            employer = gson.fromJson(entity.employer, EmployerModel::class.java),
            area = gson.fromJson(entity.area, FilterAreaModel::class.java),
            skills = gson.fromJson(entity.skills, object : TypeToken<List<String>>() {}.type),
            url = entity.url,
            industry = gson.fromJson(entity.industry, FilterIndustryModel::class.java)
        )
    }
}
