package pl.edu.wat.wcy.epistimi.config

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.extensions.Extension

internal object ProjectConfig : AbstractProjectConfig() {
    val mongoDbConfigListener = MongoDbConfigListener()
    val postgreSqlConfigListener = PostgreSqlConfigListener()

    override fun extensions(): List<Extension> {
        return listOf(
            mongoDbConfigListener,
            postgreSqlConfigListener,
        )
    }
}
