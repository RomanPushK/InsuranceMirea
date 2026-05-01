package org.example.repositories

import kotlinx.serialization.json.Json
import org.example.model.Contract
import org.example.model.User
import org.example.types.UserFields
import java.io.File
import kotlin.collections.mutableListOf

class UserRepository {
    private val file = File("src/data/Users.json")
    private val users: MutableList<User> = try {
        Json.decodeFromString<MutableList<User>>(file.readText())
    } catch(error: Exception) {
        System.err.println("Произошла ошибка при загрузке пользователей из файла. ${error.message}")
        mutableListOf()
    }

    fun getUsers(): List<User> {
        return users
    }

    fun getById(id: Int): User? {
        return users.find { it.id == id }
    }

    fun addUser(user: User): Boolean {
        try {
            users.addLast(user)
            return true
        } catch (err: Exception) { return false }
    }

    fun deleteUser(id: Int): Boolean {
        return users.removeIf { it.id == id }
    }

    fun fullUpdateUser(id: Int, newUser: User): Boolean {
        val index = users.indexOfFirst { it.id == id }
        if (index == -1) return false
        users[index] = newUser.copy(id = id)
        return true
    }

    fun updateFirstName(id: Int, firstName: String): Boolean {
        val index = users.indexOfFirst { it.id == id }
        if (index == -1) return false
        val curr = users[index]
        users[index] = curr.copy(firstName = firstName)
        return true
    }

    fun updateLastName(id: Int, lastName: String): Boolean {
        val index = users.indexOfFirst { it.id == id }
        if (index == -1) return false
        val curr = users[index]
        users[index] = curr.copy(lastName = lastName)
        return true
    }

    fun updatePassport(id: Int, passport: Int): Boolean {
        val index = users.indexOfFirst { it.id == id }
        if (index == -1) return false
        val curr = users[index]
        users[index] = curr.copy(passport = passport)
        return true
    }

    fun sortUsers(field: UserFields): Boolean {
        return when (field) {
            UserFields.ID -> { users.sortBy { it.id } ; true }
            UserFields.FIRST_NAME -> { users.sortBy { it.firstName } ; true }
            UserFields.LAST_NAME -> { users.sortBy { it.lastName } ; true }
            UserFields.PASSPORT -> { users.sortBy { it.passport } ; true}
            else -> false
        }
    }

    fun updateAllUsersContracts(contracts: List<Contract>): Boolean {
        users.forEach { user -> user.updateContracts(contracts.filter { it.userId == user.id }) }
        return true
    }

    fun updateUserContracts(userId: Int, contracts: List<Contract>): Boolean {
        getById(userId)?.updateContracts(contracts) ?: return false
        return true
    }

    fun printWithContracts(): String {
        val result: MutableList<String> = mutableListOf()
        users.forEach { result.add(it.printWithContracts() + "\n") }
        return result.joinToString(separator = "\n")
    }

    override fun toString(): String {
        return users.joinToString(separator = "\n")
    }
}