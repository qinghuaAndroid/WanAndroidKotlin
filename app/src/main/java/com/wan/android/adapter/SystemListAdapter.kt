package com.wan.android.adapter

import android.content.Context
import android.view.ViewGroup
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.DataBindingHolder
import com.wan.android.R
import com.wan.android.bean.SystemListEntity
import com.wan.android.databinding.ItemSystemBinding
import com.wan.android.ui.system.SystemListPresenter

/**
 * @author cy
 * Create at 2020/4/8.
 */
class SystemListAdapter :
    BaseQuickAdapter<SystemListEntity, DataBindingHolder<ItemSystemBinding>>() {

    private val presenter: SystemListPresenter by lazy { SystemListPresenter() }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): DataBindingHolder<ItemSystemBinding> {
        return DataBindingHolder(R.layout.item_system, parent)
    }

    override fun onBindViewHolder(
        holder: DataBindingHolder<ItemSystemBinding>,
        position: Int,
        item: SystemListEntity?
    ) {
        holder.binding.systemListEntity = item
        holder.binding.presenter = presenter
        holder.binding.executePendingBindings()
    }
}