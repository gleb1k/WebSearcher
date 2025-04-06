package ru.glebik.task2

import ru.glebik.FileHelper
import java.util.*
import com.github.demidko.aot.WordformMeaning.lookupForMeanings
import java.io.File

object Lemmatizer {

    fun execute() {
        for (docId in 1..100) {
            runCatching {
                val pageName = FileHelper.getPageSaveName(docId)
                val pagePath = FileHelper.getPageSavePath(FileHelper.pagesSavePath, pageName)
                val pageContent = FileHelper.readFileToString(pagePath)

                //1
                val pageContentArray = tokenize(pageContent)

                //2
                val lemmatizedWords = lemmatize(pageContentArray)

                //4
                val fileSavePath =
                    FileHelper.getPageSavePath(FileHelper.lemmatizedSavePath, FileHelper.getLemmatizedSaveName(docId))
                File(fileSavePath).writeText(lemmatizedWords.joinToString("\n"))

                println("Lemmatized file $docId")
            }.onFailure {
                println("Failed to process file $docId: ${it.message}")
            }
        }
    }

    private fun tokenize(text: String): List<String> {
        val tokenizer = StringTokenizer(text.lowercase(), " .,!?;:\"()[]{}-")
        val words = mutableListOf<String>()
        while (tokenizer.hasMoreTokens()) {
            words.add(tokenizer.nextToken())
        }
        return words
    }

    private fun lemmatize(words: List<String>): List<String> {
        return words.mapNotNull {
            lookupForMeanings(it).firstOrNull()?.lemma?.toString()
        }.filter { it !in StopWordsHelper.stopWords }
    }

    fun tokenizeAndLemmatize(query: String): List<String> = lemmatize(tokenize(query))

}