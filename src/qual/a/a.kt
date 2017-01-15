package qual.a

import java.io.*

fun main(args: Array<String>) {
    val input = BufferedReader(FileReader("qual/a.in"))
    val output = PrintWriter(FileWriter("qual/a.out"))
    val T = input.readLine().toInt()
    for (test in 1..T) {
        output.print("Case #$test: ")
        val (p, xt, yt) = input.readLine().split(' ').map(String::toInt)
        val (x, y) = listOf(xt, yt).map { it -> it - 50.0 }
        val angle = Math.atan2(y, x) / Math.acos(0.0) / 4
        val percent: Double = 100.0 * (
                if (-angle + 0.25 < 0) {
                    1.25 - angle
                } else {
                    0.25 - angle
                })
        if (x * x + y * y > 50 * 50 || percent > p) {
            output.println("white")
        } else {
            output.println("black")
        }
    }
    input.close()
    output.close()
}

