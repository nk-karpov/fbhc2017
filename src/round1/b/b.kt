package round1.b

import java.io.BufferedReader
import java.io.FileReader
import java.io.FileWriter
import java.io.PrintWriter
import kotlin.system.measureNanoTime
import kotlin.system.measureTimeMillis


fun main(args: Array<String>) {
    val input = BufferedReader(FileReader("round1/b.in"))
    val output = PrintWriter(FileWriter("round1/b.out"))
    val total = measureTimeMillis {
        val T = input.readLine().toInt()
        for (test in 1..T) {
            output.print("Case #$test: ")
            val time = measureNanoTime {
                solve(input, output)
            }
            println("$test = ${time / 1e6}ms")
            output.flush()
        }
    }
    println("total = ${total}ms")
    output.close()
    input.close()
}

data class Point(val x: Int, val y: Int)

fun solve(input: BufferedReader, output: PrintWriter) {
    val (n, r) = input.readLine().split(' ').map(String::toInt)
    val points =
            (1..n).map {
                input.readLine().split(' ').map(String::toInt)
                        .let { (x, y) -> Point(x, y) }
            }
    val masks =
            points.map { (x, _) -> x }.flatMap {
                x ->
                points.map { (_, y) -> y }
                        .map { y ->
                            get(x, y, r, points)
                        }
            }.distinct()
    val res =
            masks.flatMap {
                x ->
                masks.map {
                    y ->
                    bitCount(x or y)
                }
            }.max()
    output.println(res)
}

fun bitCount(x: Long): Int {
    var t = x
    var res = 0
    while (t > 0) {
        if (t % 2 == 1.toLong()) {
            res++
        }
        t /= 2
    }
    return res
}

fun get(x: Int, y: Int, r: Int, points: List<Point>): Long {
    var res = 0.toLong()
    for ((i, p) in points.withIndex()) {
        if (p.x >= x && p.x <= x + r && p.y >= y && p.y <= y + r) res += 1.toLong() shl i
    }
    return res
}
