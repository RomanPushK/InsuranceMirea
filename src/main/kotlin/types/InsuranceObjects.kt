package org.example.types

enum class InsuranceObjects(val displayName: String) {
    CAR("Автомобиль"),
    HOUSE("Дом"),
    APARTMENT("Квартира"),
    LIFE("Жизнь"),
    HEALTH("Здоровье"),
    PET("Домашнее животное"),
    BUSINESS("Бизнес");

    override fun toString() = displayName
}