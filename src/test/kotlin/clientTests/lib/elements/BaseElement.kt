package clientTests.lib.elements

import clientTests.lib.RemoteRobot
import clientTests.lib.SearchScriptBuilder
import com.jetbrains.test.swingAutomationTool.data.ElementDescription

open class BaseElement(val remoteRobot: RemoteRobot, val description: ElementDescription) {
    fun click() = remoteRobot.click(this)

    inline fun <reified T : BaseElement> findElements(request: SearchScriptBuilder.() -> Unit): List<T> {
        return remoteRobot.findElements(this, request)
    }
}