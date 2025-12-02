package ru.practicum.android.diploma.presentation.vacancy.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.theme.ImageSize_48
import ru.practicum.android.diploma.presentation.theme.PaddingBase
import ru.practicum.android.diploma.presentation.theme.PaddingSmall
import ru.practicum.android.diploma.presentation.theme.Padding_12
import ru.practicum.android.diploma.presentation.theme.Padding_1
import ru.practicum.android.diploma.presentation.theme.Padding_4
import ru.practicum.android.diploma.util.ShareTarget
import ru.practicum.android.diploma.util.drawableToBitmap

private const val TOP_TARGETS_COUNT = 3
private const val ICON_SIZE_TOP_MULTIPLIER = 1.167f
private const val ICON_SIZE_LIST_MULTIPLIER = 0.75f
private const val LABEL_FONT_SIZE = 12

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareBottomSheet(
    onDismiss: () -> Unit,
    targets: List<ShareTarget>,
    onTargetSelected: (ShareTarget) -> Unit,
    onMoreClicked: () -> Unit
) {
    val ctx = LocalContext.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = PaddingBase, vertical = Padding_12)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(Padding_4))

            val topTargets = targets.take(TOP_TARGETS_COUNT)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = PaddingSmall),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (t in topTargets) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = Padding_4)
                            .clickable {
                                onTargetSelected(t)
                                onDismiss()
                            },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val bmp = remember(t.icon) { drawableToBitmap(t.icon) }
                        Image(
                            bitmap = bmp.asImageBitmap(),
                            contentDescription = t.label,
                            modifier = Modifier
                                .size(ImageSize_48 * ICON_SIZE_TOP_MULTIPLIER)
                        )
                        Spacer(modifier = Modifier.height(PaddingSmall))
                        Text(
                            text = t.label,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = LABEL_FONT_SIZE.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = Padding_4)
                        .clickable {
                            onMoreClicked()
                            onDismiss()
                        },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = ctx.getString(R.string.more),
                        modifier = Modifier.size(ImageSize_48 * ICON_SIZE_TOP_MULTIPLIER),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(PaddingSmall))
                    Text(
                        text = ctx.getString(R.string.more),
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = LABEL_FONT_SIZE.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(PaddingSmall))
            Divider(modifier = Modifier.fillMaxWidth(), thickness = Padding_1)
            Spacer(modifier = Modifier.height(PaddingSmall))

            Column(modifier = Modifier.fillMaxWidth()) {
                val rest = if (targets.size > TOP_TARGETS_COUNT) targets.drop(TOP_TARGETS_COUNT) else emptyList()
                rest.forEachIndexed { idx, t ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onTargetSelected(t)
                                onDismiss()
                            }
                            .padding(vertical = Padding_12),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val bmp = remember(t.icon) { drawableToBitmap(t.icon) }
                        Image(
                            bitmap = bmp.asImageBitmap(),
                            contentDescription = t.label,
                            modifier = Modifier
                                .size(ImageSize_48 * ICON_SIZE_LIST_MULTIPLIER)
                                .padding(end = Padding_12)
                        )
                        Text(
                            text = t.label,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    if (idx != rest.lastIndex) {
                        Divider()
                    }
                }
            }

            Spacer(modifier = Modifier.height(Padding_12))
        }
    }
}
