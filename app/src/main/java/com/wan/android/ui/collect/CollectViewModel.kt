package com.wan.android.ui.collect

import androidx.lifecycle.viewModelScope
import com.wan.baselib.mvvm.BaseViewModel
import com.wan.baselib.mvvm.Result
import com.wan.common.base.BaseUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author cy
 * Create at 2020/4/16.
 */
@HiltViewModel
class CollectViewModel @Inject constructor(private val mRepository: CollectRepository) :
    BaseViewModel() {

    private val _uiState = MutableSharedFlow<BaseUiState<Boolean>>()
    val uiState: SharedFlow<BaseUiState<Boolean>>
        get() = _uiState

    fun collect(id: Int) {
        viewModelScope.launch {
            val result = mRepository.collect(id)
            if (result is Result.Success) _uiState.emit(BaseUiState.Success(true))
            else if (result is Result.Error) _uiState.emit(BaseUiState.Failure(result.exception))
        }
    }

    fun unCollect(id: Int) {
        viewModelScope.launch {
            val result = mRepository.unCollect(id)
            if (result is Result.Success) _uiState.emit(BaseUiState.Success(false))
            else if (result is Result.Error) _uiState.emit(BaseUiState.Failure(result.exception))
        }
    }

    fun unMyCollect(id: Int, originId: Int) {
        viewModelScope.launch {
            val result = mRepository.unMyCollect(id, originId)
            if (result is Result.Success) _uiState.emit(BaseUiState.Success(false))
            else if (result is Result.Error) _uiState.emit(BaseUiState.Failure(result.exception))
        }
    }
}