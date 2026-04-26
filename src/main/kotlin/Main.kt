package org.example

import org.example.model.User
import org.example.repositories.ContractsRepository
import org.example.repositories.UserRepository
import org.example.types.UserFields
import java.io.PrintStream

fun main() {
    System.setProperty("console.encoding", "UTF-8")
    System.setOut(PrintStream(System.out, true, "UTF-8"))
    System.setErr(PrintStream(System.err, true, "UTF-8"))

    val users = UserRepository()
    val contracts = ContractsRepository()
    users.addUser(User(16, "Ilya", "Sventoigrafov", 228337))
    users.deleteUser(3)
    println(users)
    users.fullUpdateUser(1, User(1, "qwe", "234", 23))
    println()
    println(users)
    users.updateFirstName(1, "2342ds4")
    println(users)
    println()
    users.sortUsers(UserFields.FIRST_NAME)
    println(users)

    println()
    println(contracts)
    println()

    users.updateAllUsersContracts(contracts.getContracts())
    print(users.printWithContracts())
}