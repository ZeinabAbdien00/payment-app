package com.example.paymentapp

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.paymentapp.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navHostFragment: NavHostFragment
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var binding: ActivityMainBinding
    private val viewModel : MainActivityViewModel by viewModels()

    private val pushPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupNavigation()
        setUpVisibilityOfBottomBar()
        setStatusBarGradiant()
        requestForPermission()
    }

    private fun setupNavigation() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        bottomNavigationView = findViewById(R.id.bottom_nav)
        NavigationUI.setupWithNavController(bottomNavigationView, navHostFragment.navController)
    }

    private fun requestForPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pushPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun setStatusBarGradiant() {
        val window: Window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.navigationBarColor = ContextCompat.getColor(this, android.R.color.transparent)
    }

    private fun setUpVisibilityOfBottomBar() {
        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->

            if (destination.id == R.id.splashFragment){
                if (true==viewModel.isFirstTimeSplash.value){
                    viewModel.isFirstTimeSplash.value=false
                }else {
                    onBackPressed()
                }
            }

            if (destination.id == R.id.passwordFragment){
                if (true==viewModel.isFirstTimePassword.value){
                    viewModel.isFirstTimePassword.value=false
                }else {
                    onBackPressed()
                }
            }

            when (destination.id) {
                R.id.homeFragment, R.id.notificationsFragment, R.id.moreFragment ->
                    bottomNavigationView.visibility = View.VISIBLE
                else -> bottomNavigationView.visibility = View.GONE
            }
        }
    }
}