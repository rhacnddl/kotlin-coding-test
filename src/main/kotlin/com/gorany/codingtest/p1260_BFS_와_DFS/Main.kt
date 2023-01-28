package com.gorany.codingtest.p1260_BFS_와_DFS

import java.util.*
import java.util.stream.IntStream

data class Vertex(
    val num: Int,
    var check: Boolean = false,
    val neighbors: MutableList<Int> = mutableListOf(),
) {
    fun visit() {
        if (check) {
            throw IllegalStateException("$num's check is true.")
        }
        check = true
    }

    fun connect(neighbor: Int) {
        neighbors.add(neighbor)
    }
}

fun main() {
    val (vertexCount, edgeCount, startNum) = readLine()?.split(' ')!!
        .map { it.toInt() }
        .take(3)

    val originalVertexList: List<Vertex> = getVertexList(vertexCount, edgeCount)

    println(DFS(startNum, originalVertexList.map { it.copy() }).joinToString(" "))
    println(BFS(startNum, originalVertexList.map { it.copy() }).joinToString(" "))
}

private fun getVertexList(
    vertexCount: Int,
    edgeCount: Int,
): List<Vertex> {
    val originalVertexList: List<Vertex> = IntStream.rangeClosed(0, vertexCount)
        .toArray()
        .map { Vertex(it) }

    for (i in 0 until edgeCount) {
        val (vertex1, vertex2) = readLine()?.split(' ')!!
            .map { it.toInt() }
            .take(2)

        originalVertexList[vertex1].connect(vertex2)
        originalVertexList[vertex2].connect(vertex1)
    }

    originalVertexList.forEach { it.neighbors.sort() }

    return originalVertexList
}

fun BFS(startVertex: Int, vertexList: List<Vertex>): List<Int> {
    val queue = LinkedList<Int>()
    val result = mutableListOf(startVertex)

    vertexList[startVertex].visit()
    queue.add(startVertex)

    while (!queue.isEmpty()) {
        val (_, _, neighbors) = vertexList[queue.remove()]

        neighbors.forEach {
            if (!vertexList[it].check) {
                vertexList[it].visit()
                result.add(it)
                queue.add(it)
            }
        }
    }

    return result
}

fun DFS(startVertex: Int, vertexList: List<Vertex>): List<Int> {
    val result = mutableListOf(startVertex)
    val current: Vertex = vertexList[startVertex]

    current.visit()

    current.neighbors.forEach {
        if (!vertexList[it].check) {
            result += DFS(it, vertexList)
        }
    }

    return result
}
