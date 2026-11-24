package  com.wan.android.ui.search

import androidx.lifecycle.viewModelScope
import com.wan.android.bean.HotSearchEntity
import com.wan.baselib.mvvm.BaseViewModel
import com.wan.baselib.mvvm.Result
import com.wan.common.base.BaseUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val mRepository: SearchRepository) :
    BaseViewModel() {

    private val _uiState = MutableSharedFlow<BaseUiState<MutableList<HotSearchEntity>>>()
    val uiState: SharedFlow<BaseUiState<MutableList<HotSearchEntity>>> get() = _uiState

    private val _historyList = MutableStateFlow<List<String>>(emptyList())
    val historyList: StateFlow<List<String>> get() = _historyList

    fun queryAll() {
        _historyList.value = mRepository.queryAll().toList()
    }

    fun saveSearchKey(key: String) {
        mRepository.saveSearchKey(key)
        queryAll()
    }

    fun deleteByKey(key: String) {
        mRepository.deleteByKey(key)
        queryAll()
    }

    fun clearAll() {
        mRepository.clearAll()
        queryAll()
    }

    fun getHotSearchData() {
        viewModelScope.launch {
            val result = mRepository.getHotSearchData()
            if (result is Result.Success) _uiState.emit(BaseUiState.Success(result.data))
            else if (result is Result.Error) _uiState.emit(BaseUiState.Failure(result.exception))
        }
    }
}