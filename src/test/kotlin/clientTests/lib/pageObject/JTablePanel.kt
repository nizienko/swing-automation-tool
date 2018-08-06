package clientTests.lib.pageObject

import clientTests.lib.RemoteRobot
import clientTests.lib.elements.BaseElement
import clientTests.lib.elements.JTextFieldElement
import com.jetbrains.test.swingAutomationTool.data.ElementDescription
import com.jetbrains.test.swingAutomationTool.data.SearchFilter

class JTablePanel(robot: RemoteRobot, description: ElementDescription) : BaseElement(robot, description) {
    val printingHeaderTextField
        get() = this.findElements<JTextFieldElement>(
                SearchFilter(
                        isShowing = true,
                        className = "javax.swing.JTextField"
                )
        ).first()
}