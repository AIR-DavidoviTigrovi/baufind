package hr.foi.air.baufind.ui.screens.UserProfileScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.foi.air.baufind.service.UserProfileService.UserProfileService
import hr.foi.air.baufind.ws.network.AppTokenProvider
import hr.foi.air.baufind.ws.network.TokenProvider
import hr.foi.air.baufind.ws.request.UpdateUserRequest
import hr.foi.air.baufind.ws.response.UserProfileResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserProfileViewModel(tokenProvider: AppTokenProvider) : ViewModel() {

    private val userProfileService = UserProfileService(tokenProvider)
    private val _userProfile = MutableStateFlow<UserProfileResponse?>(null)
    val userProfile: StateFlow<UserProfileResponse?> get() = _userProfile

    private val _loading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _loading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    fun setUserProfile(profile: UserProfileResponse) {
        _userProfile.value = profile
    }

    private fun fetchUserProfile() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val fetchedProfile = userProfileService.fetchUserProfile()
                if (fetchedProfile != null) {
                    _userProfile.value = fetchedProfile
                } else {
                    _errorMessage.value = "Failed to load user profile."
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _errorMessage.value = "An error occurred while fetching the profile."
            } finally {
                _loading.value = false
            }
        }
    }

    fun updateUserProfile(
        userId: Int,
        name: String,
        address: String,
        phone: String,
        profilePicture: String?,
        addSkills: List<Int>,
        removeSkills: List<Int>,
        tokenProvider: TokenProvider
    ) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val request = UpdateUserRequest(
                    userId = userId,
                    name = name,
                    address = address,
                    phone = phone,
                    profilePicture = profilePicture,
                    addSkills = addSkills,
                    removeSkills = removeSkills
                )
                val servis = UserProfileService(tokenProvider)
                val response = servis.updateUserProfile(request)
                if (response != null) {
                    if (response.success) {
                        fetchUserProfile()
                    } else {
                        _errorMessage.value = response.errors?.joinToString("\n")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _errorMessage.value = "An error occurred while updating the profile."
            } finally {
                _loading.value = false
            }
        }
    }
}
