package org.example

import java.io.File
import java.io.FileWriter

fun main() {
    val inputLines =
        File("src/main/resources/day06.txt").readLines()
    val map = inputLines.map { it.toCharArray().toTypedArray() }.toTypedArray()
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
    val moveList = mutableListOf<Pair<Int, Int>>()
    var row = startRow
    var col = startCol
    try {
        do {
            val next = takeStep(row, col, map)
            row = next.first
            col = next.second
            moveList.add(next)
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
    for ((testInjRow, testInjCol) in moveList.subList(0, moveList.size - 1)) {
        // Deepcopy map
        val injectionMap = Array(map.size) { i -> map[i].copyOf() }
        // inject new wall
        injectionMap[testInjRow][testInjRow] = '#'
        // Collect all moves
        val moves = HashMap<Pair<Int, Int>, MutableSet<Char>>()
        injectionMap.forEachIndexed { i, arr ->
            arr.forEachIndexed { j, c ->
                moves[Pair(i, j)] = if (charArrayOf('<', '>', '^', 'v').contains(c)) mutableSetOf(c) else mutableSetOf()
            }
        }

        try {
            var tmpRow = testInjRow
            var tmpCol = testInjCol
            do {
                val tmpNext = takeStep(tmpRow, tmpCol, injectionMap)
                tmpRow = tmpNext.first
                tmpCol = tmpNext.second
                val pastMovesHere = moves.getOrDefault(Pair(tmpRow, tmpCol), mutableSetOf())
                if (pastMovesHere.contains(injectionMap[tmpRow][tmpCol])) {
                    possibleInjections++
                    break
                }
                pastMovesHere.add(injectionMap[tmpRow][tmpCol])
                moves[Pair(tmpRow, tmpCol)] = pastMovesHere
            } while (true)
        } catch (_: ArrayIndexOutOfBoundsException) {
        }
    }

    File("src/main/resources/day06_vis.txt").writeText(map.toList().joinToString("\n") { it.joinToString("") })

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