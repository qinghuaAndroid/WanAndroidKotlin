package com.wan.common.base

sealed class BaseUiState<out T> {

    data object Loading : BaseUiState<Nothing>()

    data class Success<T>(
        val data: T
    ) : BaseUiState<T>()

    data class Failure(
        val exception: Throwable
    ) : BaseUiState<Nothing>()
}

inline fun <T> BaseUiState<T>.onLoading(
    action: () -> Unit
): BaseUiState<T> {
    if (this is BaseUiState.Loading) action()
    return this
}

inline fun <T> BaseUiState<T>.onSuccess(
    action: (T) -> Unit
): BaseUiState<T> {
    if (this is BaseUiState.Success) action(data)
    return this
}

inline fun <T> BaseUiState<T>.onFailure(
    action: (Throwable) -> Unit
): BaseUiState<T> {
    if (this is BaseUiState.Failure) action(exception)
    return this
}

sealed class ListUiState<out T> {

    data object Loading : ListUiState<Nothing>()

    data class Success<T>(
        val isRefresh: Boolean = false,
        val data: T,
        val hasMore: Boolean = true
    ) : ListUiState<T>()

    data class Failure(
        val exception: Throwable
    ) : ListUiState<Nothing>()
}

inline fun <T> ListUiState<T>.onLoading(
    action: () -> Unit
): ListUiState<T> {
    if (this is ListUiState.Loading) action()
    return this
}

inline fun <T> ListUiState<T>.onSuccess(
    action: (isRefresh: Boolean, data: T, hasMore: Boolean) -> Unit
): ListUiState<T> {
    if (this is ListUiState.Success) {
        action(isRefresh, data, hasMore)
    }
    return this
}

inline fun <T> ListUiState<T>.onFailure(
    action: (Throwable) -> Unit
): ListUiState<T> {
    if (this is ListUiState.Failure) action(exception)
    return this
}