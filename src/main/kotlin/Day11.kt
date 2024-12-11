package org.example

import java.io.File

fun main() {
    val input = File("./src/main/resources/day11.txt")
        .readText()
        .replace("\n", "")
        .split(" ")


    fun compute(times: Int): Int {
        var nextList = input

        for (i in 1..times) {
            nextList = nextList.flatMap { num ->
                if (num == "0")
                    listOf("1")
                else if (num.length % 2 == 0)
                    listOf(
                        num.substring(0, num.length/2),
                        num.substring(num.length/2).toInt().toString()
                    )
                else
                    listOf((num.toLong() * 2024).toString())
            }
        }
        return nextList.size
    }

    println("Part One: ${compute(25)}")
//    println("Part Two: ${compute(75)}")
}