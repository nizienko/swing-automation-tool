package com.jetbrains.test.swingAutomationTool.utils

class LimitedMap<K, V>(private val maxEntries: Long = 10_000_000L) : LinkedHashMap<K, V>() {
    override fun removeEldestEntry(eldest: MutableMap.MutableEntry<K, V>?): Boolean {
        return this.size > maxEntries
    }
}