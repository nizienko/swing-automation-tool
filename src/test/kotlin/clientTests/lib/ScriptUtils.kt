package clientTests.lib

import java.awt.Component

/*
generate script to execute in on server side with type checking. If component is not a T than the Test will failed
 */
inline fun <reified T : Component> generateScript(script: String): String {
    return """
        { r: Robot, c: Component ->
                if (c is ${T::class.qualifiedName}) {
                    $script
                } else {
                    throw AssertionError("Component is not a ${T::class.qualifiedName}")
                }
            }
    """.trimIndent()
}