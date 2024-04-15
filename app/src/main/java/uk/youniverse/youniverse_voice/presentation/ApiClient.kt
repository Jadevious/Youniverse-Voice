package uk.youniverse.youniverse_voice.presentation

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uk.youniverse.youniverse_voice.presentation.interfaces.ApiService

// Defining the client as an object makes it a singleton,
// ensures only one request can be active at a time.
object ApiClient {

    // OkHttp client configuration
    private val client = OkHttpClient.Builder().apply {
        addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
    }.build()

    // Retrofit client configuration
    val apiService: ApiService = Retrofit.Builder()
        .baseUrl(HEART_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)
}