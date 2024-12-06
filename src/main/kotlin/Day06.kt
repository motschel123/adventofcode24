package org.example

import java.io.File
import java.io.FileWriter

fun main() {
    val inputLines =
        File("src/main/resources/day06_jojo.txt").readLines()
    val initialMap = inputLines.map { it.toCharArray().toTypedArray() }.toTypedArray()
    val map = Array(initialMap.size) { i -> initialMap[i].copyOf() }
    // find start
    var startCol: Int = -1
    val startRow = map.indexOfFirst {
        val idx = it.indexOfFirst { c -> c == '>' || c == '<' || c == '^' || c == 'v' }
        if (idx > -1) {
            startCol = idx
            return@indexOfFirst true
        }
        false
    }
    println("Part One:")
    val moveList = mutableListOf<Triple<Int, Int, Char>>()
    val moves = HashMap<Pair<Int, Int>, MutableSet<Char>>()
    var row = startRow
    var col = startCol
    try {
        do {
            moveList.add(Triple(row, col, map[row][col]))
            moves.getOrPut(Pair(row, col)) { mutableSetOf() }.add(map[row][col])

            val next = takeStep(row, col, map)
            row = next.first
            col = next.second

        } while (true)
    } catch (_: ArrayIndexOutOfBoundsException) {}
    val result = map.flatten().map {
        if (it == '>'  || it == '<' || it == '^' || it == 'v')
            'X'
        else
            it
    }.count { it == 'X' }

    println("$result")

    // Check injection
    var possibleInjections = 0
    val iterator = moveList.subList(0, moveList.size - 1).iterator()
    var prev = iterator.next()
    var x = 0
    while (iterator.hasNext()) {
        x++
        val curr = iterator.next()
        // Deepcopy map
        val injectionMap = Array(initialMap.size) { i -> initialMap[i].copyOf() }
        // inject new wall
        val (wallInjRow, wallInjCol, _) = curr
        injectionMap[wallInjRow][wallInjCol] = '#'
        // inject start position
        val (testInjRow, testInjCol, testInjDir) = prev
        injectionMap[testInjRow][testInjCol] = testInjDir

        try {
            var tmpRow = testInjRow
            var tmpCol = testInjCol
            do {
                val tmpNext = takeStep(tmpRow, tmpCol, injectionMap)
                tmpRow = tmpNext.first
                tmpCol = tmpNext.second


                if (moves.getOrPut(tmpNext) {HashSet()}.contains(injectionMap[tmpRow][tmpCol])) {
                    possibleInjections++
                    break
                }

                moves[tmpNext]?.add(injectionMap[tmpRow][tmpCol])
            } while (true)
        } catch (_: ArrayIndexOutOfBoundsException) { }

        prev = curr
    }
    println("Part Two: $possibleInjections")
}

fun takeStep(row: Int, col: Int, map: Array<Array<Char>>): Pair<Int, Int>  {
    var dir = map[row][col]
    // Next pos
    var nextCoords = nextCoords(row, col, dir)
    // Turn if wall
    if (map[nextCoords.first][nextCoords.second] == '#') {
        dir = rotate90(dir)
        nextCoords = nextCoords(row, col, dir)
        if (map[nextCoords.first][nextCoords.second] == '#') {
            dir = rotate90(dir)
            nextCoords = nextCoords(row, col, dir)
            if (map[nextCoords.first][nextCoords.second] == '#') {
                dir = rotate90(dir)
                nextCoords = nextCoords(row, col, dir)
                if (map[nextCoords.first][nextCoords.second] == '#') {
                    throw Exception("Err: Stuck in one pos: row = $row, col = $col")
                }
            }
        }
    }
    val (nextRow, nextCol) = nextCoords

    map[row][col] = dir
    map[nextRow][nextCol] = dir
    return Pair(nextRow, nextCol)
}

fun rotate90(dir: Char): Char {
    return when (dir) {
        '^' -> '>'
        '>' -> 'v'
        'v' -> '<'
        '<' -> '^'
        else -> '?'
    }
}

fun nextCoords(row: Int, col: Int, dir: Char): Pair<Int, Int> {
    var nextRow = row
    var nextCol = col
    when (dir) {
        '>' -> nextCol++
        '<' -> nextCol--
        '^' -> nextRow--
        'v' -> nextRow++
    }
    return Pair(nextRow, nextCol)
}