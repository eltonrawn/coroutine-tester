package org.example

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun main() {
    code30()
    log("-------------------------------------")
    code30WithDelay()
    log("-------------------------------------")
    code31()
    log("-------------------------------------")
    code32()
    log("-------------------------------------")
    code33()
    log("-------------------------------------")
    code34()

}

fun code30() = runBlocking {
    try {
        val channel = Channel<Int>()
        launch {
            // this might be heavy CPU-consuming computation or async logic,
            // we'll just send five squares
            for (x in 1..5) {
                log("sending ${x * x}")
                channel.send(x * x)
            }
            channel.close()
        }
        // here we print five received integers:
        repeat(6) {
            log(channel.receive())
        }
    } catch(e: Exception) {
        log("Done! with exception: $e")
    }

}

fun code30WithDelay() = runBlocking {
    try {
        val channel = Channel<Int>()
        launch {
            // this might be heavy CPU-consuming computation or async logic,
            // we'll just send five squares
            for (x in 1..5) {
                log("sending ${x * x}")
                channel.send(x * x)
            }
            channel.close()
        }
        // here we print five received integers:
        repeat(6) {
            delay(1000L)
            log(channel.receive())
        }
    } catch(e: Exception) {
        log("Done! with exception: $e")
    }

}

fun code31() = runBlocking {
    val channel = Channel<Int>()
    launch {
        for (x in 1..5) {
            delay(1000L)
            log("sending ${x * x}")
            channel.send(x * x)
        }
        channel.close() // we're done sending
    }
    // here we print received values using `for` loop (until the channel is closed)
    for (y in channel) println(y)
    log("Done!")
}

fun code32() = runBlocking {
    fun produceSquares(): ReceiveChannel<Int> = produce {
        for (x in 1..5) send(x * x)
    }
    val squares = produceSquares()
    squares.consumeEach { println(it) }
    log("Done!")
}

fun code33() = runBlocking {
    fun launchProcessor(id: Int, channel: ReceiveChannel<Int>) = launch {
    for (msg in channel) {
        log("Processor #$id received $msg")
    }
}

    fun produceNumbers() = produce<Int> {
        var x = 1 // start from 1
        while (true) {
            send(x++) // produce next
            delay(100) // wait 0.1s
        }
    }
    val producer = produceNumbers()
    repeat(5) { launchProcessor(it, producer) }
    delay(950)
    producer.cancel() // cancel producer coroutine and thus kill them all
}

data class Ball(var hits: Int)
fun code34() = runBlocking {
    val table = Channel<Ball>() // a shared table
    launch { player("ping", table) }
    launch { player("pong", table) }
    table.send(Ball(0)) // serve the ball
    delay(1000) // delay 1 second
    coroutineContext.cancelChildren() // game over, cancel them
}

suspend fun player(name: String, table: Channel<Ball>) {
    for (ball in table) { // receive the ball in a loop
        ball.hits++
        println("$name $ball")
        delay(300) // wait a bit
        table.send(ball) // send the ball back
    }
}
