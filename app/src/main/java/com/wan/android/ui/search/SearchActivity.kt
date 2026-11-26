package com.wan.android.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import com.wan.android.R
import com.wan.android.bean.HotSearchEntity
import com.wan.android.constant.Const
import com.wan.android.databinding.ActivitySearchBinding
import com.wan.android.ui.search.list.SearchListActivity
import com.wan.baselib.ext.collectOnLifecycle
import com.wan.baselib.ext.listener.queryTextListener
import com.wan.baselib.ext.showToast
import com.wan.baselib.mvvm.BaseVMActivity
import com.wan.baselib.router.Router
import com.wan.baselib.router.RouterPath
import com.wan.common.base.onFailure
import com.wan.common.base.onSuccess
import dagger.hilt.android.AndroidEntryPoint
import splitties.views.onClick

/**
 * @author cy
 * Create at 2020/4/14.
 */
@AndroidEntryPoint
class SearchActivity : BaseVMActivity<SearchViewModel, ActivitySearchBinding>() {

    private val searchViewModel by viewModels<SearchViewModel>()

    override fun getLayoutId(): Int = R.layout.activity_search

    override fun initData(savedInstanceState: Bundle?) {

    }

    override fun initView() {
        setTitle(R.string.search)
        initListener()
    }

    private fun initListener() {
        binding.ivClearAll.onClick { searchViewModel.clearAll() }
    }

    private fun goToSearchList(key: String) {
        searchViewModel.saveSearchKey(key)
        Router.build(RouterPath.PATH_SEARCH_LIST)
            .withString(Const.SEARCH_KEY, key)
            .let { Router.navigation(this, it) }
    }

    override fun loadData() {
        searchViewModel.getHotSearchData()
        searchViewModel.queryAll()
    }

    override fun subscribeUi() {
        searchViewModel.uiState.collectOnLifecycle(this) { uiState ->
            uiState.onSuccess { showHotSearchData(it) }
                .onFailure { showToast(it.message) }
        }
        searchViewModel.historyList.collectOnLifecycle(this) { list ->
            showHistoryData(list)
        }
    }

    private fun showHotSearchData(hotSearchDatas: MutableList<HotSearchEntity>) {
        binding.hotLabelsView.run {
            setLabels(hotSearchDatas) { _, _, data ->
                data.name
            }
            setOnLabelClickListener { _, _, position ->
                onLabelClick(hotSearchDatas[position].name)
            }
        }
    }

    private fun showHistoryData(historyBeans: List<String>) {
        binding.historyLabelsView.run {
            setLabels(historyBeans) { _, _, data ->
                data
            }
            setOnLabelClickListener { _, _, position ->
                onLabelClick(historyBeans[position])
            }
            setOnLabelLongClickListener { _, _, position ->
                onLabelLongClick(historyBeans[position])
                true
            }
        }
    }

    private fun onLabelLongClick(key: String) {
        searchViewModel.deleteByKey(key)
    }

    private fun onLabelClick(queryTxt: String) {
        goToSearchList(queryTxt)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchView = menu.findItem(R.id.action_search)?.actionView as SearchView
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.onActionViewExpanded()
        searchView.queryHint = getString(R.string.search_tint)
        searchView.queryTextListener {
            onQueryTextSubmit { query ->
                query?.let { goToSearchList(it) }
            }
        }
        searchView.isSubmitButtonEnabled = true
        initStyle(searchView)
        return super.onCreateOptionsMenu(menu)
    }

    private fun initStyle(searchView: SearchView) {
        val goButton = searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_go_btn)
        goButton.setImageResource(R.drawable.ic_search_white_24dp)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}