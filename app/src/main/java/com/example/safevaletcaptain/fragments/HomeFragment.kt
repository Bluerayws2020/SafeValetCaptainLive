package com.example.safevaletcaptain.fragments

import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.view.isEmpty
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.safevaletcaptain.R
import com.example.safevaletcaptain.adapters.RideAdabter
import com.example.safevaletcaptain.adapters.StationAdabter
import com.example.safevaletcaptain.adapters.TakeOrder
import com.example.safevaletcaptain.databinding.FragmentHomeBinding
import com.example.safevaletcaptain.helpers.HelperUtils
import com.example.safevaletcaptain.helpers.ViewUtils.hide
import com.example.safevaletcaptain.helpers.ViewUtils.show
import com.example.safevaletcaptain.model.NetworkResults
import com.example.safevaletcaptain.model.RideModel
import com.example.safevaletcaptain.model.StationsName
import com.example.safevaletcaptain.viewmodel.RideViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
//import com.google.android.gms.location.FusedLocationProviderClient
//import com.huawei.hms.location.FusedLocationProviderClient
//import com.huawei.hms.location.LocationServices
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import com.onesignal.OneSignal


class HomeFragment: BaseFragment<FragmentHomeBinding>(), View.OnClickListener,
    AdapterView.OnItemSelectedListener {
    private lateinit var locationManager: LocationManager
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    companion object {
        private const val PERMISSIONS_REQUEST_LOCATION = 1
    }
    private val locationPermissionCode = 2
    private val stationListItem = mutableListOf<StationsName>()
    private val stationNameList = mutableListOf<String>()

    private var navController: NavController? = null
    private var res: String? = null
    private var rideAdapter: RideAdabter? = null
    private val rideViewModel by viewModels<RideViewModel>()
    private var latLocation: String? = ""
    private var longLocation: String? = ""
    private var statAdapter: StationAdabter? = null
    val handler = Handler()

    var driverActive:String? = ""
    private var station_id:String = ""

    private var start_ride_response :String = "None"

    lateinit var mdiloag: Dialog
    var mpopup: PopupWindow? = null

    private val myDialog: Dialog by lazy {
        Dialog(requireContext())
    }


    var rideId:String = ""
    var keyNumberStr:String = ""
    var carNameStr:String = ""
    var  progressBar:ProgressBar? = null

    override fun onStart() {
        super.onStart()
        rideViewModel.getAvalibleRide()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        rideViewModel.getAvalibleRide()

    }
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.noDatatxt.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        HelperUtils.setDefaultLanguage(requireContext(),HelperUtils.getLang(context))

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context?.applicationContext!!)


//        to get availble ride when notfication comming
        oneSignalShowNotfication()
        driverWithCustomer()

        rideViewModel.getAvalibleRide()


// refresh
        binding.swipeToRefresh.setOnRefreshListener {
            binding.carItems.adapter = null
            binding.progressBarCate.show()
            binding.noDatatxt.hide()
            rideViewModel.getAvalibleRide()
            rideViewModel.getdriverCustomerStatus()

        }
        rideViewModel.getAvalibleRide()

//        stope&start Work
        getDataDriver()
        rideViewModel.getdriverCustomerStatus()
//
//        binding.onOff.setOnCheckedChangeListener{
//
//
//            if (driverActive == "1"){
//                stopeWork()
//            }else
//            {
//                startOfWork()
//            }
//
//
//
//
//        }





        binding.onOff.setOnCheckedChangeListener { _, isChecked ->
            if ( driverActive == "1") {
                stopeWork()


            } else if (driverActive == "0"){

                startOfWork()


            }

        }





//        uncompletd Trip
        checkUnCompletedTrip()


//        rideViewModel.getdriverCustomerInfo()


//call location mathod
        isLocationPermissionGranted()

//        call main Recycler Api
        getAllAvalibeRide()

//take Car Back
//        takeOrderBack()

