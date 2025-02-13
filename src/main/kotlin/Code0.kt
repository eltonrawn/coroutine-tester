package org.example

import kotlinx.coroutines.*

private var zeroTime = System.currentTimeMillis()
fun log(message: Any?) = println("${System.currentTimeMillis() - zeroTime}"
        + "[${Thread.currentThread().name}] $message")

fun main() {
    code01()
    log("-------------------------------------")
    code02()
    log("-------------------------------------")
    code03()
}

fun code01() = runBlocking { // this: CoroutineScope was created with new co-routine context
    launch { // launch a new coroutine and continue
        delay(1000L) // non-blocking delay for 1 second (default time unit is ms)
        log("World!") // print after delay
    }
    log("Hello") // main coroutine continues while a previous one is delayed
}

fun code02() = runBlocking { // this: CoroutineScope
    launch { code02DoWorld() }
    log("Hello") // main coroutine continues while a previous one is delayed
}

suspend fun code02DoWorld() {
    delay(1000L)
    log("World!")
}

fun code03() = runBlocking { // this: CoroutineScope
    log("extra")
    code03DoWorld()
}

suspend fun code03DoWorld() = coroutineScope {  // this: CoroutineScope was created but uses existing co-routine context
    launch {
        delay(1000L)
        log("World!")
    }
    log("Hello")
}
