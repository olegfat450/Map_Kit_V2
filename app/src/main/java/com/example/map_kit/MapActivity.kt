package com.example.map_kit

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Point
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.map_kit.databinding.ActivityMapBinding
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.runtime.image.ImageProvider

class MapActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMapBinding

    var location = com.yandex.mapkit.geometry.Point(0.0, 0.0)
    private val zoomValue = 16.5f
          private var lat = 0.0;private var lon = 0.0
       // var  location: Location = (0.0,0.0)
       //  private lateinit var location: Location

       private lateinit var locationManager: LocationManager

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
         locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

                 getlocation(10f)






        binding.button.setOnClickListener { binding.mapTv.map.mapObjects.addEmptyPlacemark(location); getlocation(2f) } }

    @SuppressLint("MissingPermission")
    private fun getlocation(duration: Float) {

        val location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

        try { lat = location1!!.latitude; lon = location1.longitude } catch (_:Exception) {
            Toast.makeText(this,"Ошибка GPS",Toast.LENGTH_LONG).show() }

        if ( (lat != 0.0) and (lon != 0.0)){
            location = com.yandex.mapkit.geometry.Point(lat, lon)
            moveLocation(duration)
            setMarket() }
    }

    private fun setMarket() {


        val marker = crateMarker(R.drawable.lication)

        val mapObjectCollection: MapObjectCollection = binding.mapTv.map.mapObjects


        val placemarkMapObject: PlacemarkMapObject = mapObjectCollection.addPlacemark(location,ImageProvider.fromBitmap(marker))

            placemarkMapObject.opacity = 0.5f

    }

    private fun crateMarker(resourse: Int): Bitmap? {
       val drawable = ContextCompat.getDrawable(this,resourse) ?: return null
          val bitmap: Bitmap = Bitmap.createBitmap(drawable.intrinsicWidth,drawable.intrinsicHeight,Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0,0,canvas.width,canvas.height)
        drawable.draw(canvas)
        return bitmap

    }

    private fun moveLocation(duration: Float ) {
        binding.mapTv.map.move(CameraPosition(location,zoomValue,0f,0f), Animation(Animation.Type.SMOOTH,duration),null)
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding.mapTv.onStart() }

    override fun onStop() {
        binding.mapTv.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop() }




}