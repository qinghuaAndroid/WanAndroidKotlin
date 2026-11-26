package com.wan.android.ui.navigation

import android.view.View
import com.wan.android.bean.ArticleEntity
import com.wan.baselib.router.Router
import com.wan.baselib.router.RouterPath
import com.wan.common.constant.Const

/**
 * @author cy
 * Create at 2020/5/9.
 */
class NavigationPresenter {

    val clickLabel: (View, ArticleEntity.DatasBean) -> Unit = { view, datasBean ->
        clickLabel(view, datasBean)
    }

    private fun clickLabel(view: View, datasBean: ArticleEntity.DatasBean) {
        val context = view.context ?: return
        val routerRequest = Router.build(RouterPath.PATH_BROWSER)
            .withString(Const.WEB_TITLE, datasBean.title)
            .withString(Const.WEB_URL, datasBean.link)
        Router.navigation(context, routerRequest)
    }
}