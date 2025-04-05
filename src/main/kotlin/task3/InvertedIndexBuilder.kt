package ru.glebik.task3

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
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
        val jsonElement = json.encodeToJsonElement(invertedIndex)

        val compactArraysJson = compactArrayElements(jsonElement)

        File(fileName).writeText(compactArraysJson)
    }

    private fun compactArrayElements(element: JsonElement, indent: String = "  ", level: Int = 0): String {
        return when (element) {
            is JsonObject -> {
                val content = element.entries.joinToString(",\n") { (k, v) ->
                    val key = "\"$k\""
                    val value = compactArrayElements(v, indent, level + 1)
                    "${indent.repeat(level + 1)}$key: $value"
                }
                "{\n$content\n${indent.repeat(level)}}"
            }
            is JsonArray -> {
                val inline = element.joinToString(", ") { compactArrayElements(it, indent, 0) }
                "[$inline]"
            }
            else -> element.toString()
        }
    }
}