package com.gorany.codingtest.p2178_미로탐색

import java.util.*
import java.util.stream.IntStream

val Y_BOUND = arrayOf(0, 0, 1, -1)
val X_BOUND = arrayOf(1, -1, 0, 0)
const val POSITION = "position"
const val COUNT = "count"

fun main() {
    val (row, col) = readLine()?.split(' ')!!.map { it.toInt() }.take(2)
    val vertexList: MutableList<MutableList<Vertex>> = mutableListOf()

    for (i in 0 until row) {
        vertexList.add(mutableListOf())
        readLine()?.forEachIndexed { j, it ->
            vertexList[i].add(j, Vertex(position = Position(i, j), possible = it == '1'))
        }
    }

    println(bfs(row, col, vertexList))
}

data class Vertex(
    val position: Position,
    var check: Boolean = false,
    val possible: Boolean,
) {
    fun visit() {
        check(!check) { "(y, x) = (${position.y}, ${position.x})'s check is true" }
        check = true
    }

    fun getNeighborPosition(): List<Position> = IntStream.range(0, 4)
        .toArray()
        .map {
            Position(position.y + Y_BOUND[it], position.x + X_BOUND[it])
        }
}

data class Position(val y: Int, val x: Int)

private fun bfs(row: Int, col: Int, vertexList: List<List<Vertex>>): Int {
    val queue = LinkedList<Map<String, Any>>()

    vertexList[0][0].visit()
    queue.offer(mapOf(POSITION to Position(0, 0), COUNT to 1))

    while (!queue.isEmpty()) {
        val poll = queue.poll()
        val (y, x) = poll[POSITION] as Position
        val currentCount = poll[COUNT] as Int

        if (y == row - 1 && x == col - 1) {
            return currentCount
        }

        for ((nextY, nextX) in vertexList[y][x].getNeighborPosition()) {
            if (nextY < 0 ||
                nextX < 0 ||
                nextY > row - 1 ||
                nextX > col - 1 ||
                vertexList[nextY][nextX].check ||
                !vertexList[nextY][nextX].possible
            ) {
                continue
            }

            vertexList[nextY][nextX].visit()
            queue.offer(mapOf(POSITION to Position(nextY, nextX), COUNT to currentCount + 1))
        }
    }

    throw IllegalArgumentException("Wrong Logic.")
}
