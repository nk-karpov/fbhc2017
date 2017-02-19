package round2.a

import java.io.BufferedReader
import java.io.FileReader
import java.io.FileWriter
import java.io.PrintWriter
import java.lang.Math.min
import kotlin.system.measureNanoTime


fun main(args: Array<String>) {
    val roundName = "round2"
    val problemName = "a"
    val io = "$roundName/$problemName"
    val input = BufferedReader(FileReader("$io.in"))
    val output = PrintWriter(FileWriter("$io.out"))
    val T = input.readLine().toInt()
    val total = measureNanoTime {
        for (test in 1..T) {
            output.print("Case #$test: ")
            val time = measureNanoTime { solve(input, output) }
            println("test $test = ${time / 1e6}ms")
            output.flush()
        }
    }
    println("total = ${total / 1e6}ms")
    output.close()
    input.close()
}

fun solve(input: BufferedReader, output: PrintWriter) {
    val (n, m, k) = input.readLine().split(' ').map(String::toInt)
    var res = min(calc(n, m, k), calc(m, n, k))
    if (res == Int.MAX_VALUE) res = -1
    output.println(res)
}

fun calc(n: Int, m: Int, k: Int): Int {
    var res = Int.MAX_VALUE
    if (n <= k || m <= k) return Int.MAX_VALUE
    if (k == 1 && n >= 3 && m >= 5) res = min(5, res)
    if (k >= 2 && n >= 2 * k + 1 && m >= 3 * k + 1) res = min(4, res)
    if (m > 2 * (k + 1)) res = min((n + k - 1) / k, res)
    return res
}

/*
* ........
* ######
*
*
* */
/*
* .##.....
* .##.##..
* ....##..
* ####
* ####
*
* */
/*
.#....
...#..
###...
......
......
......
* */

