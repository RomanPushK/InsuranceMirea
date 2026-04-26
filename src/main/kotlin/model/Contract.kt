package org.example.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import org.example.types.InsuranceObjects
import org.example.types.InsuranceStatuses

@Serializable
data class Contract (
    val id: Int,
    val userId: Int,
    val insuranceObject: InsuranceObjects,
    val price: Int,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val status: InsuranceStatuses
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

    fun updateAmount() {
        amount = calculateAmount()
    }

    @kotlinx.serialization.Transient
    var amount: Int = calculateAmount()

    override fun toString(): String {
        return "ContractID: $id, UserID: $userId, Insurance object: $insuranceObject, Price: $price, Start date: $startDate, End date: $endDate, Status: $status, Amount: $amount"
    }
}