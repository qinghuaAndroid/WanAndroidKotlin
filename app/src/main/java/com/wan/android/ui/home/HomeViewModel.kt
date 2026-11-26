package com.wan.android.ui.home

import androidx.lifecycle.viewModelScope
import com.wan.android.bean.ArticleEntity
import com.wan.android.bean.BannerEntity
import com.wan.baselib.mvvm.BaseViewModel
import com.wan.baselib.mvvm.Result
import com.wan.common.base.BaseUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author cy
 * Create at 2020/4/16.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(private val mRepository: HomeRepository) : BaseViewModel() {
    private val _topArticleUiState = MutableStateFlow<BaseUiState<List<ArticleEntity.DatasBean>>>(
        BaseUiState.Success(emptyList())
    )
    val topArticleUiState: SharedFlow<BaseUiState<List<ArticleEntity.DatasBean>>>
        get() = _topArticleUiState
    private val _bannerUiState = MutableStateFlow<BaseUiState<List<BannerEntity>>>(
        BaseUiState.Success(emptyList())
    )
    val bannerUiState: SharedFlow<BaseUiState<List<BannerEntity>>>
        get() = _bannerUiState

    fun getTopArticles() {
        viewModelScope.launch {
            val result = mRepository.loadTopArticles()
            if (result is Result.Success) _topArticleUiState.emit(BaseUiState.Success(result.data))
            else if (result is Result.Error) _topArticleUiState.emit(BaseUiState.Failure(result.exception))
        }
    }

    fun getBanner() {
        viewModelScope.launch {
            val result = mRepository.loadBanner()
            if (result is Result.Success) _bannerUiState.emit(BaseUiState.Success(result.data))
            else if (result is Result.Error) _bannerUiState.emit(BaseUiState.Failure(result.exception))
        }
    }
}