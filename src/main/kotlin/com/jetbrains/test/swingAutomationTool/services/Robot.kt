package com.jetbrains.test.swingAutomationTool.services

import com.jetbrains.test.swingAutomationTool.data.JbElement
import com.jetbrains.test.swingAutomationTool.data.SearchFilter
import org.fest.swing.core.BasicRobot
import org.fest.swing.core.GenericTypeMatcher
import org.fest.swing.core.Robot
import org.fest.swing.launcher.ApplicationLauncher
import java.awt.Component
import java.io.File
import java.net.URL
import java.net.URLClassLoader
import java.util.*
import kotlin.concurrent.thread

private var applicationThread: Thread? = null
private var _robot: Robot? = null
private val componentStorage = mutableMapOf<String, Component>()

val robot
    get() = _robot ?: throw IllegalStateException("Start app first")


private val jarPath = "/home/eugenenizienko/IdeaProjects/SwingSet2Tests/src/test/resources/targetApp/SwingSet2.jar"
private val className = "SwingSet2"

fun start() {
    if (applicationThread == null) {
        applicationThread = thread {
            val classLoader = URLClassLoader(arrayOf<URL>(File(jarPath).toURI().toURL()))
            try {
                val clazz = classLoader.loadClass(className)
                ApplicationLauncher.application(clazz).start()
            } catch (e: ClassNotFoundException) {
                throw IllegalArgumentException("Bad className $className", e)
            }
        }
        _robot = BasicRobot.robotWithCurrentAwtHierarchy()
    }
}

fun stop() {
    _robot?.cleanUp()
    _robot = null
    applicationThread?.join()
    applicationThread = null
}

fun findElements(filter: SearchFilter): List<JbElement> {
    return robot.finder()
            .findAll { it.filter(filter) }
            .map {
                val uid = UUID.randomUUID().toString()
                componentStorage[uid] = it
                return@map it.toData(uid)
            }
}

fun click(id: String) {
    val component = componentStorage[id]?:throw IllegalStateException("Unknown element id $id")
    robot.click(component)
}


private fun Component.filter(filter: SearchFilter): Boolean {
    var result = true
    filter.isShowing?.let { result = result && this.isShowing }
    filter.name?.let { result = result && this.name == it }
    filter.className?.let { result = result && this::class.java.canonicalName == it }
    return result
}

private fun Component.toData(id: String): JbElement = JbElement(
        id,
        this::class.java.canonicalName,
        this.name
)

inline fun <reified T : Component> matcher(crossinline matchFun: (component: T) -> Boolean): GenericTypeMatcher<T> =
        object : GenericTypeMatcher<T>(T::class.java) {
            override fun isMatching(component: T): Boolean {
                return matchFun(component)
            }
        }