package org.example

import org.example.model.Contract
import org.example.model.User
import org.example.repositories.ContractsRepository
import org.example.repositories.UserRepository
import org.example.types.ContractFields
import org.example.types.InsuranceObjects
import org.example.types.InsuranceStatuses
import org.example.types.UserFields
import org.example.util.ConsoleInput
import java.awt.Paint
import java.io.PrintStream

fun MutableList<UserFields>.tryAdd(field: UserFields) {
    if (this.size >= 2) {
        println("Ошибка. Нельзя добавить больше полей сортировки")
        return
    }
    else if (this.contains(field)) {
        println("Ошибка. Данное поле уже выбрано")
        return
    }
    else {
        this.addLast(field)
    }
}

fun MutableList<ContractFields>.tryAdd(field: ContractFields) {
    if (this.size >= 2) {
        println("Ошибка. Нельзя добавить больше полей сортировки")
        return
    }
    else if (this.contains(field)) {
        println("Ошибка. Данное поле уже выбрано")
        return
    }
    else {
        this.addLast(field)
    }
}

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
                    usersRepo.saveUsers()
                    contractsRepo.saveContracts()
                    println("Данные сохранены. Выход из программы.")
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
        println("\nСПИСОК ПОЛЬЗОВАТЕЛЕЙ")
        println(usersRepo)
        consoleWait()
    }

    private fun printContracts() {
        println("\nСПИСОК ДОГОВОРОВ")
        println(contractsRepo)
        consoleWait()
    }

    private fun printUsersWithContracts() {
        println("\nПОЛЬЗОВАТЕЛИ С ДОГОВОРАМИ")
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
        if (console.readYesNo("Вы действительно хотите удалить пользователя $id и все его контракты? (y/n)")) {
            deleteUser(id)
            println("Пользователь и его контракты удалены")
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
                    val userContracts = contractsRepo.getContractsByUser(userId)
                    if (userContracts.isEmpty()) {
                        println("У ПОЛЬЗОВАТЕЛЯ НЕТ КОНТРАКТОВ")
                    }
                    else {
                        println("КОНТРАКТЫ ПОЛЬЗОВАТЕЛЯ $userId")
                        println(contractsRepo.getContractsByUser(userId).joinToString(separator = "\n"))
                    }
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

        val insuranceObject = console.readEnum<InsuranceObjects>("Объект страхования",
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
                    if (usersRepo.getById(newVal) == null) {
                        println("Пользователь не найден")
                        continue
                    }
                    contractsRepo.updateUserID(id, newVal)
                    usersRepo.updateUserContracts(contract.userId, contractsRepo.getContractsByUser(contract.userId))
                    usersRepo.updateUserContracts(newVal, contractsRepo.getContractsByUser(newVal))
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
        val id = console.readPositiveInt("ID контракта")
        val contract = contractsRepo.getById(id)
        if (contract == null) {
            println("Контракт не найден")
            consoleWait()
            return
        }
        if (console.readYesNo("Вы действительно хотите удалить контракт $id? (y/n)")) {
            deleteContract(id)
            println("Контракт удалён")
        }
        consoleWait()
    }

    private fun searchUsers() {
        while (true) {
            console.printSearchMenu("ПОЛЬЗОВАТЕЛЕЙ")
            val ch = console.validateInputInt("Выбор", 0, 2)
            when (ch) {
                0 -> return
                1 -> searchUsersId()
                2 -> searchUsersString()
            }
        }
    }

    private fun searchUsersId() {
        println("Введите ID для поиска")
        val ch = console.readPositiveInt("Id")
        val found = usersRepo.search(ch)
        if (found.isEmpty()) println("\nНичего не найдено")
        else println(found.joinToString(separator = "\n", prefix = "\n"))
        consoleWait()
    }

    private fun searchUsersString() {
        println("Введите ID для поиска")
        val ch = console.readString("Подстрока")
        val found = usersRepo.search(ch)
        if (found.isEmpty()) println("\nНичего не найдено")
        else println(found.joinToString(separator = "\n", prefix = "\n"))
        consoleWait()
    }

    private fun searchContracts() {
        while (true) {
            console.printSearchMenu("КОНТРАКТОВ")
            val ch = console.validateInputInt("Выбор", 0, 2)
            when (ch) {
                0 -> return
                1 -> searchContractsId()
                2 -> searchContractsString()
            }
        }
    }

    private fun searchContractsId() {
        println("Введите ID для поиска")
        val ch = console.readPositiveInt("Id")
        val found = contractsRepo.search(ch)
        if (found.isEmpty()) println("\nНичего не найдено")
        else println(found.joinToString(separator = "\n", prefix = "\n"))
        consoleWait()
    }

    private fun searchContractsString() {
        println("Введите ID для поиска")
        val ch = console.readString("Подстрока")
        val found = contractsRepo.search(ch)
        if (found.isEmpty()) println("\nНичего не найдено")
        else println(found.joinToString(separator = "\n", prefix = "\n"))
        consoleWait()
    }

    private fun sortUsersMenu() {
        var fields: MutableList<UserFields> = mutableListOf()
        var currSort = true
        while (true) {
            console.printUserSortMenu(fields, currSort)
            val ch = console.validateInputInt("Выбор", 0, 2)
            when (ch) {
                0 -> return
                1 -> {
                    val res = selectUserSort(fields, currSort)
                    fields = res.first
                    currSort = res.second
                }
                2 -> {
                    usersRepo.sortUsers(fields, currSort)
                    println(usersRepo)
                    consoleWait()
                }
            }
        }
    }

    private fun selectUserSort(
        selected: MutableList<UserFields> = mutableListOf(),
        currSort_: Boolean = true
    ): Pair<MutableList<UserFields>, Boolean> {
        var currSort = currSort_
        while (true) {
            console.printUserSortOptions(selected, currSort)
            val ch = console.validateInputInt("Выбор",0, 6)
            when (ch) {
                0 -> break
                1 -> selected.tryAdd(UserFields.ID)
                2 -> selected.tryAdd(UserFields.FIRST_NAME)
                3 -> selected.tryAdd(UserFields.LAST_NAME)
                4 -> selected.tryAdd(UserFields.PASSPORT)
                5 -> {
                    if (selected.isNotEmpty()) {
                        selected.removeLast()
                    }
                    else println("В списке нет элементов")
                }
                6 -> currSort = !currSort
            }
        }
        return Pair(selected, currSort)
    }

    private fun sortContractsMenu() {
        var fields: MutableList<ContractFields> = mutableListOf()
        var currSort = true
        while (true) {
            console.printContractSortMenu(fields, currSort)
            val ch = console.validateInputInt("Выбор", 0, 2)
            when (ch) {
                0 -> return
                1 -> {
                    val res = selectContractsSort(fields, currSort)
                    fields = res.first
                    currSort = res.second
                }
                2 -> {
                    contractsRepo.sortContracts(fields, currSort)
                    println(contractsRepo)
                    consoleWait()
                }
            }
        }
    }

    private fun selectContractsSort(
        selected: MutableList<ContractFields> = mutableListOf(),
        currSort_: Boolean = true
    ): Pair<MutableList<ContractFields>, Boolean> {
        var currSort = currSort_
        while (true) {
            console.printContractSortOptions(selected, currSort)
            val ch = console.validateInputInt("Выбор",0, 10)
            when (ch) {
                0 -> break
                1 -> selected.tryAdd(ContractFields.ID)
                2 -> selected.tryAdd(ContractFields.USER_ID)
                3 -> selected.tryAdd(ContractFields.INSURANCE_OBJECT)
                4 -> selected.tryAdd(ContractFields.PRICE)
                5 -> selected.tryAdd(ContractFields.START_DATE)
                6 -> selected.tryAdd(ContractFields.END_DATE)
                7 -> selected.tryAdd(ContractFields.STATUS)
                8 -> selected.tryAdd(ContractFields.AMOUNT)
                9 -> {
                    if (selected.isNotEmpty()) {
                        selected.removeLast()
                    }
                    else println("В списке нет элементов")
                }
                10 -> currSort = !currSort
            }
        }
        return Pair(selected, currSort)
    }

    private fun calculateProfit(data: List<Contract>): Double {
        var totalProfit: Double = 0.0
        for (i in data) {
            if (i.status == InsuranceStatuses.EXPIRED) totalProfit += i.price
            else if (i.status == InsuranceStatuses.PAID) totalProfit -= i.amount
        }
        return totalProfit
    }

    private fun showStatistics() {
        println("\nСТАТИСТИКА СТРАХОВОЙ КОМПАНИИ")
        val users = usersRepo.getUsers()
        val contracts = contractsRepo.getContracts()
        if (contracts.isEmpty()) {
            println("Нет данных для статистики.")
            consoleWait()
            return
        }
        val totalContracts = contracts.size
        val activeContracts = contracts.count { it.status == InsuranceStatuses.ACTIVE }
        val expiredContracts = contracts.count { it.status == InsuranceStatuses.EXPIRED }
        val paidContracts = contracts.count { it.status == InsuranceStatuses.PAID }
        val totalPremium = contracts.sumOf { it.price }
        val avgPremium = totalPremium / totalContracts
        val totalAmount = contracts.filter { it.status == InsuranceStatuses.PAID } .sumOf { it.amount }
        val activePremium = contracts.filter { it.status == InsuranceStatuses.ACTIVE }
            .sumOf { it.price }

        println("Количество пользователей: ${users.size}")
        println("Количество контрактов: $totalContracts")
        println("Активных контрактов: $activeContracts")
        println("Истекших контрактов: $expiredContracts")
        println("Выплаченных контрактов: $paidContracts")
        println("Общий доход: ${"%.2f".format(totalPremium - totalAmount)}")
        println("Средняя цена контракта: ${"%.2f".format(avgPremium)}")
        println("Общая сумма выплат: ${"%.2f".format(totalAmount)}")
        println("Общая цена активных контрактов: ${"%.2f".format(activePremium)}")

        if (users.isNotEmpty()) {
            var bestUser = users[0]
            var bestProfit = calculateProfit(bestUser.contracts)

            var worstUser = users[0]
            var worstProfit = calculateProfit(worstUser.contracts)

            for (user in users) {
                val profit = calculateProfit(user.contracts)
                if (profit > bestProfit) {
                    bestProfit = profit
                    bestUser = user
                }
                if (profit < worstProfit) {
                    worstProfit = profit
                    worstUser = user
                }
            }
            if (bestProfit > 0) println("Самый прибыльный клиент: " +
                    "${bestUser.firstName} ${bestUser.lastName}, прибыль: $bestProfit")
            if (worstProfit < 0) println("Самый убыточный клиент: " +
                    "${worstUser.firstName} ${worstUser.lastName}, убыток: $worstProfit")
            consoleWait()
        }
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