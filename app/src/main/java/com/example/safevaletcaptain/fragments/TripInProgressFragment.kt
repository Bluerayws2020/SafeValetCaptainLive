package com.example.safevaletcaptain.fragments

import android.Manifest
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import com.example.safevaletcaptain.HomeActivity
import com.example.safevaletcaptain.R
import com.example.safevaletcaptain.adapters.CarAdapter
import com.example.safevaletcaptain.databinding.*
import com.example.safevaletcaptain.helpers.HelperUtils
import com.example.safevaletcaptain.helpers.ViewUtils.hide
import com.example.safevaletcaptain.helpers.ViewUtils.show
import com.example.safevaletcaptain.model.NetworkResults
import com.example.safevaletcaptain.viewmodel.RideViewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*


        class TripInProgressFragment: AppCompatActivity(){

            init {
                d("ayham","TripInProgressFragment")
            }

        private var navController: NavController? = null



            private lateinit var binding: TripInProgressBinding



        private var carAdapter: CarAdapter? = null



        private val cameraRequest = 1888

        private val rideViewModel by viewModels<RideViewModel>()
var lat = ""
    var lon = ""



            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                binding = TripInProgressBinding.inflate(layoutInflater)
                setContentView(binding.root)

                val ride_id:String = intent?.getStringExtra("ride_id").toString()

        binding.carNumber.text = intent?.getStringExtra("carNumber").toString()

                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

                HelperUtils.setDefaultLanguage(this,HelperUtils.getLang(this))

                binding.toolbarInclude.toolbar.title = resources.getString(R.string.trip_in_progress)


                binding.toolbarInclude.notficationBtn.setOnClickListener{



                    startActivity(Intent(this,Notfication::class.java))

                }

                if (HelperUtils.getLang(this) == "ar"){
                    binding.toolbarInclude.menuNotfication .setImageDrawable(resources.getDrawable(R.drawable.ar_back))

                }else {
                    binding.toolbarInclude.menuNotfication .setImageDrawable(resources.getDrawable(R.drawable.back))

                }
                HelperUtils.setDefaultLanguage(this,HelperUtils.getLang(this))


                binding.toolbarInclude.menuNotfication.setOnClickListener{
                    onBackPressed()
                }





                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), cameraRequest)

        binding.takePhoto.setOnClickListener{
    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    startActivityForResult(cameraIntent, cameraRequest)



}

// call car
        binding.callBackMyCarImg.setOnClickListener{



        }




//        send keyNumber
binding.sendBtn.setOnClickListener{

    sendKeyNumber()
    binding.progressBarTrip.show()
    binding.sendBtn.hide()

}






    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == cameraRequest) {


//            send pohto to api

            val photo: Bitmap = data?.extras?.get("data") as Bitmap
            val bmp = photo
            val wrapper = ContextWrapper(this)
            var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
            file = File(file,"${UUID.randomUUID()}.jpg")
            val stream: OutputStream = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.flush()
            stream.close()






      lat = intent.getStringExtra("lat").toString()
         lon   = intent.getStringExtra("lon").toString()
sendDataToApi(file ,lat,lon)
binding.progressBardDataSend.show()


        }
    }




    fun sendDataToApi(imageFile:File,lat:String,lon:String,){


        rideViewModel.sendDataToApi(imageFile,lat,lon)

        rideViewModel.sendCarInfo().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.msg.status == 1) {
                        binding.txtContainer.show()

                        Toast.makeText(this, result.data.msg.message, Toast.LENGTH_SHORT).show()
                        binding.progressBardDataSend.hide()

                    }
                    binding.progressBardDataSend.hide()

                }
                is NetworkResults.Error -> {
                    Toast.makeText(this,"err", Toast.LENGTH_SHORT).show()

                    Toast.makeText(this, result.exception.message.toString(), Toast.LENGTH_SHORT).show()
                    binding.progressBardDataSend.hide()

                    result.exception.printStackTrace()
                }
            }
        }

    }






fun  sendKeyNumber(){
val key_num = binding.keyNumtxt.text
    val loc_desc = binding.locationDescription.text

    if (key_num.isNullOrEmpty() ){
        Toast.makeText(this, resources.getString(R.string.valdateKey), Toast.LENGTH_SHORT).show()
        binding.sendBtn.show()
        binding.progressBarTrip.hide()
    }else {
        rideViewModel.sendKey(key_num.toString(),loc_desc.toString())

        rideViewModel.getKeyNumber().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.msg.status == 1) {

                        val intent = Intent(this, HomeActivity::class.java);


                        startActivity(intent);
                        finishAffinity()
                        finish()



                        Toast.makeText(this, result.data.msg.message, Toast.LENGTH_SHORT).show()


                    }
                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                }
            }
        }

    }

}



    }