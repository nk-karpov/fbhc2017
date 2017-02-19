package round2.b

import java.io.BufferedReader
import java.io.FileReader
import java.io.FileWriter
import java.io.PrintWriter
import java.util.*
import java.util.Collections.emptySortedMap
import kotlin.system.measureNanoTime


fun main(args: Array<String>) {
    val roundName = "round2"
    val problemName = "b"
    val io = "$roundName/$problemName"
    val input = BufferedReader(FileReader("$io.in"))
    val output = PrintWriter(FileWriter("$io.out"))
    val T = input.readLine().toInt()
    val total = measureNanoTime {
        for (test in 1..T) {
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
    val n = input.readLine().toInt()
    val (xx, ax, bx, cx) = input.readLine().split(' ').map(String::toLong)
    val (hh, ah, bh, ch) = input.readLine().split(' ').map(String::toLong)
    var x = xx
    var h = hh
    var res: Double = 0.0
    var answer: Double = 0.0
    val store = TreeMap<Long, Triple<Long, Long, Long>>()
    repeat(n) {
        val a = store.headMap(x)
        val b = store.tailMap(x)
        val flag = (a.isEmpty() || store[a.lastKey()]!!.first - (x - a.lastKey()) < h) &&
                (b.isEmpty() || store[b.firstKey()]!!.first - (b.firstKey() - x) < h)
        if (flag) {
            while (store.headMap(x).isNotEmpty()) {
                val w = store.headMap(x).lastKey()
                if (store[w]!!.first > h - (x - w)) break
                val t = store.remove(w)!!
                res -= (2 * t.first - t.second) * t.second / 2.0
                res -= (2 * t.first - t.third) * t.third / 2.0
            }
            while (store.tailMap(x).isNotEmpty()) {
                val w = store.tailMap(x).firstKey()
                if (store[w]!!.first > h - (w - x)) break
                val t = store.remove(w)!!
                res -= (2 * t.first - t.second) * t.second / 2.0
                res -= (2 * t.first - t.third) * t.third / 2.0
            }
            var left = 0L
            var right = 0L
            if (store.headMap(x).isNotEmpty()) {
                val w = store.headMap(x).lastKey()
                if (x - w < store[w]!!.first + h) {
                    var t = store[w]!!
                    res -= (2 * t.first - t.third) * t.third / 2.0
                    left = (t.first + h) - (x - w)
                    t = Triple(t.first, t.second, t.third - left)
                    res += (2 * t.first - t.third) * t.third / 2
                    store[w] = t
                }
            }
            if (store.tailMap(x).isNotEmpty()) {
                val w = store.tailMap(x).firstKey()
                if (w - x < store[w]!!.first + h) {
                    var t = store[w]!!
                    res -= (2 * t.first - t.second) * t.second / 2.0
                    right = (t.first + h) - (w - x)
                    t = Triple(t.first, t.second - right, t.third)
                    res += (2 * t.first - t.second) * t.second / 2.0
                    store[w] = t
                }
            }
            val t = Triple(h, h - left, h - right)
            store[x] = t
            res += (2 * t.first - t.second) * t.second / 2.0
            res += (2 * t.first - t.third) * t.third / 2.0
        }
        answer += res
        x = (ax * x + bx) % cx + 1
        h = (ah * h + bh) % ch + 1
    }
    output.println(answer)
}


