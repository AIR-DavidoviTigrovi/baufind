package hr.foi.air.baufind.ui.screens.UserProfileScreen

import androidx.lifecycle.ViewModel
import hr.foi.air.baufind.ws.response.UserProfileResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UserProfileViewModel : ViewModel() {
    private val _userProfile = MutableStateFlow<UserProfileResponse?>(null)
    val userProfile: StateFlow<UserProfileResponse?> get() = _userProfile

    fun setUserProfile(profile: UserProfileResponse) {
        _userProfile.value = profile

    }
    fun updateUserProfile(updatedProfile : UserProfileResponse) {
        _userProfile.value = updatedProfile
    }
}