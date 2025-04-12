package ru.glebik

import ru.glebik.task1.ParallelWebCrawler
import ru.glebik.task2.Lemmatizer
import ru.glebik.task3.InvertedIndexBuilder
import ru.glebik.task3.InvertedIndexParser
import ru.glebik.task4.TermMetricsCalculator
import ru.glebik.task5.WebSearcher
import java.io.FileOutputStream


fun main() {
    val urls = listOf(
        "https://habr.com/ru/articles/277509/",
        "https://vk.com/glebgafeev"
    )

    //task 1
    ParallelWebCrawler(urls).crawl()

    //task 2
    Lemmatizer.execute()

    //task3
    val invertedIndex = InvertedIndexBuilder.buildInvertedIndex()

    val queries = listOf(
        "например & открыть & текст",
        "например & !открыть & !текст",
        "например | открыть | текст",
        "например | !открыть | !текст",
        "например & открыть & текст"
    )

    queries.forEach {
        InvertedIndexParser.parse(
            query = it,
            index = invertedIndex.indexMap
        )
    }

    //task4
    val termMetricsCalculator = TermMetricsCalculator(invertedIndex)

    //idf
    val idfMap = termMetricsCalculator.calculateIdf()
    FileOutputStream("${FileHelper.csvSavePath}/idf.csv").apply { writeCsvMap(idfMap, "word", "idf") }

    //tf
    val tfDocuments = termMetricsCalculator.calculateTf()
    FileOutputStream("${FileHelper.csvSavePath}/tf.csv").apply { writeTfWordsCsv(tfDocuments) }

    //tf-idf
    val tfIdfDocuments = termMetricsCalculator.calculateTfIdf(tfDocuments, idfMap)
    FileOutputStream("${FileHelper.csvSavePath}/tf_idf.csv").apply { writeTfWordsCsv(tfIdfDocuments) }

    //task5
    val ws = WebSearcher(tfIdfDocuments, idfMap)
    val queriesSearch = listOf(
        "кот",
        "кот собака",
        "как выбрать кота",
        "совет какой купить автомобиль",
        "рост прибыль"
    )

    queriesSearch.forEach {
        ws.search(it)
    }

}
