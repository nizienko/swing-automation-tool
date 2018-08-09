package com.jetbrains.test.swingAutomationTool.data

import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

fun Any.serializeToBytes(): ByteArray = ByteArrayOutputStream().use { it -> ObjectOutputStream(it).writeObject(this); it }.toByteArray()
inline fun <reified T : Any> ByteArray.deserialize(): T = ObjectInputStream(inputStream()).readObject() as T
