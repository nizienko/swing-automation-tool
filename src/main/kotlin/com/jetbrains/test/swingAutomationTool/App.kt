package com.jetbrains.test.swingAutomationTool

import com.google.gson.Gson
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
import org.fest.swing.core.GenericTypeMatcher
import org.fest.swing.finder.WindowFinder
import org.slf4j.event.Level
import java.awt.Component
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
            get("/start") {
                start()
                call.respond(Response("success"))
            }
            get("/stop") {
                stop()
                call.respond(Response("success"))
            }
            post("/elements") {
                call.respond(findElements(call.receiveJson()))
            }
            get("/{id}/click") {
                val id = call.parameters["id"] ?: throw IllegalArgumentException("empty id")
                click(id)
                call.respond(Response("success"))
            }
        }
    }.start(wait = true)
}

data class Response(val status: String)


val gson = Gson()

suspend inline fun <reified T> ApplicationCall.receiveJson(): T {
    val text = this.receiveText()
    println(text)
    return gson.fromJson(text, T::class.java)
}

