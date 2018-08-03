package clientTests

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jetbrains.test.swingAutomationTool.data.JbElement
import com.jetbrains.test.swingAutomationTool.data.SearchFilter
import org.apache.http.client.fluent.Content
import org.apache.http.client.fluent.Request
import org.apache.http.entity.ContentType
import org.junit.jupiter.api.Test

class CommonRobotTest {

    val gson = Gson()


    @Test
    fun findElements() {
        sendGetRequest("start")
        Thread.sleep(2000)
        val elements = findElements(SearchFilter(isShowing = true, name = "null.layeredPane"))
        elements.forEach {
            clickElement(it.id)
        }

        sendGetRequest("stop")
    }

    fun sendGetRequest(method: String) {
        println(Request.Get("http://127.0.0.1:8080/$method")
                .execute().returnContent().asString())
    }

    fun clickElement(id: String) {
        println(Request.Get("http://127.0.0.1:8080/$id/click")
                .execute().returnContent().asString())
    }

    fun findElements(request: SearchFilter): List<JbElement> {
        return Request.Post("http://127.0.0.1:8080/elements")
                .bodyString(gson.toJson(request), ContentType.APPLICATION_JSON)
                .execute().returnContent().parseElementList()

    }

    fun Content.parseElementList(): List<JbElement> {
        val type = object : TypeToken<List<JbElement>>() {}.type
        return gson.fromJson(this.asString(), type)
    }
}