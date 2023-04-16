package com.farhanrv.thestory.ui.map

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.farhanrv.thestory.R
import com.farhanrv.thestory.data.network.ApiResource
import com.farhanrv.thestory.databinding.ActivityShowMapBinding
import com.farhanrv.thestory.ui.auth.login.LoginActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import org.koin.androidx.viewmodel.ext.android.viewModel

class ShowMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var token: String
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityShowMapBinding
    private val viewModel: MapViewModel by viewModel()
    private val boundsBuilder = LatLngBounds.Builder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShowMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Peta"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val intentExtra = intent.getStringExtra(EXTRA_TOKEN)
        if (intentExtra != null) {
            token = intentExtra
        } else {
            Toast.makeText(this@ShowMapActivity, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
            finish()
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun addManyMarker() {
        viewModel.getAllStoriesWithMap(token).observe(this@ShowMapActivity) { data ->
            if (data != null) {
                when (data) {
                    is ApiResource.Loading -> {
                        Log.e("observer.Loading", "ApiResource.Loading")
                    }
                    is ApiResource.Success -> {
                        Log.e("observer.Success", data.data.toString())
                        data.data?.forEach { item ->
                            if (item.lat != null && item.lon != null) {
                                val latLng = LatLng(item.lat, item.lon)
                                mMap.addMarker(MarkerOptions().position(latLng).title(item.name))
                                boundsBuilder.include(latLng)
                            }
                        }
                        val bounds: LatLngBounds = boundsBuilder.build()
                        mMap.animateCamera(
                            CameraUpdateFactory.newLatLngBounds(
                                bounds,
                                resources.displayMetrics.widthPixels,
                                resources.displayMetrics.heightPixels,
                                300
                            )
                        )
                    }
                    is ApiResource.Error -> {
                        Toast.makeText(
                            this@ShowMapActivity,
                            "Silahkan login kembali",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(this@ShowMapActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true

        addManyMarker()
        getMyLocation()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val EXTRA_TOKEN = "EXTRA_TOKEN"
    }
}