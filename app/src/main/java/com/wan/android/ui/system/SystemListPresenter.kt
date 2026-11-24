package com.wan.android.ui.system

import android.content.Intent
import android.view.View
import com.wan.android.bean.SystemListEntity
import com.wan.android.constant.Const
import com.wan.android.ui.system.act.SystemActivity

/**
 * @author cy
 * Create at 2020/5/9.
 */
class SystemListPresenter {

    val clickLabel: (View, SystemListEntity.ChildrenBean) -> Unit =
        { view, childrenBean -> clickLabel(view, childrenBean) }

    private fun clickLabel(view: View, childrenBean: SystemListEntity.ChildrenBean) {
        val context = view.context ?: return
        val intent = Intent(context, SystemActivity::class.java)
        intent.putExtra(Const.SYSTEM_ID, childrenBean.id)
        intent.putExtra(Const.SYSTEM_TITLE, childrenBean.name)
        context.startActivity(intent)
    }
}