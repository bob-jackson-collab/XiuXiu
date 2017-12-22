package com.ys.baselib.remote.data

import io.reactivex.functions.Function

/**
 * Created by yangsong on 2017/12/22.
 */

class ResultFunction<T> : Function<BaseResult<T>, T> {

    @Throws(Exception::class)
    override fun apply(tBaseResult: BaseResult<T>): T? {
        val errorCode = tBaseResult.errorCode
        if (errorCode == 0) {
            return tBaseResult.data
        }
        throw ErrorException(tBaseResult.message)
    }
}
