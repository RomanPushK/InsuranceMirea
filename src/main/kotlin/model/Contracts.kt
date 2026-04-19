package org.example.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import org.example.types.InsuranceObjects
import org.example.types.InsuranceStatuses

@Serializable
data class Contracts (
    val id: Int,
    val userId: Int,
    var insuranceObject: InsuranceObjects,
    var price: Int,
    var startDate: LocalDate,
    var endDate: LocalDate,
    var status: InsuranceStatuses
) {
    fun calculateAmount(): Int {
        return when (insuranceObject) {
            InsuranceObjects.CAR -> price * 10
            InsuranceObjects.HOUSE -> price * 12
            InsuranceObjects.APARTMENT -> price * 15
            InsuranceObjects.LIFE -> price * 30
            InsuranceObjects.HEALTH -> price * 5
            InsuranceObjects.PET -> price * 7
            InsuranceObjects.BUSINESS -> price * 12
            else -> price * 5
        }
    }

    @Transient
    var amount: Int = calculateAmount()
}