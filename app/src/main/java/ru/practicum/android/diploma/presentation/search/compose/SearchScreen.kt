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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.VacancyDetailModel
import ru.practicum.android.diploma.presentation.search.viewmodel.SearchViewModel
import ru.practicum.android.diploma.presentation.theme.Black
import ru.practicum.android.diploma.presentation.theme.Blue
import ru.practicum.android.diploma.presentation.theme.FieldHeight
import ru.practicum.android.diploma.presentation.theme.ImageSize_48
import ru.practicum.android.diploma.presentation.theme.PaddingBase
import ru.practicum.android.diploma.presentation.theme.PaddingSmall
import ru.practicum.android.diploma.presentation.theme.PaddingZero
import ru.practicum.android.diploma.presentation.theme.Padding_12
import ru.practicum.android.diploma.presentation.theme.Padding_38
import ru.practicum.android.diploma.presentation.theme.Padding_4
import ru.practicum.android.diploma.presentation.theme.Size_20

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    hasActiveFilters: Boolean = false,
    onSearchTextChange: (String) -> Unit,
    onFilterFragment: () -> Unit,
    onDetailClick: (String) -> Unit
) {
    var searchState by remember { mutableStateOf("") }
    val pagingItems = viewModel.vacanciesPaging.collectAsLazyPagingItems()
    val isTyping by viewModel.isTyping.collectAsState()
    val totalCount by viewModel.totalCount.collectAsState()

    LaunchedEffect(Unit) {
        searchState = viewModel.getSearchText()
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
                            painter = painterResource(
                                id = if (hasActiveFilters) {
                                    R.drawable.filter_on__24px
                                } else {
                                    R.drawable.filter_24px
                                }
                            ),
                            contentDescription = stringResource(R.string.filter),
                            tint = if (hasActiveFilters) {
                                Color.Unspecified // Не применять tint для иконки с фоном
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            }
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
                isTyping = isTyping,
                totalCount = totalCount,
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
            horizontalArrangement = Arrangement.spacedBy(PaddingSmall)
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
                    modifier = Modifier.size(Size_20)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.close_24px),
                        contentDescription = stringResource(R.string.clear),
                        tint = Black,
                        modifier = Modifier.size(Size_20)
                    )
                }
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.search_24px),
                    contentDescription = stringResource(R.string.search),
                    tint = Black,
                    modifier = Modifier.size(Size_20)
                )
            }
        }
    }
}

@Composable
private fun BlueSpace(textRes: Int, vararg formatArgs: Any, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
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
    isTyping: Boolean,
    totalCount: Int?,
    onDetailClick: (String) -> Unit
) {
    val refreshLoadState = pagingItems.loadState.refresh

    when {
        searchText.isBlank() -> {
            ImageWithText(
                imageRes = R.drawable.default_screen_icon,
                textRes = R.string.empty_text
            )
        }

        isTyping -> {
            Box(modifier = Modifier.fillMaxSize())
        }

        searchText == "test_server_error" -> {
            ImageWithText(
                imageRes = R.drawable.server_sick,
                textRes = R.string.server_error
            )
        }

        refreshLoadState is LoadState.Loading -> {
            LoadingState()
        }

        refreshLoadState is LoadState.Error -> {
            HandleErrorState(refreshLoadState)
        }

        refreshLoadState is LoadState.NotLoading && pagingItems.itemCount == 0 && searchText.isNotBlank() -> {
            ShowNoResultsState()
        }

        else -> {
            VacancyListState(
                pagingItems = pagingItems,
                totalCount = totalCount,
                onDetailClick = onDetailClick
            )
        }
    }
}

@Composable
private fun HandleErrorState(
    error: LoadState.Error
) {
    val errorMessage = error.error.message ?: error.error.localizedMessage ?: ""

    when {
        isInternetError(errorMessage) -> ShowInternetError()
        isServerError(errorMessage) -> ShowServerError()
        else -> ShowGenericError()
    }
}

private fun isInternetError(errorMessage: String): Boolean {
    return errorMessage.contains("интернет", ignoreCase = true) ||
        errorMessage.contains("internet", ignoreCase = true) ||
        errorMessage.contains("network", ignoreCase = true)
}

private fun isServerError(errorMessage: String): Boolean {
    return errorMessage.contains("сервер", ignoreCase = true) ||
        errorMessage.contains("server", ignoreCase = true) ||
        errorMessage.contains("500", ignoreCase = true) ||
        errorMessage.contains("503", ignoreCase = true) ||
        errorMessage.contains("504", ignoreCase = true)
}

