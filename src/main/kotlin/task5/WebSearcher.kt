package ru.glebik.task5

import ru.glebik.task2.Lemmatizer.tokenizeAndLemmatize
import ru.glebik.task4.TfWord
import kotlin.math.sqrt

class WebSearcher(
    private val tfidfWords: List<TfWord>,
    private val idf: Map<String, Double>
) {


    fun search(query: String): List<Pair<Int, Double>> {
        val docVectors = mutableMapOf<Int, MutableMap<String, Double>>()

        // Шаг 1: Построить векторы документов
        for (tfWord in tfidfWords) {
            for (doc in tfWord.docs) {
                docVectors
                    .getOrPut(doc.id) { mutableMapOf() }[tfWord.word] = doc.tfValue
            }
        }

        // Шаг 2: Построить вектор запроса
        val queryTerms = tokenizeAndLemmatize(query)
        val queryVector = mutableMapOf<String, Double>()

        for (term in queryTerms) {
            val wordCount = queryTerms.count { it == term }
            val totalWords = queryTerms.size
            val tf = if (totalWords > 0) wordCount.toDouble() / totalWords else 0.0
            val idf = idf[term] ?: 0.0
            val tdIdf = tf * idf
            queryVector[term] = tdIdf
        }

        // Шаг 3: Вычислить косинусное сходство
        fun cosineSimilarity(docVector: Map<String, Double>, queryVector: Map<String, Double>): Double {
            val commonTerms = docVector.keys.intersect(queryVector.keys)

            val dotProduct = commonTerms.sumOf { docVector[it]!! * queryVector[it]!! }

            val docNorm = sqrt(docVector.values.sumOf { it * it })
            val queryNorm = sqrt(queryVector.values.sumOf { it * it })

            return if (docNorm != 0.0 && queryNorm != 0.0)
                dotProduct / (docNorm * queryNorm)
            else 0.0
        }

        val scoredDocs = docVectors.mapValues { (_, vector) ->
            cosineSimilarity(vector, queryVector)
        }

        // Шаг 4: Отсортировать по релевантности
        val result = scoredDocs
            .filterValues { it > 0.0 }
            .toList()
            .sortedByDescending { it.second } // (docId, score)

        println("---Результаты поиска для query \"$query\": ---")
        result.forEach { (docId, weight) ->
            println("Документ $docId — релевантность: %.5f".format(weight))
        }

        return result
    }

//    fun search(query: String): List<Pair<Int, Double>> {
//        val queryWords = query.split(" ").map { it.lowercase() }.toSet()
//
//        val docScores = mutableMapOf<Int, Double>()
//
//        for (word in queryWords) {
//            val tfWord = tfidfWords.find { it.word == word } ?: continue
//
//            for (doc in tfWord.docs) {
//                docScores[doc.id] = docScores.getOrDefault(doc.id, 0.0) + doc.tfValue
//            }
//        }
//
//        val result = docScores.entries
//            .sortedByDescending { it.value }
//            .map { it.key to it.value }
//
//        println("---Результаты поиска для query \"$query\": ---")
//        result.forEach { (docId, weight) ->
//            println("Документ $docId — релевантность: %.5f".format(weight))
//        }
//
//        return result
//    }

}