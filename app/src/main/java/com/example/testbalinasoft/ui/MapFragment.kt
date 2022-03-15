package com.example.testbalinasoft.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.testbalinasoft.R
import com.example.testbalinasoft.network.checkForInternet
import com.example.testbalinasoft.viewmodel.MapViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat

@AndroidEntryPoint
class MapFragment : Fragment(R.layout.fragment_map) {

    private lateinit var myMap: GoogleMap

    private val viewModel: MapViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            myMap = googleMap
            updateMap()
        }

        if (!checkForInternet(requireContext())) {
            Snackbar.make(requireView(), "Lost internet connection", Snackbar.LENGTH_LONG).show()
        }

        val listener = activity as MainActivity
        listener.showFab()
    }

    private fun updateMap() {
        viewModel.images.observe(viewLifecycleOwner) { images ->
            for (image in images) {
                val marker = LatLng(image.lat, image.lng)
                val title = SimpleDateFormat("dd.MM.yyyy hh:mm").format(image.date * 1000L)
                myMap.addMarker(MarkerOptions().position(marker).title(title))
            }
        }
    }
}