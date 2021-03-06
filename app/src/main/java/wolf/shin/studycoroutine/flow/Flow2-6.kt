package wolf.shin.studycoroutine.flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

/**
 * flow에서 지원하는 flatMap은 3가지이다
 *
 * flatMapConcat
 * flatMapMerge
 * flatMapLatest
 *
 * */

fun requestFlow(i: Int) = flow{
    emit("$i First")
    delay(500L)
    emit("$i Second")
}

fun flowFlatMapConcat() = runBlocking {
    val startTime = System.currentTimeMillis()
    (1..3).asFlow().onEach { delay(100L) }
        .flatMapConcat { /** 첫 번째 요소에 대해서 플레트닝을 하고 나서 두번째 요소를 한다.*/
            requestFlow(it) // 결과를 하나로 묶어서 이어붙인다.
        }
        .collect { value -> println("$value at ${System.currentTimeMillis() - startTime} ms from Start")}
}
fun flowFlatMapMerge() = runBlocking {
    val startTime = System.currentTimeMillis()
    (1..3).asFlow().onEach { delay(100L) }
        .flatMapMerge { /** 첫번째 요소를 플레트닝 하고 다음 요소가 바로 이어나온다. 기다리지 않음. */
            requestFlow(it)
        }
        .collect { value -> println("$value at ${System.currentTimeMillis() - startTime} ms from Start")}
}
fun flowFlatMapLatest() = runBlocking {
    val startTime = System.currentTimeMillis()
    (1..3).asFlow().onEach { delay(100L) }
        .flatMapLatest { /** 순차적으로 진행되지만 다음요소가 들어오면 진행중인 요소의 플레트닝은 취소된다. */
            requestFlow(it)
        }
        .collect { value -> println("$value at ${System.currentTimeMillis() - startTime} ms from Start")}
}