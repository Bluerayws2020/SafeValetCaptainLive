package com.example.safevaletcaptain.fragments

import android.app.ActionBar
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.example.safevaletcaptain.HomeActivity
import com.example.safevaletcaptain.R
import com.example.safevaletcaptain.adapters.CarAdapter
import com.example.safevaletcaptain.databinding.PickUpTheCarBinding
import com.example.safevaletcaptain.helpers.HelperUtils
import com.example.safevaletcaptain.helpers.ViewUtils.hide
import com.example.safevaletcaptain.helpers.ViewUtils.show
import com.example.safevaletcaptain.model.NetworkResults
import com.example.safevaletcaptain.viewmodel.RideViewModel
import com.google.android.material.card.MaterialCardView


class PickingCarFragment: AppCompatActivity() {

    init {
        d("ayham","PickingCarFragment")
    }

    private var navController: NavController? = null
    private var carAdapter: CarAdapter? = null
    var mpopup: PopupWindow? = null
    var ride_id: String = ""
    var lat: String = ""
    var lon: String = ""
    var image: String = ""

    var carNumber: String = ""
    var keyNumber: String = ""

    private val rideViewModel by viewModels<RideViewModel>()


    private lateinit var binding: PickUpTheCarBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PickUpTheCarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarInclude.toolbar.title = resources.getString(R.string.pick_up_the_car)
        startBack()
        HelperUtils.setDefaultLanguage(this,HelperUtils.getLang(this))

//            ride info
        rideViewModel.getdriverCustomerInfo()

        getDataDriver()
//            Log.d("ride_id",intent?.getStringExtra("ride_ida").toString())
        binding.whiteMyCar.text = intent?.getStringExtra("car_number").toString()

        ride_id = intent?.getStringExtra("rideId").toString()
        lat = intent?.getStringExtra("lat").toString()
        lon = intent?.getStringExtra("lon").toString()
            image = intent?.getStringExtra("image").toString()


        val keyNumber = intent?.getStringExtra("key_number").toString()
        binding.keyNum.text = keyNumber

        if (keyNumber.equals(null))
            binding.keyNum.text = "-"

val carImage =  image
Glide.with(applicationContext)
    .load(carImage)
    .placeholder(R.drawable.logocaptin)
    .error(R.drawable.logocaptin)
    .into(binding.carImg)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        HelperUtils.setDefaultLanguage(this,HelperUtils.getLang(this))


        binding.toolbarInclude.notficationBtn.setOnClickListener {


            startActivity(Intent(this, Notfication::class.java))

        }

        if (HelperUtils.getLang(this) == "ar") {
            binding.toolbarInclude.menuNotfication.setImageDrawable(resources.getDrawable(R.drawable.ar_back))

        } else {
            binding.toolbarInclude.menuNotfication.setImageDrawable(resources.getDrawable(R.drawable.back))

        }

        binding.toolbarInclude.menuNotfication.setOnClickListener {
            onBackPressed()
        }



        binding.driveBtn.setOnClickListener {
            binding.progressBar.show()
//            rideViewModel.getcarBack(ride_id)
//            takeOrderBack()
            rideViewModel.startback("1")

        }

        binding.direction.setOnClickListener{
Log.d("LATTT",lat)
            Log.d("LATTT",lon)

            if (lat.isNotEmpty() && lon.isNotEmpty()) {

//
//                val uri =  "http://maps.google.com/maps?q=loc:$lat,$lon"
//                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
//                intent.setPackage("com.google.android.apps.maps")
//                startActivity(intent)

                val navigationIntentUri = Uri.parse("google.navigation:q=" + lat + "," + lon)
                val mapIntent = Intent(Intent.ACTION_VIEW, navigationIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            }
            else{
                toast("We Cant Find Direction ")
            }
        }

        notficationSend()
    }


