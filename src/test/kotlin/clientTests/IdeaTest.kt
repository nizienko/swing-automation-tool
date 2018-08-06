package clientTests

import clientTests.lib.RemoteRobot
import org.junit.jupiter.api.Test

class IdeaTest {
    private val className = "com.intellij.idea.Main"
    private val url = "http://127.0.0.1:8080"

    private val classPath = "/home/eugenenizienko/soft/testIdea/idea/lib/"

    private val remoteRobot = RemoteRobot(url, className, classPath)

    @Test
    fun test() {
        remoteRobot.start()
    }
}