package org.example

import java.io.File


fun main() {
    println("Part Two: ")
    val result1 = File("src/main/resources/Day13.txt")
        .readLines()
        .asSequence()
        .chunked(4)
        .map { it.filter(String::isNotEmpty) }
        .map { line ->
            val (aLeft, aRight) = line[0].split(",")
            val x_a = aLeft.split("+")[1].toInt()
            val y_a = aRight.split("+")[1].toInt()

            val (bLeft, bRight) = line[1].split(",")
            val x_b = bLeft.split("+")[1].toInt()
            val y_b = bRight.split("+")[1].toInt()

            val (wantLeft, wantRight) = line[2].split(",")
            val x = 10000000000000 + wantLeft.split("=")[1].toInt()
            val y = 10000000000000 + wantRight.split("=")[1].toInt()

            val s = (x * y_b - y * x_b) / (x_a * y_b - x_b * y_a)
            val t = (x - x_a * s) / x_b

            Pair(s, t)
        }
        .sumOf {
            it.first * 3 + it.second
        }
    println(result1)

}