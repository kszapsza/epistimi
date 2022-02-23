package pl.edu.wat.wcy.epistimi

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.full.companionObject

fun <T : Any> T.logger(): Lazy<Logger> =
    lazy { LoggerFactory.getLogger(javaClass.unwrapCompanionObject()) }

private fun <T : Any> Class<T>.unwrapCompanionObject(): Class<*> =
    if (this.enclosingClass?.kotlin?.companionObject?.java == this) this.enclosingClass else this
