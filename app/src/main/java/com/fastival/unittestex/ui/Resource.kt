package com.fastival.unittestex.ui

sealed class Resource<out T>(val data: T? = null, val msg: String? = null) {

    class Success<out T>(data: T, msg: String?) : Resource<T>(data, msg)
    class Error<out T>(data: T? = null, message: String) : Resource<T>(data, message)
    class Loading<out T>(data: T? = null) : Resource<T>(data)

    override fun equals(other: Any?): Boolean {
        if (other?.javaClass != javaClass) return false

        val resource = other as Resource<T>

        if (resource.data != null) {
            if (resource.data != data) {
                return false
            }
        }

        if (resource.msg != null) {
            if (msg == null) return false
            if (resource.msg != msg) return false
        }
        return true
    }
}