package app.lajusta.ui.quinta.map

import android.content.pm.PackageManager
import android.graphics.Camera
import android.location.Address
import android.location.Geocoder
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import app.lajusta.R
import app.lajusta.ui.familia.Familia
import app.lajusta.ui.generic.BaseFragment
import app.lajusta.ui.quinta.model.QuintaCompleta

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOError
import java.io.IOException
import java.util.jar.Manifest

class QuintaMapaFragment : BaseFragment() {

    private lateinit var quinta: QuintaCompleta
    private lateinit var map: GoogleMap

    companion object {
        const val REQUEST_CODE_LOCATION = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle ->
            quinta = bundle.getParcelable("quinta")!!
        }
    }

    private val callback = OnMapReadyCallback { googleMap ->

        map = googleMap
        enableLocation()

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */

        if (quinta != null) {
            val geocoder: Geocoder = Geocoder(activity)
            var addressList: List<Address>

            try {
                addressList = geocoder.getFromLocationName(quinta.direccion, 1)

                if (addressList != null) {
                    val lat = addressList.get(0).latitude
                    val long = addressList.get(0).longitude
                    val quintaMarker = LatLng(lat, long)
                    map.addMarker(
                        MarkerOptions().position(quintaMarker).title("Marcador en una quinta")
                    )
                    map.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(quintaMarker, 12f),
                        4000,
                        null
                    )
                }

            } catch (e: IOException) {
                Log.e("Error con geocoder", e.message!!)
                longToast("Hubo un error al encontrar la direccion de la quinta, la direccio no existe.")
            }

        } else {
            shortToast("Hubo un error al mostrar la direccion de la quinta.")
            val laPlata = LatLng(-34.933333333333, -57.95)
            map.animateCamera(
                CameraUpdateFactory.newLatLngZoom(laPlata, 12f),
                4000,
                null
            )
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_quinta_mapa, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun isLocationPermissionGranted() =
        ContextCompat.checkSelfPermission(
            activity!!,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    private fun enableLocation() {
        //Asummos que el mapa existe porque no se deberia llamar si no
        if (isLocationPermissionGranted()) {
            try {
                map.isMyLocationEnabled = true
            } catch (e: SecurityException) {
                Log.e("SECURITY EXCEPTION", e.message!!)
            }

        } else {
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                activity!!,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            longToast("Para ver su localizacion vaya a ajustes y acepte los permisos.")
        } else {
            ActivityCompat.requestPermissions(
                activity!!,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_LOCATION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            REQUEST_CODE_LOCATION -> if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                try {
                    map.isMyLocationEnabled = true
                } catch (e: SecurityException) {
                    Log.e("SECURITY EXCEPTION", e.message!!)
                }
            } else {
                shortToast("Para activar la localizacion vaya a ajustes y acepte los permisos")
            }
            else -> {}
        }
    }
}