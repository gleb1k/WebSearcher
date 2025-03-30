package ru.glebik.task3

import kotlinx.serialization.Serializable

@Serializable
data class InvertedIndex(val index: Map<String, List<Int>>)