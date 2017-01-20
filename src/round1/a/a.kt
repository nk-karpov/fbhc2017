package round1.a

import java.io.BufferedReader
import java.io.FileReader
import java.io.FileWriter
import java.io.PrintWriter
import java.lang.Math.min
import kotlin.system.measureNanoTime
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    val input = BufferedReader(FileReader("round1/a.in"))
    val output = PrintWriter(FileWriter("round1/a.out"))
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

fun solve(input: BufferedReader, output: PrintWriter) {
    val (n, m) = input.readLine().split(' ').map(String::toInt)
    val C = Array(n, { input.readLine().split(' ').map(String::toInt).sorted() })
    val dp = Array(n + 1, { LongArray(n + 1, { Long.MAX_VALUE }) })
    dp[0][0] = 0
    for (i in C.indices) {
        for (j in dp[i].indices) {
            if (j < i) continue
            if (dp[i][j] == Long.MAX_VALUE) continue
            var sum = 0.toLong()
            for (k in 0..m) {
                if (k > 0) sum += C[i][k - 1]
                if (j + k > n) continue
                dp[i + 1][j + k] = min(dp[i + 1][j + k], dp[i][j] + k * k + sum)
            }
        }
    }
    output.println("${dp[n][n]}")
}
