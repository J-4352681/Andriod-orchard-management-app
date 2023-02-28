package app.lajusta.ui.quinta.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import app.lajusta.R
import app.lajusta.ui.generic.BaseFragment
import app.lajusta.ui.quinta.Quinta
import app.lajusta.ui.usuarios.api.UsuariosApi
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.beust.klaxon.Parser.Companion.default
import com.google.android.gms.location.FusedLocationProviderClient

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import kotlinx.coroutines.runBlocking
import java.io.IOException
import java.net.URL

class QuintaMapaFragment : BaseFragment() {

    private lateinit var quinta: Quinta
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

    @SuppressLint("MissingPermission")
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

                    if (isLocationPermissionGranted()) {
                        FusedLocationProviderClient(activity!!).lastLocation.addOnSuccessListener {
                            //Log.e("LO LOGRO", "ES " + it.latitude + " " + it.longitude)

                            val uPos = LatLng(it.latitude, it.longitude)

                            val options = PolylineOptions()
                            options.color(Color.RED)
                            options.width(5f)

                            val url = getURL(uPos, quintaMarker)
                            var result: String = ""

                            apiCall(suspend {
                                result = URL(url).readText()
                            }, {
                                // this will execute in the main thread, after the async call is done
                                try {
                                    val parser: Parser = default()
                                    val stringBuilder: StringBuilder = StringBuilder(result)
                                    val json: JsonObject = parser.parse(stringBuilder) as JsonObject

                                    val routes = json.array<JsonObject>("routes")
                                    val array = routes!!["legs"]["steps"]
                                    if (array.getOrNull(0) != null) {
                                        val points =
                                            routes!!["legs"]["steps"][0] as JsonArray<JsonObject>
                                        val polypts = points.flatMap {
                                            PolyUtil.decode(
                                                it.obj("polyline")?.string("points")!!
                                            )
                                        }
                                        if (polypts.isEmpty()) longToast("Hubo un error visualizando la ruta.")

                                        //polyline
                                        options.add(uPos)
                                        for (point in polypts) options.add(point)
                                        options.add(quintaMarker)
                                        map!!.addPolyline(options)
                                    }

                                } catch (e: java.lang.Error) {
                                    Log.e("Error UI", e.message!!)
                                }

                            }, "Hubo un error al buscar la ruta mas rapida a la quinta.")
                        }
                    } else Log.e("Permisos", "No estan aceptados ambos permisos de localizacion")


                    //Animate camara
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

    private fun getURL(from: LatLng, to: LatLng): String {
        val origin = "origin=" + from.latitude + "," + from.longitude
        val dest = "destination=" + to.latitude + "," + to.longitude
        val sensor = "sensor=false"
        val key = "key=" + getString(R.string.directions_api_key)
        val params = "$origin&$dest&$sensor&$key"
        return "https://maps.googleapis.com/maps/api/directions/json?$params"
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
        when (requestCode) {
            REQUEST_CODE_LOCATION -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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