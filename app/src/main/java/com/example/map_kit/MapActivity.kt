package com.example.map_kit

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.example.map_kit.databinding.ActivityMapBinding
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKit
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.RequestPoint
import com.yandex.mapkit.RequestPointType
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.directions.driving.DrivingOptions
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.directions.driving.DrivingRouter
import com.yandex.mapkit.directions.driving.DrivingSection
import com.yandex.mapkit.directions.driving.DrivingSession
import com.yandex.mapkit.directions.driving.VehicleOptions
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.GeoObjectTapListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.runtime.Error
import com.yandex.runtime.image.ImageProvider

class MapActivity : AppCompatActivity(),DrivingSession.DrivingRouteListener {

    private lateinit var binding: ActivityMapBinding

    var location = com.yandex.mapkit.geometry.Point(56.0184, 92.8672)
    private val zoomValue = 16.5f

          private var lat = 56.0184; private var lon = 92.8672
          private var latx = 0.0; private var lony = 0.0

                 var probki = true
       private lateinit var locationManager: LocationManager

   private  var  placemarkMapObject: PlacemarkMapObject? = null

     private var mapObject: MapObjectCollection? = null
     private var drivingRouter: DrivingRouter? = null
     private var drivingSession: DrivingSession? = null

    val onMapTap = object: InputListener {
        override fun onMapTap(p0: Map, p1: Point) {
            Toast.makeText(applicationContext,"${p1.latitude}  --  ${p1.longitude}",Toast.LENGTH_LONG).show()

        }

        override fun onMapLongTap(p0: Map, p1: Point) {
            latx = p1.latitude; lony = p1.longitude
            createRoad() } }

    @SuppressLint("MissingPermission", "UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
          locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager


             drivingRouter = DirectionsFactory.getInstance().createDrivingRouter()






                 getlocation(10f)

               val mapKit: MapKit = MapKitFactory.getInstance()
                  val probki_layer = mapKit.createTrafficLayer(binding.mapTv.mapWindow)

                       probki_layer.isTrafficVisible = true

                          binding.buttonprobki.background = getDrawable(R.drawable.circle)




                    binding.mapTv.map.addInputListener(onMapTap)







        binding.buttonprobki.setOnClickListener {

            when ( probki) {

                true -> { probki_layer.isTrafficVisible = false; binding.buttonprobki.background = getDrawable(R.drawable.circle_2); probki = false }
                false -> { probki_layer.isTrafficVisible = true; binding.buttonprobki.background = getDrawable(R.drawable.circle); probki = true }
            }

        }

        binding.button.setOnClickListener {

          if (placemarkMapObject != null) { binding.mapTv.map.mapObjects.remove(placemarkMapObject!!); placemarkMapObject = null}
                                             getlocation(2f) } }




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

        placemarkMapObject = mapObjectCollection.addPlacemark(location,ImageProvider.fromBitmap(marker))

                                    mapObjectCollection.addEmptyPlacemark(location)



            placemarkMapObject!!.opacity = 1f

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

    override fun onDrivingRoutes(p0: MutableList<DrivingRoute>) {
             for ( i in p0 ) { mapObject!!.addPolyline(i.geometry) }

    }

    override fun onDrivingRoutesError(p0: Error) {

        Toast.makeText(applicationContext,"Проезд не найден",Toast.LENGTH_LONG).show()
    }
    private fun createRoad() {

           if (drivingSession != null) { binding.mapTv.map.mapObjects.remove(mapObject!!) }


        mapObject = binding.mapTv.map.mapObjects.addCollection()
         val reqestPoint: ArrayList<RequestPoint> = ArrayList()
               reqestPoint.add(RequestPoint(Point(lat,lon),RequestPointType.WAYPOINT,null))
               reqestPoint.add(RequestPoint(Point(latx,lony),RequestPointType.WAYPOINT,null))

            drivingSession = drivingRouter!!.requestRoutes(reqestPoint, DrivingOptions(), VehicleOptions(),this)
    }

}










