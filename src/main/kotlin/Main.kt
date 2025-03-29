package ru.glebik

import kotlinx.coroutines.runBlocking
import ru.glebik.task1.ParallelWebCrawler
import ru.glebik.task2.Lemmatizer


fun main() = runBlocking {
    val urls = listOf(
        "https://habr.com/ru/articles/277509/",
        "https://vk.com/glebgafeev"
    )

    val parallelWebCrawler = ParallelWebCrawler(urls)

    //task 1
    parallelWebCrawler.crawl()

    //task 2
    Lemmatizer().execute()
}
