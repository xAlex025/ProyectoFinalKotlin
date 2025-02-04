package com.example.proyectofinalikotlin.network

import com.example.proyectofinal.model.FighterRecordResponse
import com.example.proyectofinal.model.FighterResponse
import com.example.proyectofinalikotlin.models.CategoriesResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("fighters") // ✅ Asegúrate de que el endpoint es "fighters" y no "teams"
    suspend fun getFightersById(@Query("id") id: String): FighterResponse

    @GET("fighters")
    suspend fun getFightersByCategory(@Query("category") category: String): FighterResponse

    @GET("fighters")
    suspend fun getFightersBySearch(@Query("search") search: String): FighterResponse

    @GET("fighters/records")
    suspend fun getFighterStats(@Query("id") id: String): FighterRecordResponse


    @GET("categories")
    suspend fun getAllCategories(): CategoriesResponse



     companion object {
        private const val BASE_URL = "https://v1.mma.api-sports.io/"
        private const val API_KEY = "0b7f7428c63a9531637766d1af1be3fb" // ⚠️ Reemplaza con tu API Key real

        // ✅ OkHttpClient con Interceptor para incluir la API Key automáticamente
        private val client = OkHttpClient.Builder()
            .addInterceptor { chain: Interceptor.Chain ->
                val request: Request = chain.request()
                    .newBuilder()
                    .addHeader("x-apisports-key", API_KEY) // ✅ Se añade el header correctamente
                    .build()
                chain.proceed(request)
            }
            .build()

        // ✅ Retrofit instancia correctamente el `ApiService`
        val instance: ApiService by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client) // ✅ Se pasa el OkHttpClient con el interceptor
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java) // ✅ Se crea el servicio directamente
        }
    }}