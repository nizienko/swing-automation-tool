package clientTests.lib

import com.google.gson.Gson
import com.jetbrains.test.swingAutomationTool.data.*
import org.apache.http.client.fluent.Request
import org.apache.http.entity.ContentType

class RemoteRobot(
        private val url: String,
        private val jarPath: String,
        private val className: String
) {
    private val gson = Gson()

    fun start() {
        Request.Post("$url/start")
                .bodyString(gson.toJson(ApplicationSettings(jarPath, className)), ContentType.APPLICATION_JSON)
                .execute().returnContent().asResponse<CommonResponse>()
    }

    fun stop() {
        Request.Get("$url/stop")
                .execute().returnContent().asResponse<CommonResponse>()
    }

    fun findElements(request: SearchFilter): List<BaseElement> {
        return Request.Post("$url/elements")
                .bodyString(gson.toJson(request), ContentType.APPLICATION_JSON)
                .execute().returnContent().asResponse<FindElementsResponse>().elementList
    }

    fun BaseElement.findElements(request: SearchFilter): List<BaseElement> {
        return Request.Post("$url/${this.id}/elements")
                .bodyString(gson.toJson(request), ContentType.APPLICATION_JSON)
                .execute().returnContent().asResponse<FindElementsResponse>().elementList
    }

    fun click(element: BaseElement) {
        Request.Get("$url/${element.id}/click")
                .execute().returnContent().asResponse<CommonResponse>()
    }
}