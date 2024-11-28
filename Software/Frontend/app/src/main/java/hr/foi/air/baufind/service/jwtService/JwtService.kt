package hr.foi.air.baufind.service.jwtService

import android.content.Context
import android.util.Base64
import org.json.JSONObject

object JwtService {
    fun saveJwt(context: Context, jwt: String) {
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("jwt_token", jwt)
        editor.apply()
    }

    fun getJwt(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("jwt_token", null)
    }

    fun clearJwt(context: Context) {
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("jwt_token")
        editor.apply()
    }

    fun getNameFromJwt(jwt: String): String? {
        try {
            val parts = jwt.split(".")
            if (parts.size == 3) {
                val payload = parts[1]
                val decodedPayload = Base64.decode(payload, Base64.URL_SAFE or Base64.NO_WRAP)
                val jsonPayload = String(decodedPayload)
                val jsonObject = JSONObject(jsonPayload)
                return jsonObject.optString("email")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun getIdFromJwt(jwt: String): String? {
        try {
            val parts = jwt.split(".")
            if (parts.size == 3) {
                val payload = parts[1]
                val decodedPayload = Base64.decode(payload, Base64.URL_SAFE or Base64.NO_WRAP)
                val jsonPayload = String(decodedPayload)
                val jsonObject = JSONObject(jsonPayload)
                return jsonObject.optString("sub")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

}
