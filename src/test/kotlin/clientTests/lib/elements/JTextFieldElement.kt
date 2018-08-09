package clientTests.lib.elements

import clientTests.lib.RemoteRobot
import clientTests.lib.generateScript
import com.jetbrains.test.swingAutomationTool.data.ElementDescription
import javax.swing.text.JTextComponent

class JTextFieldElement(robot: RemoteRobot, description: ElementDescription) : BaseElement(robot, description) {
    // example of executing action with remoteRobot from test
    fun setTest(text: String) {
        remoteRobot.executeScript(
                this,
                generateScript<JTextComponent>("""
                    JTextComponentFixture(r, c).setText("$text")
                """.trimIndent()))
    }
}