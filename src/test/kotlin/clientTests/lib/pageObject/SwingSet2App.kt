package clientTests.lib.pageObject

import clientTests.lib.RemoteRobot
import clientTests.lib.elements.BaseElement
import javax.swing.JPanel
import javax.swing.JTabbedPane

class SwingSet2App(private val robot: RemoteRobot) {
    val topPanel
        get() = robot.findElements<TopPanel> {
            isShowing()
            canonicalName("SwingSet2.ToolBarPanel")
        }.first()

    val jTablePanel
        get() = robot.findElements<BaseElement> {
            isShowing()
            type(JTabbedPane::class.java)
        }.first().findElements<TablePanel> {
            isShowing()
            type(JPanel::class.java)
        }.first()
}