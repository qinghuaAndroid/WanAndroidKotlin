package com.wan.baselib.router.interceptor

import com.wan.baselib.router.Router
import com.wan.baselib.router.RouterPath
import com.wan.baselib.router.navigation

class LoginInterceptor(
    private val isLoggedIn: () -> Boolean
) : RouterInterceptor {

    override fun intercept(chain: RouterInterceptor.Chain) {
        if (isLoggedIn()) {
            // 已登录，放行原请求
            chain.proceed(chain.request)
        } else {
            // 未登录，拦截跳转并改跳登录页（不调用 chain.proceed）
            Router.build(RouterPath.PATH_LOGIN)
                .withString("target_path", chain.request.path)
                .navigation(chain.context)
        }
    }
}