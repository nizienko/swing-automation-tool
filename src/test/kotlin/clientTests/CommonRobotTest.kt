package clientTests

import clientTests.lib.RemoteRobot
import clientTests.lib.elements.BaseElement
import clientTests.lib.elements.JTextFieldElement
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
                .findElements(
                        SearchFilter(
                                isShowing = true,
                                className = "javax.swing.JToggleButton")
                ).forEach { it.click() }
    }

    @Test
    fun editTextField() {
        remoteRobot.findElements<BaseElement>(
                SearchFilter(
                        isShowing = true,
                        className = "javax.swing.JPanel"
                )).first()
                .findElements(
                        SearchFilter(
                                isShowing = true,
                                className = "javax.swing.JToggleButton")
                )[13].click()
        remoteRobot.findElements<JTextFieldElement>(
                SearchFilter(
                        isShowing = true,
                        className = "javax.swing.JTextField"
                )
        )[1].setTest("Test text")
        println()
    }
}