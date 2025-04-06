package ru.glebik.task1

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withLock
import org.jsoup.Jsoup
import ru.glebik.FileHelper
import ru.glebik.FileHelper.indexSavePath
import ru.glebik.FileHelper.normalizeUrl
import java.io.File
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

class ParallelWebCrawler(
    private val urls: List<String>,
    private val targetPageCount: Int = DEFAULT_TARGET_PER_PAGE_COUNT,
    private val minWordsPerPage: Int = DEFAULT_MIN_WORDS_PER_COUNT,
) {
    private val visited = ConcurrentHashMap.newKeySet<String>()
    private val urlsQueue = ConcurrentLinkedQueue<String>().apply {
        addAll(urls)
    }

    private val indexFile = File("$indexSavePath/index.txt").apply {
        writeText("")
    }

    private var pageCounter = 1

    fun crawl() {
        if (urls.isEmpty()) {
            return
        }

        while (urlsQueue.isNotEmpty() && pageCounter <= targetPageCount) {
            val url = urlsQueue.poll().normalizeUrl()
            crawlPage(url)
        }
    }


    private fun crawlPage(url: String?) {
        runCatching {
            if (url == null || visited.contains(url) || urlsQueue.contains(url)) return

            val doc = Jsoup.connect(url).userAgent("Mozilla").get()
            val rawHtml = doc.body().html()
            val text = rawHtml
                .replace(Regex("<[^>]+>"), " ")
                .replace(Regex("\\s+"), " ")
                .trim()

            val cyrillicWords = Regex("[а-яА-ЯёЁ]+").findAll(text).map { it.value }.toList()
            val wordCount = cyrillicWords.size
            if (wordCount >= minWordsPerPage) {
                val filename = FileHelper.getPageSaveName(pageCounter)
                val fileSavePath = FileHelper.getPageSavePath(FileHelper.pagesSavePath, filename)

                File(fileSavePath).writeText(text)
                indexFile.appendText("$pageCounter $url\n")
                println("Added page $pageCounter $url to $filename, words count = $wordCount ")
                pageCounter++

            } else {
                println("Skipped $url, words count = $wordCount ")
            }

            val links = doc.select("a[href]")
                .mapNotNull { it.absUrl("href").normalizeUrl() }
                .filter {
                    visited.contains(it).not() && urlsQueue.contains(it).not() && it.startsWith("http")
                }.toSet()
            visited.add(url)
            urlsQueue.addAll(links)

        }.onFailure {
            println("Failed to process $url: ${it.message}")
        }
    }


    companion object {
        private const val DEFAULT_TARGET_PER_PAGE_COUNT = 100
        private const val DEFAULT_MIN_WORDS_PER_COUNT = 1000
    }

}


