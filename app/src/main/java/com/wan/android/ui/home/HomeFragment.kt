package com.wan.android.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.QuickAdapterHelper
import com.chad.library.adapter4.loadState.LoadState
import com.chad.library.adapter4.loadState.trailing.TrailingLoadStateAdapter
import com.wan.android.R
import com.wan.android.adapter.ArticleAdapter
import com.wan.android.adapter.ImageNetAdapter
import com.wan.android.bean.ArticleEntity
import com.wan.android.databinding.FragmentHomeBinding
import com.wan.android.ui.ArticleViewModel
import com.wan.android.ui.collect.CollectViewModel
import com.wan.baselib.ext.collectOnLifecycle
import com.wan.baselib.ext.showToast
import com.wan.baselib.mvvm.BaseVMFragment
import com.wan.baselib.router.Router
import com.wan.baselib.router.RouterPath
import com.wan.common.base.BaseUiState
import com.wan.common.base.ListUiState
import com.wan.common.constant.Const
import dagger.hilt.android.AndroidEntryPoint


/**
 * @author cy
 * Create at 2020/4/2.
 */
@AndroidEntryPoint
class HomeFragment : BaseVMFragment<ArticleViewModel, FragmentHomeBinding>() {

    private val homeViewModel by viewModels<HomeViewModel>()
    private val articleViewModel by viewModels<ArticleViewModel>()
    private val collectViewModel by viewModels<CollectViewModel>()
    private val articleAdapter by lazy { ArticleAdapter() }
    private lateinit var helper: QuickAdapterHelper
    private var curPosition = 0

    override fun attachLayoutRes(): Int = R.layout.fragment_home

    override fun initData(savedInstanceState: Bundle?) {

    }

    override fun initView(view: View) {
        initRecyclerView()
        binding.swipeRefresh.setOnRefreshListener { loadData() }
        binding.appBarLayout.addOnOffsetChangedListener { _, verticalOffset ->
            binding.swipeRefresh.isEnabled = verticalOffset >= 0
        }
    }

    private fun initRecyclerView() {
        binding.rvHomeList.layoutManager = LinearLayoutManager(context)
        helper = QuickAdapterHelper.Builder(articleAdapter)
            .setTrailingLoadStateAdapter(mOnTrailingListener)
            .setTrailPreloadSize(0) // 预加载（默认值为0）
            .isTrailAutoLoadMore(true) // 是否自动加载更多（默认为true）
            .build()
        binding.rvHomeList.adapter = helper.adapter
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
            val routerRequest = Router.build(RouterPath.PATH_BROWSER)
                .withString(Const.WEB_TITLE, datasBean.title)
                .withString(Const.WEB_URL, datasBean.link)
            Router.navigation(requireContext(), routerRequest)
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
        homeViewModel.getBanner()
        homeViewModel.getTopArticles()
        articleViewModel.getArticles(true)
    }

    private fun loadMore() {
        articleViewModel.getArticles(false)
    }

    override fun onStart() {
        super.onStart()
        binding.banner.start()
    }

    override fun onStop() {
        super.onStop()
        binding.banner.stop()
    }

    override fun subscribeUi() {
        homeViewModel.bannerUiState.collectOnLifecycle(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is BaseUiState.Loading -> {}
                is BaseUiState.Success -> binding.banner.setAdapter(ImageNetAdapter(uiState.data))
                is BaseUiState.Failure -> uiState.exception.message?.let { showToast(it) }
            }
        }
        homeViewModel.topArticleUiState.collectOnLifecycle(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is BaseUiState.Loading -> {}
                is BaseUiState.Success -> articleAdapter.submitList(uiState.data)
                is BaseUiState.Failure -> uiState.exception.message?.let { showToast(it) }
            }
        }
        articleViewModel.uiState.collectOnLifecycle(viewLifecycleOwner) { uiState ->
            when (uiState) {
                is ListUiState.Loading -> {
                    binding.swipeRefresh.isRefreshing = true
                    // 重置“加载更多”时状态
                    helper.trailingLoadState = LoadState.None
                }

                is ListUiState.Success -> {
                    binding.swipeRefresh.isRefreshing = false
                    uiState.data.datas?.let { articleAdapter.addAll(it) }
                    helper.trailingLoadState = LoadState.NotLoading(!uiState.hasMore)
                }

                is ListUiState.Failure -> {
                    binding.swipeRefresh.isRefreshing = false
                    uiState.exception.message?.let { showToast(it) }
                    helper.trailingLoadState = LoadState.Error(uiState.exception)
                }
            }
        }
        collectViewModel.uiState.collectOnLifecycle(viewLifecycleOwner) { uiState ->
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
}