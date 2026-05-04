package org.example.util

import kotlinx.datetime.LocalDate
import org.example.types.ContractFields
import org.example.types.UserFields

class ConsoleInput {
    fun clearConsole() {
        repeat(50) {
            println()
        }
    }
    
    fun validateInputInt(str: String, startNum: Int, endNum: Int): Int {
        while (true) {
            print("$str: ")
            try {
                val res = readln().toInt()
                if (res in startNum..endNum) return res
                throw Exception()
            } catch (e: Exception) {
                println("Ошибка ввода. Введите целое число от $startNum до $endNum")
            }
        }
    }

    fun readPositiveInt(str: String): Int {
        while (true) {
            print("$str: ")
            try {
                val res = readln().toInt()
                if (res >= 0) return res
                throw Exception()
            } catch (e: Exception) {
                println("Ошибка ввода. Введите положительное число")
            }
        }
    }

    fun readPositiveDouble(str: String): Double {
        while (true) {
            print("$str: ")
            try {
                val res = readln().toDouble()
                if (res >= 0) return res
                throw Exception()
            } catch (e: Exception) {
                println("Ошибка ввода. Введите положительное число")
            }
        }
    }

    fun readString(str: String): String {
        print("$str: ")
        return readln()
    }

    fun readYesNo(str: String): Boolean {
        print("$str: ")
        return readln().lowercase() in listOf<String>("yes", "y", "да", "д")
    }

    fun readDate(str: String): LocalDate {
        while (true) {
            print("$str / формат: (YYYY-MM-DD): ")
            try {
                return LocalDate.parse(readln().trim())
            } catch (e: Exception) {
                println("Ошибка формата даты. Используйте YYYY-MM-DD")
            }
        }
    }

    fun readDate(
        str: String,
        start: LocalDate = LocalDate(1970, 1, 1),
        end: LocalDate = LocalDate(2077, 1, 1)
    ): LocalDate {
        while (true) {
            print("$str / формат: (YYYY-MM-DD): ")
            try {
                val res = LocalDate.parse(readln().trim())
                if (res in start..end) return res
                println("Введите корректную дату")
            } catch (e: Exception) {
                println("Ошибка формата даты. Используйте YYYY-MM-DD")
            }
        }
    }

    fun <T : Enum<T>> readEnum(str: String, enumValues: Array<T>, display: (T) -> String = { it.name }): T {
        println("$str: ")
        enumValues.forEachIndexed { index, value ->
            println("\t${index + 1}. ${display(value)}")
        }
        val choice = validateInputInt("Выберите номер", 1, enumValues.size)
        return enumValues[choice - 1]
    }

    fun printMainMenu() {
        println("\nГЛАВНОЕ МЕНЮ")
        println("1. Вывод пользователей")
        println("2. Вывод контрактов")
        println("3. Вывод пользователей с детальными контрактами")
        println("4. Редактор пользователей")
        println("5. Редактор контрактов")
        println("6. Поиск пользователей")
        println("7. Поиск контрактов")
        println("8. Сортировка пользователей")
        println("9. Сортировка контрактов")
        println("10. Вычисление показателей")
        println("0. Выход")
    }

    fun printUserEditorMenu() {
        println("\nРЕДАКТОР ПОЛЬЗОВАТЕЛЕЙ")
        println("1. Добавить пользователя")
        println("2. Редактировать пользователя (по ID)")
        println("3. Удалить пользователя")
        println("4. Список всех пользователей")
        println("0. Назад в главное меню")
    }

    fun printContractEditorMenu() {
        println("\nРЕДАКТОР КОНТРАКТОВ")
        println("1. Добавить контракт")
        println("2. Редактировать контракт (по ID)")
        println("3. Удалить контракт")
        println("4. Список всех контрактов")
        println("5. Контракты пользователя (по ID пользователя)")
        println("0. Назад в главное меню")
    }

    fun <T: Enum<T>> printSortMenu(str: String, fields: List<T>, currSort: Boolean) {
        println("\nСОРТИРОВКА $str")
        print("Выбранные поля: ")
        if (!fields.isEmpty()) {
            println(fields.joinToString(separator = ", "))
        }
        else {
            println("Нет")
            println("По стандарту сортировка происходит по ID")
        }
        if (currSort) {
            println("Выбранный порядок: По возрастанию\n")
        }
        else println("Выбранный порядок: По убыванию\n")
    }

    fun <T: Enum<T>>printSortOptions(fields: List<T>, currSort: Boolean) {
        println("\nВыберите, как будет происходить сортировка")
        println("Можно выбрать максимум 2 поля")
        print("Выбранные поля: ")
        if (!fields.isEmpty()) {
            println(fields.joinToString(separator = ", "))
        } else println("Нет")
        if (currSort) {
            println("Выбранный порядок: По возрастанию\n")
        }
        else println("Выбранный порядок: По убыванию\n")
    }

    fun printUserSortMenu(fields: List<UserFields>, currSort: Boolean) {
        printSortMenu("ПОЛЬЗОВАТЕЛЕЙ", fields, currSort)
        println("1. Выбрать поля сортировки")
        println("2. Отсортировать и вывести")
        println("0. Назад")
    }

    fun printUserSortOptions(fields: MutableList<UserFields>, currSort: Boolean) {
        printSortOptions(fields, currSort)
        println("1. ID")
        println("2. Имя")
        println("3. Фамилия")
        println("4. Паспорт")
        println("5. Удалить последнее поле")
        println("6. Поменять порядок сортировки")
        println("0. Назад")
    }

    fun printContractSortMenu(fields: List<ContractFields>, currSort: Boolean) {
        printSortMenu("КОНТРАКТОВ", fields, currSort)
        println("1. Выбрать поля сортировки")
        println("2. Отсортировать и вывести")
        println("0. Назад")
    }

    fun printContractSortOptions(fields: MutableList<ContractFields>, currSort: Boolean) {
        printSortOptions(fields, currSort)
        println("1. ID")
        println("2. ID пользователя")
        println("3. Объект страховки")
        println("4. Цена страховки")
        println("5. Дата начала")
        println("6. Дата истечения")
        println("7. Статус")
        println("8. Страховая выплата")
        println("9. Удалить последнее поле")
        println("10. Поменять порядок сортировки")
        println("0. Назад")
    }

    fun printSearchMenu(str: String) {
        println("\nПОИСК $str")
        println("1. Поиск по ID")
        println("2. Поиск по тексту")
        println("0. Назад")
    }
}