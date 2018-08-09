package com.jetbrains.test.swingAutomationTool

import com.google.gson.Gson
import com.jetbrains.test.swingAutomationTool.data.CommonResponse
import com.jetbrains.test.swingAutomationTool.data.FindElementsResponse
import com.jetbrains.test.swingAutomationTool.data.ListResponse
import com.jetbrains.test.swingAutomationTool.data.Response
import com.jetbrains.test.swingAutomationTool.data.ResponseStatus.ERROR
import com.jetbrains.test.swingAutomationTool.services.*
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.Compression
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.gson.gson
import io.ktor.request.receiveText
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.slf4j.event.Level
import java.text.DateFormat

fun main(args: Array<String>) {
    embeddedServer(Netty, port = 8080) {
        install(DefaultHeaders)
        install(Compression)
        install(CallLogging) {
            level = Level.DEBUG
        }
        install(ContentNegotiation) {
            gson {
                setDateFormat(DateFormat.LONG)
                setPrettyPrinting()
            }
        }
        routing {
            post("/start") {
                call.commonRequest { start(call.receiveJson()) }
            }
            get("/stop") {
                call.commonRequest { stop() }
            }
            post("/elements") {
                call.dataRequest {
                    FindElementsResponse(
                            elementList = findElements(filter = call.receiveJson()))
                }
            }
            post("/{id}/elements") {
                call.dataRequest {
                    val id = call.parameters["id"] ?: throw IllegalArgumentException("empty id")
                    FindElementsResponse(
                            elementList = findElements(
                                    containerId = id,
                                    filter = call.receiveJson())
                    )
                }
            }
            get("/{id}/click") {
                call.commonRequest {
                    val id = call.parameters["id"] ?: throw IllegalArgumentException("empty id")
                    click(id)
                }
            }
            get("/hierarchy") {
                call.dataRequest {
                    ListResponse(list = hierarchy())
                }
            }
            post("/{id}/setText") {
                call.commonRequest {
                    val id = call.parameters["id"] ?: throw IllegalArgumentException("empty id")
                    val text = call.receiveText()
                    setText(id, text)
                }
            }
            post("/{id}/executeScript") {
                call.commonRequest {
                    val id = call.parameters["id"] ?: throw IllegalArgumentException("empty id")
                    val script = call.receiveText()
                    doAction(id, script)
                }
            }
            get("/debug") {
                call.commonRequest { debug() }
            }
        }
    }.start(wait = true)
}

suspend inline fun ApplicationCall.commonRequest(code: () -> Unit) {
    val response = try {
        code()
        CommonResponse()
    } catch (e: Throwable) {
        e.printStackTrace()
        CommonResponse(ERROR, e.message)
    }
    this.respond(response)
}

suspend inline fun ApplicationCall.dataRequest(code: () -> Response) {
    val response = try {
        code()
    } catch (e: Throwable) {
        e.printStackTrace()
        CommonResponse(ERROR, e.message)
    }
    this.respond(response)
}

val gson = Gson()

suspend inline fun <reified T> ApplicationCall.receiveJson(): T {
    val text = this.receiveText()
    println(text)
    return gson.fromJson(text, T::class.java)
}



