package ru.glebik

import ru.glebik.task3.InvertedIndexBuilder
import ru.glebik.task4.TermMetricsCalculator
import ru.glebik.task4.TfWord
import java.io.FileOutputStream
import java.io.OutputStream


fun main() {
    val urls = listOf(
        "https://habr.com/ru/articles/277509/",
        "https://vk.com/glebgafeev"
    )

    //task 1
    //ParallelWebCrawler(urls).crawl()

    //task 2
    //Lemmatizer.execute()

    //task3
    val invertedIndex = InvertedIndexBuilder.buildInvertedIndex()

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
}

fun OutputStream.writeCsvMap(map: Map<String, Any>, name1: String, name2: String) {
    val writer = bufferedWriter()
    writer.write(""""$name1", "$name2"""")
    writer.newLine()
    map.forEach { (key, value) ->
        writer.write("${key}, $value")
        writer.newLine()
    }
    writer.flush()
}

fun OutputStream.writeTfWordsCsv(tfWords: List<TfWord>) {
    val writer = bufferedWriter()

    // Сначала собираем все уникальные docId
    val allDocIds = tfWords
        .flatMap { it.docs }
        .map { it.id }
        .toSet()
        .sorted()

    // Записываем заголовок
    writer.write("word" + allDocIds.joinToString(separator = ",", prefix = ",") { "doc$it" })
    writer.newLine()

    // Записываем строки
    for (tfWord in tfWords) {
        val tfMap = tfWord.docs.associate { it.id to it.tfValue }
        val row = buildString {
            append(tfWord.word)
            for (docId in allDocIds) {
                append(",")
                append(tfMap[docId]?.toString() ?: "")
            }
        }
        writer.write(row)
        writer.newLine()
    }

    writer.flush()
}