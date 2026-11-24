package com.wan.android.adapter

import android.content.Context
import android.view.ViewGroup
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.viewholder.DataBindingHolder
import com.wan.android.R
import com.wan.android.bean.ArticleEntity
import com.wan.android.databinding.ItemHomeArticleBinding

class CollectAdapter :
    BaseQuickAdapter<ArticleEntity.DatasBean, DataBindingHolder<ItemHomeArticleBinding>>() {
    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): DataBindingHolder<ItemHomeArticleBinding> {
        return DataBindingHolder(R.layout.item_home_article, parent)
    }

    override fun onBindViewHolder(
        holder: DataBindingHolder<ItemHomeArticleBinding>,
        position: Int,
        item: ArticleEntity.DatasBean?
    ) {
        holder.binding.article = item
        holder.binding.collected = true
        holder.binding.executePendingBindings()
    }
}