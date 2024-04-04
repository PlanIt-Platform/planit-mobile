package com.example.planit_mobile.ui.screens.common

open class LoadState<out T>
object Idle : LoadState<Nothing>()
object Loading : LoadState<Nothing>()
data class Loaded<T>(val value: T) : LoadState<T>()

fun idle(): Idle = Idle
fun loading(): Loading = Loading
fun <T> loaded(value: T): Loaded<T> = Loaded(value)

fun <T> LoadState<T>.getOrNull(): T? = when (this) {
    is Loaded -> value
    else -> null
}

fun <T> LoadState<T>.getOrThrow(): T = when (this) {
    is Loaded -> value
    else -> throw IllegalStateException("No value available")
}
