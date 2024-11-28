package hr.foi.air.baufind.ws.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object NetworkService {
    private const val BASE_URL = "http://10.0.2.2:5009/"

    private fun createRetrofit(tokenProvider: TokenProvider): Retrofit {
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenProvider))
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun createAuthService(tokenProvider: TokenProvider): AuthenticationService {
        return createRetrofit(tokenProvider).create(AuthenticationService::class.java)
    }
    fun createUserProfileService(tokenProvider: TokenProvider): UserProfileService {
        return createRetrofit(tokenProvider).create(UserProfileService::class.java)
    }
}

