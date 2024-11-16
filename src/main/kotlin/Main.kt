package org.example

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.withContext
import kotlin.random.Random
import kotlin.system.measureTimeMillis

suspend fun main() {
    val size = persons.size
    val time = measureTimeMillis {
        withContext(newSingleThreadContext("My_Thread_Context")) {
            var personVar: Person? = null
            launch {
                getPersonsFlow().collect { i ->
                    //print(i)
                    personsTwo.add(i)
                }
            }
            launch {
                getPhoneFlow(size).collect { i ->
                    delay(100L)
                    phones.add(i)
                }
            }
            launch {
                print("[")
                delay(150L*size)
                personsTwo.forEachIndexed { index, person ->
                    personInfo.set(person, phones.get(index))
                }
                personsTwo.asFlow().onEach { print(it) }
                print("]")

            }
        }
    }

    time


}
val persons = listOf(
    Person("Игорь", "Разработчик"),
    Person("Максим", "врач"),
    Person("Анастасия", "юрист"),
    Person("Виктория", "учитель"))
fun getPersonsFlow() = persons.asFlow().onEach { delay(100L) }

fun getPhoneFlow(length: Int) = flow {
    for (i in 1 .. length) {
        val random = Random(System.currentTimeMillis())
        val stringBuilder = StringBuilder(length)
        repeat(length) {
            val randomDigit = random.nextInt(0, 10)
            stringBuilder.append(randomDigit)
        }
        emit("+7914${stringBuilder}")
    }
}
val personsTwo = mutableListOf<Person>()
val phones = mutableListOf<String>()
val personInfo = mutableMapOf<Person, String>()