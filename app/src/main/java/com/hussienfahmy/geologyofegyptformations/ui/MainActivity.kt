package com.hussienfahmy.geologyofegyptformations.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnPreDraw
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.hussienfahmy.geologyofegyptformations.NavGraphDirections
import com.hussienfahmy.geologyofegyptformations.R
import com.hussienfahmy.geologyofegyptformations.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val navController
        get() = findNavController(R.id.nav_host_fragment)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.root.doOnPreDraw {
            // onPreDraw as the navController can be retrieved
            initToolbar(binding)
            initBottomNav(binding)
            initOnNavigationDestinationListener(binding)
        }
    }

    private fun initBottomNav(binding: ActivityMainBinding) {
        binding.bottomNav.setupWithNavController(navController)
        binding.bottomNav.setOnItemReselectedListener {
            //just to prevent the navigation to the same destination
        }
    }

    private fun initOnNavigationDestinationListener(binding: ActivityMainBinding) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.questionsFragment -> {
                    enableBottomBar(binding, false)
                }
                else -> enableBottomBar(binding, true)
            }
        }
    }

    private fun enableBottomBar(binding: ActivityMainBinding, enable: Boolean) {
        for (i in 0 until binding.bottomNav.menu.size()) {
            binding.bottomNav.menu.getItem(i).isEnabled = enable
        }
    }

    private fun initToolbar(binding: ActivityMainBinding) {
        setSupportActionBar(binding.toolbar)
        val appBarConfiguration =
            AppBarConfiguration.Builder(R.id.preQuestionsFragment, R.id.dataFragment).build()
        navController.let {
            binding.toolbar.setupWithNavController(it, appBarConfiguration)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_about -> {
                val navDirections = NavGraphDirections.actionGlobalAboutFragment()
                navController.navigate(navDirections)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}