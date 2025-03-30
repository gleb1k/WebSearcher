package ru.glebik.task4

import ru.glebik.task3.InvertedIndex
import kotlin.math.ln

class TermMetricsCalculator(
    private val invertedIndex: InvertedIndex,
) {

    fun calculateTf() {
        val tfMap = mutableMapOf<Int, Map<String, Double>>()

    }

    fun calculateIdf(): Map<String, Double> {
        val idfMap = mutableMapOf<String, Double>()
        val totalDocuments = 100 //D

        invertedIndex.indexMap.forEach { (word, list) ->
            val idf = ln(totalDocuments.toDouble() / list.size.toDouble()) // ln(D / df(t))
            idfMap[word] = idf
        }

        return idfMap
    }

}