package clientTests

import clientTests.lib.RemoteRobot
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jetbrains.test.swingAutomationTool.data.ApplicationSettings
import com.jetbrains.test.swingAutomationTool.data.BaseElement
import com.jetbrains.test.swingAutomationTool.data.SearchFilter
import org.apache.http.client.fluent.Content
import org.apache.http.client.fluent.Request
import org.apache.http.entity.ContentType
import org.junit.jupiter.api.Test

class CommonRobotTest {

    val gson = Gson()

    private val jarPath = "/home/def/IdeaProjects/SwingSet2Tests/src/test/resources/targetApp/SwingSet2.jar"
    private val className = "SwingSet2"
    private val url = "http://127.0.0.1:8080"

    @Test
    fun findElementsTest() {
        with(RemoteRobot(url, jarPath, className)) {
            start()
            Thread.sleep(2000)
            val topPanel = findElements(
                    SearchFilter(
                            isShowing = true,
                            className = "javax.swing.JPanel"

                    )).first()
            topPanel.findElements(
                    SearchFilter(
                            isShowing = true,
                            className = "javax.swing.JToggleButton")
            ).forEach { click(it) }
            stop()
        }

    }
}