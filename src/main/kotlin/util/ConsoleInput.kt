package org.example.util

import kotlinx.datetime.LocalDate
import org.example.types.UserFields

class ConsoleInput {
    fun clearConsole() {
        repeat(50) {
            println()
        }
    }
    
    fun validateInputInt(str: String, startNum: Int, endNum: Int): Int {
        var res = Int.MIN_VALUE
        while (res == Int.MIN_VALUE) {
            print("$str: ")
            try {
                res = readln().toInt()
                if (res !in startNum..endNum) throw Exception()
            } catch (e: Exception) {
                println("Ошибка ввода. Введите целое число от $startNum до $endNum")
            }
        }

        return res
    }

    fun readPositiveInt(str: String): Int {
        var res = Int.MIN_VALUE
        while (res == Int.MIN_VALUE) {
            print("$str: ")
            try {
                res = readln().toInt()
                if (res < 0) throw Exception()
            } catch (e: Exception) {
                println("Ошибка ввода. Введите положительное число")
            }
        }
        return res
    }

    fun readPositiveDouble(str: String): Double {
        var res = Double.MIN_VALUE
        while (res == Double.MIN_VALUE) {
            print("$str: ")
            try {
                res = readln().toDouble()
                if (res < 0) throw Exception()
            } catch (e: Exception) {
                println("Ошибка ввода. Введите положительное число")
            }
        }

        return res
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
        println("ГЛАВНОЕ МЕНЮ")
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
        println("РЕДАКТОР ПОЛЬЗОВАТЕЛЕЙ")
        println("1. Добавить пользователя")
        println("2. Редактировать пользователя (по ID)")
        println("3. Удалить пользователя")
        println("4. Список всех пользователей")
        println("0. Назад в главное меню")
    }

    fun printContractEditorMenu() {
        println("РЕДАКТОР КОНТРАКТОВ")
        println("1. Добавить контракт")
        println("2. Редактировать контракт (по ID)")
        println("3. Удалить контракт")
        println("4. Список всех контрактов")
        println("5. Контракты пользователя (по ID пользователя)")
        println("0. Назад в главное меню")
    }



    fun printUserSortMenu(fields: List<UserFields>, ) {
        println("СОРТИРОВКА ПОЛЬЗОВАТЕЛЕЙ")
        if (!fields.isEmpty()) {
            print("Выбранные поля: ")
            println(fields.joinToString(separator = ", ", postfix = "\n"))
        }
        else {
            println("По стандарту сортировка происходит по Id")
        }
        println()
        println("1. Выбрать поля сортировки")
        println("2. Отсортировать и вывести")
        println("0. Назад")
    }

    fun printUserSortOptions(fields: MutableList<UserFields>, currSort: Boolean) {
        println("\nВыберите, как будет происходить сортировка")
        println("Можно выбрать максимум 2 поля")
        if (!fields.isEmpty()) {
            print("Выбранные поля: ")
            println(fields.joinToString(separator = ", "))
        }
        if (currSort) {
            println("Выбранный порядок: По возрастанию\n")
        }
        else println("Выбранный порядок: По убыванию")
        println("1. ID")
        println("2. Имя")
        println("3. Фамилия")
        println("4. Пасспорт")
        println("5. Удалить последний вариант")
        println("6. Поменять порядок сортировки")
        println("0. Назад")
    }

    fun printSearchMenu(title: String) {
        println("ПОИСК — $title")
        println("1. Поиск по ID")
        println("2. Поиск по тексту")
        println("0. Назад")
    }
}