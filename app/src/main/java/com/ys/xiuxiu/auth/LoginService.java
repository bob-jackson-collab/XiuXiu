package com.ys.xiuxiu.auth;


import com.ys.baselib.remote.data.BaseResult;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by yangsong on 2017/12/22.
 */

interface LoginService {

    @POST("user/login")
    Observable<BaseResult<User>> getLoginUser(@Body LoginParam param);

    @POST("user/register")
    Observable<User> registerUser();
}
