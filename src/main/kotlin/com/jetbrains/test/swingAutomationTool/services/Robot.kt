package com.jetbrains.test.swingAutomationTool.services

import com.jetbrains.test.swingAutomationTool.data.ApplicationSettings
import com.jetbrains.test.swingAutomationTool.data.ElementDescription
import com.jetbrains.test.swingAutomationTool.data.SearchFilter
import org.fest.swing.core.BasicRobot
import org.fest.swing.core.Robot
import org.fest.swing.fixture.JTextComponentFixture
import org.fest.swing.launcher.ApplicationLauncher
import java.awt.Component
import java.awt.Container
import java.io.File
import java.lang.reflect.Modifier
import java.net.URL
import java.net.URLClassLoader
import java.util.*
import javax.swing.text.JTextComponent
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

fun findElements(containerId: String? = null, filter: SearchFilter): List<ElementDescription> {
    if (containerId == null) {
        return robot.finder()
                .findAll { it.filter(filter) }
                .map {
                    val uid = UUID.randomUUID().toString()
                    componentStorage[uid] = it
                    return@map it.getDescription(uid)
                }
    } else {
        val component = componentStorage[containerId] ?: throw IllegalStateException("Unknown element id $containerId")
        if (component is Container) {
            return robot.finder()
                    .findAll { it.filter(filter) }
                    .map {
                        val uid = UUID.randomUUID().toString()
                        componentStorage[uid] = it
                        return@map it.getDescription(uid)
                    }
        } else throw IllegalStateException("Component is not a container")
    }
}

fun click(id: String) {
    val component = componentStorage[id] ?: throw IllegalStateException("Unknown element id $id")
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
        val thisElement: ElementDescription,
        val children: List<DescribedComponent>
)

private fun Component.toDescribed(): DescribedComponent {
    val children = mutableListOf<DescribedComponent>()
    if (this is Container) {
        this.components.forEach {
            children.add(it.toDescribed())
        }
    }
    return DescribedComponent(this.getDescription(""), children)
}


private fun Component.filter(filter: SearchFilter): Boolean {
    var result = true
    filter.isShowing?.let { result = result && this.isShowing }
    filter.name?.let { result = result && this.name == it }
    filter.className?.let { result = result && this::class.java.canonicalName == it }
    return result
}

private fun Component.getDescription(id: String): ElementDescription {
    val fieldsMap = mutableMapOf<String, Any?>()
    this::class.java.declaredFields.forEach {
        val name = it.name
        if (Modifier.isPublic(it.modifiers)) {
            val value = it.get(this)
            fieldsMap[name] = value
        } else {
            it.isAccessible = true
            val value = it.get(this)
            fieldsMap[name] = value?.toString()
        }
    }
    return ElementDescription(
            id,
            this::class.java.canonicalName,
            this.name,
            this.x,
            this.y,
            this.width,
            this.height,
            this.isVisible,
            this.isEnabled,
            this.isValid,
            fieldsMap
    )
}

fun setText(id: String, text: String) {
    val component = componentStorage[id] ?: throw IllegalStateException("Unknown element id $id")
    if (component is JTextComponent) {
        JTextComponentFixture(robot, component).setText(text)
    } else {
        throw IllegalStateException("Component is not a TextField ")
    }
}