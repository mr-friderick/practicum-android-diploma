package ru.practicum.android.diploma.presentation.search.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.VacancyDetailModel
import ru.practicum.android.diploma.util.formatToSalary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    vacanciesPaging: Flow<androidx.paging.PagingData<VacancyDetailModel>>,
    onSearchTextChange: (String) -> Unit,
    onFavoriteClick: () -> Unit,
    onDetailClick: (String) -> Unit,
    onFilterFragment: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    val pagingItems = vacanciesPaging.collectAsLazyPagingItems()

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
                .padding(paddingValues)
        ) {
            OutlinedButton(
                onClick = onFilterFragment,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text("Фильтр")
            }
            OutlinedTextField(
                value = searchText,
                onValueChange = { newText ->
                    searchText = newText
                    onSearchTextChange(newText)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                label = { Text("Поиск вакансий") },
                placeholder = { Text("Введите название вакансии") },
                singleLine = true
            )

            when {
                searchText.isBlank() -> {
                    // Начальное состояние - пустой экран
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Введите название вакансии для поиска",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                pagingItems.loadState.refresh is LoadState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                pagingItems.loadState.refresh is LoadState.Error -> {
                    val error = pagingItems.loadState.refresh as LoadState.Error
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Ошибка: ${error.error.localizedMessage}",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                pagingItems.itemCount == 0 && searchText.isNotBlank() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Ничего не найдено")
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(
                            horizontal = 16.dp,
                            vertical = 8.dp
                        )
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
        }
    }
}

@Composable
fun VacancyItem(
    vacancy: VacancyDetailModel,
    onClick: () -> Unit
) {
    // Оптимизация: вычисляем salaryText один раз и кэшируем
    val salaryText = remember(vacancy.salary) {
        if (vacancy.salary != null) {
            buildString {
                vacancy.salary.from?.let { append("от ${it.formatToSalary()}") }
                vacancy.salary.to?.let {
                    if (isNotEmpty()) {
                        append(" ")
                    }
                    append("до ${it.formatToSalary()}")
                }
                vacancy.salary.currency?.let { append(" $it") }
            }
        } else {
            null
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = vacancy.name,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            if (salaryText != null) {
                Text(
                    text = salaryText,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            vacancy.address?.let { address ->
                Text(
                    text = address.raw,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Text(
                text = vacancy.employer.name,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

