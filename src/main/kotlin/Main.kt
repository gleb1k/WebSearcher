package ru.glebik

import kotlinx.coroutines.runBlocking
import ru.glebik.task1.ParallelWebCrawler
import ru.glebik.task2.Lemmatizer
import ru.glebik.task3.InvertedIndexBuilder
import ru.glebik.task3.InvertedIndexParser


fun main() = runBlocking {
    val urls = listOf(
        "https://habr.com/ru/articles/277509/",
        "https://vk.com/glebgafeev"
    )

    //task 1
    ParallelWebCrawler(urls).crawl()

    //task 2
    //Lemmatizer.execute()

    //task3
    //val invertedIndex = InvertedIndexBuilder.buildInvertedIndex()

//    val queries = listOf(
//        "например & открыть & текст",
//        "например & !открыть & !текст",
//        "например | открыть | текст",
//        "например | !открыть | !текст",
//        "например & открыть & текст"
//    )
//
//    queries.forEach {
//        InvertedIndexParser.parse(
//            query = it,
//            index = invertedIndex.indexMap
//        )
//    }

    //task4

}
