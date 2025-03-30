package ru.glebik.task3

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.glebik.FileHelper
import java.io.File


object InvertedIndexBuilder {

    fun buildInvertedIndex(): InvertedIndex {
        val index = mutableMapOf<String, MutableList<Int>>()

        for (docId in 1..100) {
            runCatching {
                val pageName = FileHelper.getLemmatizedSaveName(docId)
                val pagePath = FileHelper.getPageSavePath(FileHelper.lemmatizedSavePath, pageName)
                val pageContent = FileHelper.readFileToString(pagePath)

                pageContent.split(Regex("\n"))
                    .distinct()
                    .forEach { word ->
                        index.computeIfAbsent(word) { mutableListOf() }.add(docId)
                    }
            }.onFailure {
                println("Failed to index file $docId: ${it.message}")
            }

        }

        return InvertedIndex(index.toSortedMap()).also {
            saveToJsonFile(it, FileHelper.invertedIndexSavePath + "/invertedIndex.json" )
        }
    }

    private fun saveToJsonFile(invertedIndex: InvertedIndex, fileName: String) {
        val json = Json { prettyPrint = true }
        val jsonString = json.encodeToString(invertedIndex)
        File(fileName).writeText(jsonString)
    }

}