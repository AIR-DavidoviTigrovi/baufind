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

    fun createWorkersService(tokenProvider: TokenProvider): WorkersSkillService {
        return createRetrofit(tokenProvider).create(WorkersSkillService::class.java)
    }

    fun createAuthService(tokenProvider: TokenProvider): AuthenticationService {
        return createRetrofit(tokenProvider).create(AuthenticationService::class.java)
    }

    fun createSkillService(tokenProvider: TokenProvider): SkillService{
        return createRetrofit(tokenProvider).create(SkillService::class.java)
    }
    fun createJobService(tokenProvider: TokenProvider): JobService{
        return createRetrofit(tokenProvider).create(JobService::class.java)
    }
}

