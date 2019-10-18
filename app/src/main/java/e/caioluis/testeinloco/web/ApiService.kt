package e.caioluis.testeinloco.web

import e.caioluis.testeinloco.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiService {

    private fun initRetrofit(): Retrofit {

        val client = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create()
            ).client(client)
            .build()
    }

    val service: WebAPI = initRetrofit().create(WebAPI::class.java)
}