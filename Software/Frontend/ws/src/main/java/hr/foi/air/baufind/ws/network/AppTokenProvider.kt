package hr.foi.air.baufind.ws.network


import android.content.SharedPreferences

class AppTokenProvider(private val sharedPreferences: SharedPreferences) : TokenProvider{
    override fun getToken(): String? {
        return sharedPreferences.getString("jwt_token", null)
    }
}

