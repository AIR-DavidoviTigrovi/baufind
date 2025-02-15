package hr.foi.air.baufind.ws.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object NetworkService {
    private const val BASE_URL = "http://10.0.2.2:5009/"

    private fun createRetrofitNoAuth(): Retrofit {
        val client = OkHttpClient.Builder()
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

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
    fun createJobRoomService(tokenProvider: TokenProvider): JobRoomService {
        return createRetrofit(tokenProvider).create(JobRoomService::class.java)
    }
    fun createAuthService(tokenProvider: TokenProvider): AuthenticationService {
        return createRetrofit(tokenProvider).create(AuthenticationService::class.java)
    }
    fun createUserProfileService(tokenProvider: TokenProvider): UserProfileService {
        return createRetrofit(tokenProvider).create(UserProfileService::class.java)
    }
    fun createSkillService(tokenProvider: TokenProvider): SkillService{
        return createRetrofit(tokenProvider).create(SkillService::class.java)
    }
    fun createJobService(tokenProvider: TokenProvider): JobService{
        return createRetrofit(tokenProvider).create(JobService::class.java)
    }
    fun createReviewService(tokenProvider: TokenProvider): ReviewService{
        return createRetrofit(tokenProvider).create(ReviewService::class.java)
    }
    fun createGeocodingService(): GeocodingService {
        return createRetrofitNoAuth().create(GeocodingService::class.java)
    }
}

