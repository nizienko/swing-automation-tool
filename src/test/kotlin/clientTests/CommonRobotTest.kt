package clientTests

import clientTests.lib.RemoteRobot
import clientTests.lib.elements.BaseElement
import clientTests.lib.pageObject.SwingSet2App
import com.google.gson.Gson
import com.jetbrains.test.swingAutomationTool.data.SearchFilter
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CommonRobotTest {

    val gson = Gson()

    private val jarPath = "/home/eugenenizienko/IdeaProjects/SwingSet2Tests/src/test/resources/targetApp/SwingSet2.jar"
    private val className = "SwingSet2"
    private val url = "http://127.0.0.1:8080"

    private val remoteRobot = RemoteRobot(url, jarPath, className)

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
        remoteRobot.findElements<BaseElement>(
                SearchFilter(
                        isShowing = true,
                        className = "javax.swing.JPanel"
                )).first()
                .findElements<BaseElement>(
                        SearchFilter(
                                isShowing = true,
                                className = "javax.swing.JToggleButton")
                ).forEach { it.click() }
    }

    @Test
    fun editTextField() {
        with(app) {
            topPanel.jTableButton.click()
            jTablePanel.printingHeaderTextField.setTest("Hello from test")
        }
        println()
    }
}