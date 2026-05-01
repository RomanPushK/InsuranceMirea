package org.example.repositories

import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json
import org.example.model.Contract
import org.example.model.User
import org.example.types.InsuranceObjects
import org.example.types.InsuranceStatuses
import java.io.File

class ContractsRepository {
    private val file = File("src/data/Contracts.json")
    private val contracts: MutableList<Contract> = try {
        Json.decodeFromString<MutableList<Contract>>(file.readText())
    } catch(error: Exception) {
        System.err.println("Произошла ошибка при загрузке контрактов из файла. ${error.message}")
        mutableListOf()
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
        return contracts.removeIf { it.id == id }
    }

    fun deleteContractsByUser(userId: Int): Boolean {
        return contracts.removeIf { it.userId == userId }
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
        contracts[index].calculateAmount()
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