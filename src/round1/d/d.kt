package round1.d

import java.io.BufferedReader
import java.io.FileReader
import java.io.FileWriter
import java.io.PrintWriter
import java.lang.Math.max
import java.lang.Math.min
import kotlin.system.measureNanoTime
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    val input = BufferedReader(FileReader("round1/d.in"))
    val output = PrintWriter(FileWriter("round1/d.out"))
    val total = measureTimeMillis {
        val T = input.readLine().toInt()
        for (test in 1..T) {
            output.print("Case #$test: ")
            val time = measureNanoTime {
                solve(input, output)
            }
            println("test #$test = ${time / 1e6}ms")
            output.flush()
        }
    }
    println("total = ${total}ms")
    output.close()
    input.close()

}

const val MOD: Int = 1_000_000_007

fun mult(a: Int, b: Int) = ((a.toLong() * b.toLong()) % MOD).toInt()

fun add(a: Int, b: Int): Int {
    val c = a + b
    if (c >= MOD) {
        return c - MOD
    } else {
        return c
    }
}

fun pow(a_: Int, b_: Int): Int {
    var b = b_
    var a = a_
    var res = 1
    while (b > 0) {
        if (b % 2 == 1) res = mult(res, a)
        a = mult(a, a)
        b /= 2
    }
    return res
}

fun rev(a: Int) = pow(a, MOD - 2)

fun count(n: Int, k: Int): Int {
    val N = n + k - 1
    val K = k - 1
    if (N < 0) return 0
    var num = 1
    for (i in (N - K + 1)..N) {
        num = mult(num, i)
    }
    var den = 1
    for (i in 1..K) {
        den = mult(den, i)
    }
    return mult(num, rev(den))
}

fun solve(input: BufferedReader, output: PrintWriter) {
    val (n, m) = input.readLine().split(' ').map(String::toInt)
    val R = IntArray(n, { input.readLine().toInt() }).sorted()
    if (n == 1) {
        output.println(m)
        return
    }
    val fact = IntArray(n + 1)
    fact[0] = 1
    for (i in 1..n) fact[i] = mult(fact[i - 1], i)
    val s = 2 * R.sum()
    val part = IntArray(2 * R.last() + 1)
    for (i in R) part[i] += 1
    for (i in part.lastIndex downTo 1) part[i - 1] += part[i]
    var res = 0
    res = add(res, mult(count(m - 1 - s, n + 1), fact[n]))
    for (pen in 1..(2 * R.last())) {
        val m1 = count(m - 1 + pen - s, n)
        val m2 = count(m - 1 + pen - s, n - 1)
        for (v1 in 0..pen) {
            val v2 = pen - v1
            val i = R.count { v1 <= it}
            val j = R.count { v2 <= it}
            if (v1 == 0 || v2 == 0) {
                val a = mult(mult(m1, fact[n - 1]), min(i, j))
                res = add(res, a)
            } else {
                val a = min(i, j)
                val b = max(i, j)
                val c = (a * (a - 1) + (b - a) * a) % MOD
                val d = mult(mult(m2, fact[n - 2]), c)
                res = add(res, d)
            }
        }
    }
    output.println(res)
}