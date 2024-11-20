package hr.foi.air.baufind.ws.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object NetworkService {
    private const val BASE_URL = "http://10.0.2.2:5009/"

    private var instance: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val authService: AuthenticationService = instance.create(AuthenticationService::class.java)

}
