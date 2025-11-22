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
import androidx.compose.ui.graphics.Color
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
                    Text(text = "Поиск вакансий")
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .size(width = 180.dp, height = 60.dp)
                    .background(color = Color.Gray)
                    .clickable(
                        onClick = {
                            onDetailClick()
                        },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "К деталям вакансии",
                    color = Color.White
                )
            }

            Box(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .size(width = 180.dp, height = 60.dp)
                    .background(color = Color.Gray)
                    .clickable(
                        onClick = {
                            onFavoriteClick()
                        },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Избранное",
                    color = Color.White
                )
            }

            Box(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .size(width = 180.dp, height = 60.dp)
                    .background(color = Color.Gray)
                    .clickable(
                        onClick = {
                            onTeamClick()
                        },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Команда",
                    color = Color.White
                )
            }

            Box(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .size(width = 180.dp, height = 60.dp)
                    .background(color = Color.Gray)
                    .clickable(
                        onClick = {
                            onFilterFragment()
                        },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Фильтр Фрагмент",
                    color = Color.White
                )
            }
        }
    }
}}


