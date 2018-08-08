package com.jetbrains.test.swingAutomationTool.services

import java.awt.Component
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager

class ComponentFilterParser {
    private val engine: ScriptEngine = ScriptEngineManager().getEngineByExtension("kts")

    init {
        engine.eval("import java.awt.*")
    }

    @Suppress("UNCHECKED_CAST")
    fun getFilter(script: String): (component: Component) -> Boolean {
        return engine.eval(script) as (component: Component) -> Boolean

    }
}