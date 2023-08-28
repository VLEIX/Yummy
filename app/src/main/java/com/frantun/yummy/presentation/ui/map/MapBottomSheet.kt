package com.frantun.yummy.presentation.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import com.frantun.yummy.R
import com.frantun.core.utils.BitmapHelper
import com.frantun.yummy.databinding.BottomSheetMapBinding
import com.frantun.core.base.BaseBottomSheet
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.R as Material

class MapBottomSheet : BaseBottomSheet() {

    private var _binding: BottomSheetMapBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<MapBottomSheetArgs>()
    private val origin by lazy { args.origin }
    private val latLng by lazy { LatLng(origin.latitude, origin.longitude) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUi()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupUi() {
        binding.apply {
            nameTextView.text = origin.name
            descriptionTextView.text = origin.description
            val mapFragment =
                childFragmentManager.findFragmentById(R.id.map_fragment) as? SupportMapFragment
            mapFragment?.getMapAsync { googleMap ->
                googleMap.uiSettings.apply {
                    isScrollGesturesEnabled = false
                    isZoomGesturesEnabled = false
                    isZoomControlsEnabled = false
                }
                googleMap.setOnMapLoadedCallback {
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_STANDARD))
                }
                addMarker(googleMap)
            }
        }
    }

    private fun markerIcon(): BitmapDescriptor {
        val color =
            ContextCompat.getColor(
                requireContext(),
                Material.color.design_default_color_primary_variant
            )
        return BitmapHelper.vectorToBitmap(requireContext(), R.drawable.ic_marker, color)
    }

    private fun addMarker(googleMap: GoogleMap) {
        googleMap.addMarker(
            MarkerOptions()
                .title(origin.name)
                .position(latLng)
                .icon(markerIcon())
        )?.also {
            it.tag = origin
        }
    }

    private companion object {
        const val ZOOM_STANDARD = 12f
    }
}
