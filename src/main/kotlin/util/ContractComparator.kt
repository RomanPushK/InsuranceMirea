package org.example.util

import org.example.model.Contract
import org.example.types.ContractFields

class ContractComparator(val first: ContractFields, val second: ContractFields, val currSort: Boolean) : Comparator<Contract> {
    override fun compare(o1: Contract, o2: Contract): Int {
        val firstComparison = when (first) {
            ContractFields.ID -> o1.id.compareTo(o2.id)
            ContractFields.USER_ID -> o1.userId.compareTo(o2.userId)
            ContractFields.INSURANCE_OBJECT -> o1.insuranceObject.compareTo(o2.insuranceObject)
            ContractFields.PRICE -> o1.price.compareTo(o2.price)
            ContractFields.START_DATE -> o1.startDate.compareTo(o2.startDate)
            ContractFields.END_DATE -> o1.endDate.compareTo(o2.endDate)
            ContractFields.STATUS -> o1.status.compareTo(o2.status)
            ContractFields.AMOUNT -> o1.amount.compareTo(o2.amount)
        }
        val res = if (firstComparison == 0) {
            when (second) {
                ContractFields.ID -> o1.id.compareTo(o2.id)
                ContractFields.USER_ID -> o1.userId.compareTo(o2.userId)
                ContractFields.INSURANCE_OBJECT -> o1.insuranceObject.compareTo(o2.insuranceObject)
                ContractFields.PRICE -> o1.price.compareTo(o2.price)
                ContractFields.START_DATE -> o1.startDate.compareTo(o2.startDate)
                ContractFields.END_DATE -> o1.endDate.compareTo(o2.endDate)
                ContractFields.STATUS -> o1.status.compareTo(o2.status)
                ContractFields.AMOUNT -> o1.amount.compareTo(o2.amount)
            }
        } else {
            firstComparison
        }
        if (currSort) return res
        return -res
    }
}