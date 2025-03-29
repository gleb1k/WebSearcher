package ru.glebik.task2

import ru.glebik.FileHelper

object StopWordsHelper {

    private const val DEFAULT_STOP_WORDS_PATH = "\\src\\main\\kotlin\\task2\\sources"

    val stopWords : Set<String> by lazy {
        loadStopWords()
    }

    private fun loadStopWords(): Set<String> {
        val stopWords = mutableSetOf<String>()

        val stopwordsFilePath = FileHelper.getSavePath(DEFAULT_STOP_WORDS_PATH) + "\\stopwords.txt"
        stopWords.addAll(readStopWordsFromFile(stopwordsFilePath))

        val stopwordsRuFilePath = FileHelper.getSavePath(DEFAULT_STOP_WORDS_PATH) + "\\stopwords-ru.txt"
        stopWords.addAll(readStopWordsFromFile(stopwordsRuFilePath))

        return stopWords
    }

    private fun readStopWordsFromFile(filePath: String): List<String> {
        return try {
            FileHelper.readFileToString(filePath).lines().map { it.trim() }.filter { it.isNotEmpty() }
        } catch (e: Exception) {
            println("Error reading stop words from $filePath: ${e.message}")
            emptyList()
        }
    }
}