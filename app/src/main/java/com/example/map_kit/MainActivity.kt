package com.example.map_kit

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.map_kit.databinding.ActivityMainBinding
import com.yandex.mapkit.MapKitFactory
import android.Manifest
import android.os.Handler
import android.view.animation.AnimationUtils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity(){

      private lateinit var  binding: ActivityMainBinding



    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageView.setImageResource(R.drawable.earth)

                 val anim = AnimationUtils.loadAnimation(applicationContext,R.anim.rotate)
                binding.imageView.startAnimation(anim)


                  val handler = Handler()

                    handler.postDelayed( {mapActivity()},5000)


                          setApikey(savedInstanceState)
                         MapKitFactory.initialize(this)

                // mapActivity()


                binding.imageView.setOnClickListener { handler.removeCallbacksAndMessages(null); binding.imageView.clearAnimation(); mapActivity() }




    }




    private fun mapActivity() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) { startActivity(Intent(this,MapActivity::class.java))
        } else { ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION),1) }
    }

    private fun setApikey(savedInstanceState: Bundle?) {
          val haveApiKey = savedInstanceState?.getBoolean("haveApiKey") ?: false
             if (!haveApiKey) {MapKitFactory.setApiKey(Utils.MAP_API_KEY)}
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("haveApiKey",true) }

    override fun onRequestPermissionsResult (requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if ( ( grantResults[0] == PackageManager.PERMISSION_GRANTED) or
               (grantResults[1] == PackageManager.PERMISSION_GRANTED)) { startActivity(Intent(this,MapActivity::class.java)) }


    }


}












