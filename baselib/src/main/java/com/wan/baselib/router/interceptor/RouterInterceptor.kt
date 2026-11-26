package com.wan.baselib.router.interceptor

import android.content.Context
import com.wan.baselib.router.RouterRequest

fun interface RouterInterceptor {
    fun intercept(chain: Chain)

    interface Chain {
        val context: Context
        val request: RouterRequest
        fun proceed(request: RouterRequest)
    }
}

/**
 * 按通用谓词条件决定是否执行目标拦截器，不满足则直接放行。
 */
class ConditionalInterceptor(
    private val predicate: (RouterRequest) -> Boolean,
    private val delegate: RouterInterceptor
) : RouterInterceptor {

    override fun intercept(chain: RouterInterceptor.Chain) {
        if (predicate(chain.request)) {
            delegate.intercept(chain)
        } else {
            chain.proceed(chain.request)
        }
    }
}

/**
 * 按请求标签（tag）决定是否执行目标拦截器，不包含指定 tag 则直接放行。
 */
class TagAwareInterceptor(
    private val targetTag: String,
    private val delegate: RouterInterceptor
) : RouterInterceptor {

    override fun intercept(chain: RouterInterceptor.Chain) {
        if (targetTag in chain.request.tags) {
            delegate.intercept(chain)
        } else {
            chain.proceed(chain.request)
        }
    }
}