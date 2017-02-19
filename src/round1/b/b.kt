package round1.b

import java.io.BufferedReader
import java.io.FileReader
import java.io.FileWriter
import java.io.PrintWriter
import java.lang.Long.bitCount
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong
import kotlin.system.measureNanoTime

fun main(args: Array<String>) {
    fun <T> measureTimeAndGet(block: () -> T): Pair<T, Long> {
        val start = System.nanoTime()
        return Pair(block(), System.nanoTime() - start)
    }

    class Point(val x: Int, val y: Int)

    class Input(val id: Int, val r: Int, val points: List<Point>)

    class Output(val id: Int, val result: Int)

    fun read(test: Int, input: BufferedReader): Input {
        val (n, r) = input.readLine().split(' ').map(String::toInt)
        val points =
                (1..n).map {
                    input.readLine().split(' ').map(String::toInt)
                            .let { (x, y) -> Point(x, y) }
                }
        return Input(test, r, points)
    }

    fun PrintWriter.write(ret: Output) {
        println("Case #${ret.id}: ${ret.result}")
        flush()
    }

    fun get(x: Int, y: Int, r: Int, points: List<Point>): Long {
        var res = 0L
        for ((i, p) in points.withIndex()) {
            if (p.x >= x && p.x <= x + r && p.y >= y && p.y <= y + r) res += 1L shl i
        }
        return res
    }

    fun solve(test: Input): Output {
        val masks = test.points.map { (x, _) -> x }.flatMap { x ->
            test.points.map { (_, y) -> y }
                    .map { y -> get(x, y, test.r, test.points) }
        }
        val res = masks.flatMap { x -> masks.map { y -> bitCount(x or y) } }.max()!!
        return Output(test.id, res)
    }

    val input = BufferedReader(FileReader("round1/b.in"))
//    val input = BufferedReader(InputStreamReader(System.`in`))


    val output = PrintWriter(FileWriter("round1/b.out"))
//    val output = PrintWriter(OutputStreamWriter(System.out))
    val testsProcessed = AtomicInteger(0)
    val totalTime = AtomicLong(0)
    val T = input.readLine().toInt()
    val threadPool = Executors.newFixedThreadPool(4)
    val time = measureNanoTime {
        val futures = Array(T) { read(it + 1, input) }.map {
            threadPool.submit(
                    Callable<Output> {
                        val (ret, time) = measureTimeAndGet { solve(it) }
                        System.err.println("Finished test ${it.id} in ${time / 1e6}ms, processed ${testsProcessed.incrementAndGet()} of $T tests")
                        totalTime.addAndGet(time)
                        ret
                    })
        }
        futures.forEach { output.write(it.get()) }
        threadPool.shutdownNow()
    }
    System.err.println("Processed $testsProcessed in ${time / 1e6}ms, and ${totalTime.toLong() / 1e6}ms of total time")
}


