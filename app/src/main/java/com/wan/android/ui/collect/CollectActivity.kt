package com.wan.android.ui.collect

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.QuickAdapterHelper
import com.chad.library.adapter4.loadState.LoadState
import com.chad.library.adapter4.loadState.trailing.TrailingLoadStateAdapter
import com.wan.android.R
import com.wan.android.adapter.CollectAdapter
import com.wan.android.bean.ArticleEntity
import com.wan.android.databinding.ActivityCollectBinding
import com.wan.android.ui.ArticleViewModel
import com.wan.android.ui.browser.BrowserNormalActivity
import com.wan.baselib.ext.collectOnLifecycle
import com.wan.baselib.ext.showToast
import com.wan.baselib.mvvm.BaseVMActivity
import com.wan.baselib.router.Router
import com.wan.baselib.router.RouterPath
import com.wan.common.base.onFailure
import com.wan.common.base.onLoading
import com.wan.common.base.onSuccess
import com.wan.common.constant.Const
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author cy
 * Create at 2020/4/13.
 */
@AndroidEntryPoint
class CollectActivity : BaseVMActivity<ArticleViewModel, ActivityCollectBinding>() {
    private val articleViewModel by viewModels<ArticleViewModel>()
    private val collectViewModel by viewModels<CollectViewModel>()
    private val collectAdapter by lazy { CollectAdapter() }
    private lateinit var helper: QuickAdapterHelper
    private var curPosition = 0

    override fun getLayoutId(): Int = R.layout.activity_collect

    override fun initData(savedInstanceState: Bundle?) {

    }

    override fun initView() {
        title = getString(R.string.nav_my_collect)
        initRecyclerView()
        binding.swipeRefresh.setOnRefreshListener { loadData() }
    }

    private fun initRecyclerView() {
        binding.rvCollect.layoutManager = LinearLayoutManager(this)
        helper = QuickAdapterHelper.Builder(collectAdapter)
            .setTrailingLoadStateAdapter(mOnTrailingListener)
            .setTrailPreloadSize(0) // 预加载（默认值为0）
            .isTrailAutoLoadMore(true) // 是否自动加载更多（默认为true）
            .build()
        binding.rvCollect.adapter = helper.adapter
        collectAdapter.setOnItemClickListener(mOnItemClickListener)
        collectAdapter.addOnItemChildClickListener(R.id.ivCollect, mOnItemChildClickListener)
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
            val routerRequest = Router.build(RouterPath.PATH_BROWSER)
                .withString(Const.WEB_TITLE, datasBean.title)
                .withString(Const.WEB_URL, datasBean.link)
            Router.navigation(this, routerRequest)
        }

    private val mOnItemChildClickListener =
        BaseQuickAdapter.OnItemChildClickListener<ArticleEntity.DatasBean> { adapter, view, position ->
            val datasBean = adapter.getItem(position)
            curPosition = position
            when (view.id) {
                R.id.ivCollect -> {
                    collectViewModel.unMyCollect(datasBean.id, datasBean.originId)
                }
            }
        }

    override fun loadData() {
        articleViewModel.getCollectData(true)
    }

    private fun loadMore() {
        articleViewModel.getCollectData(false)
    }

    override fun subscribeUi() {
        articleViewModel.uiState.collectOnLifecycle(this) { uiState ->
            uiState.onLoading {
                binding.swipeRefresh.isRefreshing = true
                // 重置“加载更多”时状态
                helper.trailingLoadState = LoadState.None
            }.onSuccess { isRefresh, data, hasMore ->
                binding.swipeRefresh.isRefreshing = false
                if (isRefresh) {
                    collectAdapter.submitList(data.datas)
                } else {
                    data.datas?.let { collectAdapter.addAll(it) }
                }
                helper.trailingLoadState = LoadState.NotLoading(!hasMore)
            }.onFailure {
                binding.swipeRefresh.isRefreshing = false
                showToast(it.message)
                helper.trailingLoadState = LoadState.Error(it)
            }
        }
        collectViewModel.uiState.collectOnLifecycle(this) { uiState ->
            uiState.onLoading {
                showProgressDialog()
            }.onSuccess { collect ->
                if (collect.not()) {
                    collectAdapter.removeAt(curPosition)
                }
            }.onFailure {
                showToast(it.message)
            }
        }
    }
}