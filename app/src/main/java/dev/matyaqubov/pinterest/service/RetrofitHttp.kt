package dev.matyaqubov.pinterest.service

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

object RetrofitHttp {

    val IS_TESTER=true
    var SERVER_DEVELOPMENT = "https://api.unsplash.com/"
    var SERVER_PRODUCTION = "https://api.unsplash.com/"

    private val client= getClient()
    private val retrofit = getRetrofit(client)

    fun getRetrofit(client: OkHttpClient): Retrofit {
        val build = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(server())
            .client(client)
            .build()
        return build
    }
    fun <T> createService(service:Class<T>):T{
        return retrofit.create(service)
    }

    fun getClient(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .addInterceptor(Interceptor { chain ->
            val builder = chain.request().newBuilder()
//            builder.header("Authorization", "Client-ID r_PBgcCBLgDICSpQXjRHOPe7e0N0kK4frHbnE60dryE")
            builder.header("Authorization", "Client-ID NyAiU_SXanRoelK1qE97R_gUpz-9hkCvQQZJvjBjxhc")
            chain.proceed(builder.build())
        })
        .build()


    fun <T> createServiceWithAuth(service: Class<T>?): T {

        val newClient =
            client.newBuilder().addInterceptor(object : Interceptor {
                @Throws(IOException::class)
                override fun intercept(chain: Interceptor.Chain): Response {
                    var request = chain.request()
                    val builder = request.newBuilder()
                    builder.addHeader("Authorization", "Client-ID NyAiU_SXanRoelK1qE97R_gUpz-9hkCvQQZJvjBjxhc")
                    request = builder.build()
                    return chain.proceed(request)
                }
            }).build()
        val newRetrofit = retrofit.newBuilder().client(newClient).build()
        return newRetrofit.create(service)
    }

    private fun server(): String {
        if (IS_TESTER)
            return SERVER_DEVELOPMENT
        return SERVER_PRODUCTION
    }



    val apiService: ApiService = retrofit.create(ApiService::class.java)
    //...
}