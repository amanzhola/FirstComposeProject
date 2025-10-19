package com.example.firstcomposeproject

object SampleContacts {
    val all = listOf(
        Contact(
            name = "Евгений",
            surname = "Андреевич",
            familyName = "Лукашин",
            imageRes = null,
            isFavorite = true,
            phone = "+7 495 495 95 95",
            address = "г. Москва, 3-я улица Строителей,\nд. 25, кв. 12",
            email = "ELukashin@practicum.ru"
        ),
        Contact(
            name = "Василий",
            familyName = "Кузякин",
            imageRes = R.drawable.barsik,
            isFavorite = false,
            phone = "",
            address = "Ивановская область, дер.\nКрутово, д. 4",
            email = null
        )
    )
}
