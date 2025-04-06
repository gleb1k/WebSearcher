package ru.glebik

import ru.glebik.task4.TfWord
import java.io.OutputStream
import java.math.RoundingMode

fun Double.round(decimals: Int = 5): Double {
    return this.toBigDecimal().setScale(decimals, RoundingMode.UP).toDouble()
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

    val allDocIds = tfWords
        .flatMap { it.docs }
        .map { it.id }
        .toSet()
        .sorted()

    writer.write("word" + allDocIds.joinToString(separator = ",", prefix = ",") { "doc$it" })
    writer.newLine()

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