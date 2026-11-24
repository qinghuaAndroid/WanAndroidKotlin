package com.wan.android.ui.myarticle

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.QuickAdapterHelper
import com.chad.library.adapter4.loadState.LoadState
import com.chad.library.adapter4.loadState.trailing.TrailingLoadStateAdapter
import com.wan.android.R
import com.wan.android.adapter.ArticleAdapter
import com.wan.android.bean.ArticleEntity
import com.wan.android.databinding.ActivityMyArticleBinding
import com.wan.android.ui.browser.BrowserNormalActivity
import com.wan.android.ui.collect.CollectViewModel
import com.wan.baselib.ext.collectOnLifecycle
import com.wan.baselib.ext.showToast
import com.wan.baselib.mvvm.BaseVMActivity
import com.wan.common.base.BaseUiState
import com.wan.common.base.ListUiState
import com.wan.common.constant.Const
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author cy
 * Create at 2020/4/15.
 */
@AndroidEntryPoint
class MyArticleActivity : BaseVMActivity<MyArticleViewModel, ActivityMyArticleBinding>() {

    private val articleViewModel by viewModels<MyArticleViewModel>()
    private val collectViewModel by viewModels<CollectViewModel>()
    private val articleAdapter by lazy { ArticleAdapter() }
    private lateinit var helper: QuickAdapterHelper
    private var curPosition = 0

    override fun subscribeUi() {
        articleViewModel.uiState.collectOnLifecycle(this) { uiState ->
            when (uiState) {
                is ListUiState.Loading -> {
                    binding.swipeRefresh.isRefreshing = true
                    // 重置“加载更多”时状态
                    helper.trailingLoadState = LoadState.None
                }

                is ListUiState.Success -> {
                    binding.swipeRefresh.isRefreshing = false
                    if (uiState.isRefresh) {
                        articleAdapter.submitList(uiState.data.shareArticles?.datas)
                    } else {
                        uiState.data.shareArticles?.datas?.let { articleAdapter.addAll(it) }
                    }
                    helper.trailingLoadState = LoadState.NotLoading(!uiState.hasMore)
                }

                is ListUiState.Failure -> {
                    binding.swipeRefresh.isRefreshing = false
                    uiState.exception.message?.let { showToast(it) }
                    helper.trailingLoadState = LoadState.Error(uiState.exception)
                }
            }
        }
        collectViewModel.uiState.collectOnLifecycle(this) { uiState ->
            when (uiState) {
                is BaseUiState.Loading -> showProgressDialog()
                is BaseUiState.Success -> {
                    dismissProgressDialog()
                    articleAdapter.items[curPosition].collect = uiState.data
                    articleAdapter.notifyItemChanged(curPosition)
                }

                is BaseUiState.Failure -> {
                    dismissProgressDialog()
                    uiState.exception.message?.let { showToast(it) }
                }
            }
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_my_article

    override fun initData(savedInstanceState: Bundle?) {

    }

    override fun initView() {
        title = getString(R.string.my_article)
        initRecyclerView()
        binding.swipeRefresh.setOnRefreshListener { loadData() }
    }

    private fun initRecyclerView() {
        binding.rvMyArticle.layoutManager = LinearLayoutManager(this)
        helper = QuickAdapterHelper.Builder(articleAdapter)
            .setTrailingLoadStateAdapter(mOnTrailingListener)
            .setTrailPreloadSize(0) // 预加载（默认值为0）
            .isTrailAutoLoadMore(true) // 是否自动加载更多（默认为true）
            .build()
        binding.rvMyArticle.adapter = helper.adapter
        articleAdapter.setOnItemClickListener(mOnItemClickListener)
        articleAdapter.addOnItemChildClickListener(R.id.ivCollect, mOnItemChildClickListener)
    }

    private val mOnTrailingListener = object : TrailingLoadStateAdapter.OnTrailingListener {
        override fun onLoad() {
            loadMore()
        }

        override fun onFailRetry() {
            loadMore()
        }

        override fun isAllowLoading(): Boolean {
            return !binding.swipeRefresh.isRefreshing
        }
    }

    private val mOnItemClickListener =
        BaseQuickAdapter.OnItemClickListener<ArticleEntity.DatasBean> { adapter, _, position ->
            val datasBean = adapter.getItem(position)
            val intent = Intent(this, BrowserNormalActivity::class.java).apply {
                putExtra(Const.WEB_TITLE, datasBean.title)
                putExtra(Const.WEB_URL, datasBean.link)
            }
            startActivity(intent)
        }

    private val mOnItemChildClickListener =
        BaseQuickAdapter.OnItemChildClickListener<ArticleEntity.DatasBean> { adapter, view, position ->
            val datasBean = adapter.getItem(position)
            curPosition = position
            when (view.id) {
                R.id.ivCollect -> {
                    if (datasBean.collect) collectViewModel.unCollect(datasBean.id)
                    else collectViewModel.collect(datasBean.id)
                }
            }
        }

    override fun loadData() {
        articleViewModel.getMyArticle(true)
    }

    private fun loadMore() {
        articleViewModel.getMyArticle(false)
    }
}