package qual.c

import java.io.*
import java.lang.Math.max
import kotlin.system.measureNanoTime
import kotlin.system.measureTimeMillis

val indexToSpell = listOf(4, 6, 8, 10, 12, 20)
val spellToIndex = listOf(4, 6, 8, 10, 12, 20).zip((0..5).map {it}).toMap()

val table = Array(6, {x -> Array(21, {DoubleArray(indexToSpell[x] * 20 + 1)})})

fun main(args: Array<String>) {
    val input = BufferedReader(FileReader("qual/c.in"))
    val output = PrintWriter(FileWriter("qual/c.out"))
    val T = input.readLine().toInt()
    val initTime = measureNanoTime {
        for (i in 0..5) {
            val x = indexToSpell[i]
            System.err.println(x)
            table[i][0][0] = 1.0
            for (j in 1..20) {
                for (k in j..(j * x)) {
                    for (l in (Math.max(k - x, 0))..(k - 1)) {
                        table[i][j][k] += table[i][j - 1][l] / x
                    }
                }
            }
        }
    }
    println("init = ${initTime / 1e6}ms")
    val total =
            measureTimeMillis {
                for (test in 1..T) {
                    output.print("Case #$test: ")
                    val time = measureNanoTime{ solve(input, output) }
                    println("time of $test = ${time/1e6}ms")
                    output.flush()
                }
            }
    println("totalTime = ${total}ms")
}


fun solve(input: BufferedReader, output: PrintWriter) {
    val (h, _) = input.readLine().split(' ').map(String::toInt)
    val acts = input.readLine().split(' ')
    var res = 0.0
    for (act in acts) {
        val flag = act.contains('-')
        val a = act.split('d', '-', '+').map(String::toInt)
        val current = if (a.size == 2) {
            calc(a[0], a[1], h)
        } else if (flag) {
            calc(a[0], a[1], h + a[2])

        } else {
            calc(a[0], a[1], h - a[2])
        }
        res = max(res, current)
    }
    output.println(String.format("%.10e", res))
}


fun calc(y: Int, x: Int, z: Int): Double {
    val q = spellToIndex[x]!!
    return table[q][y].indices
            .filter { it >= z }
            .sumByDouble { table[q][y][it] }
}

