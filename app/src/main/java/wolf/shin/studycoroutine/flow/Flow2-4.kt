package wolf.shin.studycoroutine.flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun flowBufferSimple() = flow{
    for (i in 1..3){
        delay(100L)
        emit(i)
    }
}

fun flowBuffer() = runBlocking {
    val time = measureTimeMillis {
        flowBufferSimple().buffer().collect { value -> // buffer연산자를 사용하면 보내는쪽에서 더 이상 기다리지 않게한다.
            delay(300L)
            println(value)
        }
    }

    println("Collected in $time ms")
}

fun flowConflate() = runBlocking {
    val time = measureTimeMillis {
        flowBufferSimple().conflate().collect { value -> // conflate 사용하면 처리하지 못하는동안 받았던 값을 누락시킨다.
            delay(300L)
            println(value)
        }
    }

    println("Collected in $time ms")
}

fun flowCollectLatest() = runBlocking {
    val time = measureTimeMillis {
        flowBufferSimple().collectLatest { value -> // collectLatest : 값을 처리하는 과정중에 다른 값이 들어오면 이전에 처리하는 내용을 cancel한다.
            delay(300L)
            println(value)
        }
    }

    println("Collected in $time ms")
}