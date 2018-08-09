package clientTests

import clientTests.lib.RemoteRobot
import clientTests.lib.SearchScriptBuilder
import clientTests.lib.elements.BaseElement
import clientTests.lib.pageObject.SwingSet2App
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.swing.JPanel
import javax.swing.JToggleButton

class CommonRobotTest {

    private val className = "SwingSet2"
    private val url = "http://127.0.0.1:8080"
    private val classPath = "/home/eugenenizienko/IdeaProjects/SwingSet2Tests/src/test/resources/targetApp/"

    private val remoteRobot = RemoteRobot(url, className, classPath)

    private val app = SwingSet2App(remoteRobot)

    @BeforeEach
    fun runApp() {
        remoteRobot.start()
        Thread.sleep(2000)
    }

    @AfterEach
    fun closeApp() {
        remoteRobot.stop()
    }

    @Test
    fun findAndClickElementsTest() {
        remoteRobot.findElements<BaseElement> {
            isShowing()
            type(JPanel::class.java)
        }.first()
                .findElements<BaseElement> {
                    isShowing()
                    type(JToggleButton::class.java)
                }.forEach { it.click() }
    }

    @Test
    fun editTextField() {
        with(app) {
            topPanel.jTableButton.click()
            jTablePanel.printingHeaderTextField.setTest("Hello from test")
        }
    }

    @Test
    fun testScriptBuilder() {
        val script = SearchScriptBuilder().apply {
            type(JPanel::class.java)
        }.build()
        println(script)
    }
}