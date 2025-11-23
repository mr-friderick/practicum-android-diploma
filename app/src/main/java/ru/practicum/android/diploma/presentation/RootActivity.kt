package ru.practicum.android.diploma.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.data.network.request.VacanciesRequest

class RootActivity : AppCompatActivity() {
    private var navHostFragment: NavHostFragment? = null
    private var navController: NavController? = null
    private val network: NetworkClient by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment?.navController

        lifecycleScope.launch {
            val result = network.doRequest(
                VacanciesRequest.Vacancy()
            )
        }
    }
}
