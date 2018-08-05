package com.jetbrains.test.swingAutomationTool.services

import com.jetbrains.test.swingAutomationTool.data.ApplicationSettings
import com.jetbrains.test.swingAutomationTool.data.BaseElement
import com.jetbrains.test.swingAutomationTool.data.SearchFilter
import org.fest.swing.core.BasicRobot
import org.fest.swing.core.GenericTypeMatcher
import org.fest.swing.core.Robot
import org.fest.swing.launcher.ApplicationLauncher
import java.awt.Component
import java.awt.Container
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


fun start(settings: ApplicationSettings) {
    if (applicationThread == null) {
        applicationThread = thread {
            val classLoader = URLClassLoader(arrayOf<URL>(File(settings.path).toURI().toURL()))
            try {
                val clazz = classLoader.loadClass(settings.className)
                ApplicationLauncher.application(clazz).start()
            } catch (e: ClassNotFoundException) {
                throw IllegalArgumentException("Bad className ${settings.className}", e)
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

fun findElements(containerId: String? = null, filter: SearchFilter): List<BaseElement> {
    if (containerId == null) {
        return robot.finder()
                .findAll { it.filter(filter) }
                .map {
                    val uid = UUID.randomUUID().toString()
                    componentStorage[uid] = it
                    return@map it.toBaseElement(uid)
                }
    } else {
        val component = componentStorage[containerId]?:throw IllegalStateException("Unknown element id $containerId")
        if (component is Container) {
            return robot.finder()
                    .findAll { it.filter(filter) }
                    .map {
                        val uid = UUID.randomUUID().toString()
                        componentStorage[uid] = it
                        return@map it.toBaseElement(uid)
                    }
        } else throw IllegalStateException("Component is not a container")
    }
}

fun click(id: String) {
    val component = componentStorage[id]?:throw IllegalStateException("Unknown element id $id")
    robot.click(component)
}

fun hierarchy(): List<Any> {
    val list = mutableListOf<Any>()
    robot.hierarchy().roots().forEach {
        list.add(it.toDescribed())
    }
    return list
}

data class DescribedComponent(
        val thisElement: BaseElement,
        val children: List<DescribedComponent>
)

private fun Component.toDescribed(): DescribedComponent {
    val children = mutableListOf<DescribedComponent>()
    if (this is Container) {
        this.components.forEach {
            children.add(it.toDescribed())
        }
    }
    return DescribedComponent(this.toBaseElement(""), children)
}


private fun Component.filter(filter: SearchFilter): Boolean {
    var result = true
    filter.isShowing?.let { result = result && this.isShowing }
    filter.name?.let { result = result && this.name == it }
    filter.className?.let { result = result && this::class.java.canonicalName == it }
    return result
}

private fun Component.toBaseElement(id: String): BaseElement = BaseElement(
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