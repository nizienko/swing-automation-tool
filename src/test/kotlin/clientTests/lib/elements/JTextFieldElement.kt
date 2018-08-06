package clientTests.lib.elements

import clientTests.lib.RemoteRobot
import com.jetbrains.test.swingAutomationTool.data.ElementDescription

class JTextFieldElement(robot: RemoteRobot, description: ElementDescription) : BaseElement(robot, description) {
    fun setTest(text: String) {
        robot.setText(this, text)
    }
}