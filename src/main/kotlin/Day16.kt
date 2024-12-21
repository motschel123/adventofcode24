//package org.example
//
//import java.io.File
//import kotlin.math.abs
//
//const val turnCosts = 1000
//
//var maze: Array<Array<Pair<Int, List<Any>>>>? = null
//var end: Pair<Int, Int>? = null
//fun main() {
//    maze = File("src/main/resources/Day16.txt")
//        .readLines()
//        .map {
//            it.toCharArray().toTypedArray()
//        }.toTypedArray()
//
//    val dir = '>'
//
//    val startY = maze!!.indexOfFirst{
//        it.indexOfFirst{ it == 'S'} != -1
//    }
//    val startX = maze!![startY].indexOfFirst{ it == 'S' }
//    val start = Triple(dir, startY, startX)
//
//    val endY = maze!!.indexOfFirst{
//        it.indexOfFirst{ it == 'E'} != -1
//    }
//    val endX = maze!![endY].indexOfFirst{ it == 'E' }
//    end = Pair(endY, endX)
//
//    val optimalPaths = aStar(start, end!!)
//    println("Part One: " + optimalPaths.first().second)
//
//    val result2 = mutableSetOf<Pair<Int, Int>>()
//    optimalPaths.forEach { path ->
//        path.first.forEach {
//            result2.add(it.second to it.third)
//            maze!![it.second][it.third] = '0'
//        }
//    }
//    printMaze()
//    println("Part Two: " + result2.size)
//
//}
//
//fun checksum(path: List<Triple<Char, Int, Int>>): Int {
//    return path.drop(1).fold(path.first() to 0) { (prev, costs), curr ->
//        (curr to costs + costs(prev, curr))
//    }.second
//}
//
//fun printMaze() {
//    println(maze?.joinToString("\n") { it.joinToString("") })
//}
//
//fun dijkstra(start: Triple<Char, Int, Int>, goal: Pair<Int, Int>): List<Pair<List<Triple<Char, Int, Int>>, Int>> {
//    val optimalPaths = mutableListOf<Pair<List<Triple<Char, Int, Int>>, Int>>()
//    var optimalPathCost = Int.MAX_VALUE
//
//    val openSet = mutableSetOf(start)
//    val cameFrom = mutableMapOf<Triple<Char, Int, Int>, List<Triple<Char, Int, Int>>>()
//    val gScore = mutableMapOf<Triple<Char, Int, Int>, Int>()
//    gScore[start] = 0
//
//    while (openSet.isNotEmpty()) {
//        val current = openSet.filter{ gScore.getOrDefault(it, Int.MAX_VALUE) <= optimalPathCost }.minByOrNull { gScore.getOrDefault(it, Int.MAX_VALUE) } ?: break
//        maze!![current.second][current.third] = current.first
//        printMaze()
//        if ((current.second to current.third) == goal) {
//            val optiPath = reconstructPath(cameFrom, current)
//            optimalPathCost = checksum(optiPath)
//            optimalPaths.add(optiPath to optimalPathCost)
//            openSet.remove(current)
//            continue
//        }
//
//        openSet.remove(current)
//        for (neighbor in neighbours((current.second to current.third))) {
//            val currGScore: Int = gScore.getOrDefault(current, Int.MAX_VALUE)
//            var tentativeGScore = costs(current, neighbor)
//            if (currGScore == Int.MAX_VALUE)
//                tentativeGScore = Int.MAX_VALUE
//            else
//                tentativeGScore += currGScore
//            if (tentativeGScore < gScore.getOrDefault(neighbor, Int.MAX_VALUE)) {
//                cameFrom[neighbor] = current
//                gScore[neighbor] = tentativeGScore
//                if (neighbor !in openSet) {
//                    openSet.add(neighbor)
//                }
//            }
//        }
//    }
//    return optimalPaths
//}
//
//fun reconstructPath(cameFrom: Map<Triple<Char, Int, Int>, List<Triple<Char, Int, Int>>>, current: Triple<Char, Int, Int>): List<List<Triple<Char, Int, Int>>> {
//    var curr = current
//    val allPaths = mutableListOf(mutableListOf(current))
//    var path = 0
//    while (curr in cameFrom.keys) {
//
//        curr = cameFrom[curr]!!
//        allPaths[path].addFirst(curr)
//    }
//    return totalPath
//}
//
//fun costs(current: Triple<Char, Int, Int>, next: Triple<Char, Int, Int>): Int {
//    val diffY = current.first - next.first
//    val diffX = current.second - next.second
//
//    if (abs(diffX) == 1 && abs(diffY) == 1)
//        return Int.MAX_VALUE
//
//    return when (current.first) {
//        // Dir Up
//        '^' -> when (next.first) {
//                '^' // Move up
//                    -> 1
//                'v' // Move Down
//                    -> 2 * turnCosts + 1
//                else // Move Left/Right
//                    -> turnCosts + 1
//            }
//        // Dir right
//        '>' -> when (next.first) {
//            '>' // Move right
//                -> 1
//            '<' // Move left
//                -> 2 * turnCosts + 1
//            else // Move up/down
//                -> turnCosts + 1
//        }
//        // Dir down
//        'v' -> when (next.first) {
//            '^' // Move up
//                -> 2 * turnCosts + 1
//            'v' // Move Down
//                -> 1
//            else // Move Left/Right
//                -> turnCosts + 1
//        }
//        // Dir left
//        '<' -> when (next.first) {
//            '>' // Move right
//                -> 2 * turnCosts + 1
//            '<' // Move left
//                -> 1
//            else // Move up/down
//                -> turnCosts + 1
//        }
//        else -> throw Exception("Illegal Direction: ${current.first}")
//    }
//}
//
//fun neighbours(pos: Pair<Int, Int>): List<Triple<Char, Int, Int>> {
//    fun helper(y: Int, x: Int): Boolean {
//        return arrayOf('.', 'E').contains(maze?.getOrNull(y)?.getOrNull(x))
//    }
//
//    return listOfNotNull(
//        if (helper(pos.first+1, pos.second)) Triple('v', pos.first+1, pos.second) else null,
//        if (helper(pos.first-1, pos.second)) Triple('^', pos.first-1, pos.second) else null,
//        if (helper(pos.first, pos.second+1)) Triple('>', pos.first, pos.second+1) else null,
//        if (helper(pos.first, pos.second-1)) Triple('<', pos.first, pos.second-1) else null,
//    )
//}