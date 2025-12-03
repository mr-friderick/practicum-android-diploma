package ru.practicum.android.diploma.presentation.favourites.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.favourites.viewmodel.FavoriteViewState
import ru.practicum.android.diploma.presentation.search.compose.VacancyItem
import ru.practicum.android.diploma.presentation.theme.AppTheme
import ru.practicum.android.diploma.presentation.theme.PaddingBase

@Preview
@Composable
fun FavoriteScreenPreview() {
    AppTheme {
        FavoriteScreen(
            state = FavoriteViewState.Empty,
            onVacancyClick = { }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    state: FavoriteViewState,
    onVacancyClick: (String) -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.favorite),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (state) {
                is FavoriteViewState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }
                is FavoriteViewState.Content -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(state.vacancies.size) { index ->
                            VacancyItem(
                                vacancy = state.vacancies[index],
                                onClick = { onVacancyClick(state.vacancies[index].id) }
                            )
                        }
                    }
                }
                is FavoriteViewState.Empty -> {
                    ListVacanciesEmpty()
                }
                is FavoriteViewState.Error -> {
                    NoGetListVacancies()
                }
            }
        }
    }
}

@Preview
@Composable
fun NoGetListVacancies() {
    ImageWithText(
        imageRes = R.drawable.cat,
        textRes = R.string.couldnt_get_list_vacancies
    )
}

@Preview
@Composable
fun ListVacanciesEmpty() {
    ImageWithText(
        imageRes = R.drawable.man_with_magnifying,
        textRes = R.string.list_empty
    )
}

@Composable
private fun ImageWithText(
    imageRes: Int,
    textRes: Int
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painterResource(id = imageRes),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )
        Spacer(modifier = Modifier.height(PaddingBase))

        Text(
            stringResource(textRes),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
    }
}
