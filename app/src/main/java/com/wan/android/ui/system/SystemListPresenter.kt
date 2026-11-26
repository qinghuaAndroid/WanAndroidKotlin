package com.wan.android.ui.system

import android.view.View
import com.wan.android.bean.SystemListEntity
import com.wan.android.constant.Const
import com.wan.baselib.router.Router
import com.wan.baselib.router.RouterPath

/**
 * @author cy
 * Create at 2020/5/9.
 */
class SystemListPresenter {

    val clickLabel: (View, SystemListEntity.ChildrenBean) -> Unit =
        { view, childrenBean -> clickLabel(view, childrenBean) }

    private fun clickLabel(view: View, childrenBean: SystemListEntity.ChildrenBean) {
        val context = view.context ?: return
        Router.navigation(
            context,
            Router.build(RouterPath.PATH_SYSTEM)
                .withInt(Const.SYSTEM_ID, childrenBean.id)
                .withString(Const.SYSTEM_TITLE, childrenBean.name)
        )
    }
}