//stope Work
        driverStartWork()

        binding.showMyQRImg.setOnClickListener(this)



    }



    // Launch
    fun onButtonClick(view: View?) {
        barcodeLauncher.launch(ScanOptions())
    }
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.showMyQRImg -> {
//
                val options = ScanOptions()
                options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
                options.setCameraId(0) // Use a specific camera of the device
                options.setBeepEnabled(false)
                options.setOrientationLocked(true)
                options.setBarcodeImageEnabled(true)
                barcodeLauncher.launch(options)




            }


        }
    }

    fun getAllAvalibeRide(){
        binding.progressBarCate.show()

//        call api
        rideViewModel.getAvalibleRideResponse().observe(viewLifecycleOwner) { result ->
            binding.swipeToRefresh.isRefreshing = false
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.msg.status == 1) {

                        setupRecyclerView(result.data.ride_data.myRides)
                        binding.progressBarCate.hide()

                        if(result.data.ride_data.myRides.isEmpty()){
                            binding.noDatatxt.show()

                        }

                    }

                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                }
            }
        }


    }




    // Register the launcher and result handler
    private val barcodeLauncher = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents == null) {
            Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show()
        } else {
//            start Ride

//            Toast.makeText(context, "Scanned: " + result.contents, Toast.LENGTH_LONG)
//                .show()

//            call Start Ride Api




            rideViewModel.startRideCall().observe(viewLifecycleOwner) { result ->
                when (result) {
                    is NetworkResults.Success -> {
                        if (result.data.msg.status == 1) {

//                            showCustomePopUp( result.data.customerInfo?.carNickName.toString(),result.data.customerInfo?.carNumber.toString(),rideId,myDialog)
                            rideViewModel.getdriverCustomerInfo()
                            driverWithCustomer()

                        }


                        toast(result.data.msg.message .toString())
                    }
                    is NetworkResults.Error -> {
                        result.exception.printStackTrace()
                    }
                }
            }






            rideViewModel.startRide(result.contents.toString(),latLocation.toString(),longLocation.toString())
Log.d("DTTTTTTTA",result.contents.toString() + latLocation.toString() + longLocation.toString() )




        }
    }






    //    permtion location
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }




//    permetion of Location

    private fun isLocationPermissionGranted(): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                101
            )
            false
        } else {




            fusedLocationClient.lastLocation
                .addOnSuccessListener { location : Location? ->
                    Log.d("Locatiooon!",location.toString())
                    Log.d("Locatiooon!!",location?.latitude.toString())
                    Log.d("Locatiooon!!!",location?.longitude.toString())

                    if (location?.longitude.toString() != "" && location?.latitude.toString() != "") {

                        Log.d("Locatiooon!",location.toString())
                        Log.d("Locatiooon!!",location?.latitude.toString())
                        Log.d("Locatiooon!!!",location?.longitude.toString())
                        latLocation = location?.latitude.toString()
                        longLocation = location?.longitude.toString()

                    }





                }


            true
        }
    }

    //setup Recycler View
    fun setupRecyclerView(ride:List<RideModel>){

        rideAdapter = RideAdabter(object : TakeOrder{

            override fun takeOrder(ride_id: String, key_Number: String, car_number: String,pd: ProgressBar) {
//call take car api
                rideViewModel.getcarBack(ride_id)
                takeOrderBack()


                carNameStr = car_number
                rideId = ride_id
                keyNumberStr = key_Number
                progressBar = pd
            }


        },ride, requireContext())
        val layoutManager = LinearLayoutManager(context)
        binding.carItems.layoutManager = layoutManager
        binding.carItems.adapter = rideAdapter

    }

    //takeCar Back
    fun takeOrderBack() {
        binding.progressBarCate.show()
        rideViewModel.getTakeCarBack().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    binding.progressBarCate.hide()

                    if (result.data.msg.status == 1) {
//                        ride back
                        toast(result.data.msg.message.toString())


                        val intent = Intent(context,PickingCarFragment::class.java)
                            intent.putExtra("rideId",rideId)
                            intent.putExtra("car_number", carNameStr)
                            intent.putExtra("key_number",keyNumberStr)
                            intent.putExtra("lat",result.data.takeCarmodel.lat)
                            intent.putExtra("lon",result.data.takeCarmodel.lon)
                            intent.putExtra("image",result.data.takeCarmodel.image)
Log.d("TTTTTA",result.data.takeCarmodel.toString())

                startActivity(intent)








//                        startActivity(intent)
                    }else {
                        toast(result.data.msg.message.toString())
                        binding.progressBarCate.hide()

                    }

                }
                is NetworkResults.Error -> {
                    binding.progressBarCate.hide()

                    result.exception.printStackTrace()
                }
            }
        }


    }






