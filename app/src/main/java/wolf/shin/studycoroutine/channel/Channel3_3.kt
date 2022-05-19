package wolf.shin.studycoroutine.channel

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun CoroutineScope.produceNumbers1() = produce<Int> {
    var x = 1
    while (true){
        send(x++)
    }
}

fun CoroutineScope.processNumber(id: Int, channel: ReceiveChannel<Int>) = launch {
    channel.consumeEach {
        println("$id 가 $it 를 받았습니다")
    }
}

fun FanInOut1() = runBlocking {
    val producer = produceNumbers1()
    repeat(5){
        processNumber(it, producer)
    }

    delay(1000L)
    producer.cancel()
}

suspend fun produceNumbers2(channel: SendChannel<Int>, from: Int, interval: Long){
    var x = from
    while(true){
        channel.send(x)
        x+=2
        delay(interval)
    }
}

fun CoroutineScope.processNumber(channel: ReceiveChannel<Int>) = launch {
    channel.consumeEach {
        println("${it}를 받았습니다")
    }
}

fun FanInOut2() = runBlocking {
    val channel = Channel<Int>() // 채널

    launch {
        produceNumbers2(channel, 1, 100L) // 생산자
    }

    launch {
        produceNumbers2(channel, 2, 150L) // 생산자
    }

    processNumber(channel) // 소비자
    delay(1000L)
    coroutineContext.cancelChildren()
}