package org.giste.profiles.domain

sealed interface Setting<T: Any> {
    val id: Long
    var override: Boolean
    var value: T
}