package org.example

import org.example.model.Contract
import org.example.model.User
import org.example.repositories.ContractsRepository
import org.example.repositories.UserRepository
import org.example.types.InsuranceObjects
import org.example.types.InsuranceStatuses
import org.example.types.UserFields
import org.example.util.ConsoleInput
import java.io.PrintStream

class Main {
    private val usersRepo = UserRepository()
    private val contractsRepo = ContractsRepository()
    private val console = ConsoleInput()

    init {
        usersRepo.updateAllUsersContracts(contractsRepo.getContracts())
    }

    fun start() {
        while (true) {
            console.printMainMenu()
            val choice = console.validateInputInt("Выбор", 0, 10)

            when (choice) {
                0 -> {
                    println("Выход из программы")
                    return
                }
                1 -> printUsers()
                2 -> printContracts()
                3 -> printUsersWithContracts()
                4 -> userEditorLoop()
                5 -> contractEditorLoop()
                6 -> searchUsers()
                7 -> searchContracts()
                8 -> sortUsersMenu()
                9 -> sortContractsMenu()
                10 -> showStatistics()
            }
        }
    }


    private fun printUsers() {
        println("СПИСОК ПОЛЬЗОВАТЕЛЕЙ")
        println(usersRepo)
        consoleWait()
    }

    private fun printContracts() {
        println("СПИСОК ДОГОВОРОВ")
        println(contractsRepo)
        consoleWait()
    }

    private fun printUsersWithContracts() {
        println("ПОЛЬЗОВАТЕЛИ С ДОГОВОРАМИ")
        println(usersRepo.printWithContracts())
        consoleWait()
    }


    private fun userEditorLoop() {
        while (true) {
            console.printUserEditorMenu()
            val choice = console.validateInputInt("Выбор", 0, 4)

            when (choice) {
                0 -> return
                1 -> addNewUser()
                2 -> editUser()
                3 -> deleteUser()
                4 -> printUsers()
            }
        }
    }

    private fun addNewUser() {
        println("\nДОБАВЛЕНИЕ ПОЛЬЗОВАТЕЛЯ")
        val firstName = console.readString("Имя")
        val lastName = console.readString("Фамилия")
        val passport = console.readPositiveInt("Номер паспорта")

        val id = UserRepository.nextId++
        val newUser = User(id, firstName, lastName, passport)

        if (usersRepo.addUser(newUser)) {
            println("Пользователь успешно добавлен (ID = $id)")
        } else {
            println("Ошибка при добавлении")
        }
        consoleWait()
    }

    private fun editUser() {
        val id = console.readPositiveInt("Введите ID пользователя")
        var user = usersRepo.getById(id)
        if (user == null) {
            println("Пользователь не найден")
            consoleWait()
            return
        }

        while (true) {
            println("\nРЕДАКТИРОВАНИЕ ПОЛЬЗОВАТЕЛЯ ID=$id")
            println(user!!)
            println("1. Имя")
            println("2. Фамилия")
            println("3. Паспорт")
            println("0. Назад")
            val ch = console.validateInputInt("Выбор", 0, 3)

            when (ch) {
                0 -> return
                1 -> {
                    val newVal = console.readString("Новое имя")
                    usersRepo.updateFirstName(id, newVal)
                    user = user.copy(firstName = newVal)
                }
                2 -> {
                    val newVal = console.readString("Новая фамилия")
                    usersRepo.updateLastName(id, newVal)
                    user = user.copy(lastName = newVal)
                }
                3 -> {
                    val newVal = console.readPositiveInt("Новый номер паспорта")
                    usersRepo.updatePassport(id, newVal)
                    user = user.copy(passport = newVal)
                }
            }
        }
    }

    private fun deleteUser() {
        val id = console.readPositiveInt("Введите ID пользователя для удаления")
        val user = usersRepo.getById(id)
        if (user == null) {
            println("Пользователь не найден")
            consoleWait()
            return
        }
        if (console.readYesNo("Вы действительно хотите удалить пользователя $id и все его договоры? (y/n)")) {
            deleteUser(id)
            println("Пользователь и его договоры удалены")
        }
        consoleWait()
    }


    private fun contractEditorLoop() {
        while (true) {
            console.printContractEditorMenu()
            val choice = console.validateInputInt("Выбор", 0, 5)

            when (choice) {
                0 -> return
                1 -> addNewContract()
                2 -> editContract()
                3 -> deleteContract()
                4 -> printContracts()
                5 -> {
                    val userId = console.readPositiveInt("ID пользователя")
                    println("ДОГОВОРЫ ПОЛЬЗОВАТЕЛЯ $userId")
                    println(contractsRepo.getContractsByUser(userId).joinToString(separator = "\n"))
                    consoleWait()
                }
            }
        }
    }