@Composable
private fun ShowInternetError() {
    ImageWithText(
        imageRes = R.drawable.skull,
        textRes = R.string.no_internet
    )
}

@Composable
private fun ShowServerError() {
    ImageWithText(
        imageRes = R.drawable.server_sick,
        textRes = R.string.server_error
    )
}

@Composable
private fun ShowGenericError() {
    BlueSpace(R.string.there_are_no_such_vacancies)
    ImageWithText(
        imageRes = R.drawable.cat,
        textRes = R.string.couldnt_get_list_vacancies
    )
}

@Composable
private fun ShowNoResultsState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        BlueSpace(R.string.there_are_no_such_vacancies)
        ImageWithText(
            imageRes = R.drawable.cat,
            textRes = R.string.couldnt_get_list_vacancies
        )
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

private const val MIN_SNACKBAR_INTERVAL_MS = 3000L // Минимальный интервал между показами Snackbar (3 секунды)

@Composable
private fun VacancyListState(
    pagingItems: androidx.paging.compose.LazyPagingItems<VacancyDetailModel>,
    totalCount: Int?,
    onDetailClick: (String) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val appendLoadState = pagingItems.loadState.append
    val defaultErrorMessage = stringResource(R.string.couldnt_get_list_vacancies)
    val listState = rememberLazyListState()
    var lastShownErrorKey by remember { mutableStateOf<String?>(null) }
    var lastSnackbarShowTime by remember { mutableStateOf(0L) }
    val blueSpaceHeight = Padding_38

    LaunchedEffect(appendLoadState) {
        when (appendLoadState) {
            is LoadState.Error -> {
                // Создаем уникальный ключ для ошибки на основе сообщения
                val errorKey = appendLoadState.error.message
                    ?: appendLoadState.error.localizedMessage
                    ?: "unknown_error"

                val currentTime = System.currentTimeMillis()
                val timeSinceLastShow = currentTime - lastSnackbarShowTime

                // Показываем Snackbar только если:
                // 1. Это новая ошибка (не та же самая)
                // 2. Прошло достаточно времени с последнего показа (минимум 3 секунды)
                if (errorKey != lastShownErrorKey && timeSinceLastShow >= MIN_SNACKBAR_INTERVAL_MS) {
                    val errorMessage = appendLoadState.error.localizedMessage
                        ?: appendLoadState.error.message
                        ?: defaultErrorMessage

                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = errorMessage
                        )
                    }

                    lastShownErrorKey = errorKey
                    lastSnackbarShowTime = currentTime

                    // Если пользователь уже в конце списка, сразу вызываем retry
                    val layoutInfo = listState.layoutInfo
                    val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index
                    val totalItems = pagingItems.itemCount
                    val isAtEnd = lastVisibleIndex != null && totalItems > 0 && lastVisibleIndex >= totalItems - 1
                    if (isAtEnd) {
                        pagingItems.retry()
                    }
                }
            }

            is LoadState.Loading, is LoadState.NotLoading -> {
                // При переходе в Loading или NotLoading сбрасываем ключ ошибки
                // Это позволит показать Snackbar снова, если произойдет новая ошибка
                if (lastShownErrorKey != null) {
                    lastShownErrorKey = null
                }
            }

            else -> {}
        }
    }

    // Отслеживаем скролл и вызываем retry при достижении конца списка, если состояние в Error
    LaunchedEffect(listState, appendLoadState, pagingItems.itemCount) {
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index
            val totalItems = pagingItems.itemCount
            val isAtEnd = lastVisibleIndex != null && totalItems > 0 && lastVisibleIndex >= totalItems - 1
            val isError = appendLoadState is LoadState.Error
            isAtEnd && isError
        }
            .distinctUntilChanged()
            .collect { shouldRetry ->
                if (shouldRetry) {
                    pagingItems.retry()
                }
            }
    }

    Box {
        Box(modifier = Modifier.fillMaxSize()) {
            val shownCount = totalCount ?: pagingItems.itemCount

            if (shownCount > 0) {
                BlueSpace(
                    textRes = R.string.vacancies_found,
                    formatArgs = arrayOf(shownCount),
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .zIndex(1f)
                )
            }
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(0f),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(
                    top = if (shownCount > 0) blueSpaceHeight + 8.dp else 0.dp,
                    bottom = PaddingZero
                ),
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
                    when (appendLoadState) {
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
                            // Не показываем ошибку в списке, чтобы не блокировать скролл
                            // Ошибка показывается через Snackbar
                        }

                        else -> {}
                    }
                }
            }
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
