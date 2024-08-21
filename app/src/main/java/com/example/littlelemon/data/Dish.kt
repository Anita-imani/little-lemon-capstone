package com.example.littlelemon.data

import com.example.littlelemon.R

data class Dish (
    val name: String,
    val price: String,
    val description: String,
    val image: Int
)

val Dishes = listOf(
    Dish("Greek Salad", "$12.99", "The famous greek saad", R.drawable.greeksalad),
    Dish("Bruschetta", "$5.99", "The famous Bruschetta", R.drawable.greeksalad),
    Dish("Greek Salad", "$12.99", "The famous greek saad", R.drawable.greeksalad),
    Dish("Greek Salad", "$12.99", "The famous greek saad", R.drawable.greeksalad),
    Dish("Greek Salad", "$12.99", "The famous greek saad", R.drawable.greeksalad),
    Dish("Greek Salad", "$12.99", "The famous greek saad", R.drawable.greeksalad),
    Dish("Greek Salad", "$12.99", "The famous greek saad", R.drawable.greeksalad)
    )

