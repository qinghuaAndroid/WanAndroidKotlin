package com.wan.android.ui.integral

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.animation.DecelerateInterpolator
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter4.QuickAdapterHelper
import com.chad.library.adapter4.loadState.LoadState
import com.chad.library.adapter4.loadState.trailing.TrailingLoadStateAdapter
import com.wan.android.R
import com.wan.android.adapter.IntegralAdapter
import com.wan.android.adapter.IntegralHeaderAdapter
import com.wan.android.bean.CoinInfo
import com.wan.android.databinding.ActivityIntegralBinding
import com.wan.android.ui.main.MainViewModel
import com.wan.baselib.ext.collectOnLifecycle
import com.wan.baselib.ext.showToast
import com.wan.baselib.mvvm.BaseVMActivity
import com.wan.common.base.ListUiState
import com.wan.common.base.onSuccess
import dagger.hilt.android.AndroidEntryPoint


/**
 * @author cy
 * Create at 2020/4/10.
 */
@AndroidEntryPoint
class IntegralActivity : BaseVMActivity<IntegralViewModel, ActivityIntegralBinding>() {

    private val integralViewModel by viewModels<IntegralViewModel>()
    private val mainViewModel by viewModels<MainViewModel>()
    private val integralAdapter by lazy { IntegralAdapter() }
    private lateinit var helper: QuickAdapterHelper

    override fun subscribeUi() {
        mainViewModel.uiState.collectOnLifecycle(this) { uiState ->
            uiState.onSuccess { userInfoEntity ->
                userInfoEntity.coinInfo?.let { coinInfo ->
                    startAnim(coinInfo)
                }
            }
        }

        integralViewModel.uiState.collectOnLifecycle(this) { uiState ->
            when (uiState) {
                is ListUiState.Loading -> {
                    binding.swipeRefresh.isRefreshing = true
                    // 重置“加载更多”时状态
                    helper.trailingLoadState = LoadState.None
                }

                is ListUiState.Success -> {
                    binding.swipeRefresh.isRefreshing = false
                    if (uiState.isRefresh) {
                        integralAdapter.submitList(uiState.data.datas)
                    } else {
                        uiState.data.datas?.let {
                            integralAdapter.addAll(it)
                        }
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
    }

    override fun getLayoutId(): Int = R.layout.activity_integral

    override fun initData(savedInstanceState: Bundle?) {}

    override fun initView() {
        title = getString(R.string.my_integral)
        initRecyclerView()
        binding.swipeRefresh.setOnRefreshListener { loadData() }
        binding.appBar.addOnOffsetChangedListener { _, verticalOffset ->
            binding.swipeRefresh.isEnabled = verticalOffset >= 0
        }
    }

    /**
     * 开启积分动画
     */
    private fun startAnim(coinInfo: CoinInfo) {
        val animator = ValueAnimator.ofInt(0, coinInfo.coinCount)
        //播放时长
        animator.duration = 1500
        animator.interpolator = DecelerateInterpolator()
        animator.addUpdateListener { animation ->
            //获取改变后的值
            val currentValue = animation.animatedValue as Int
            binding.tvIntegralAnim.text = "$currentValue"
        }
        animator.start()
    }

    private fun initRecyclerView() {
        binding.rvIntegralList.layoutManager = LinearLayoutManager(this)
        helper = QuickAdapterHelper.Builder(integralAdapter)
            .setTrailingLoadStateAdapter(mOnTrailingListener)
            .setTrailPreloadSize(0) // 预加载（默认值为0）
            .isTrailAutoLoadMore(true) // 是否自动加载更多（默认为true）
            .build()
        binding.rvIntegralList.adapter = helper.adapter
        val headerAdapter = IntegralHeaderAdapter()
        helper.addBeforeAdapter(0, headerAdapter)
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

    override fun loadData() {
        mainViewModel.getUserInfo()
        integralViewModel.getIntegralRecord(true)
    }

    private fun loadMore() {
        integralViewModel.getIntegralRecord(false)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.tvIntegralAnim.clearAnimation()
    }
}