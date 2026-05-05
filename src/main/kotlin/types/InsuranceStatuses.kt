package org.example.types

enum class InsuranceStatuses(val displayName: String) {
    ACTIVE("Активен"),
    EXPIRED("Истек"),
    CANCELLED("Отменен"),
    PAID("Выплачен");

    override fun toString() = displayName
}