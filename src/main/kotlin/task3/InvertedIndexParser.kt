package ru.glebik.task3

object InvertedIndexParser {

    fun parse(query: String, index: Map<String, List<Int>>): Set<Int> {
        val words = index.keys
        val tokens = query.split(Regex("\\s+"))
        val documentsIds = mutableListOf<Set<Int>>() //документы где есть слово

        val operators = listOf(tokens[1], tokens[3])
        val queryWords = tokens - operators.toSet()

        queryWords.forEach { queryWord ->
            if (queryWord.startsWith("!")) {
                val word = queryWord.substring(1)
                val allDocs = words.flatMap { index[it] ?: emptyList() }.toSet()
                val wordDocs = index[word]?.toSet() ?: emptySet()
                documentsIds.add(logicNot(allDocs, wordDocs)) // логическое НЕ
            } else {
                documentsIds.add(index[queryWord]?.toSet() ?: emptySet()) // обычное добавление
            }
        }

        val leftOperator = operators[0]
        val newLeft = when (leftOperator) {
            "|" -> logicOr(documentsIds[0], documentsIds[1])
            "&" -> logicAnd(documentsIds[0], documentsIds[1])
            else -> throw IllegalArgumentException("Unknown operator")

        }

        val rightOperator = operators[1]
        val result = when (rightOperator) {
            "|" -> logicOr(newLeft, documentsIds[2])
            "&" -> logicAnd(newLeft, documentsIds[2])
            else -> throw IllegalArgumentException("Unknown operator")

        }

        println("\"$query\" found in $result docs")

        return result
    }

    private fun logicOr(left: Set<Int>, right: Set<Int>): Set<Int> = left.union(right)

    private fun logicAnd(left: Set<Int>, right: Set<Int>): Set<Int> = left.intersect(right.toSet())

    private fun logicNot(all: Set<Int>, value: Set<Int>): Set<Int> = all.toSet() - value.toSet()
}