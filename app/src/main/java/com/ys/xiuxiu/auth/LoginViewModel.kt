package com.ys.xiuxiu.auth

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel

/**
 * Created by yangsong on 2017/12/22.
 */
class LoginViewModel:ViewModel() {
    private var user:LiveData<User>?=null
    private var mRepository :LoginRepository?=null

    fun getUser(loginParam:LoginParam): LiveData<User>? {
         mRepository = LoginRepository()
        return mRepository!!.login(loginParam)
    }
}