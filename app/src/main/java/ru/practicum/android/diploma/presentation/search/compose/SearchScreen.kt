package ru.practicum.android.diploma.presentation.search.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.VacancyDetailModel
import ru.practicum.android.diploma.presentation.theme.Black
import ru.practicum.android.diploma.presentation.theme.Blue
import ru.practicum.android.diploma.presentation.theme.FieldHeight
import ru.practicum.android.diploma.presentation.theme.ImageSize_48
import ru.practicum.android.diploma.presentation.theme.PaddingBase
import ru.practicum.android.diploma.presentation.theme.PaddingSmall
import ru.practicum.android.diploma.presentation.theme.PaddingZero
import ru.practicum.android.diploma.presentation.theme.Padding_12
import ru.practicum.android.diploma.presentation.theme.Padding_4

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    vacanciesPaging: Flow<androidx.paging.PagingData<VacancyDetailModel>>,
    onSearchTextChange: (String) -> Unit,
    onFilterFragment: () -> Unit,
    onDetailClick: (String) -> Unit
) {
    var searchState by remember { mutableStateOf("") }
    var lastSearchedText by remember { mutableStateOf<String?>(null) }
    val pagingItems = vacanciesPaging.collectAsLazyPagingItems()

    // Отслеживаем, для какого текста была завершена загрузка
    LaunchedEffect(pagingItems.loadState.refresh) {
        if (pagingItems.loadState.refresh is LoadState.NotLoading &&
            pagingItems.loadState.refresh !is LoadState.Error &&
            searchState.isNotBlank()) {
            lastSearchedText = searchState
        }
    }

    // Сбрасываем при изменении текста
    LaunchedEffect(searchState) {
        if (searchState.isBlank()) {
            lastSearchedText = null
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.job_search),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    IconButton(onClick = { onFilterFragment() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.filter_24px),
                            contentDescription = stringResource(R.string.filter),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues = paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            SearchField(
                searchText = searchState,
                onSearchTextChanged = { newText ->
                    searchState = newText
                    onSearchTextChange(newText)
                }
            )
            SearchContent(
                searchText = searchState,
                pagingItems = pagingItems,
                lastSearchedText = lastSearchedText,
                onDetailClick = onDetailClick
            )
        }
    }
}

@Composable
private fun SearchField(
    searchText: String,
    onSearchTextChanged: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(FieldHeight)
            .padding(horizontal = PaddingBase, PaddingSmall)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.tertiaryContainer)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(FieldHeight)
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BasicTextField(
                value = searchText,
                onValueChange = { newText ->
                    onSearchTextChanged(newText)
                },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = Black
                ),
                singleLine = true,
                cursorBrush = SolidColor(Blue),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (searchText.isEmpty()) {
                            Text(
                                text = stringResource(R.string.enter_request),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }
                        innerTextField()
                    }
                }
            )
            if (searchText.isNotEmpty()) {
                IconButton(
                    onClick = {
                        onSearchTextChanged("")
                    },
                    modifier = Modifier.size(20.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.close_24px),
                        contentDescription = stringResource(R.string.clear),
                        tint = Black,
                        modifier = Modifier.size(20.dp)
                    )
                }
            } else {
                IconButton(
                    onClick = {
                        // выполнить поиск
                    },
                    modifier = Modifier.size(20.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.search_24px),
                        contentDescription = stringResource(R.string.search),
                        tint = Black,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun BlueSpace(textRes: Int, vararg formatArgs: Any) {
    Box(
        modifier = Modifier
            .padding(Padding_12)
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            stringResource(textRes, *formatArgs),
            modifier = Modifier.padding(Padding_12, Padding_4),
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
private fun SearchContent(
    searchText: String,
    pagingItems: androidx.paging.compose.LazyPagingItems<VacancyDetailModel>,
    lastSearchedText: String?,
    onDetailClick: (String) -> Unit
) {
    when {
        searchText.isBlank() -> {
            ImageWithText(
                imageRes = R.drawable.default_screen_icon,
                textRes = R.string.empty_text
            )
        }
        searchText == "test_server_error" -> {
            // Показываем экран ошибки сервера для тестового запроса
            ImageWithText(
                imageRes = R.drawable.server_sick,
                textRes = R.string.server_error
            )
        }
        pagingItems.loadState.refresh is LoadState.Loading -> {
            LoadingState()
        }
        pagingItems.loadState.refresh is LoadState.Error -> {
            val error = pagingItems.loadState.refresh as LoadState.Error
            val errorMessage = error.error.message ?: error.error.localizedMessage ?: ""

            when {
                errorMessage.contains("интернет", ignoreCase = true) ||
                    errorMessage.contains("internet", ignoreCase = true) ||
                    errorMessage.contains("network", ignoreCase = true) -> {
                    ImageWithText(
                        imageRes = R.drawable.skull,
                        textRes = R.string.no_internet
                    )
                }
                errorMessage.contains("сервер", ignoreCase = true) ||
                    errorMessage.contains("server", ignoreCase = true) ||
                    errorMessage.contains("500", ignoreCase = true) ||
                    errorMessage.contains("503", ignoreCase = true) ||
                    errorMessage.contains("504", ignoreCase = true) -> {
                    ImageWithText(
                        imageRes = R.drawable.server_sick,
                        textRes = R.string.server_error
                    )
                }
                else -> {
                    BlueSpace(R.string.there_are_no_such_vacancies)
                    ImageWithText(
                        imageRes = R.drawable.cat,
                        textRes = R.string.couldnt_get_list_vacancies
                    )
                }
            }
        }
        searchText.isNotBlank() &&
            lastSearchedText == searchText &&
            pagingItems.itemCount == 0 &&
            pagingItems.loadState.refresh is LoadState.NotLoading &&
            pagingItems.loadState.refresh !is LoadState.Error &&
            pagingItems.loadState.append is LoadState.NotLoading &&
            pagingItems.loadState.prepend is LoadState.NotLoading -> {
            BlueSpace(R.string.there_are_no_such_vacancies)
            ImageWithText(
                imageRes = R.drawable.cat,
                textRes = R.string.couldnt_get_list_vacancies
            )
        }
        else -> {
            VacancyListState(
                pagingItems = pagingItems,
                onDetailClick = onDetailClick
            )
        }
    }
}

@Preview
@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(ImageSize_48)
        )
    }
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

@Composable
private fun VacancyListState(
    pagingItems: androidx.paging.compose.LazyPagingItems<VacancyDetailModel>,
    onDetailClick: (String) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (pagingItems.itemCount > 0) {
            BlueSpace(R.string.vacancies_found, pagingItems.itemCount)
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(PaddingZero),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                count = pagingItems.itemCount,
                key = pagingItems.itemKey { it.id },
                contentType = pagingItems.itemContentType { "vacancy" }
            ) { index ->
                val vacancy = pagingItems[index]
                if (vacancy != null) {
                    VacancyItem(
                        vacancy = vacancy,
                        onClick = { onDetailClick(vacancy.id) }
                    )
                }
            }

            item {
                when (pagingItems.loadState.append) {
                    is LoadState.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    is LoadState.Error -> {
                        val error = pagingItems.loadState.append as LoadState.Error
                        Text(
                            text = "Ошибка загрузки: ${error.error.localizedMessage}",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    else -> {}
                }
            }
        }
    }
}
