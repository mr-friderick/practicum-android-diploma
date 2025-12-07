package ru.practicum.android.diploma.presentation.filtering.workplace.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.filtering.workplace.viewmodel.RegionViewState
import ru.practicum.android.diploma.presentation.theme.Black
import ru.practicum.android.diploma.presentation.theme.Blue
import ru.practicum.android.diploma.presentation.theme.FieldHeight
import ru.practicum.android.diploma.presentation.theme.ImageSize_48
import ru.practicum.android.diploma.presentation.theme.PaddingBase
import ru.practicum.android.diploma.presentation.theme.PaddingSmall
import ru.practicum.android.diploma.presentation.theme.PaddingZero
import ru.practicum.android.diploma.presentation.theme.Size_20

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegionScreen(
    onBackClick: () -> Unit,
    onAreaSelected: (Int, String, Int?) -> Unit = { _, _, _ -> },
    regionState: RegionViewState? = null,
    onSearchTextChanged: (String) -> Unit = {}
) {
    var searchText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.region_select),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    Icon(
                        painter = painterResource(R.drawable.arrow_back_24px),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(PaddingBase)
                            .clickable(
                                onClick = {
                                    onBackClick()
                                },
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            )
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
        ) {
            // Поле поиска
            SearchRegion(
                searchText = searchText,
                onRegionTextChanged = { newText ->
                    searchText = newText
                    onSearchTextChanged(newText)
                }
            )

            // Контент в зависимости от состояния
            when (regionState) {
                is RegionViewState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(ImageSize_48)
                        )
                    }
                }

                is RegionViewState.NoInternet -> {
                    NoInternetConnection()
                }

                is RegionViewState.Error -> {
                    NoGetTheList()
                }

                is RegionViewState.Region -> {
                    if (regionState.regions.isEmpty()) {
                        ThereNoRegion()
                    } else {
                        // Фильтруем регионы по поисковому запросу
                        val filteredRegions = if (searchText.isNotBlank()) {
                            regionState.regions.filter { region ->
                                region.name.contains(searchText, ignoreCase = true)
                            }
                        } else {
                            regionState.regions
                        }

                        if (filteredRegions.isEmpty()) {
                            ThereNoRegion()
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(filteredRegions) { region ->
                                    RegionItem(
                                        regionName = region.name,
                                        onClick = {
                                            onAreaSelected(region.id, region.name, region.parentId)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(ImageSize_48)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchRegion(
    searchText: String,
    onRegionTextChanged: (String) -> Unit
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
                    onRegionTextChanged(newText)
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
                                text = stringResource(R.string.enter_region),
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
                        onRegionTextChanged("")
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
fun RegionItem(
    regionName: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(start = PaddingBase, top = PaddingSmall, bottom = PaddingSmall, end = PaddingZero)
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = regionName,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge
        )
        Icon(
            painter = painterResource(R.drawable.arrow_forward_24px),
            contentDescription = null,
            modifier = Modifier
                .padding(PaddingBase)
        )
    }
}

@Composable
private fun ImageWithText(
    imageRes: Int,
    textRes: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        androidx.compose.foundation.Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            contentScale = ContentScale.Fit
        )
        androidx.compose.foundation.layout.Spacer(
            modifier = Modifier.height(PaddingBase)
        )
        Text(
            text = stringResource(textRes),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = PaddingBase)
        )
    }
}

@Composable
private fun ThereNoRegion() {
    ImageWithText(
        imageRes = R.drawable.cat,
        textRes = R.string.there_no_region
    )
}

@Composable
private fun NoGetTheList() {
    ImageWithText(
        imageRes = R.drawable.carpet,
        textRes = R.string.couldnt_get_the_list
    )
}

@Composable
private fun NoInternetConnection() {
    ImageWithText(
        imageRes = R.drawable.carpet,
        textRes = R.string.no_internet
    )
}