    fun notficationSend(){
        rideViewModel.getNotficationtype().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {

                    Toast.makeText(this, result.data.msg.message, Toast.LENGTH_LONG).show()

                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                }
            }
        }
    }

    fun showCustomePopUp() {
        val popUpView: View = layoutInflater.inflate(
            R.layout.car_back_popup,
            null
        ) // inflating popup layout

        mpopup = PopupWindow(
            popUpView, ActionBar.LayoutParams.FILL_PARENT,
            ActionBar.LayoutParams.WRAP_CONTENT, true
        ) // Creation of popup

        mpopup!!.setAnimationStyle(android.R.style.Animation_Dialog)
        mpopup!!.showAtLocation(popUpView, Gravity.CENTER, 0, 0) // Displaying popup


        val backCarToPark =
            popUpView.findViewById<Button>(R.id.backCarToPark)
        val iamCloseBtn: Button =
            popUpView.findViewById<Button>(R.id.sendNotfitionClose)
//        val iamHereBtn: MaterialCardView =
//            popUpView.findViewById<View>(R.id.sendNotfitionIamHere) as MaterialCardView
        val lineUpCar: Button =
            popUpView.findViewById<Button>(R.id.backCarBtn)

        val containerCarPark: LinearLayout =
            popUpView.findViewById<View>(R.id.containerCarPark) as LinearLayout
        val heretxt: Button = popUpView.findViewById<Button>(R.id.heretxt)
        var count = 0
        heretxt.text = getString(
            R.string.send_notficatio_i_am_here_1,
            count.toString(),
        )

        lineUpCar.setOnClickListener {
            endTrip("1", "1")
        }

        backCarToPark.setOnClickListener {
            sendNotfication(ride_id,"5")
            mpopup!!.dismiss()
//         startActivity(Intent(this,home))
            onBackPressed()
        }

        heretxt.setOnClickListener {
            count++
            heretxt.text = getString(
                R.string.send_notficatio_i_am_here_1,
                count.toString(),
            )

//            if (HelperUtils.getLang(this) == "ar"){
//                heretxt.text  = "Send Notfication I am Here ($count) "
//
//            }else {
//                heretxt.text  = "ارسل اشعار انا هنا ($count)"
//
//            }


//            count++
            print(count)


            if (count == 3) {

                backCarToPark.show()
                containerCarPark.hide()

            }
            sendNotfication(ride_id, count.toString())

        }
        iamCloseBtn.setOnClickListener {
            rideViewModel.sendNotifcation(ride_id, "4")
            Toast.makeText(this, ride_id, Toast.LENGTH_LONG).show()

        }


    }


    fun sendNotfication(ride_id: String, type: String) {
        rideViewModel.sendNotifcation(ride_id, type)
    }

    fun startBack() {

        rideViewModel.getStartBack().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {

                    if (result.data.msg.newStatus == 4) {

                        Toast.makeText(this, result.data.msg.message, Toast.LENGTH_LONG)
                            .show()
                        showCustomePopUp()

                    }else if ((result.data.msg.newStatus == 5) ) {
                        Toast.makeText(this, result.data.msg.message, Toast.LENGTH_LONG)
                            .show()
                        showCustomePopUp()
                    }

                    binding.progressBar.hide()


                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    binding.progressBar.hide()

                }
            }
        }

    }


    fun endTrip(delivered: String, backStart: String) {
        rideViewModel.endTripBackLiveData(delivered, backStart)

        rideViewModel.getEndTrip().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.msg.status == 1) {

                        Toast.makeText(this, result.data.msg.message, Toast.LENGTH_LONG).show()
                        val intent = Intent(this, HomeActivity::class.java);

                        startActivity(intent);
                        finishAffinity()
                        finish()
                    }
                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                }
            }
        }

    }


    fun getDataDriver() {

        rideViewModel.driverWithCustomerInfo().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.msg.newStatus == 5) {
                        binding.keyNum.text = result.data.keyNumber.toString()
                        binding.carModel.text = result.data.customerInfo?.carNumber
                        lat = result.data.lat.toString()
                        lon = result.data.lon.toString()

                        ride_id =result.data.customerInfo?.rideId.toString()

                    }
                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                }
            }
        }

    }


    //takeCar Back
    fun takeOrderBack() {
        binding.progressBar.show()
        rideViewModel.getTakeCarBack().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    binding.progressBar.hide()

                    if (result.data.msg.status == 1) {
//                        ride back
                        rideViewModel.startback("1")

                        toast(result.data.msg.message.toString())
//                        startActivity(intent)
                    }else {
                        toast(result.data.msg.message.toString())
                        binding.progressBar.hide()

                    }

                }
                is NetworkResults.Error -> {
                    binding.progressBar.hide()

                    result.exception.printStackTrace()
                }
            }
        }


    }
    fun toast(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

}