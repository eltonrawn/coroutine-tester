package org.example

import kotlinx.coroutines.*



fun main() {
    executeRunBlocking1()
    log("-------------------------------------")
    executeRunBlocking2()
    log("-------------------------------------")
    executeRunBlocking3()
}

fun executeRunBlocking1() {
    runBlocking {
        doWorld1()
        log("Done")
    }
}
// Concurrently executes both sections
suspend fun doWorld1() = coroutineScope { // this: CoroutineScope
    launch {
        delay(2000L)
        log("World 2")
    }
    launch {
        delay(1000L)
        log("World 1")
    }
    log("Hello")
}
fun executeRunBlocking3() {
    runBlocking {
        launch {
            doWorld1()
        }
        log("Done")
    }
}

fun executeRunBlocking2() {
    runBlocking {
        doWorld2(this)
        log("Done")
    }
}

suspend fun doWorld2(scope: CoroutineScope) {
    with(scope) {
        launch {
            delay(2000L)
            log("World 2")
        }
        launch {
            delay(1000L)
            log("World 1")
        }
    }
    // doWorld's own execution resumes immediately after launching coroutines
    log("Hello")
}