//
//car Information Confirm
//    fun showCustomePopUp(carNameStr:String,carNumberStr:String,rideId:String){
//        val popUpView: View = layoutInflater.inflate(
//            R.layout.action_customp_popup,
//            null
//        ) // inflating popup layout
//        mpopup = PopupWindow(
//            popUpView, ActionBar.LayoutParams.FILL_PARENT,
//            ActionBar.LayoutParams.WRAP_CONTENT, true
//        ) // Creation of popup
//
//        mpopup!!.setAnimationStyle(android.R.style.Animation_Dialog)
//        mpopup!!.showAtLocation(popUpView, Gravity.CENTER, 0, 0) // Displaying popup
//        val carName = popUpView.findViewById<View>(R.id.carName) as TextView
//        val carNumber = popUpView.findViewById<View>(R.id.carNumber) as TextView
//
//        val carButton = popUpView.findViewById<View>(R.id.carNumber) as TextView
//
//        carName.text = carNameStr
//        carNumber.text = carNumberStr
//
//        val doneBtn: Button = popUpView.findViewById<View>(R.id.doneBtn) as Button
//        doneBtn.setOnClickListener{
//            mpopup!!.dismiss()
////    call Driver Status api with Action
//
//            confirmRideAction()
//
//        }
//
//    }
    fun showCustomePopUp(carNameStr:String,carNumberStr:String,rideId:String) {
        val dialogBinding = layoutInflater.inflate(R.layout.action_customp_popup, null)

        myDialog.setContentView(dialogBinding)


        myDialog.setCancelable(true)
        myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myDialog.show()
        val carName = dialogBinding.findViewById<TextView>(R.id.carName) as TextView
        val carNumber = dialogBinding.findViewById<TextView>(R.id.carNumber) as TextView

        val carButton = dialogBinding.findViewById<TextView>(R.id.carNumber) as TextView

        carName.text = carNameStr
        carNumber.text = carNumberStr

        val doneBtn: TextView = dialogBinding.findViewById<TextView>(R.id.doneBtnaction) as TextView
        doneBtn.setOnClickListener{
//            mpopup!!.dismiss()
//    call Driver Status api with Action
            rideViewModel.getconfiremRide()
            confirmRideAction()


myDialog.dismiss()

        }


    }


//    Driver With Customer Info Firest Request On API

    fun  driverWithCustomer(){
//
        rideViewModel.driverWithCustomerInfo().observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.msg.newStatus == 1  && result.data.msg.status == 2) {

//showCustomePop
//                        toast(result.data.msg.status.toString() + "HERRRRR" .toString())


                    }

                        showCustomePopUp( result.data.customerInfo?.carNickName.toString(),result.data.customerInfo?.carNumber.toString(),rideId)

//                    toast(result.data.msg.message .toString())
                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                }
            }
        }




    }

//    Driver With Customer Info Firest Request On API

    fun  checkUnCompletedTrip(){

        rideViewModel.driverWithCustomerInfo().observe(viewLifecycleOwner) { result ->
            when (result) {

                is NetworkResults.Success -> {
                    val mlang  = HelperUtils.getLang(context)


//                    check if user dose not complete driver parck

                    var msg = ""
                    if (mlang == "ar"){
                        msg = "تأكيد معلومات السيارة"

                    }else {
                        msg =  "Car information confirmation"

                    }

//confirm Informationn
                    if (result.data.msg.newStatus == 1 &&  result.data.msg.message == msg) {

                        binding.unCompletedTrip.show()

                        binding.unCompletedTrip.setOnClickListener{
                            toast(result.data.msg.message)

//                            val intent = Intent(context,TripInProgressFragment::class.java);
//                            intent.putExtra("ride_id", result.data.customerInfo?.rideId)
//                            intent.putExtra("carNumber", result.data.customerInfo?.carNumber)
//
//                            intent.putExtra("lat",latLocation)
//                            intent.putExtra("lon", longLocation)
//
//                            context?.startActivity(intent);



                                showCustomePopUp(
                                    result.data.customerInfo?.carNickName.toString(),
                                    result.data.customerInfo?.carNumber.toString(),
                                    rideId
                                )

                        }

                    }
//                        uncompleted Ride

                     if (result.data.msg.newStatus == 2 || result.data.msg.newStatus == 3) {
                        toast(result.data.msg.newStatus.toString())

                        binding.unCompletedTrip.show()

                        binding.unCompletedTrip.setOnClickListener{
toast(result.data.msg.message)

                            val intent = Intent(context,TripInProgressFragment::class.java)
                            intent.putExtra("ride_id", result.data.customerInfo?.rideId)
                            intent.putExtra("carNumber", result.data.customerInfo?.carNumber)

                            intent.putExtra("lat",latLocation)
                            intent.putExtra("lon", longLocation)

                            context?.startActivity(intent)
                        }

                    }
//                    pick car from park
                     if (result.data.msg.newStatus == 4 ){
                         binding.unCompletedTrip.show()

                         binding.unCompletedTrip.setOnClickListener {

                            val intent = Intent(context, PickingCarFragment::class.java)
                             intent.putExtra("ride_id", result.data.customerInfo?.rideId)
                            intent.putExtra("carNumber", result.data.customerInfo?.carNumber)

                            intent.putExtra("lat", latLocation)
                            intent.putExtra("lon", longLocation)

                            context?.startActivity(intent)
                         }

                    }


//                  check if user dose Not Complete Car Back
                    var msgUncompleteCarBackk = ""
                    if (mlang ==  "ar"){
                        msgUncompleteCarBackk = "السيارة قيد التسليم"
                    }else {
                        msgUncompleteCarBackk = "The car is under delivery to the customer"
                    }

                    if (result.data.msg.status == 2  && result.data.msg.message == msgUncompleteCarBackk) {

                        binding.unCompletedTrip.show()
                        binding.unCompletedTrip.setOnClickListener{


                            val intent = Intent(context,PickingCarFragment::class.java)
                            intent.putExtra("ride_id", result.data.customerInfo?.rideId)
                            intent.putExtra("car_number", result.data.customerInfo?.carNumber)


                            intent.putExtra("lat",latLocation)
                            intent.putExtra("lon", longLocation)

                            context?.startActivity(intent)
                        }

                    }

                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                }
            }
        }




    }

