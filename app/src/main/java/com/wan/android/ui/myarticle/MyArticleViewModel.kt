package com.wan.android.ui.myarticle

import androidx.lifecycle.viewModelScope
import com.wan.android.bean.MyArticleEntity
import com.wan.baselib.mvvm.BaseViewModel
import com.wan.baselib.mvvm.Result
import com.wan.common.base.ListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author cy
 * Create at 2020/4/15.
 */
@HiltViewModel
class MyArticleViewModel @Inject constructor(private val mRepository: MyArticleRepository) :
    BaseViewModel() {

    private val _uiState = MutableSharedFlow<ListUiState<MyArticleEntity>>()
    val uiState: SharedFlow<ListUiState<MyArticleEntity>>
        get() = _uiState

    private var pageNum = 0

    fun getMyArticle(isRefresh: Boolean) {
        viewModelScope.launch {
            if (isRefresh) {
                pageNum = 0
                _uiState.emit(ListUiState.Loading)
            }
            val result = mRepository.getMyArticle(pageNum)
            if (result is Result.Success) {
                val myArticleEntity = result.data
                val shareArticles = myArticleEntity.shareArticles
                shareArticles?.let {
                    if (it.curPage >= it.pageCount) {
                        _uiState.emit(ListUiState.Success(isRefresh, myArticleEntity, false))
                        return@launch
                    } else {
                        _uiState.emit(ListUiState.Success(isRefresh, myArticleEntity, true))
                    }
                }
                pageNum++
            } else if (result is Result.Error) {
                _uiState.emit(ListUiState.Failure(result.exception))
            }
        }

    }
}