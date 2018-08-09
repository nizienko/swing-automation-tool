package com.jetbrains.test.swingAutomationTool.services

import com.jetbrains.test.swingAutomationTool.utils.LimitedMap
import org.fest.swing.core.Robot
import java.awt.Component
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager

class RobotActionParser {
    private val engine: ScriptEngine = ScriptEngineManager().getEngineByExtension("kts")
    private val cachedLambdas = LimitedMap<String, (robot: Robot, component: Component) -> Unit>()

    init {
        engine.eval("import java.awt.*")
        engine.eval("import javax.swing.*")
        engine.eval("import javax.swing.text.*")
        engine.eval("import org.fest.swing.fixture.*")
        engine.eval("import org.fest.swing.core.*")
    }

    @Suppress("UNCHECKED_CAST")
    fun getAction(script: String): (robot: Robot, component: Component) -> Unit {
        return cachedLambdas.getOrPut(script) {
            engine.eval(script) as (robot: Robot, component: Component) -> Unit
        }
    }
}