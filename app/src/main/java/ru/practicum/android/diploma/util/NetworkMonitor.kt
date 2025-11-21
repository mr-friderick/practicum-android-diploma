package ru.practicum.android.diploma.util

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

/**
 * Вспомогательный класс для проверки доступа к Интернету!
 * При запросе выдача : Boolean - значение - есть интернет или нет.
 * Проверяет пытаясь подключиться к сайту Google.
 * Использование : если отсутствует интернет будем показывать заглушку
 */


class NetworkMonitor(private val context: Context) {
    
    suspend fun isOnline(): Boolean = withContext(Dispatchers.IO) {
        val hasNetworkConnection = isOnlineSync()
        if (!hasNetworkConnection) {
            return@withContext false
        }
        
        // Дополнительная проверка - пытаемся подключиться к интернету
        val hasInternetAccess = checkInternetAccess()
        
        hasInternetAccess
    }
    
    private fun checkInternetAccess(): Boolean {
        return try {
            val url = URL("https://www.google.com")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "HEAD"
            connection.connectTimeout = 1000 // Уменьшили таймаут до 1 секунды
            connection.readTimeout = 1000
            connection.responseCode == 200
        } catch (e: Exception) {
            false
        }
    }
    
    @SuppressLint("MissingPermission") //ВРЕМЕННАЯ АННОТАЦИЯ ПОКА НЕ ОТКРЫТ ДОСТУП В ИНТЕРНЕТ ( MANIFEST)
    private fun isOnlineSync(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        
        // Проверяем, что сеть имеет доступ к интернету
        val hasInternet = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        val isValidated = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        
        val hasTransport = when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
        
        val isOnline = hasInternet && hasTransport
        
        return isOnline
    }
    

}
