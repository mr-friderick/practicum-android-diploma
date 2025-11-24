package ru.practicum.android.diploma.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL

/**
 * Вспомогательный класс для проверки доступа к Интернету!
 * При запросе выдача : Boolean - значение - есть интернет или нет.
 * Проверяет пытаясь подключиться к сайту Google.
 * Использование : если отсутствует интернет будем показывать заглушку
 */

class NetworkMonitor(private val context: Context) {

    companion object {
        private const val CONNECT_TIMEOUT_MS = 1000
        private const val READ_TIMEOUT_MS = 1000
        private const val HTTP_SUCCESS_CODE = 200
    }

    suspend fun isOnline(): Boolean = withContext(Dispatchers.IO) {
        val hasNetworkConnection = isOnlineSync()
        if (!hasNetworkConnection) {
            false
        } else {
            // Дополнительная проверка - пытаемся подключиться к интернету
            checkInternetAccess()
        }
    }

    private fun checkInternetAccess(): Boolean {
        return try {
            val url = URL("https://www.google.com")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "HEAD"
            connection.connectTimeout = CONNECT_TIMEOUT_MS
            connection.readTimeout = READ_TIMEOUT_MS
            connection.responseCode == HTTP_SUCCESS_CODE
        } catch (e: IOException) {
            false
        } catch (e: SocketTimeoutException) {
            false
        }
    }

    private fun isOnlineSync(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        if (network == null) {
            return false
        }

        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
        if (networkCapabilities == null) {
            return false
        }

        // Проверяем, что сеть имеет доступ к интернету
        val hasInternet = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        val hasTransport = when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }

        return hasInternet && hasTransport
    }
}
