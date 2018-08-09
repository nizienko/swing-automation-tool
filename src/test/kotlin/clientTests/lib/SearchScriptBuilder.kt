package clientTests.lib

import java.awt.Component
import java.lang.reflect.Type
import java.util.*

class SearchScriptBuilder {
    val conditions = StringJoiner(" && ")

    fun isShowing() {
        custom("isShowing()")
    }

    fun type(type: Type) {
        conditions.add("""c::class.java.canonicalName == "${type.typeName}"""")
    }

    inline fun <reified T : Component> type() {
        conditions.add("""c is ${T::class.qualifiedName}""")
    }

    fun custom(condition: String) {
        conditions.add("c.$condition")
    }

    fun canonicalName(name: String) {
        conditions.add("""c::class.java.canonicalName == "$name"""")
    }

    fun canonicalNameContains(name: String) {
        conditions.add("""c::class.java.canonicalName.contains("$name")""")
    }

    fun build(): String {
        return """{c: Component -> $conditions }"""
    }
}