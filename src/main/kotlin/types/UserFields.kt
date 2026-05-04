package org.example.types

enum class UserFields(val displayName: String) {
    ID("ID Пользователя"),
    FIRST_NAME("Имя"),
    LAST_NAME("Фамилия"),
    PASSPORT("Пасспорт");

    override fun toString() = displayName
}