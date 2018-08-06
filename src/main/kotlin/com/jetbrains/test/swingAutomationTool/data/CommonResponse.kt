package com.jetbrains.test.swingAutomationTool.data

import com.jetbrains.test.swingAutomationTool.data.ResponseStatus.SUCCESS
import com.jetbrains.test.swingAutomationTool.gson
import org.apache.http.client.fluent.Content

interface Response {
    val status: ResponseStatus
    val message: String?
}

data class CommonResponse(
        override val status: ResponseStatus = SUCCESS,
        override val message: String? = null) : Response

data class FindElementsResponse(
        override val status: ResponseStatus = SUCCESS,
        override val message: String? = null,
        val elementList: List<ElementDescription>) : Response

data class ListResponse(
        override val status: ResponseStatus = SUCCESS,
        override val message: String? = null,
        val list: List<Any?>
): Response

inline fun <reified R : Response> Content.asResponse(): R {
    val responseString = this.asString()
    println(responseString)
    val response = gson.fromJson(responseString, R::class.java)
    if (response.status != SUCCESS) {
        throw IllegalStateException(response.message ?: "Unknown error")
    }
    return response
}

enum class ResponseStatus { SUCCESS, ERROR }