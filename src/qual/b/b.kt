package qual.b

import java.io.BufferedReader
import java.io.FileReader
import java.io.FileWriter
import java.io.PrintWriter
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    val input = BufferedReader(FileReader("qual/b.in"))
    val output = PrintWriter(FileWriter("qual/b.out"))
    val T = input.readLine().toInt()
    val total = measureTimeMillis {
        for (test in 1..T) {
            output.print("Case #$test: ")
            val time = measureTimeMillis { solve(input, output) }
            println("$test = ${time}ms")
        }
    }
    println("${total}ms")
    input.close()
    output.close()
}

fun solve(input: BufferedReader, output: PrintWriter) {
    val n = input.readLine().toInt()
    val w = Array(n, { input.readLine().toInt() })
            .apply {
                sort()
                reverse()
            }
    var res = 0
    var j = w.lastIndex
    var i = 0
    while (i <= j) {
        var k = 1
        while (j > i && w[i] * k < 50) {
            k++
            j--
        }
        if (w[i] * k >= 50) {
            res++
        }
        i++
    }
    output.println(res)
}
