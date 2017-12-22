package com.ys.baselib.remote.data;

/**
 * Created by yangsong on 2017/12/22.
 */

open class BaseResult<T> {

    var data: T? = null

    var errorCode: Int = 0

    var message: String = ""


}
