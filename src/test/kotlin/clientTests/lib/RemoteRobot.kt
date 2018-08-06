package clientTests.lib

import clientTests.lib.elements.BaseElement
import com.google.gson.Gson
import com.jetbrains.test.swingAutomationTool.data.*
import org.apache.http.client.fluent.Request
import org.apache.http.entity.ContentType

class RemoteRobot(
        val url: String,
        private val jarPath: String,
        private val className: String
) {
    val gson = Gson()

    fun start() {
        Request.Post("$url/start")
                .bodyString(gson.toJson(ApplicationSettings(jarPath, className)), ContentType.APPLICATION_JSON)
                .execute().returnContent().asResponse<CommonResponse>()
    }

    fun stop() {
        Request.Get("$url/stop")
                .execute().returnContent().asResponse<CommonResponse>()
    }

    inline fun <reified T : BaseElement> findElements(request: SearchFilter, container: BaseElement? = null): List<T> {
        val urlString = if (container != null) {
            "$url/${container.description.id}/elements"
        } else {
            "$url/elements"
        }
        return Request.Post(urlString)
                .bodyString(gson.toJson(request), ContentType.APPLICATION_JSON)
                .execute().returnContent().asResponse<FindElementsResponse>().elementList
                .map {
                    T::class.java.getConstructor(
                            RemoteRobot::class.java, ElementDescription::class.java
                    ).newInstance(this, it)
                }
    }

    fun click(element: BaseElement) {
        Request.Get("$url/${element.description.id}/click")
                .execute().returnContent().asResponse<CommonResponse>()
    }

    fun setText(element: BaseElement, text: String) {
        Request.Post("$url/${element.description.id}/setText")
                .bodyString(text, ContentType.TEXT_PLAIN)
                .execute().returnContent().asResponse<CommonResponse>()
    }
}

