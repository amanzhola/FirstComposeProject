package com.example.firstcomposeproject

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// Если скруглять углы фото — раскомментировать:
// import androidx.compose.foundation.shape.RoundedCornerShape
// import androidx.compose.ui.draw.clip

// ---------- ЭКРАН С ШАПКОЙ (синяя полоса с названием приложения) ----------
@Composable
fun ContactScreen(contact: Contact,
                  onBackClick: (() -> Unit)? = null   // ← добавили
) {
    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    // Название приложения в шапке
                    title = { Text(text = stringResource(R.string.app_name)) },
                    // Цвета шапки (можно заменить на MaterialTheme.colors.primary)
                    backgroundColor = Color(0xFF3F51B5),
                    contentColor = Color.White,
                    navigationIcon = if (onBackClick != null) {
                        {
                            androidx.compose.material.IconButton(onClick = onBackClick) {
                                Icon(
                                    painter = painterResource(id = android.R.drawable.ic_menu_revert),
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        }
                    } else null
                )
            }
        ) { padding ->
            // Контент экрана — ContactDetails
            ContactDetails(
                contact = contact,
                modifier = Modifier.padding(padding)
            )
        }
    }
}

// ---------- ОСНОВНОЙ КОНТЕНТ С КОНТАКТОМ ----------
@Composable
fun ContactDetails(contact: Contact, modifier: Modifier = Modifier) {
    val fullName = remember(contact.name, contact.surname, contact.familyName) {
        buildFullName(contact)
    }
    val phoneValue = remember(contact.phone) {
        if (contact.phone.isBlank()) "---" else contact.phone
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // аватар/инициалы
        AvatarOrInitials(
            name = contact.name,
            familyName = contact.familyName,
            imageRes = contact.imageRes,
            size = 56.dp,
            photoWidth = 160.dp,
            photoHeight = 96.dp,
            photoCornerRadius = 0.dp // ← углы прямые, не скругляем
        )

        Spacer(Modifier.height(16.dp))
        // ФИО по центру + звезда
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = fullName, // ← используем кэш
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h6.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier.padding(end = 6.dp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            val starSize = 20.dp
            if (contact.isFavorite) {
                Image(
                    painter = painterResource(android.R.drawable.star_big_on),
                    contentDescription = null,
                    modifier = Modifier.size(starSize)
                )
            } else {
                Spacer(Modifier.size(starSize)) // резервируем место, но без иконки
            }
        }

        Spacer(Modifier.height(24.dp))

        // Информация: телефон / адрес / email (как в макете, по центру, курсивные лейблы)
        InfoRowCentered(
            label = stringResource(R.string.phone),
            value = phoneValue // ← кэшированный телефон
        )
        Spacer(Modifier.height(12.dp))
        InfoRowCentered(
            label = stringResource(R.string.address),
            value = contact.address,
            centerValue = true // ← только адрес центрируем
        )
        if (!contact.email.isNullOrBlank()) {
            Spacer(Modifier.height(12.dp))
            InfoRowCentered(
                label = stringResource(R.string.email),
                value = contact.email!!
            )
        }
    }
}

// ---------- АВАТАР / ИНИЦИАЛЫ (сохранены твои комментарии) ----------
@Composable
private fun AvatarOrInitials(
    name: String,
    familyName: String,
    imageRes: Int?,
    size: Dp,
    // ↓ новые НЕОБЯЗАТЕЛЬНЫЕ параметры только для фото
    photoWidth: Dp? = null,         // если null — берём размер контейнера (size x size)
    photoHeight: Dp? = null,        // если null — берём размер контейнера (size x size)
    photoCornerRadius: Dp = 0.dp    // 0.dp = строго прямоугольное фото
) {
    // Контейнер для выравнивания по центру
    Box(
        modifier = Modifier.wrapContentSize(), // не ограничиваем контейнер для фото
        contentAlignment = Alignment.Center
    ) {
        if (imageRes != null) {
            // ---- ФОТО (прямоугольное) ----
            Image(
                painter = painterResource(imageRes),
                contentDescription = null,
                modifier = Modifier
                    .then(
                        if (photoWidth != null && photoHeight != null)
                            Modifier
                                .width(photoWidth)
                                .height(photoHeight)
                        else
                            Modifier.fillMaxSize()
                    ),
                contentScale = ContentScale.Crop
            )
        } else {
            // ---- ЕСЛИ НЕТ ФОТО (круг с инициалами) ----
            Box(
                modifier = Modifier
                    .size(size)               // ограничиваем размер круга
                    .wrapContentSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.circle),
                    contentDescription = null,
                    tint = Color(0xFFBDBDBD),
                    modifier = Modifier.fillMaxSize()
                )
                Text(
                    text = "${name.take(1)}${familyName.take(1)}".uppercase(),
                    style = MaterialTheme.typography.subtitle1.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    ),
                    color = Color.Black
                )
            }
        }
    }
}

// ---------- РЯД С ЛЕЙБЛОМ И ЗНАЧЕНИЕМ (адрес по центру, остальные — как в макете) ----------
@Composable
private fun InfoRowCentered(
    label: String,
    value: String,
    centerValue: Boolean = false
) {
    val labelWithColon = remember(label) { "$label:" } // ← кэш

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Левая часть — лейбл (всегда прижат к правому краю)
        Text(
            text = labelWithColon,
            style = MaterialTheme.typography.body1.copy(fontStyle = FontStyle.Italic),
            textAlign = TextAlign.End,
            modifier = Modifier.width(90.dp) // фиксированная ширина колонки лейбла
        )

        Spacer(Modifier.width(8.dp))

        // Правая часть — значение
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = if (centerValue) Alignment.Center else Alignment.CenterStart
        ) {
            Text(
                text = value, // value уже пришёл «готовый»
                style = MaterialTheme.typography.body1,
                softWrap = true,
                maxLines = Int.MAX_VALUE,
                lineHeight = 20.sp
            )
        }
    }
}

// ---------- ВСПОМОГАТЕЛЬНОЕ: сборка ФИО в 2 строки ----------
private fun buildFullName(c: Contact): String =
    buildString {
        append(c.name)
        if (!c.surname.isNullOrBlank()) {
            append(" ")
            append(c.surname)
        }
        append("\n")
        append(c.familyName)
    }

// ---------------- PREVIEWS ----------------
@Preview(name = "ProfileWithoutPhotoPreview", showSystemUi = false)
@Composable
private fun ProfileWithoutPhotoPreview() {
    ContactScreen(
        contact = Contact(
            name = "Евгений",
            surname = "Андреевич",
            familyName = "Лукашин",
            imageRes = null,                 // нет фото → инициалы
            isFavorite = true,
            phone = "+7 495 495 95 95",
            address = "г. Москва, 3-я улица Строителей,\nд. 25, кв. 12",
            email = "ELukashin@practicum.ru"
        )
    )
}

@Preview(name = "ProfileWithPhotoPreview", showSystemUi = false)
@Composable
private fun ProfileWithPhotoPreview() {
    ContactScreen(
        contact = Contact(
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
