package com.wan.android.adapter

import android.content.Context
import android.view.ViewGroup
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.DataBindingHolder
import com.wan.android.R
import com.wan.android.bean.IntegralRecordEntity
import com.wan.android.databinding.ItemIntegralBinding

/**
 * @author cy
 * Create at 2020/4/14.
 */
class IntegralAdapter :
    BaseQuickAdapter<IntegralRecordEntity.DatasBean, DataBindingHolder<ItemIntegralBinding>>() {

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): DataBindingHolder<ItemIntegralBinding> {
        return DataBindingHolder(R.layout.item_integral, parent)
    }

    override fun onBindViewHolder(
        holder: DataBindingHolder<ItemIntegralBinding>,
        position: Int,
        item: IntegralRecordEntity.DatasBean?
    ) {
        holder.binding.data = item
        holder.binding.executePendingBindings()
    }

}