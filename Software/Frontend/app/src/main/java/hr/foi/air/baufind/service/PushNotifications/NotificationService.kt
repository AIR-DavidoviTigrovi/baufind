package hr.foi.air.baufind.service.PushNotifications

import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import kotlinx.coroutines.tasks.await

class NotificationService {
    suspend fun getToken(): String {
        return Firebase.messaging.token.await() ?: ""
    }
}