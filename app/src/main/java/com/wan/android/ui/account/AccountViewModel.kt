package com.wan.android.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.tencent.mmkv.MMKV
import com.wan.baselib.mvvm.BaseViewModel
import com.wan.baselib.mvvm.Result
import com.wan.common.base.BaseUiState
import com.wan.common.constant.Const
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val mRepository: AccountRepository
) : BaseViewModel() {

    private val mmkv by lazy(LazyThreadSafetyMode.NONE) { MMKV.defaultMMKV() }

    private val _isLogged = MediatorLiveData<Boolean>()
    val isLogged: LiveData<Boolean> get() = _isLogged

    val isLogin: Boolean
        get() = mmkv.decodeBool(Const.IS_LOGIN, false)

    private val _uiState = MutableLiveData<BaseUiState<Boolean>>()
    val uiState: LiveData<BaseUiState<Boolean>>
        get() = _uiState

    init {
        _isLogged.addSource(mRepository.getUser().asLiveData()) { user ->
            _isLogged.postValue(user != null)
        }
    }

    fun logout() {
        viewModelScope.launch(Dispatchers.Main) {
            val result = mRepository.logout()
            if (result is Result.Success) {
                _uiState.value = BaseUiState.Success(data = true)
            } else if (result is Result.Error) {
                _uiState.value = BaseUiState.Failure(exception = result.exception)
            }
        }
    }
}