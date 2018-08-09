package clientTests.lib.pageObject

import clientTests.lib.RemoteRobot
import clientTests.lib.elements.BaseElement
import clientTests.lib.elements.JTextFieldElement
import com.jetbrains.test.swingAutomationTool.data.ElementDescription
import javax.swing.JTextField

class TablePanel(robot: RemoteRobot, description: ElementDescription) : BaseElement(robot, description) {
    val printingHeaderTextField
        get() = this.findElements<JTextFieldElement> {
            isShowing()
            type(JTextField::class.java)
        }.first()
}