//Driver Statuts (Confirm Ride )


    fun  confirmRideAction(){

        rideViewModel.getConfiremRideWithActiobn().observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.msg.newStatus == 2 && result.data.msg.status == 2 ) {
//ride Confirmation

                        val intent = Intent(context,TripInProgressFragment::class.java)
                        intent.putExtra("ride_id", result.data.customerInfo?.rideId)
                        intent.putExtra("carNumber", result.data.customerInfo?.carNumber)

                        intent.putExtra("lat",latLocation)
                        intent.putExtra("lon", longLocation)

                        context?.startActivity(intent)

                    }

                    toast(result.data.msg.message .toString())

                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                }
            }
        }




    }


    fun startOfWork(){
//        val popUpView: View = layoutInflater.inflate(
//            R.layout.station,
//            null
//        ) // inflating popup layout


        val popUpView: View = layoutInflater.inflate(
            R.layout.station,
            null
        ) // inflating popup layout

        mpopup = PopupWindow(
            popUpView, ActionBar.LayoutParams.FILL_PARENT,
            ActionBar.LayoutParams.MATCH_PARENT, true
        ) // Creation of popup

        mpopup!!.animationStyle = android.R.style.Animation_Dialog
        mpopup!!.showAtLocation(popUpView, Gravity.CENTER, 0, 0) // Displaying popup
//        val carName = popUpView.findViewById<View>(R.id.carName) as TextView
//        val carNumber = popUpView.findViewById<View>(R.id.carNumber) as TextView
//





        val spinnerStation: Spinner = popUpView.findViewById<View>(R.id.stataionName) as Spinner
        val pd: ProgressBar = popUpView.findViewById<View>(R.id.progressBarStationItem) as ProgressBar
        val saveBtn: Button = popUpView.findViewById<View>(R.id.save_btn) as Button



        val doneBtn: ConstraintLayout = popUpView.findViewById<View>(R.id.dismiss) as ConstraintLayout
        doneBtn.setOnClickListener {
            mpopup!!.dismiss()
//            call Api

stationListItem.clear()


        }
        stationList(spinnerStation,pd)

        saveBtn.setOnClickListener{

            stationListItem.clear()

            if (spinnerStation == null || spinnerStation.isEmpty()){
                toast("You Have Choise The station First")
            }else {
                rideViewModel.driverStartWork(station_id)
                mpopup!!.dismiss()

            }



        }
    }

    fun stopeWork(){
        val popUpView: View = layoutInflater.inflate(
            R.layout.onn_off_popup,
            null
        ) // inflating popup layout

        mpopup = PopupWindow(
            popUpView, ActionBar.LayoutParams.FILL_PARENT,
            ActionBar.LayoutParams.MATCH_PARENT, true
        ) // Creation of popup

        mpopup!!.animationStyle = android.R.style.Animation_Dialog
        mpopup!!.showAtLocation(popUpView, Gravity.CENTER, 0, 0) // Displaying popup
//        val carName = popUpView.findViewById<View>(R.id.carName) as TextView
//        val carNumber = popUpView.findViewById<View>(R.id.carNumber) as TextView
//
//
//
        val cancelBtn: TextView = popUpView.findViewById<View>(R.id.cancelBtn) as TextView
        val doneeBtn: TextView = popUpView.findViewById<View>(R.id.doneeBtn) as TextView

        val dismiss: ConstraintLayout = popUpView.findViewById<View>(R.id.dismis_view) as ConstraintLayout
        dismiss.setOnClickListener{
            mpopup!!.dismiss()

        }


        doneeBtn.setOnClickListener{

            rideViewModel.driverStartWork("")






        }
        cancelBtn.setOnClickListener{
            mpopup!!.dismiss()
        }

    }
