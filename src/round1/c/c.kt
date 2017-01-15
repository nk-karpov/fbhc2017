package round1.c

import java.io.BufferedReader
import java.io.FileReader
import java.io.FileWriter
import java.io.PrintWriter
import java.lang.Math.max
import java.lang.Math.min
import kotlin.system.measureNanoTime
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    val input = BufferedReader(FileReader("round1/c.in"))
    val output = PrintWriter(FileWriter("round1/c.out"))
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
    val (n, m, k) = input.readLine().split(' ').map(String::toInt)
    val w = Array(n, { LongArray(n, { Long.MAX_VALUE }) })
    for (i in w.indices) w[i][i] = 0
    repeat(m) {
        val (a, b, g) = input.readLine().split(' ').map(String::toInt)
        w[a - 1][b - 1] = min(w[a - 1][b - 1], g.toLong())
        w[b - 1][a - 1] = min(w[b - 1][a - 1], g.toLong())
    }
    for (l in w.indices) {
        for (i in w.indices) {
            for (j in w.indices) {
                if (w[i][l] == Long.MAX_VALUE || w[l][j] == Long.MAX_VALUE) continue
                w[i][j] = min(w[i][j], w[i][l] + w[l][j])
            }
        }
    }
    val s = IntArray(k)
    val d = IntArray(k)
    for (i in s.indices) {
        val (st, dt) = input.readLine().split(' ').map(String::toInt)
        s[i] = st - 1
        d[i] = dt - 1
    }
    val dp = Array(2, { LongArray(k + 1, { Long.MAX_VALUE }) })
    for (i in s.indices) {
        if (max(w[s[i]][0], w[d[i]][0]) == Long.MAX_VALUE ) {
            output.println(-1)
            return
        }
    }
    dp[1][0] = w[0][s[0]]
    for (i in s.indices) {
        for (flag in dp.indices) {
            if (dp[flag][i] == Long.MAX_VALUE) continue
            val cost0 =
                    if (flag == 1) {
                        0.toLong()
                    } else {
                        w[d[i - 1]][s[i]]
                    }
            val cost1 =
                    if (flag == 1) {
                        if (i > 0) {
                            w[d[i - 1]][d[i]]
                        } else {
                            w[s[i]][d[i]]
                        }
                    } else {
                        w[s[i]][d[i]]
                    }
            val cost2 = if (i + 1 != k) {
                if (flag == 1) {
                    if (i > 0) {
                        w[d[i - 1]][s[i + 1]] + w[s[i + 1]][d[i]]
                    } else {
                        w[s[i]][s[i + 1]] + w[s[i + 1]][d[i]]
                    }
                } else {
                    w[s[i]][s[i + 1]] + w[s[i + 1]][d[i]]
                }
            } else Int.MAX_VALUE.toLong()
            dp[0][i + 1] = min(dp[0][i + 1], dp[flag][i] + cost0 + cost1)
            dp[1][i + 1] = min(dp[1][i + 1], dp[flag][i] + cost0 + cost2)
        }
    }
    output.println(dp[0][k])
}
