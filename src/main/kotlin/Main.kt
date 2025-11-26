package ru.netology.deadlock

import kotlin.concurrent.thread

fun main() {
    val resourceA = Any()
    val resourceB = Any()

    val consumerA = Consumer("A")
    val consumerB = Consumer("B")

    val t1 = thread {
        consumerA.lockFirstAndTrySecond(resourceA, resourceB)
    }
    val t2 = thread {
        consumerB.lockFirstAndTrySecond(resourceA, resourceB)
    }
    val t3 = thread {
        Consumer("D").lockFirstAndTrySecond(resourceA,resourceB)
    }

    t1.join()
    t2.join()
    t3.join()

    println("main successfully finished")
}
    /* Избавляемся от Deadlock захватываем ресурсы по хешу
    Золотое правило: Всегда захватывать общие ресурсы
    в одном и том же порядке (например, по хэшу, имени, идентификатору)
     */
class Consumer(private val name: String) {
    fun lockFirstAndTrySecond(first: Any, second: Any) {
        val (low,high) = if (System.identityHashCode(first) < System.identityHashCode(second)) {
            first to second
        }else {
            second to first
        }
        synchronized(low) {
            println("$name locked first, sleep and wait for second")
            Thread.sleep(1000)
            lockSecond(high)
        }
    }

    fun lockSecond(second: Any) {
        synchronized(second) {
            println("$name locked second")
        }
    }
}