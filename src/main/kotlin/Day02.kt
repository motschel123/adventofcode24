package org.example

import java.io.File
import kotlin.math.absoluteValue


fun main() {
    println("Day 2!")

    val inputData: List<List<Int>> = File("src/main/resources/day02.txt").readLines()
        .map{ reportStr: String ->
            reportStr.split(" ").map { it.toInt() }
        }

    println("Part One:")
    val validDiffs = inputData.map(::calcDiffs).map(::validateDiffs)
    println(validDiffs.count{it.all{b -> b}})


    println("Part Two:")

    val result2 = validDiffs.mapIndexed{ idx, levelDiffs ->
        if (levelDiffs.all { it })
            true
        else {
            for (i in 0 until validDiffs.size+1) {
                val newData = inputData[idx].filterIndexed { index, _ -> index != i }
                if (validateDiffs(calcDiffs(newData)).all { it })
                    return@mapIndexed true
            }
            return@mapIndexed false
        }
    }
    println(result2.toString())
    println(result2.count{it})
}

fun calcDiffs(list: List<Int>): List<Int> {
    return list.subList(0, list.size-1).mapIndexed { idx, elem -> list[idx + 1] - elem}
}

fun validateDiffs(list: List<Int>): List<Boolean> {
    return list.map { elem ->
        elem.absoluteValue <= 3
                &&
                (if (list[0] > 0)
                    elem > 0
                else
                    elem < 0)
    }
}