package com.wan.android.ui.navigation

import androidx.lifecycle.viewModelScope
import com.wan.android.bean.NavigationEntity
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
 * Create at 2020/4/8.
 */
@HiltViewModel
class NavigationViewModel @Inject constructor(private val mRepository: NavigationRepository) :
    BaseViewModel() {

    private val _uiState = MutableSharedFlow<BaseUiState<MutableList<NavigationEntity>>>()
    val uiState: SharedFlow<BaseUiState<MutableList<NavigationEntity>>>
        get() = _uiState

    fun getNavigation() {
        viewModelScope.launch {
            val result = mRepository.getNavigation()
            if (result is Result.Success) _uiState.emit(BaseUiState.Success(result.data))
            else if (result is Result.Error) _uiState.emit(BaseUiState.Failure(result.exception))
        }
    }
}
