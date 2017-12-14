package com.hair.hairstyle.net

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.ys.baselib.net.NetChangeObserver

import java.util.ArrayList

/* 使用广播去监听网络
        * Created by YS on 16/9/13
        */

class NetStateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        mBroadcastReceiver = this@NetStateReceiver
        if (intent.action!!.equals(ANDROID_NET_CHANGE_ACTION, ignoreCase = true) || intent.action!!.equals(CUSTOM_ANDROID_NET_CHANGE_ACTION, ignoreCase = true)) {
            if (!NetUtils.isNetworkAvailable(context)) {  // 如果无网络
                Log.e(TAG, "<--- network disconnected --->")
                isNetworkAvailable = false
            } else {
                Log.e(TAG, "<--- network connected --->")
                isNetworkAvailable = true
                //获取网络类型
                apnType = NetUtils.getAPNType(context)
            }
            //通知观察者
            notifyObserver()
        }

    }

    // 观察者做相应的action
    private fun notifyObserver() {
        if (!mNetChangeObservers!!.isEmpty()) {
            val size = mNetChangeObservers!!.size
            for (i in 0 until size) {
                val observer = mNetChangeObservers!![i]
                    if (isNetworkAvailable) {
                        observer.onNetConnected(apnType)
                    } else {
                        observer.onNetDisConnect()
                    }
            }
        }
    }

    companion object {
        val CUSTOM_ANDROID_NET_CHANGE_ACTION = "com.hairstyle.api.netstatus.CONNECTIVITY_CHANGE"
        private val ANDROID_NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE"
        private val TAG = NetStateReceiver::class.java.simpleName
        private var isNetworkAvailable = false
        private var apnType: NetUtils.NetType? = null
        private var mNetChangeObservers: ArrayList<NetChangeObserver>? = ArrayList<NetChangeObserver>() //网络观察者
        private var mBroadcastReceiver: BroadcastReceiver? = null


        //单利创建实例
        private val receiver: BroadcastReceiver
            get() {
                if (null == mBroadcastReceiver) {
                    synchronized(NetStateReceiver::class.java) {
                        if (null == mBroadcastReceiver) {
                            mBroadcastReceiver = NetStateReceiver()
                        }
                    }
                }
                return this.mBroadcastReceiver!!
            }


        /**
         * 注册
         *
         * @param mContext
         */

        fun registerNetworkStateReceiver(mContext: Context) {
            val filter = IntentFilter()
            filter.addAction(CUSTOM_ANDROID_NET_CHANGE_ACTION)
            filter.addAction(ANDROID_NET_CHANGE_ACTION)
            mContext.registerReceiver(receiver, filter)
        }


        /**
         * 清除
         *
         * @param mContext
         */

        fun checkNetworkState(mContext: Context) {
            val intent = Intent()
            intent.action = CUSTOM_ANDROID_NET_CHANGE_ACTION
            mContext.sendBroadcast(intent)
        }


        /**
         * 反注册
         *
         * @param mContext
         */

        fun unRegisterNetworkStateReceiver(mContext: Context) {
            if (mBroadcastReceiver != null) {
                try {
                    mContext.unregisterReceiver(mBroadcastReceiver)
                } catch (e: Exception) {
                }
            }
        }


        /**
         * 添加网络监听
         *
         * @param observer
         */

        fun registerObserver(observer: NetChangeObserver) {
            if (mNetChangeObservers == null) {
                mNetChangeObservers = ArrayList<NetChangeObserver>()
            }
            Log.e("--->>", "添加网络")
            mNetChangeObservers!!.add(observer)
        }


        /**
         * 移除网络监听
         *
         * @param observer
         */

        fun removeRegisterObserver(observer: NetChangeObserver) {
            if (mNetChangeObservers != null) {
                if (mNetChangeObservers!!.contains(observer)) {
                    Log.e("--->", "移除网络")
                    mNetChangeObservers!!.remove(observer)
                }
            }
        }
    }
}
