package com.example.littlelemon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

interface Destinations {
    val route: String
    val icon: ImageVector
    val title: String
}

object Home: Destinations{
    override val route = "Home"
    override val icon: ImageVector
        get() = TODO("Not yet implemented")
    override val title: String
        get() = TODO("Not yet implemented")

}

object Onboarding: Destinations{
    override val route = "OnboardingActivity"
    override val icon = Icons.Filled.Info
    override val title = "Onboarding"
}

object Profile: Destinations{
    override val route = "ProfileActivity"
    override val icon = Icons.Filled.Person
    override val title = "Profile"
}

object DishDetails : Destinations {
    override val route = "Menu"
    const val argDishId = "dishId"
    override val icon: ImageVector
        get() = TODO("Not yet implemented")
    override val title: String
        get() = TODO("Not yet implemented")
    const val argOrderNo = "OrderNo"
}
