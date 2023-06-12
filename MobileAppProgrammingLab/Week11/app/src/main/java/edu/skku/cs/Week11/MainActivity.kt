package edu.skku.cs.Week11

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import edu.skku.cs.Week11.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    var find_lat=37.295881
    var find_lon=126.975931

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn = findViewById<Button>(R.id.button)
        val textView = findViewById<TextView>(R.id.textView)
        val et = findViewById<EditText>(R.id.editText)

        val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.mapview) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val client = OkHttpClient()
        val host = "https://api.weatherapi.com/v1/current.json"

        btn.setOnClickListener {
            val api_key=""////////
            val find_city=et.text
            val path = "?key=" + api_key + "&q=" + find_city
            val req = Request.Builder().url(host+path).build()
            client.newCall(req).enqueue(object : Callback{
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }
                override fun onResponse(call: Call, response: Response) {
                    response.use{
                        if (!response.isSuccessful) throw IOException("Unexpected code $response")
                        val data = response.body!!.string()
                        val dat_parse = Gson().fromJson(data, DataModel::class.java)
                        val find_lat= dat_parse.location.lat!!
                        val find_lon= dat_parse.location.lon!!
                        val condition_find=dat_parse.current.condition.text
                        val wind_mph=dat_parse.current.wind_mph
                        val temp_c=dat_parse.current.temp_c
                        CoroutineScope(Dispatchers.Main).launch {
                            var concat: String =
                                "Current " + find_city.toString() + " City weather is .."+
                                        condition_find.toString() + " and wind_mph & temperature is " +
                                        wind_mph.toString() + ", " + temp_c.toString()
                            textView.text = concat
                            /*
                            1. clear map
                            2. remake newmarker use find_lat, find_lon
                             */
                            mMap.clear()
                            mMap.addMarker(MarkerOptions().position(LatLng(find_lat, find_lon)).title("MAP"))
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(find_lat, find_lon),10F))
                        }
                    }
                }
            })
        }
    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val marker = LatLng(find_lat, find_lon)
        mMap.addMarker(MarkerOptions().position(marker).title("MAP"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker,10F))
    }
}