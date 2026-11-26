package com.wan.android.app

import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.tencent.mmkv.MMKV
import com.wan.android.ui.browser.BrowserNormalActivity
import com.wan.android.ui.collect.CollectActivity
import com.wan.android.ui.integral.IntegralActivity
import com.wan.android.ui.main.MainActivity
import com.wan.android.ui.question.QuestionActivity
import com.wan.android.ui.search.SearchActivity
import com.wan.android.ui.search.list.SearchListActivity
import com.wan.android.ui.setting.SettingActivity
import com.wan.android.ui.share.ShareListActivity
import com.wan.android.ui.system.act.SystemActivity
import com.wan.baselib.app.App
import com.wan.baselib.router.interceptor.ConditionalInterceptor
import com.wan.baselib.router.interceptor.LoginInterceptor
import com.wan.baselib.router.Router
import com.wan.baselib.router.RouterPath
import com.wan.baselib.router.RouterTags
import com.wan.baselib.router.interceptor.TagAwareInterceptor
import com.wan.common.constant.Const
import com.wan.login.ui.LoginActivity
import dagger.hilt.android.HiltAndroidApp

/**
 * Created by cy on 2019/10/21.
 *
 * 初始化操作使用jetpack-startup
 * [com.wan.android.provider.WanAppInitializer]
 */
@HiltAndroidApp
class WanApplication : App(), ViewModelStoreOwner {

    private val mmkv by lazy(LazyThreadSafetyMode.NONE) { MMKV.defaultMMKV() }
    private lateinit var mAppViewModelStore: ViewModelStore

    override fun onCreate() {
        super.onCreate()
        mAppViewModelStore = ViewModelStore()
        ProcessLifecycleOwner.get().lifecycle.addObserver(LifecycleChecker())
        registerRouter()
        registerInterceptor()
    }

    private fun registerRouter() {
        Router.init(
            hashMapOf(
                RouterPath.PATH_MAIN to MainActivity::class.java,
                RouterPath.PATH_SHARE_LIST to ShareListActivity::class.java,
                RouterPath.PATH_SETTING to SettingActivity::class.java,
                RouterPath.PATH_QUESTION to QuestionActivity::class.java,
                RouterPath.PATH_BROWSER to BrowserNormalActivity::class.java,
                RouterPath.PATH_INTEGRAL to IntegralActivity::class.java,
                RouterPath.PATH_COLLECT to CollectActivity::class.java,
                RouterPath.PATH_SEARCH to SearchActivity::class.java,
                RouterPath.PATH_SEARCH_LIST to SearchListActivity::class.java,
                RouterPath.PATH_LOGIN to LoginActivity::class.java,
                RouterPath.PATH_SYSTEM to SystemActivity::class.java
            )
        )
    }

    private fun registerInterceptor() {
        // 只有带了 "require_login" 标签的跳转才会走 LoginInterceptor
        Router.addInterceptor(
            TagAwareInterceptor(
                targetTag = RouterTags.REQUIRE_LOGIN,
                delegate = LoginInterceptor(isLoggedIn = {
                    mmkv.decodeBool(Const.IS_LOGIN, false)
                })
            )
        )

        val loginRequiredPaths = setOf(
            RouterPath.PATH_COLLECT,
        )
        // 当跳转路径在 loginRequiredPaths 中时，才执行 LoginInterceptor
        Router.addInterceptor(
            ConditionalInterceptor(
                predicate = { request -> request.path in loginRequiredPaths },
                delegate = LoginInterceptor(isLoggedIn = {
                    mmkv.decodeBool(Const.IS_LOGIN, false)
                })
            )
        )
    }

    override val viewModelStore: ViewModelStore
        get() = mAppViewModelStore

}