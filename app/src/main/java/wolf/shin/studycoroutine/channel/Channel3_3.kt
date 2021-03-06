package wolf.shin.studycoroutine.channel

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.selects.select

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

suspend fun someone(channel: Channel<String>, name: String){
    for (comment in channel){
        println("${name}: ${comment}")
        channel.send(comment.drop(1) + comment.first()) // 자동집 + 군
        delay(100L)
    }
}

fun FanInOut3() = runBlocking {
    val channel = Channel<String>() // 채널
    launch {
        someone(channel, "동휘") // 생산자
    }

    launch {
        someone(channel, "정화") // 생산자
    }
    channel.send("군자동집")
    delay(1000L)
    coroutineContext.cancelChildren()
}

fun CoroutineScope.sayFast() = produce<String> {
    while (true){
        delay(50L)
        send("군자동")
    }
}

fun CoroutineScope.sayHouse() = produce<String> { // return 리시브 채널
    while (true){
        delay(150L)
        send("집")
    }
}

fun FanInOut4() = runBlocking {
    val fast = sayFast()
    val house = sayHouse()

    repeat(5){
        select { // 5번동안 먼저 도착하는 소비자만 처리한다.
            fast.onReceive{
                println("fast: $it")
            }
            house.onReceive{
                println("house: $it")
            }
        }
    }
}