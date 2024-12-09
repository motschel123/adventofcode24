package org.example

import java.io.File

fun main() {
    val input = File("src/main/resources/day09.txt")
        .readText()
        .toCharArray()
        .toList()
        .filter { it in '0'..'9' }
        .map(Char::digitToInt)

    var isFile: Boolean = true
    var fileId: Int = 0

    val fileSysString = input
        .map{ digit ->
            if (isFile) {
                isFile = false
                val str = fileId.toString().repeat(digit)
                fileId++
                return@map str
            } else {
                isFile = true
                return@map ".".repeat(digit)
            }
        }
        .joinToString("")
        .toCharArray()
        .toTypedArray()
        .toMutableList()

    var i = 0
    var j = fileSysString.size - 1

    while (i < j) {
        if (fileSysString[i] != '.') {
            i++
            continue
        }
        if (fileSysString[j] == '.') {
            j--
            continue
        }

        fileSysString[i] = fileSysString[j]
        fileSysString[j] = '.'
        i++
        j--
    }

    println(fileSysString.joinToString(""))

    val result = fileSysString
        .takeWhile { it != '.' }
        .mapIndexed { idx, c ->
            c.digitToInt().toULong() * idx.toULong()
        }
        .sum()

    println(result)
}

