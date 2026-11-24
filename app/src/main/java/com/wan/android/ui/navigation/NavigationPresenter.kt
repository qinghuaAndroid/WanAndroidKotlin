package com.wan.android.ui.navigation

import android.content.Intent
import android.view.View
import com.wan.android.bean.ArticleEntity
import com.wan.android.ui.browser.BrowserNormalActivity
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
        val intent = Intent(context, BrowserNormalActivity::class.java).apply {
            putExtra(Const.WEB_TITLE, datasBean.title)
            putExtra(Const.WEB_URL, datasBean.link)
        }
        context.startActivity(intent)
    }
}