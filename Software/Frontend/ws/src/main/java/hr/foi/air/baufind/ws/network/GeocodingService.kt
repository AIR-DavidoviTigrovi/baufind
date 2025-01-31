package hr.foi.air.baufind.ws.network

import hr.foi.air.baufind.ws.response.ReverseGeocodingResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingService {
    @GET("geocoding/reverse")
    suspend fun reverseGeocode(@Query("lat") lat: Double, @Query("lng") lng: Double): ReverseGeocodingResponse
}