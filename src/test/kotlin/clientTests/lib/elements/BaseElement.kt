package clientTests.lib.elements

import clientTests.lib.RemoteRobot
import com.jetbrains.test.swingAutomationTool.data.ElementDescription
import com.jetbrains.test.swingAutomationTool.data.SearchFilter

open class BaseElement(protected val robot: RemoteRobot, val description: ElementDescription) {
    fun click() = robot.click(this)

    fun findElements(request: SearchFilter): List<BaseElement> {
        return robot.findElements(request, this)
    }
}