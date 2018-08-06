package clientTests.lib.elements

import clientTests.lib.RemoteRobot
import com.jetbrains.test.swingAutomationTool.data.ElementDescription
import com.jetbrains.test.swingAutomationTool.data.SearchFilter

open class BaseElement(val robot: RemoteRobot, val description: ElementDescription) {
    fun click() = robot.click(this)

    inline fun <reified T : BaseElement> findElements(request: SearchFilter): List<T> {
        return robot.findElements(request, this)
    }
}