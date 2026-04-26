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
        return "UserID: $id, First name: $firstName, Last name: $lastName, Passport: $passport, Contracts: {\n" +
                contracts.joinToString(separator = "\n\t", prefix = "\t") + "\n}"
    }

    override fun toString(): String {
        return "UserID: $id, First name: $firstName, Last name: $lastName, Passport: $passport"
    }
}