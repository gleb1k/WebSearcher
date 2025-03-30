package ru.glebik.task3

import kotlinx.serialization.Serializable

@Serializable
data class InvertedIndex(val indexMap: Map<String, List<Int>>)