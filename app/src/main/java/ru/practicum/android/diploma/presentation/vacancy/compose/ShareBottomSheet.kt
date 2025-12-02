package ru.practicum.android.diploma.presentation.vacancy.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.util.ShareTarget
import ru.practicum.android.diploma.util.drawableToBitmap

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
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            val topCount = 3 // покажем 3 реальных + 1 More
            val topTargets = targets.take(topCount)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (t in topTargets) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
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
                                .size(56.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = t.label,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 12.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp)
                        .clickable {
                            onMoreClicked()
                            onDismiss()
                        },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = ctx.getString(R.string.more),
                        modifier = Modifier.size(56.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = ctx.getString(R.string.more),
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 12.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            // Divider full width
            Divider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp)
            Spacer(modifier = Modifier.height(8.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                val rest = if (targets.size > topCount) targets.drop(topCount) else emptyList()
                rest.forEachIndexed { idx, t ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onTargetSelected(t)
                                onDismiss()
                            }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val bmp = remember(t.icon) { drawableToBitmap(t.icon) }
                        Image(
                            bitmap = bmp.asImageBitmap(),
                            contentDescription = t.label,
                            modifier = Modifier
                                .size(36.dp)
                                .padding(end = 12.dp)
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

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}
