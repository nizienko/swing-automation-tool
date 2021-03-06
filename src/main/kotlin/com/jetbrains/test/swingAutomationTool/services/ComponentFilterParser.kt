package com.jetbrains.test.swingAutomationTool.services

import com.jetbrains.test.swingAutomationTool.utils.LimitedMap
import java.awt.Component
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager

class ComponentFilterParser {
    private val engine: ScriptEngine = ScriptEngineManager().getEngineByExtension("kts")
    private val cachedLambdas = LimitedMap<String, (component: Component) -> Boolean>()

    init {
        engine.eval("import java.awt.*")
        engine.eval("import javax.swing.*")
    }

    @Suppress("UNCHECKED_CAST")
    fun getFilter(script: String): (component: Component) -> Boolean {
        return cachedLambdas.getOrPut(script) {
            engine.eval(script) as (component: Component) -> Boolean
        }
    }
}