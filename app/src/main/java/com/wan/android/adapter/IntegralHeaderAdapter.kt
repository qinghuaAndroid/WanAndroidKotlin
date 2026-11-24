package com.wan.android.adapter

import android.content.Context
import android.view.ViewGroup
import com.chad.library.adapter4.BaseSingleItemAdapter
import com.chad.library.adapter4.viewholder.DataBindingHolder
import com.wan.android.R
import com.wan.android.databinding.HeaderIntegralBinding

class IntegralHeaderAdapter :
    BaseSingleItemAdapter<Any, DataBindingHolder<HeaderIntegralBinding>>() {
    override fun onBindViewHolder(
        holder: DataBindingHolder<HeaderIntegralBinding>,
        item: Any?
    ) {

    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): DataBindingHolder<HeaderIntegralBinding> {
        return DataBindingHolder(R.layout.header_integral, parent)
    }

}