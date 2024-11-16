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
        newSingleThreadContext("My_Thread_Context").use { threadContext ->
            withContext(threadContext) {
                val personsJob = launch {
                    getPersonsFlow().collect { i ->
                        personsTwo.add(i)
                    }
                }
                val phonesJob = launch {
                    getPhoneFlow(size).collect { i ->
                        delay(100L)
                        phones.add(i)
                    }
                }

                // Ждем завершения всех корутин
                personsJob.join()
                phonesJob.join()

                // Теперь можно производить вывод
                val printJob = launch {
                    print("[")
                    personsTwo.forEachIndexed { index, person ->
                        personInfo[person] = phones[index]
                        print("${person} ${phones[index]}, ")
                    }
                    print("]")
                }

                // Ждем завершения вывода
                printJob.join()
            }
        }
    }

    println("\nExecution time: $time ms")
}

val persons = listOf(
    Person("Игорь", "Разработчик"),
    Person("Максим", "Врач"),
    Person("Анастасия", "Юрист"),
    Person("Виктория", "Учитель")
)

fun getPersonsFlow() = persons.asFlow().onEach { delay(100L) }

fun getPhoneFlow(length: Int) = flow {
    for (i in 1..length) {
        delay(100L) // Simulating some delay for fetching phone numbers
        val random = Random(System.currentTimeMillis() + i) // Different seed for each number
        val stringBuilder = StringBuilder()
        repeat(10) {
            val randomDigit = random.nextInt(0, 10)
            stringBuilder.append(randomDigit)
        }
        emit("+7${stringBuilder}") // Измените номер телефона, чтобы он был к правильному формату
    }
}

val personsTwo = mutableListOf<Person>()
val phones = mutableListOf<String>()
val personInfo = mutableMapOf<Person, String>()