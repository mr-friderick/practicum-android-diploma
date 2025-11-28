package ru.practicum.android.diploma.presentation.search.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onFavoriteClick: () -> Unit,
    onTeamClick: () -> Unit,
    onDetailClick: () -> Unit,
    onFilterFragment: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Поиск вакансий",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextBox("К деталям вакансии", onDetailClick)
            TextBox("Фильтр Фрагмент", onFilterFragment)
        }
    }
}

@Composable
fun TextBox(text: String, navigation: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(top = 10.dp)
            .size(width = 180.dp, height = 60.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(color = MaterialTheme.colorScheme.primary)
            .clickable(
                onClick = navigation,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}
