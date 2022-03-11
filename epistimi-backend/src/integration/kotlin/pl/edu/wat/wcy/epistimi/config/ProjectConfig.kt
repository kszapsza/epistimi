package pl.edu.wat.wcy.epistimi.config

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.extensions.Extension

object ProjectConfig : AbstractProjectConfig() {
    val mongoListener = MongoListener()

    override fun extensions(): List<Extension> {
        return listOf(mongoListener)
    }
}
