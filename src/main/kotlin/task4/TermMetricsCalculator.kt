package ru.glebik.task4

import ru.glebik.FileHelper
import ru.glebik.round
import ru.glebik.task3.InvertedIndex
import kotlin.math.ln

data class TfWord(
    val word: String,
    val docs: List<TfDoc>
) {
    data class TfDoc(
        val id: Int,
        val tfValue: Double
    )
}

class TermMetricsCalculator(
    private val invertedIndex: InvertedIndex,
) {

    fun calculateTfIdf(tf: List<TfWord>, idf: Map<String, Double>) : List<TfWord> {
        return tf.map { tfWord ->
            val idfValue = idf[tfWord.word] ?: 0.0
            val updatedDocs = tfWord.docs.map { doc ->
                doc.copy(tfValue = (doc.tfValue * idfValue).round())
            }
            tfWord.copy(docs = updatedDocs)
        }
    }

    fun calculateTf() : List<TfWord> {
        val documents = mutableListOf<TfWord>()
        invertedIndex.indexMap.forEach { (word, docsIds) ->
            documents.add(calculateTfForWord(word, docsIds))
        }
        return documents
    }

    private fun calculateTfForWord(word: String, docsIds: List<Int>) : TfWord {
        val tfDocs = docsIds.map { docId ->
            val pageName = FileHelper.getLemmatizedSaveName(docId)
            val pagePath = FileHelper.getPageSavePath(FileHelper.lemmatizedSavePath, pageName)
            val pageContent = FileHelper.readFileToString(pagePath)

            val doc = pageContent.split(Regex("\n"))
                .distinct()
            val wordCount = doc.count { it == word }
            val totalWords = doc.size
            val tf = if (totalWords > 0) wordCount.toDouble() / totalWords else 0.0

            TfWord.TfDoc(
                id = docId,
                tfValue = tf.round()
            )
        }

        println("calculated Tf for $word")

        return TfWord(
            word = word,
            docs = tfDocs
        )
    }

    /**
    word to idf
     */
    fun calculateIdf(): Map<String, Double> {
        val idfMap = mutableMapOf<String, Double>()
        val totalDocuments = 100 //D

        invertedIndex.indexMap.forEach { (word, list) ->
            val idf = ln(totalDocuments.toDouble() / list.size.toDouble()) // ln(D / df(t))
            idfMap[word] = idf.round()
        }

        return idfMap
    }

}