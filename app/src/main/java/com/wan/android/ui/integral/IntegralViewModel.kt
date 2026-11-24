package com.wan.android.ui.integral

import androidx.lifecycle.viewModelScope
import com.wan.android.bean.IntegralRecordEntity
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
 * Create at 2020/4/10.
 */
@HiltViewModel
class IntegralViewModel @Inject constructor(private val mRepository: IntegralRepository) :
    BaseViewModel() {

    private val _uiState = MutableSharedFlow<ListUiState<IntegralRecordEntity>>()
    val uiState: SharedFlow<ListUiState<IntegralRecordEntity>>
        get() = _uiState

    private var pageNum = 1

    fun getIntegralRecord(isRefresh: Boolean) {
        viewModelScope.launch {
            if (isRefresh) {
                pageNum = 1
                _uiState.emit(ListUiState.Loading)
            }
            val result = mRepository.getIntegralRecord(pageNum)
            if (result is Result.Success) {
                val data = result.data
                if (data.curPage >= data.pageCount) {
                    _uiState.emit(ListUiState.Success(isRefresh, data, false))
                    return@launch
                } else {
                    _uiState.emit(ListUiState.Success(isRefresh, data, true))
                }
                pageNum++
            } else if (result is Result.Error) {
                _uiState.emit(ListUiState.Failure(result.exception))
            }
        }
    }
}