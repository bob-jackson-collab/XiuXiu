package com.ys.xiuxiu.auth

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.ys.baselib.*;
import com.ys.baselib.remote.data.BaseResult
import com.ys.baselib.remote.data.ResultFunction
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


/**
 * Created by yangsong on 2017/12/22.
 */

class LoginRepository {

    private var loginService: LoginService? = null

    fun login(param: LoginParam): LiveData<User>? {
        var users: MutableLiveData<User>? = MutableLiveData()
        loginService = ServiceGenerator.createService(LoginService::class.java, "")

        loginService!!.getLoginUser(param).flatMap(object : io.reactivex.functions.Function<BaseResult<User>, ObservableSource<out User>> {
            override fun apply(t: BaseResult<User>): ObservableSource<User> {
                return Observable.just(t!!.data)
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { user -> users!!.value = user }
//
        return users
    }
}

