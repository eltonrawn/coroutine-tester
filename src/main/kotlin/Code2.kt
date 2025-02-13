package org.example

import kotlinx.coroutines.*



fun main() {
    code20()
    log("-------------------------------------")
    code21()
    log("-------------------------------------")
    code22()
    log("-------------------------------------")
    code23()
}

fun code20() = runBlocking {
    launch { // launch a new coroutine and keep a reference to its Job
        delay(1000L)
        log("World!")
    }
    log("Hello")
    log("Done")
}

fun code21() = runBlocking {
    val job: Job = launch { // launch a new coroutine and keep a reference to its Job
        delay(1000L)
        log("World!")
    }
    log("Hello")
    job.join() // wait until child coroutine completes
    log("Done")
}

fun code22() = runBlocking {
    val one: Deferred<Int> = async { doSomethingUsefulOne() }
    val two: Deferred<Int> = async { doSomethingUsefulTwo() }
    log("The answer is ${one.await() + two.await()}")
}

suspend fun doSomethingUsefulOne(): Int {
    delay(1000L) // pretend we are doing something useful here
    return 13
}

suspend fun doSomethingUsefulTwo(): Int {
    delay(1000L) // pretend we are doing something useful here, too
    return 29
}

fun code23() = runBlocking {
    try {
        failedConcurrentSum()
    } catch(e: ArithmeticException) {
        log("Computation failed with ArithmeticException")
    }
}
suspend fun failedConcurrentSum(): Int = coroutineScope {
    val one = async<Int> {
        try {
            delay(Long.MAX_VALUE) // Emulates very long computation
            42
        } finally {
            log("First child was cancelled")
        }
    }
    val two = async<Int> {
        log("Second child throws an exception")
        throw ArithmeticException()
    }
    one.await() + two.await()
}


