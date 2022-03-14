package com.example.testbalinasoft.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.testbalinasoft.R
import com.example.testbalinasoft.databinding.ActivityMainBinding
import com.example.testbalinasoft.network.checkForInternet
import com.example.testbalinasoft.viewmodel.MainViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), VisibilityFab {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var requirePermission: ActivityResultLauncher<Array<String>>
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        activityResultLauncher = initActivityResultLauncher()
        requirePermission = initRequestPermission()

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)

        binding.apply {

            toolbar.title = ""

            floatingActionButton.setOnClickListener {
                requirePermission.launch(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }


            imageViewBack.setOnClickListener {
                navigateBack()
            }
        }

        setupObservers()

        setSupportActionBar(binding.toolbar)

        initNavigation()

        this.lifecycleScope.launchWhenCreated {
            viewModel.mainEvent.collect { event ->
                when (event) {
                    is MainViewModel.MainEvent.ShowErrorMessage -> {
                        Snackbar.make(view, event.message, Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun navigateBack() {
        navController.navigateUp()
    }

    private fun initNavigation() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        NavigationUI.setupWithNavController(binding.navView, navController)

        drawerLayout = findViewById(R.id.drawer_layout)
        actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            binding.toolbar,
            R.string.open,
            R.string.close
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.label) {
                "fragment_login" -> {
                    binding.imageViewBack.visibility = View.INVISIBLE
                    actionBarDrawerToggle.isDrawerIndicatorEnabled = false
                }
                "fragment_map" -> {
                    binding.imageViewBack.visibility = View.INVISIBLE
                    actionBarDrawerToggle.isDrawerIndicatorEnabled = true
                }
                "fragment_photos" -> {
                    binding.imageViewBack.visibility = View.INVISIBLE
                    actionBarDrawerToggle.isDrawerIndicatorEnabled = true
                }
                "fragment_full_screen" -> {
                    binding.imageViewBack.visibility = View.VISIBLE
                    actionBarDrawerToggle.isDrawerIndicatorEnabled = false
                }
            }
        }
    }

    private fun setupObservers() {
        viewModel.isVisibleFab.observe(this) {
            if (it) {
                binding.floatingActionButton.show()
            } else {
                binding.floatingActionButton.hide()
            }
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    @SuppressLint("MissingPermission")
    private fun initActivityResultLauncher() =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                    if (checkForInternet(this)) {
                        val lat = location.latitude
                        val lng = location.longitude
                        viewModel.postImage(
                            it.data,
                            (System.currentTimeMillis() / 1000).toInt(),
                            lat,
                            lng
                        )
                    }
                    else {
                        viewModel.showErrorMessage("Lost internet connection")
                    }
                }
            }
        }


    private fun initRequestPermission() =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions.all { it.value }) {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                activityResultLauncher.launch(cameraIntent)
            } else {
                Snackbar.make(this.currentFocus!!, "Don't have permission", Snackbar.LENGTH_SHORT)
                    .show()
            }
        }

    override fun hideFab() {
        viewModel.isVisibleFab.value = false
    }

    override fun showFab() {
        viewModel.isVisibleFab.value = true
    }
}

interface VisibilityFab {
    fun hideFab()
    fun showFab()
}