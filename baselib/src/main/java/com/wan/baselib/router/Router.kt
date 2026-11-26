package com.wan.baselib.router

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.wan.baselib.router.interceptor.RouterInterceptor

object Router {

    private val globalInterceptors = mutableListOf<RouterInterceptor>()

    fun init(routerMap: Map<String, Class<out Activity>>) {
        routerMap.forEach { (path, activityClass) ->
            RouterTable.register(path, activityClass)
        }
    }

    fun addInterceptor(interceptor: RouterInterceptor) = apply {
        globalInterceptors.add(interceptor)
    }

    fun addInterceptors(vararg list: RouterInterceptor) = apply {
        globalInterceptors.addAll(list)
    }

    fun build(path: String): RouterRequest {
        return RouterRequest(path)
    }

    fun navigation(
        context: Context,
        request: RouterRequest
    ) {
        val chainInterceptors = buildList {
            addAll(globalInterceptors)
            addAll(request.interceptors)
        }

        val realChain = RealInterceptorChain(
            context = context,
            request = request,
            interceptors = chainInterceptors,
            index = 0
        )
        realChain.proceed(request)
    }

    private fun startActivityInternal(context: Context, request: RouterRequest) {
        val activityClass = RouterTable.find(request.path)
            ?: throw IllegalArgumentException("Route not found: ${request.path}")

        val intent = Intent(context, activityClass)
        intent.putExtras(request.getParams())

        if (context !is Activity) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        context.startActivity(intent)
    }

    private class RealInterceptorChain(
        override val context: Context,
        override val request: RouterRequest,
        private val interceptors: List<RouterInterceptor>,
        private val index: Int
    ) : RouterInterceptor.Chain {

        override fun proceed(request: RouterRequest) {
            if (index < interceptors.size) {
                val next = RealInterceptorChain(
                    context = context,
                    request = request,
                    interceptors = interceptors,
                    index = index + 1
                )
                interceptors[index].intercept(next)
            } else {
                startActivityInternal(context, request)
            }
        }
    }
}

fun RouterRequest.navigation(context: Context) {
    Router.navigation(context, this)
}