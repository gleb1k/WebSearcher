package ru.glebik.task2

import ru.glebik.FileHelper
import java.util.*
import com.github.demidko.aot.WordformMeaning.lookupForMeanings
import java.io.File

class Lemmatizer {

    fun execute() {
        for (i in 1..100) {
            runCatching {
                val pageName = FileHelper.getPageSaveName(i)
                val pagePath = FileHelper.getPageSavePath(FileHelper.pagesSavePath, pageName)
                val pageContent = FileHelper.readFileToString(pagePath)

                //1
                val pageContentArray = tokenize(pageContent)

                //2
                val lemmatizedWords = pageContentArray
                    .mapNotNull {
                        lookupForMeanings(it).firstOrNull()?.lemma?.toString()
                    }
                    //3
                    .filter { it !in StopWordsHelper.stopWords }

                val fileSavePath =
                    FileHelper.getPageSavePath(FileHelper.lemmatizedSavePath, FileHelper.getLemmatizedSaveName(i))
                File(fileSavePath).writeText(lemmatizedWords.joinToString("\n"))

                println("Lemmatized file $i")
            }.onFailure {
                println("Failed to process file $i: ${it.message}")
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
}