package com.example.firstcomposeproject

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ContactListScreen(
    onContactClick: (Int) -> Unit
) {
    val contacts = SampleContacts.all

    Column(modifier = Modifier.padding(16.dp)) {
        contacts.forEachIndexed { index, contact ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable { onContactClick(index) },
                elevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${contact.name} ${contact.familyName}",
                        style = MaterialTheme.typography.h6
                    )
                }
            }
        }
    }
}
