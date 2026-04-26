package org.example.repositories

import kotlinx.serialization.json.Json
import org.example.model.Contract
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
        contracts[index] = newContract
        return true
    }

    override fun toString(): String {
        return contracts.joinToString(separator = "\n")
    }
}