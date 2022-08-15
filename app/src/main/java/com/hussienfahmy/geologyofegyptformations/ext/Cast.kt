package com.hussienfahmy.geologyofegyptformations.ext

inline fun <reified T> Any?.doIfTypeIs(block: T.() -> Unit) {
    if (this is T) block()
}