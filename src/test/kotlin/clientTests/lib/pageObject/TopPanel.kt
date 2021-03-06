package clientTests.lib.pageObject

import clientTests.lib.RemoteRobot
import clientTests.lib.elements.BaseElement
import clientTests.lib.elements.JTextFieldElement
import com.jetbrains.test.swingAutomationTool.data.ElementDescription
import javax.swing.JToggleButton

class TopPanel(robot: RemoteRobot, description: ElementDescription) : BaseElement(robot, description) {
    val jTableButton
        get() = this.findElements<JTextFieldElement> {
            isShowing()
            type(JToggleButton::class.java)
        }[13]
}