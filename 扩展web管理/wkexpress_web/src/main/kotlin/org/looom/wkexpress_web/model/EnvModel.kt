package org.looom.wkexpress_web.model

class EnvModel {
    //    var env: HashMap<Any, Any> = EnvUtils.ENV
    var webUi_resPath: String = webUi_resPath()

    companion object {
        var env = HashMap<Any, Any>()

        fun webUi_resPath(): String = env["web_ui"].toString() + "/"

        init {
            env["web_ui"] = "traditional"
        }
    }
}