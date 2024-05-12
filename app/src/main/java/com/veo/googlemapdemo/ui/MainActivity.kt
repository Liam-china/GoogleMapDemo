package com.veo.googlemapdemo.ui

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import com.veo.googlemapdemo.R
import com.veo.googlemapdemo.utils.PermissionUtil
import com.veo.googlemapdemo.viewmodel.MainViewModel


class MainActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener{

    private lateinit var imageView: ImageView
    private lateinit var textView: TextView
    private lateinit var gMap: GoogleMap
    private lateinit var locationManager: LocationManager
    private val viewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    private var isReady = false
    private var isStart = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        initUI()
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        initViewModel()
    }

    private fun initViewModel() {
        viewModel.directions.observe(this) {
            // 更新地图的路径
            Log.d(TAG, "DirectionsResponse = $it")
            gMap.clear()
            updateLocationUI()
            it.routes.forEach {route ->
                val options = PolylineOptions()
                options.addAll(PolyUtil.decode(route.overview_polyline?.points))
                options.color(Color.Red.toArgb())
                gMap.addPolyline(options)
                if (route.legs.isNotEmpty()) {
                    val leg = route.legs[0]
                    "driving,${leg.distance.text},${leg.duration.text}".also { textView.text = it }
                }
            }
        }
    }

    private fun initUI() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        findViewById<View>(R.id.btn_route).setOnClickListener {
            if (isReady)
                viewModel.getDirections(
                    gMap.cameraPosition.target.latitude,
                    gMap.cameraPosition.target.longitude
                )
        }

        textView = findViewById(R.id.tv_distance_time)
        imageView = findViewById(R.id.iv_target)
    }

    override fun onStart() {
        super.onStart()
        // Check the app location permissions
        if (PermissionUtil.hasLocationPermission(this)) {
            requestLocationInfoIfOpenGps()
        } else {
            PermissionUtil.requestLocationPermission(this, PERMISSION_LOCATION_CODE)
        }
    }

    override fun onStop() {
        locationManager.removeUpdates(this)
        super.onStop()
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationInfoIfOpenGps() {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        val providers = locationManager.getProviders(true)
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            // GPS opened
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                2000L,
                2.0.toFloat(),
                this
            )
        } else {
            // Notify user open gps
            Toast.makeText(this, "Please open gps", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_LOCATION_CODE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestLocationInfoIfOpenGps()
                }
            }
            else -> {}
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onMapReady(map: GoogleMap) {
        Log.d(TAG, "onMapReady() map = $map")
        isReady = true
        map.apply {
            uiSettings.isMyLocationButtonEnabled = true
            uiSettings.isMapToolbarEnabled = true
            uiSettings.isZoomControlsEnabled = true
            uiSettings.isZoomGesturesEnabled = true
            setOnCameraMoveListener {
                // 获取中心点的位置，来作为目的地点
                Log.d(TAG, "onCameraMove position = $cameraPosition")
            }
            setOnCameraMoveStartedListener {
                Log.d(TAG, "onCameraMoveStarted position = $cameraPosition")
            }
            setOnCameraIdleListener {
                Log.d(TAG, "onCameraIdle position = $cameraPosition")
            }
            gMap = this
        }
    }

    override fun onLocationChanged(location: Location) {
        Log.d(TAG, "onLocationChanged() location = $location")
        imageView.visibility = View.VISIBLE
        val lat = location.latitude
        val lng = location.longitude
        viewModel.originLat = lat
        viewModel.originLng = lng
        val l = LatLng(lat, lng)
        updateLocationUI(l)
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(l,10f))
    }

    private fun updateLocationUI(l: LatLng = LatLng(viewModel.originLat, viewModel.originLng)) {
        gMap.addMarker(
            MarkerOptions()
                .position(l)
                .title("Start")
        )
    }

    override fun onLocationChanged(locations: MutableList<Location>) {
        super.onLocationChanged(locations)
        Log.d(TAG, "onLocationChanged() locations = $locations")
    }

    override fun onFlushComplete(requestCode: Int) {
        super.onFlushComplete(requestCode)
        Log.d(TAG, "onFlushComplete() requestCode = $requestCode")
    }

    override fun onProviderEnabled(provider: String) {
        super.onProviderEnabled(provider)
        Log.d(TAG, "onProviderEnabled() provider = $provider")
    }

    override fun onProviderDisabled(provider: String) {
        super.onProviderDisabled(provider)
        Log.d(TAG, "onProviderDisabled() provider = $provider")
    }

    companion object {
        const val TAG = "MainActivity"
        const val PERMISSION_LOCATION_CODE = 0x1001
    }
}