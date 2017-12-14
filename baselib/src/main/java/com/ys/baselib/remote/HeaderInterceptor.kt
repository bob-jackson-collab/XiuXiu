package com.ys.baselib.remote
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class HeaderInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val headers = request.headers()
        val builder = headers.newBuilder()
        // 添加key -value
        builder.add("","")
        return chain.proceed(request)
    }

    companion object {
        val TAG :String? = HeaderInterceptor::class.simpleName
    }
}