package  com.wan.android.ui.search

import com.tencent.mmkv.MMKV
import com.wan.android.bean.HotSearchEntity
import com.wan.android.http.ApiService
import com.wan.baselib.mvvm.BaseRepository
import com.wan.baselib.mvvm.Result
import javax.inject.Inject

class SearchRepository @Inject constructor(private val apiService: ApiService) : BaseRepository() {

    companion object {
        private const val SEARCH_HISTORY_KEY = "search_history"
    }

    private val mmkv by lazy { MMKV.defaultMMKV() }

    fun queryAll(): Set<String> {
        return mmkv.decodeStringSet(SEARCH_HISTORY_KEY, mutableSetOf()) ?: mutableSetOf()
    }

    fun saveSearchKey(key: String) {
        val stringSet = mmkv.decodeStringSet(SEARCH_HISTORY_KEY, mutableSetOf())
        if (stringSet == null || !stringSet.contains(key)) {
            val newSet = stringSet ?: mutableSetOf()
            newSet.add(key)
            mmkv.encode(SEARCH_HISTORY_KEY, newSet)
        }
    }

    fun deleteByKey(key: String) {
        mmkv.decodeStringSet(SEARCH_HISTORY_KEY, mutableSetOf())?.let { stringSet ->
            if (stringSet.contains(key)) {
                stringSet.remove(key)
                mmkv.encode(SEARCH_HISTORY_KEY, stringSet)
            }
        }
    }

    fun clearAll() {
        mmkv.remove(SEARCH_HISTORY_KEY)
    }

    suspend fun getHotSearchData(): Result<MutableList<HotSearchEntity>> {
        return safeApiCall { requestHotSearchData() }
    }

    private suspend fun requestHotSearchData(): Result<MutableList<HotSearchEntity>> {
        return executeResponse(apiService.getHotSearchData())
    }
}