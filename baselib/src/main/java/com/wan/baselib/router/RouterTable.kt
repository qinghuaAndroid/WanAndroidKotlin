package com.wan.baselib.router

import android.app.Activity

object RouterTable {

    private val routes = mutableMapOf<String, Class<out Activity>>()

    fun register(
        path: String,
        activity: Class<out Activity>
    ) {
        routes[path] = activity
    }

    fun find(path: String): Class<out Activity>? {
        return routes[path]
    }
}