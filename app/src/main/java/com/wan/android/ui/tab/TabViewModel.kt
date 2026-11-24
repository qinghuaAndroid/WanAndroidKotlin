package com.wan.android.ui.tab

import androidx.lifecycle.viewModelScope
import com.wan.android.bean.TabEntity
import com.wan.baselib.mvvm.BaseViewModel
import com.wan.baselib.mvvm.Result
import com.wan.common.base.BaseUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author cy
 * Create at 2020/4/3.
 */
@HiltViewModel
class TabViewModel @Inject constructor(private val mRepository: TabRepository) : BaseViewModel() {

    private val _uiState = MutableSharedFlow<BaseUiState<MutableList<TabEntity>>>()
    val uiState: SharedFlow<BaseUiState<MutableList<TabEntity>>>
        get() = _uiState

    fun getTabList(type: Int) {
        viewModelScope.launch(Dispatchers.Main) {
            val result = withContext(Dispatchers.IO) { mRepository.getTabList(type) }
            if (result is Result.Success) _uiState.emit(BaseUiState.Success(result.data))
            else if (result is Result.Error) _uiState.emit(BaseUiState.Failure(result.exception))
        }
    }
}