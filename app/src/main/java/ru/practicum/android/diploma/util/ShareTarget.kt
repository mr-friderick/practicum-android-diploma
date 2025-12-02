package ru.practicum.android.diploma.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.core.graphics.createBitmap

// Модель целевого приложения для шаринга
data class ShareTarget(
    val packageName: String,
    val activityName: String,
    val label: String,
    val icon: Drawable
)

@SuppressLint("QueryPermissionsNeeded")
fun queryShareTargets(context: Context): List<ShareTarget> {
    val pm = context.packageManager
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        type = "text/plain"
    }
    val resolveInfos = pm.queryIntentActivities(sendIntent, 0)

    return resolveInfos.map { ri ->
        val pkg = ri.activityInfo.packageName
        val act = ri.activityInfo.name
        val label = ri.loadLabel(pm).toString()
        val icon = ri.loadIcon(pm)
        ShareTarget(pkg, act, label, icon)
    }.distinctBy { it.packageName + ":" + it.activityName }
        .sortedBy { it.label.lowercase() }
}

// Константы для магических чисел
private const val DEFAULT_ICON_SIZE = 48

fun drawableToBitmap(drawable: Drawable): Bitmap {
    if (drawable is BitmapDrawable && drawable.bitmap != null) {
        return drawable.bitmap
    }

    val width = if (drawable.intrinsicWidth > 0) drawable.intrinsicWidth else DEFAULT_ICON_SIZE
    val height = if (drawable.intrinsicHeight > 0) drawable.intrinsicHeight else DEFAULT_ICON_SIZE
    val bitmap = createBitmap(width, height)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}