    private fun addNewContract() {
        println("\nДОБАВЛЕНИЕ КОНТРАКТА")
        val userId = console.readPositiveInt("Пользователь")
        if (usersRepo.getById(userId) == null) {
            println("Ошибка добавления. Пользователь с ID $userId не найден")
            consoleWait()
            return
        }

        val insuranceObject = console.readEnum<InsuranceObjects>("Объект страховки",
            InsuranceObjects.entries.toTypedArray()
        ) { it.displayName }

        val price = console.readPositiveDouble("Цена страховки")

        val startDate = console.readDate("Дата начала")
        val endDate = console.readDate("Дата окончания", start = startDate)

        val status = console.readEnum<InsuranceStatuses>("Статус",
            InsuranceStatuses.entries.toTypedArray()
        ) { it.displayName }

        val id = ContractsRepository.nextId++
        val newContract = Contract(id, userId, insuranceObject, price, startDate, endDate, status)

        if (contractsRepo.addContract(newContract)) {
            println("Контракт успешно добавлен (ID = $id)")
            usersRepo.updateUserContracts(userId, contractsRepo.getContractsByUser(userId))
        } else {
            println("Ошибка при добавлении")
        }
        consoleWait()
    }

    private fun editContract() {
        val id = console.readPositiveInt("Введите ID контракта")
        var contract = contractsRepo.getById(id)
        if (contract == null) {
            println("Контракт не найден")
            consoleWait()
            return
        }

        while (true) {
            println("\nРЕДАКТИРОВАНИЕ КОНТРАКТА ID=$id")
            println(contract!!)
            println("1. Id Пользователя")
            println("2. Объект страхования")
            println("3. Цена страхования")
            println("4. Дата начала")
            println("5. Дата окончания")
            println("6. Статус")
            println("0. Назад")
            val ch = console.validateInputInt("Выбор", 0, 6)

            when (ch) {
                0 -> return
                1 -> {
                    val newVal = console.readPositiveInt("Новый пользователь")
                    contractsRepo.updateUserID(id, newVal)
                    contract = contract.copy(userId = newVal)
                }
                2 -> {
                    val newVal = console.readEnum<InsuranceObjects>("Новый объект",
                        InsuranceObjects.entries.toTypedArray()
                    ) { it.displayName }
                    contractsRepo.updateObject(id, newVal)
                    contract = contract.copy(insuranceObject = newVal)
                }
                3 -> {
                    val newVal = console.readPositiveDouble("Новый цена страхования")
                    contractsRepo.updatePrice(id, newVal)
                    contract = contract.copy(price = newVal)
                }
                4 -> {
                    val newVal = console.readDate("Дата начала", end = contract.endDate)
                    contractsRepo.updateStartDate(id, newVal)
                    contract = contract.copy(startDate = newVal)
                }
                5 -> {
                    val newVal = console.readDate("Дата окончания", start = contract.startDate)
                    contractsRepo.updateEndDate(id, newVal)
                    contract = contract.copy(endDate = newVal)
                }
                6 -> {
                    val newVal = console.readEnum<InsuranceStatuses>("Новый статус",
                        InsuranceStatuses.entries.toTypedArray()
                    ) { it.displayName }
                    contractsRepo.updateStatus(id, newVal)
                    contract = contract.copy(status = newVal)
                }
            }
        }
    }

    private fun deleteContract() {
        val id = console.readPositiveInt("ID договора")
        val contract = contractsRepo.getById(id)
        if (contract == null) {
            println("Контракт не найден")
            consoleWait()
            return
        }
        if (console.readYesNo("Вы действительно хотите удалить контракт $id? (y/n)")) {
            deleteContract(id)
            println("Договор удалён")
        }
        consoleWait()
    }

    // ====================== ПОИСК И СОРТИРОВКА ======================
    private fun searchUsers() { /* реализуй */
        println("Реализуй поиск пользователей")
        consoleWait()
    }

    private fun searchContracts() { /* реализуй */
        println("Реализуй поиск договоров")
        consoleWait()
    }

    private fun sortUsersMenu() {
        println("СОРТИРОВКА ПОЛЬЗОВАТЕЛЕЙ")
        println("1. По ID\n2. По имени\n3. По фамилии\n4. По паспорту")
        val ch = console.validateInputInt("Введите номер", 1, 4)
        val field = when (ch) {
            1 -> UserFields.ID
            2 -> UserFields.FIRST_NAME
            3 -> UserFields.LAST_NAME
            else -> UserFields.PASSPORT
        }
        usersRepo.sortUsers(field)
        printUsers()
    }

    private fun sortContractsMenu() { /* аналогично */
        println("Реализуй сортировку договоров")
        consoleWait()
    }

    private fun showStatistics() {
        println("СТАТИСТИКА")
        val totalUsers = usersRepo.getUsers().size
        val totalContracts = contractsRepo.getContracts().size
        println("Всего пользователей: $totalUsers")
        println("Всего договоров: $totalContracts")
        consoleWait()
    }

    private fun consoleWait() {
        println("\nНажмите Enter для продолжения...")
        readln()
    }

    fun deleteUser(id: Int) {
        usersRepo.deleteUser(id)
        contractsRepo.deleteContractsByUser(id)
    }

    fun deleteContract(id: Int): Boolean {
        val contract = contractsRepo.getById(id) ?: return false
        val deleted = contractsRepo.deleteContract(id)
        if (deleted) {
            usersRepo.updateUserContracts(contract.userId, contractsRepo.getContractsByUser(contract.userId))
        }
        return deleted
    }
}

fun main() {
    System.setProperty("console.encoding", "UTF-8")
    System.setOut(PrintStream(System.out, true, "UTF-8"))
    System.setErr(PrintStream(System.err, true, "UTF-8"))

    Main().start()
}