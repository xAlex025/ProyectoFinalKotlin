package com.example.proyectofinalikotlin.network

import com.example.proyectofinal.model.FighterRecordResponse
import com.example.proyectofinal.model.FighterResponse
import com.example.proyectofinalikotlin.models.CategoriesResponse
import com.example.proyectofinalikotlin.models.FightResponse
import com.example.proyectofinalikotlin.models.FightResultsResponse
import com.example.proyectofinalikotlin.models.FightStatisticsResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("fighters")
    suspend fun getFightersById(@Query("id") id: String): FighterResponse

    @GET("fighters")
    suspend fun getFightersByCategory(@Query("category") category: String): FighterResponse

    @GET("fighters")
    suspend fun getFightersBySearch(@Query("search") search: String): FighterResponse

    @GET("fighters/records")
    suspend fun getFighterStats(@Query("id") id: String): FighterRecordResponse

    @GET("categories")
    suspend fun getAllCategories(): CategoriesResponse

    @GET("fights")
    suspend fun getFighterFights(@Query("fighter") id: String , @Query("season") season: String): FightResponse

    @GET("fights/results")
    suspend fun getFighterFightsResults(@Query("id") idPelea: Int ): FightResultsResponse

    @GET("fights/statistics/fighters")
    suspend fun getFightsStatitics(@Query("id") idPelea: Int ): FightStatisticsResponse

    companion object {
        private const val BASE_URL = "https://v1.mma.api-sports.io/"
        private const val API_KEY = "0b7f7428c63a9531637766d1af1be3fb"

        private val client = OkHttpClient.Builder()
            .addInterceptor { chain: Interceptor.Chain ->
                val request: Request = chain.request()
                    .newBuilder()
                    .addHeader("x-apisports-key", API_KEY)
                    .build()
                chain.proceed(request)
            }
            .build()

        val instance: ApiService by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}
