package com.jetbrains.test.swingAutomationTool.data

data class ElementDescription(
        val id: String,
        val className: String? = null,
        val name: String? = null,
        val x: Int? = null,
        val y: Int? = null,
        val width: Int? = null,
        val height: Int? = null,
        val visible: Boolean? = null,
        val enabled: Boolean? = null,
        val valid: Boolean? = null,
        val fields: Map<String, Any?>? = null
)
