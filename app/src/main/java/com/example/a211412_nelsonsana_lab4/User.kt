package com.example.a211412_nelsonsana_lab4

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

// Data Class (Task 2)
data class UserProfile(
    val name: String = "",
    val rescuedMeals: Int = 5,
    val profileImage: Int = R.drawable.pp
)

// ViewModel (Task 2)
class UserViewModel : androidx.lifecycle.ViewModel() {
    var userState by mutableStateOf(UserProfile())
        private set

    fun updateName(newName: String) {
        userState = userState.copy(name = newName)
    }
}