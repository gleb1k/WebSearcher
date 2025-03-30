package ru.glebik

import java.io.File
import java.net.URI

object FileHelper {

    private const val DEFAULT_PAGES_PATH = "\\src\\main\\kotlin\\task1\\content\\pages"
    private const val DEFAULT_INDEX_PATH = "\\src\\main\\kotlin\\task1\\content"

    private const val DEFAULT_LEMMATIZED_PATH = "\\src\\main\\kotlin\\task2\\content"

    private const val DEFAULT_INVERTED_INDEX_PATH = "\\src\\main\\kotlin\\task3\\content"

    val indexSavePath = getSavePath(DEFAULT_INDEX_PATH)

    val pagesSavePath = getSavePath(DEFAULT_PAGES_PATH)

    val lemmatizedSavePath = getSavePath(DEFAULT_LEMMATIZED_PATH)

    val invertedIndexSavePath = getSavePath(DEFAULT_INVERTED_INDEX_PATH)

    fun getSavePath(path: String): String {
        val projectRoot = System.getProperty("user.dir")
        val savePath = projectRoot + path

        val directory = File(savePath)
        directory.mkdirs()

        return savePath
    }

    fun String.normalizeUrl(): String? {
        return runCatching {
            val url = URI.create(this).toURL()
            "${url.protocol}://${url.host}${url.path}"
        }.getOrNull()
    }

    fun getPageSaveName(index: Int): String {
        return "page_${index}.txt"
    }

    fun getLemmatizedSaveName(index: Int): String {
        return "lemmatized_${index}.txt"
    }

    fun getPageSavePath(pagesSavePath: String, fileName: String): String {
        val fileSavePath = "$pagesSavePath\\$fileName"
        return fileSavePath
    }

    fun readFileToString(filePath: String): String {
        return File(filePath).readText()
    }
}