package com.wan.baselib.router

import android.os.Bundle
import android.os.Parcelable
import com.wan.baselib.router.interceptor.RouterInterceptor

class RouterRequest(
    val path: String
) {
    private val params = Bundle()
    private val _tags = mutableSetOf<String>()
    private val _interceptors = mutableListOf<RouterInterceptor>()

    val tags: Set<String>
        get() = _tags

    internal val interceptors: List<RouterInterceptor>
        get() = _interceptors

    fun addTag(tag: String) = apply {
        _tags.add(tag)
    }

    fun addTags(vararg tags: String) = apply {
        _tags.addAll(tags)
    }

    fun addInterceptor(interceptor: RouterInterceptor) = apply {
        _interceptors.add(interceptor)
    }

    fun addInterceptors(vararg list: RouterInterceptor) = apply {
        _interceptors.addAll(list)
    }

    fun withString(key: String, value: String?) = apply {
        params.putString(key, value)
    }

    fun withInt(key: String, value: Int) = apply {
        params.putInt(key, value)
    }

    fun withBoolean(key: String, value: Boolean) = apply {
        params.putBoolean(key, value)
    }

    fun withLong(key: String, value: Long) = apply {
        params.putLong(key, value)
    }

    fun withBundle(bundle: Bundle) = apply {
        params.putAll(bundle)
    }

    fun withObject(key: String, value: Parcelable?) = apply {
        params.putParcelable(key, value)
    }

    internal fun getParams(): Bundle = params
}