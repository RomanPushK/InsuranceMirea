package org.example.model

import kotlinx.serialization.Serializable

@Serializable
data class User (
    val id: Int,
    val firstName: String,
    val lastName: String,
    val passport: Int
) {
    @kotlinx.serialization.Transient
    val contracts: MutableList<Contract> = mutableListOf()

    fun updateContracts(contracts: List<Contract>): Boolean {
        this.contracts.clear()
        this.contracts += contracts
        return true
    }

    fun printWithContracts(): String {
        if (contracts.isEmpty()) return "ID пользователя: $id, Имя: $firstName, Фамилия: $lastName, Паспорт: $passport, Контракты: { }"
        return "ID пользователя: $id, Имя: $firstName, Фамилия: $lastName, Паспорт: $passport, Контракты: {" +
                contracts.joinToString(separator = "\n\t", prefix = "\n\t", postfix = "\n") + "}"
    }

    override fun toString(): String {
        return "ID пользователя: $id, Имя: $firstName, Фамилия: $lastName, Паспорт: $passport, Контракты: ${contracts.size}"
    }
}