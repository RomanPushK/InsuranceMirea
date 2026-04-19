package org.example.repositories

import kotlinx.serialization.json.Json
import org.example.model.User
import java.io.File
import kotlin.collections.mutableListOf

class UserRepository {
    private val file = File("data/Users.json")
    val users: MutableList<User> = try {
        Json.decodeFromString<MutableList<User>>(file.readText())
    } catch(error: Exception) {
        System.err.println("Произошла ошибка при загрузке пользователей из файла. ${error.message}")
        mutableListOf()
    }

    fun addUser(user: User) {
        users.addLast(user)
    }

    fun deleteUser(id: Int) {
        users.filter { user -> user.id != id }
    }

    fun updateUser(id: Int, field: String, newValue: String): Boolean {
        val user = users.find { it.id == id } ?: return false
        return when (field) {
            "firstName" -> { user.firstName = newValue; true }
            "lastName" -> { user.lastName = newValue; true }
            "passport" -> newValue.toIntOrNull()?.let {
                user.passport = it
                true
            } ?: false
            else -> false
        }
    }

    override fun toString(): String {
        return super.toString()
    }
}