import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import com.ys.baselib.remote.HeaderInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.ArrayList
import javax.net.ssl.*

class ServiceGenerator{
     fun <S> createService(serviceClass: Class<S>, baseUrl: String): S {
        val gson = GsonBuilder()
                .registerTypeAdapterFactory(NotNullListTypeAdapterFactory())
                .create()

        val retrofit = Retrofit.Builder()
                .client(getOKHttpClient())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build()

        return retrofit.create(serviceClass)
    }
    private fun getOKHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(LoggingInterceptor())
        builder.addInterceptor(HeaderInterceptor())
        try {
            val sslContext = SSLContext.getInstance("TLS")
            val trustManager = object : X509TrustManager {
                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate?> {
                    return arrayOfNulls(0)
                }
            }
            sslContext.init(null, arrayOf<TrustManager>(trustManager), SecureRandom())
            builder.sslSocketFactory(sslContext.socketFactory)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        }
        builder.hostnameVerifier { _, _ -> true }
        return builder.build()
    }

    private class NotNullListTypeAdapterFactory : TypeAdapterFactory {
        override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T> {
            val adapter = gson.getDelegateAdapter(this, type)
            val rawType = type.rawType
            return object : TypeAdapter<T>() {
                @Throws(IOException::class)
                override fun write(out: JsonWriter, value: T) {
                    adapter.write(out, value)
                }

                @Throws(IOException::class)
                override fun read(`in`: JsonReader): T {
                    if (rawType == object : TypeToken<List<*>>() {}.rawType) {
                        if (`in`.peek() == JsonToken.NULL) {
                            `in`.nextNull()
                            return ArrayList<Any>() as T
                        }
                    }
                    return adapter.read(`in`)
                }
            }
        }
    }
}