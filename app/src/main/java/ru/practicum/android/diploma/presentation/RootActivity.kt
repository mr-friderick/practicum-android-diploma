package ru.practicum.android.diploma.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.practicum.android.diploma.R

class RootActivity : AppCompatActivity() {
    private var navHostFragment: NavHostFragment? = null
    private var navController: NavController? = null
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment?.navController
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        navController?.let { bottomNavigationView.setupWithNavController(it) }

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.searchFragment -> navigateAndClearOldFragments(R.id.searchFragment)
                R.id.favoriteFragment -> navigateAndClearOldFragments(R.id.favoriteFragment)
                R.id.teamFragment -> navigateAndClearOldFragments(R.id.teamFragment)
                else -> false
            }
        }
        navController?.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.searchFragment -> bottomNavigationView.isVisible = true
                R.id.favoriteFragment -> bottomNavigationView.isVisible = true
                R.id.teamFragment -> bottomNavigationView.isVisible = true
                else -> bottomNavigationView.isVisible = false
            }
        }

    }

    private fun navigateAndClearOldFragments(destinationId: Int): Boolean {
        // Чистим стек навигации и переходим на указанный пункт
        navController?.popBackStack()
        navController?.navigate(destinationId)
        return true
    }
}
