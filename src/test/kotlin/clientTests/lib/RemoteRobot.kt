package clientTests.lib

import clientTests.lib.elements.BaseElement
import com.google.gson.Gson
import com.jetbrains.test.swingAutomationTool.data.*
import org.apache.http.client.fluent.Request
import org.apache.http.entity.ContentType

class RemoteRobot(
        val url: String,
        private val className: String,
        private val classPath: String,
        private val args: String = ""
) {
    val gson = Gson()

    fun start() {
        Request.Post("$url/start")
                .bodyString(gson.toJson(ApplicationSettings(className, classPath, args)), ContentType.APPLICATION_JSON)
                .execute().returnContent().asResponse<CommonResponse>()
    }

    fun stop() {
        Request.Get("$url/stop")
                .execute().returnContent().asResponse<CommonResponse>()
    }

    inline fun <reified T : BaseElement> findElements(request: SearchScriptBuilder.() -> Unit): List<T> {
        return findElements(null, request)
    }

    inline fun <reified T : BaseElement> findElements(container: BaseElement?, request: SearchScriptBuilder.() -> Unit): List<T> {
        val urlString = if (container != null) {
            "$url/${container.description.id}/elements"
        } else {
            "$url/elements"
        }
        return Request.Post(urlString)
                .bodyString(gson.toJson(SearchFilter(script = SearchScriptBuilder().apply(request).build())), ContentType.APPLICATION_JSON)
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

    fun executeScript(element: BaseElement, script: String) {
        Request.Post("$url/${element.description.id}/executeScript")
                .bodyString(script, ContentType.TEXT_PLAIN)
                .execute().returnContent().asResponse<CommonResponse>()
    }
}

