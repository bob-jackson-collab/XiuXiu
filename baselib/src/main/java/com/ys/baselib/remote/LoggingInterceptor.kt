import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import java.io.IOException
import java.nio.charset.Charset

class LoggingInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val request = chain.request()
        //打印Request数据
        logRequest(request)
        val response = chain.proceed(request)
        logResponse(response)
        return response
    }

    @Throws(IOException::class)
    private fun logRequest(request: Request) {
        Log.i(TAG, request.method() + "   " + request.url())
        val headers = request.headers()
        var i = 0
        val count = headers.size()
        while (i < count) {
            val name = headers.name(i)
            // Skip headers from the request body as they are explicitly logged above.
            if (!"Content-Type".equals(name, ignoreCase = true) && !"Content-Length".equals(name, ignoreCase = true)) {
                Log.i(TAG, name + ": " + headers.value(i))
            }
            i++
        }

        if (request.body() != null) {
            val buffer = Buffer()
            request.body()!!.writeTo(buffer)
            var charset: Charset? = UTF8
            val contentType = request.body()!!.contentType()
            if (contentType != null) {
                charset = contentType.charset(UTF8)
            }
            Log.i(TAG, buffer.readString(charset!!))
            Log.i(TAG, "--> END " + request.method() + " (" + request.body()!!.contentLength() + "-byte body)")
        }
    }

    @Throws(IOException::class)
    private fun logResponse(response: Response?) {
        if (response != null) {
            Log.i(TAG, response.code().toString() + "  " + response.message())
            val headers = response.headers()
            var i = 0
            val count = headers.size()
            while (i < count) {
                Log.i(TAG, headers.name(i) + ": " + headers.value(i))
                i++
            }
            val contentLength = response.body()!!.contentLength()
            val source = response.body()!!.source()
            source.request(java.lang.Long.MAX_VALUE) // Buffer the entire body.
            val buffer = source.buffer()

            var charset: Charset? = UTF8
            val contentType = response.body()!!.contentType()
            if (contentType != null) {
                charset = contentType.charset(UTF8)
            }
            if (contentLength != 0L) {
                Log.i(TAG, buffer.clone().readString(charset!!))
            }

        } else {
            Log.i(TAG, "Response is null")
        }
    }

    // 声明的静态对象可以放在这里
    companion object {

        val TAG = LoggingInterceptor::class.java.simpleName
        private val UTF8 = Charset.forName("UTF-8")
    }

}