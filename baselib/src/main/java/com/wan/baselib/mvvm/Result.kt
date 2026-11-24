package com.wan.baselib.mvvm

import com.wan.baselib.network.exception.ApiException

/**
 * Created by luyao
 * on 2019/10/12 11:08
 */
sealed class Result<out T> {

    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: ApiException) : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}