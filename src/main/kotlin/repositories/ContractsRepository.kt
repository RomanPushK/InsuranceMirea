package org.example.repositories

import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json
import org.example.model.Contract
import org.example.types.ContractFields
import org.example.types.InsuranceObjects
import org.example.types.InsuranceStatuses
import org.example.types.UserFields
import org.example.util.ContractComparator
import org.example.util.UserComparator
import java.io.File

class ContractsRepository {
    companion object {
        var nextId = 1
    }

    private val file = File("src/data/Contracts.json")
    private val contracts: MutableList<Contract> = try {
        Json.decodeFromString<MutableList<Contract>>(file.readText())
    } catch(error: Exception) {
        System.err.println("Произошла ошибка при загрузке контрактов из файла. ${error.message}")
        mutableListOf()
    }

    init {
        nextId = contracts[contracts.lastIndex].id + 1
    }

    fun getSize(): Int {
        return contracts.size
    }

    fun getContracts(): List<Contract> {
        return contracts
    }

    fun getById(id: Int): Contract? {
        return contracts.find { it.id == id }
    }

    fun getContractsByUser(userId: Int): List<Contract> {
        return contracts.filter { it.userId == userId }
    }

    fun addContract(contract: Contract): Boolean {
        try {
            contracts.addLast(contract)
            return true
        } catch (err: Exception) { return false }
    }

    fun deleteContract(id: Int): Boolean {
        val res = contracts.removeIf { it.id == id }
        return res
    }

    fun deleteContractsByUser(userId: Int): Boolean {
        val res = contracts.removeIf { it.userId == userId }
        return res
    }

    fun sortContracts(fields: List<ContractFields>, currSort: Boolean) {
        if (fields.isEmpty()) {
            if (currSort) contracts.sortBy { it.id }
            else contracts.sortByDescending { it.id }
            return
        }
        if (fields.size == 1) {
            when (fields[0]) {
                ContractFields.ID -> {
                    if (currSort) contracts.sortBy { it.id }
                    else contracts.sortByDescending { it.id }
                }
                ContractFields.USER_ID -> {
                    if (currSort) contracts.sortBy { it.userId }
                    else contracts.sortByDescending { it.userId }
                }
                ContractFields.INSURANCE_OBJECT -> {
                    if (currSort) contracts.sortBy { it.insuranceObject }
                    else contracts.sortByDescending { it.insuranceObject }
                }
                ContractFields.PRICE -> {
                    if (currSort) contracts.sortBy { it.price }
                    else contracts.sortByDescending { it.price }
                }
                ContractFields.START_DATE -> {
                    if (currSort) contracts.sortBy { it.startDate }
                    else contracts.sortByDescending { it.startDate }
                }
                ContractFields.END_DATE -> {
                    if (currSort) contracts.sortBy { it.endDate }
                    else contracts.sortByDescending { it.endDate }
                }
                ContractFields.STATUS -> {
                    if (currSort) contracts.sortBy { it.status }
                    else contracts.sortByDescending { it.status }
                }
                ContractFields.AMOUNT -> {
                    if (currSort) contracts.sortBy { it.amount }
                    else contracts.sortByDescending { it.amount }
                }
            }
            return
        }
        val first = fields[0]
        val second = fields[1]
        contracts.sortWith(ContractComparator(first, second, currSort))
    }

    fun search(id: Int): List<Contract> {
        return contracts.filter { it.id.toString().lowercase().contains(id.toString().lowercase()) ||
                it.userId.toString().lowercase().contains(id.toString().lowercase())
        }
    }

    fun search(str: String): List<Contract> {
        val strLower = str.lowercase()
        return contracts.filter { it.insuranceObject.displayName.lowercase().contains(strLower) ||
                    it.price.toString().lowercase().contains(strLower) ||
                    it.startDate.toString().lowercase().contains(strLower) ||
                    it.endDate.toString().lowercase().contains(strLower) ||
                    it.status.displayName.lowercase().contains(strLower) ||
                    it.amount.toString().lowercase().contains(strLower)
        }
    }

    fun fullUpdateContract(id: Int, newContract: Contract): Boolean {
        val index = contracts.indexOfFirst { it.id == id }
        if (index == -1) return false
        contracts[index] = newContract.copy(id = id)
        return true
    }

    fun updateUserID(id: Int, userId: Int): Boolean {
        val index = contracts.indexOfFirst { it.id == id }
        if (index == -1) return false
        val curr = contracts[index]
        contracts[index] = curr.copy(userId = userId)
        contracts[index]
        return true
    }

    fun updateObject(id: Int, insuranceObject: InsuranceObjects): Boolean {
        val index = contracts.indexOfFirst { it.id == id }
        if (index == -1) return false
        val curr = contracts[index]
        contracts[index] = curr.copy(insuranceObject = insuranceObject)
        contracts[index].calculateAmount()
        return true
    }

    fun updatePrice(id: Int, price: Double): Boolean {
        val index = contracts.indexOfFirst { it.id == id }
        if (index == -1) return false
        val curr = contracts[index]
        contracts[index] = curr.copy(price = price)
        contracts[index].calculateAmount()
        return true
    }

    fun updateStartDate(id: Int, startDate: LocalDate): Boolean {
        val index = contracts.indexOfFirst { it.id == id }
        if (index == -1) return false
        val curr = contracts[index]
        if (startDate > curr.endDate) return false
        contracts[index] = curr.copy(startDate = startDate)
        return true
    }

    fun updateEndDate(id: Int, endDate: LocalDate): Boolean {
        val index = contracts.indexOfFirst { it.id == id }
        if (index == -1) return false
        val curr = contracts[index]
        if (endDate < curr.startDate)
        contracts[index] = curr.copy(endDate = endDate)
        return true
    }

    fun updateStatus(id: Int, status: InsuranceStatuses): Boolean {
        val index = contracts.indexOfFirst { it.id == id }
        if (index == -1) return false
        val curr = contracts[index]
        contracts[index] = curr.copy(status = status)
        return true
    }

    override fun toString(): String {
        return contracts.joinToString(separator = "\n")
    }
}