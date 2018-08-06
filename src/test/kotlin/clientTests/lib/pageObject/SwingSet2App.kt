package clientTests.lib.pageObject

import clientTests.lib.RemoteRobot
import clientTests.lib.elements.BaseElement
import com.jetbrains.test.swingAutomationTool.data.SearchFilter

class SwingSet2App(private val robot: RemoteRobot) {
    val topPanel
        get() = robot.findElements<TopPanel>(
                SearchFilter(
                        isShowing = true,
                        className = "SwingSet2.ToolBarPanel"
                )).first()
    val jTablePanel
        get() = robot.findElements<BaseElement>(
                SearchFilter(
                        isShowing = true,
                        className = "javax.swing.JTabbedPane"
                )
        ).first().findElements<JTablePanel>(
                SearchFilter(
                        isShowing = true,
                        className = "javax.swing.JPanel"
                )
        ).first()

}