//    fun showStationList(){
//        val popUpView: View = layoutInflater.inflate(
//            R.layout.activity_station_list,
//            null
//        ) // inflating popup layout
//
//        mpopup = PopupWindow(
//            popUpView, ActionBar.LayoutParams.FILL_PARENT,
//            ActionBar.LayoutParams.MATCH_PARENT, true
//        ) // Creation of popup
//
//        mpopup!!.setAnimationStyle(android.R.style.Animation_Dialog)
//        mpopup!!.showAtLocation(popUpView, Gravity.CENTER, 0, 0) // Displaying popup
////        val carName = popUpView.findViewById<View>(R.id.carName) as TextView
////        val carNumber = popUpView.findViewById<View>(R.id.carNumber) as TextView
////
////
////
//        val recycler: RecyclerView = popUpView.findViewById<View>(R.id.stationItem) as RecyclerView
//        val pd: ProgressBar = popUpView.findViewById<View>(R.id.progressBarStation) as ProgressBar
//
//
//        stationList(recycler,pd)
//    }






    fun stationList(spinner: Spinner,pd:ProgressBar){
        rideViewModel.stationWork()
        rideViewModel.getStationWork().observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResults.Success -> {

                    if (result.data.msg?.status == 1){
                        pd.hide()

                        stationListItem.clear()
                        stationListItem.addAll(result.data.data.stations)
                        setupProvincesSpinner(stationListItem,spinner)

                        Log.d("ARRRAYA",result.data.data.stations.toString())

                    }

                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                }
            }
        }

    }

    fun spinnerAppenData (station:ArrayList<StationsName>,spinner: Spinner){
        station.forEach {
            stationNameList.addAll(listOf(it.name.toString()))
        }

        val provincesAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            stationNameList,
        )
        provincesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = provincesAdapter




            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
//                    Toast.makeText(requireContext(),
//                        station[position].id, Toast.LENGTH_SHORT).show()
                    station_id =   station[position].id.toString()


                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }





    }

     fun setupProvincesSpinner(provinceList: List<StationsName>,spinner: Spinner) {


        Log.d("ARRStation",stationListItem.toString())
        val provincesAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            provinceList,
        )
         spinner.onItemSelectedListener = object :
             AdapterView.OnItemSelectedListener {
             override fun onItemSelected(parent: AdapterView<*>,
                                         view: View, position: Int, id: Long) {
                 Toast.makeText(requireContext(),
                     provinceList[position].id, Toast.LENGTH_SHORT).show()
                 station_id =   provinceList[position].id.toString()


             }

             override fun onNothingSelected(parent: AdapterView<*>) {
                 // write code to perform some action
             }
         }
        provincesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = provincesAdapter
    }

    fun driverStartWork(){

        rideViewModel.driverStartWork().observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResults.Success -> {

                    if (result.data.msg.status == 1){

                        Toast.makeText(context, result.data.msg.message, Toast.LENGTH_LONG).show()
                        mpopup?.dismiss()
                        rideViewModel.getdriverCustomerStatus()
                    }

                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                }
            }
        }

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

//    station_id = stationList[p2].id.toString()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    fun oneSignalShowNotfication() {
        OneSignal.setNotificationWillShowInForegroundHandler {

            it.notification.additionalData

            if (it.notification.notificationId == "No ID") {

            } else {
                rideViewModel.getAvalibleRide()
            }

        }

    }


    fun getDataDriver() {

//get data Info
        rideViewModel.getDriverWorkOrNot().observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResults.Success -> {




                    binding.onOff.isChecked = result.data.driver_active == "1"





                    driverActive = result.data.driver_active


                    if (result.data.driver_active == "1"){
//stope Woerk
                        binding.carItems.show()

                        binding.showQr.show()
                        binding.showMyQRImg.show()

                        binding.imgSilent.hide()
                        binding.DriverInActiveTxt.hide()



                        binding.title.text = getString(R.string.available_orders)

                    }else {
//        start work

                        binding.imgSilent.show()
                        binding.imgSilent.show()
                        binding.DriverInActiveTxt.show()



                        binding.carItems.hide()
                        binding.noDatatxt.hide()


                        binding.imgSilent.hide()
                        binding.showQr.hide()
                        binding.showMyQRImg.hide()
                        binding.title.text = getString(R.string.offWork)



                    }
                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                }
            }
        }

    }


}


