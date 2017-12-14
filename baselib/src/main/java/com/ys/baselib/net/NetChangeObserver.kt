package com.ys.baselib.net;

import com.hair.hairstyle.net.NetUtils

/**
 * 网络改变观察者，观察网络改变后回调的方法
 */

 interface NetChangeObserver {

    /**
     * 网络连接回调 type为网络类型
     */
    fun onNetConnected(type:NetUtils.NetType?)
    /**
     * 没有网络
     */
    fun onNetDisConnect()
}
