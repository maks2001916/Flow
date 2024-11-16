package org.example

class Person(val name: String, val role: String) {
    override fun toString(): String {
        return "Пользователь: $name, $role,"
    }
}