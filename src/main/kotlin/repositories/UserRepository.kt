package org.example.repositories

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.example.model.Contract
import org.example.model.User
import org.example.types.UserFields
import org.example.util.UserComparator
import java.io.File
import kotlin.collections.mutableListOf

class UserRepository {
    companion object {
        var nextId = 1
    }

    private val file = File("src/data/Users.json")
    private val users: MutableList<User> = try {
        Json.decodeFromString<MutableList<User>>(file.readText())
    } catch(error: Exception) {
        System.err.println("Произошла ошибка при загрузке пользователей из файла. ${error.message}")
        mutableListOf()
    }

    constructor() {
        nextId = users[users.lastIndex].id + 1
    }

    fun saveUsers() {
        file.writeText(Json.encodeToString<List<User>>(users.sortedBy { it.id }))
    }

    fun getSize(): Int {
        return users.size
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
            saveUsers()
            return true
        } catch (err: Exception) { return false }
    }

    fun deleteUser(id: Int): Boolean {
        val res = users.removeIf { it.id == id }
        if (res) saveUsers()
        return res
    }

    fun fullUpdateUser(id: Int, newUser: User): Boolean {
        val index = users.indexOfFirst { it.id == id }
        if (index == -1) return false
        users[index] = newUser.copy(id = id)
        saveUsers()
        return true
    }

    fun updateFirstName(id: Int, firstName: String): Boolean {
        val index = users.indexOfFirst { it.id == id }
        if (index == -1) return false
        val curr = users[index]
        users[index] = curr.copy(firstName = firstName)
        users[index].contracts.addAll(curr.contracts)
        saveUsers()
        return true
    }

    fun updateLastName(id: Int, lastName: String): Boolean {
        val index = users.indexOfFirst { it.id == id }
        if (index == -1) return false
        val curr = users[index]
        users[index] = curr.copy(lastName = lastName)
        users[index].contracts.addAll(curr.contracts)
        saveUsers()
        return true
    }

    fun updatePassport(id: Int, passport: Int): Boolean {
        val index = users.indexOfFirst { it.id == id }
        if (index == -1) return false
        val curr = users[index]
        users[index] = curr.copy(passport = passport)
        users[index].contracts.addAll(curr.contracts)
        saveUsers()
        return true
    }

    fun sortUsers(fields: List<UserFields>, currSort: Boolean) {
        if (fields.isEmpty()) {
            if (currSort) users.sortBy { it.id }
            else users.sortByDescending { it.id }
            saveUsers()
            return
        }
        if (fields.size == 1) {
            when (fields[0]) {
                UserFields.ID -> {
                    if (currSort) users.sortBy { it.id }
                    else users.sortByDescending { it.id }
                }
                UserFields.FIRST_NAME -> {
                    if (currSort) users.sortBy { it.firstName }
                    else users.sortByDescending { it.firstName }
                }
                UserFields.LAST_NAME -> {
                    if (currSort) users.sortBy { it.lastName }
                    else users.sortByDescending { it.lastName }
                }
                UserFields.PASSPORT -> {
                    if (currSort) users.sortBy { it.passport }
                    else users.sortByDescending { it.passport }
                }
            }
            saveUsers()
            return
        }
        val first = fields[0]
        val second = fields[1]
        users.sortWith(UserComparator(first, second, currSort))
        saveUsers()
        return
    }

    fun search(id: Int): List<User> {
        return users.filter { it.id.toString().lowercase().contains(id.toString().lowercase()) }
    }

    fun search(str: String): List<User> {
        val strLower = str.lowercase()
        return users.filter { it.firstName.lowercase().contains(strLower) ||
                it.lastName.lowercase().contains(strLower) ||
                it.passport.toString().lowercase().contains(strLower)
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