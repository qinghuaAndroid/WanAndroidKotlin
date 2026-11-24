package com.wan.android.adapter

import android.content.Context
import android.view.ViewGroup
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.DataBindingHolder
import com.wan.android.R
import com.wan.android.bean.NavigationEntity
import com.wan.android.databinding.ItemNavigationBinding
import com.wan.android.ui.navigation.NavigationPresenter

/**
 * @author cy
 * Create at 2020/4/8.
 */
class NavigationAdapter :
    BaseQuickAdapter<NavigationEntity, DataBindingHolder<ItemNavigationBinding>>() {

    private val presenter: NavigationPresenter by lazy { NavigationPresenter() }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): DataBindingHolder<ItemNavigationBinding> {
        return DataBindingHolder(R.layout.item_navigation, parent)
    }

    override fun onBindViewHolder(
        holder: DataBindingHolder<ItemNavigationBinding>,
        position: Int,
        item: NavigationEntity?
    ) {
        holder.binding.navigationEntity = item
        holder.binding.presenter = presenter
        holder.binding.executePendingBindings()
    }
}