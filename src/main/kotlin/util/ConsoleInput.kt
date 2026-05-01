package org.example.util

class ConsoleInput {
    fun clearConsole() {
        print("\u001b[H\u001b[2J")
        System.out.flush()
    }

    fun mainMenu(): Int {
        clearConsole()
        println("ГЛАВНОЕ МЕНЮ")
        println("1. Вывод пользователей")
        println("2. Вывод контрактов")
        println("3. Вывод пользователей с контрактами")
        println("4. Редактор пользователей")
        println("5. Редактор контрактов")
        println("6. Поиск пользователей")
        println("7. Поиск контрактов")
        println("8. Сортировка пользователей")
        println("9. Сортировка контрактов")
        println("10. Вычисление показателей")
        println("0. Выход")

        var res = -1
        while (res !in 0..10) {
            try {
                res = readln().toInt()
            } catch (e: Exception) {
                System.err.println("Ошибка ввода. Введите целое число - номер выбора")
            }
        }

        return res
    }
}