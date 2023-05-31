package com.example.safevaletcaptain

import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.safevaletcaptain.adapters.MenuAdapter
import com.example.safevaletcaptain.adapters.OnMenuListener
import com.example.safevaletcaptain.adapters.RideAdabter
import com.example.safevaletcaptain.databinding.ActivityHomeBinding
import com.example.safevaletcaptain.databinding.CustomesidemenuBinding
import com.example.safevaletcaptain.fragments.HistoryFragment
import com.example.safevaletcaptain.fragments.LanguageFragment
import com.example.safevaletcaptain.fragments.Notfication
import com.example.safevaletcaptain.helpers.ContextWrapper
import com.example.safevaletcaptain.helpers.HelperUtils
import com.example.safevaletcaptain.helpers.ViewUtils.hide
import com.example.safevaletcaptain.helpers.ViewUtils.show
import com.example.safevaletcaptain.model.NetworkResults
import com.example.safevaletcaptain.viewmodel.DriverViewModel
import com.example.safevaletcaptain.viewmodel.RideViewModel
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import retrofit2.HttpException
import java.util.*


class HomeActivity: AppCompatActivity() {
    init {
        d("ayham","HomeActivity")
    }
    private lateinit var binding: ActivityHomeBinding
    private var navController: NavController? = null
    private lateinit var appBarConfiguration: AppBarConfiguration
    private var rideAdapter: RideAdabter? = null
    private val rideViewModel by viewModels<RideViewModel>()
    private val language = "ar"
    private val userID by lazy { HelperUtils.getUID(applicationContext) }
    private var notificationCounterBinding: CustomesidemenuBinding? = null
    private var listMenuName: ArrayList<String>? = null
    private val driverVM by viewModels<DriverViewModel>()
    private var adapter: MenuAdapter? = null


    private var phoneNumber: String = ""
//    private val deviceId = HelperUtils.getAndroidID(application.applicationContext)

    companion object {
        var comeFromRegister = 0
        var Lang:String = ""
    }
    lateinit var mdiloag: Dialog
    var mpopup: PopupWindow? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

//        val toolbar = findViewById(R.id.toolbar) as Toolbar?
//        setSupportActionBar(toolbar)

        HelperUtils.setDefaultLanguage(this,HelperUtils.getLang(this))

        supportActionBar?.hide();//Ocultar ActivityBar anterior
        binding.navHostFragment.setOnClickListener{
            binding.menuRecycler.hide()

        }

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        HelperUtils.setDefaultLanguage(this,HelperUtils.getLang(this))
        driverVM.getLoginWithOtpResponse().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.status.status == 1) {


                        binding.name.text = result.data.driver.name.toString()
                        binding.phoneMobile.text = result.data.driver.phone.toString()

                        Log.i("driver", "onCreate: " + result.data.driver.driver_image.toString())

                        val sharedPreferences = getSharedPreferences(HelperUtils.SHARED_PREF, MODE_PRIVATE)
                        sharedPreferences?.edit()?.putString("manager", result.data.driver.manager.toString())?.apply()

                        Glide.with(applicationContext)
                            .load(result.data.driver.driver_image)
                            .placeholder(R.drawable.logocaptin)
                            .error(R.drawable.logocaptin)
                            .into(binding.headerIvProfile)
                    }

                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                }
            }
        }


        logout()
        foreignTrip()
        sendNotificationFoeAllUsers()



        val shared = getSharedPreferences(HelperUtils.SHARED_PREF, MODE_PRIVATE)
        val phone = shared.getString("phone", null)
        val driverImg = shared.getString("driver_image", null)
        val userName = shared.getString("user_type", null)


        binding.name.text = userName
        binding.phoneMobile.text = phone


        Glide.with(applicationContext)
            .load(driverImg)
            .placeholder(R.drawable.logocaptin)
            .error(R.drawable.logocaptin)
            .into(binding.headerIvProfile)

//        Log.i("Otp", "onCreate: $otpCode")



//        driverVM.driverLogin(
//            phone.toString(),
//            HelperUtils.getAndroidID(application.applicationContext)
//        )

//        binding.toolbarInclude.menuNotfication.setOnClickListener {
//            driverVM.driverLoginWithOtp(phone.toString()
//                , otpCode.toString()
//                , HelperUtils.getAndroidID(applicationContext)
//            )

//            Toast.makeText(this@HomeActivity, "lol", Toast.LENGTH_SHORT).show()
//        }


        binding.toolbarInclude.notficationBtn.setOnClickListener {

            startActivity(Intent(this, Notfication::class.java))

        }


        listMenuName = if (HelperUtils.getLang(this) == "ar") {
            arrayListOf<String>(
                "السجل",
                "مشاركة",
                "الاشعارات",
                "اللغة",
                "الرحلات الخارجية",
                "كيفية الاستعمال",

                "تسجيل خروج"
            )

        } else {
            arrayListOf<String>(
                " History",
                " Share",
                " Notifications",
                " Language",
                " Foreign Trips",
                "How To Use",

                " Logout"
            )
//            " Call All Car",
//            "استدعاء جميع السيارات",


        }

//        val listMenuName = arrayListOf<String>("My Car","History", "Share", "My Profile", "Notifications", "Language", "Logout")
        val listMenuImage = arrayListOf<Int>(
            R.drawable.hhistory,
            R.drawable.sshare,
            R.drawable.bbell,
            R.drawable.ttranslating,
            R.drawable.ttraficc,
            R.drawable.info,

            R.drawable.llogout
        )
//        R.drawable.pprotection,


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController!!.graph)

        binding.toolbarInclude.toolbar.title = resources.getString(R.string.home)


        binding.toolbarInclude.menuNotfication.setOnClickListener {
            binding.menuRecycler.show()

        }

        binding.close.setOnClickListener {
            binding.menuRecycler.hide()
        }

        val manager = shared.getString("manager", null)
        Log.d("Manager", "onCreate: $manager")


//        if(manager == "0"){

//        listMenuName!!.removeAt(5)
//        listMenuImage.removeAt(5)




        adapter = MenuAdapter(this, listMenuName!!, listMenuImage, object : OnMenuListener {
            override fun onEvent(position: Int) {
                when (position) {
                    0 -> {
                        startActivity(Intent(this@HomeActivity, HistoryFragment::class.java))
                        binding.menuRecycler.hide()

                    }
                    1 -> {

                        val intent = Intent()
                        intent.action = Intent.ACTION_SEND
                        intent.putExtra(
                            Intent.EXTRA_TEXT,
                            "https://play.google.com/store/apps/details?id=com.blueray.valet_driver"
                        )
                        intent.type = "text/plain"
                        startActivity(Intent.createChooser(intent, "Share To:"))

                        binding.menuRecycler.hide()

                    }
                    2 -> {

                        startActivity(Intent(applicationContext, Notfication::class.java))
                        binding.menuRecycler.hide()

                    }
                    3 -> {

                        startActivity(Intent(this@HomeActivity, LanguageFragment::class.java))
                        binding.menuRecycler.hide()

                    }

                    4 -> {

                        val options = ScanOptions()
                        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
                        options.setCameraId(0) // Use a specific camera of the device
                        options.setBeepEnabled(false)
                        options.setOrientationLocked(true)
                        options.setBarcodeImageEnabled(true)
                        barcodeLauncher.launch(options)

                    }

                    5 -> {
                        intent = Intent(this@HomeActivity,WebViewPage::class.java)
                        intent.putExtra("link","https://valet-jo.com/how-to-use")
                        startActivity(intent)
                        binding.menuRecycler.hide()
                    }

                    6 -> {
                        logoutFunction()
                        driverVM.logoutUser(userID)

                    }
                }
            }

        })
//    }

        if(manager == "1"){
            adapter = MenuAdapter(this, listMenuName!!, listMenuImage, object : OnMenuListener {
                override fun onEvent(position: Int) {
                    when (position) {
                        0 -> {
                            startActivity(Intent(this@HomeActivity, HistoryFragment::class.java))
                            binding.menuRecycler.hide()

                        }
                        1 -> {

                            val intent = Intent()
                            intent.action = Intent.ACTION_SEND
                            intent.putExtra(
                                Intent.EXTRA_TEXT,
                                "https://valetjo.com/SV.html"
                            )
                            intent.type = "text/plain"
                            startActivity(Intent.createChooser(intent, "Share To:"))

                            binding.menuRecycler.hide()

                        }
                        2 -> {

                            startActivity(Intent(applicationContext, Notfication::class.java))
                            binding.menuRecycler.hide()

                        }
                        3 -> {

                            startActivity(Intent(this@HomeActivity, LanguageFragment::class.java))
                            binding.menuRecycler.hide()

                        }

                        4 -> {

                            val options = ScanOptions()
                            options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
                            options.setCameraId(0) // Use a specific camera of the device
                            options.setBeepEnabled(false)
                            options.setOrientationLocked(true)
                            options.setBarcodeImageEnabled(true)
                            barcodeLauncher.launch(options)

                        }

                        5 -> {

                            if(manager == "1"){
                                rideViewModel.sendNotificationForAll("6")


                            }


                        }

                        6 -> {
                            val intent = Intent(this@HomeActivity,WebViewPage::class.java)
                            val link = "https://valet-jo.com/how-to-use"
                           intent.putExtra("link",link)

                            binding.menuRecycler.hide()
                        }
                        7 -> {
                            driverVM.logoutUser(userID)

                        }

                    }
                }

            })
        }


        binding.navigationView.adapter = adapter

        val layoutManager = LinearLayoutManager(this)
        binding.navigationView.layoutManager = layoutManager


    }


    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController!!, appBarConfiguration)
    }


    // Register the launcher and result handler
    private val barcodeLauncher = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents == null) {
            Toast.makeText(applicationContext, "Cancelled", Toast.LENGTH_LONG).show()
        } else {
            rideViewModel.startForeignRide(result.contents.toString())


        }
    }


    private fun showCustomePopUp(msg:String){
        val popUpView: View = layoutInflater.inflate(
            R.layout.foreign_trips_popup_window,
            null
        )

        mpopup = PopupWindow(
            popUpView, ActionBar.LayoutParams.FILL_PARENT,
            ActionBar.LayoutParams.WRAP_CONTENT, true
        ) // Creation of popup

        mpopup!!.animationStyle = android.R.style.Animation_Dialog
        mpopup!!.showAtLocation(popUpView, Gravity.CENTER, 0, 0) // Displaying popup
        val carName = popUpView.findViewById<View>(R.id.carName) as TextView

        carName.text = msg.toString()

        val doneBtn: Button = popUpView.findViewById<View>(R.id.doneBtn) as Button
        doneBtn.setOnClickListener{
            mpopup!!.dismiss()
        }

    }


    private fun showCustomSendNotPopUp(msg:String){
        val popUpView2: View = layoutInflater.inflate(
            R.layout.send_notification_popup_window,
            null
        )

        mpopup = PopupWindow(
            popUpView2, ActionBar.LayoutParams.FILL_PARENT,
            ActionBar.LayoutParams.WRAP_CONTENT, true
        ) // Creation of popup

        mpopup!!.animationStyle = android.R.style.Animation_Dialog
        mpopup!!.showAtLocation(popUpView2, Gravity.CENTER, 0, 0) // Displaying popup
        val carName2 = popUpView2.findViewById<View>(R.id.carName) as TextView

        carName2.text = msg.toString()

        val doneBtn2: Button = popUpView2.findViewById<View>(R.id.doneBtn) as Button
        doneBtn2.setOnClickListener{
            mpopup!!.dismiss()
        }

    }



    private fun logout() {

        driverVM.getCustomerLogoutResponse().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.msg.status == 1) {
                        logoutFunction()
                        Toast.makeText(
                            this@HomeActivity,
                            result.data.msg.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    if (result.exception is HttpException)
                        Log.e("TAG", "onViewCreated: ${result.exception}")
                    else
                        Log.e("TAG", "onViewCreated: ERROR")
                }
                else -> {}
            }

        }

    }


    private fun logoutFunction() {
        val sharedPreferences = getSharedPreferences(HelperUtils.SHARED_PREF, MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
        val intentSplash = Intent(this, MainActivity::class.java)
        startActivity(intentSplash)
        finish()
    }




    private fun sendNotificationFoeAllUsers(){

        rideViewModel.sendNotificationForAllResponse().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.msg.status == 1) {
                        showCustomSendNotPopUp(result.data.msg.message)
                    }

                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    if (result.exception is HttpException)
                        Log.e("TAG", "onViewCreated: ${result.exception}")
                    else
                        Log.e("TAG", "onViewCreated: ERROR AYA")
                }
                else -> {}
            }

        }

    }


    //Foreign Trip Call
    private fun foreignTrip()
    {
        rideViewModel.startForeignRideResponse().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {

                    if (result.data.msg.status == 1) {

                        showCustomePopUp(
                            result.data.msg.message.toString())
                    }

                    else if(result.data.msg.message.contains("Return Car Successfully Started")){
                        showCustomePopUp(
                            result.data.msg.message.toString())
                    }

//                        Toast.makeText(applicationContext, result.data.msg.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                }
            }
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        val lang = HelperUtils.getLang(newBase!!)
        val local = Locale(lang)
        val newContext = ContextWrapper.wrap(newBase, local)
        super.attachBaseContext(newContext)
    }



}

























//        setSupportActionBar(binding.toolbar); //HERE'S THE PROBLEM !!!!


//        appBarConfiguration = AppBarConfiguration(navController!!.graph)
//        NavigationUI.setupActionBarWithNavController(this, navController!!, appBarConfiguration)
//        NavigationUI.setupWithNavController(binding.navigationView, navController!!)

//        binding.navigationView.setNavigationItemSelectedListener(this)


//        binding.toolbar.title = resources.getString(R.string.home)

//        binding.menuNotfication.setOnClickListener {
//
//            val navDrawer: DrawerLayout = binding.drawerLayout
//
//            if (!navDrawer.isDrawerOpen(GravityCompat.START))
//                navDrawer.openDrawer(GravityCompat.START)
//            else navDrawer.closeDrawer(
//                GravityCompat.START
//            )
//        }



//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        TODO("Not yet implemented")
//    }
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.toolbar, menu)
//
//        notificationCounterBinding =
//            CustomesidemenuBinding.bind(menu.findItem(R.id.notficaionItemsToolbar).actionView)
//
//
//        notificationCounterBinding?.notificationParent?.setOnClickListener {
//
//navController?.navigate(R.id.action_homeFragment_to_notfication)
//
//
//
//        }


//        notificationCounterBinding =
//            CustomesidemenuBinding.bind(menu.findItem(R.id.menuToolbars).actionView)
//
//        notificationCounterBinding?.menus?.setOnClickListener {
//
//            val navDrawer: DrawerLayout = binding.drawerLayout
////
//            if (!navDrawer.isDrawerOpen(GravityCompat.START))
//                navDrawer.openDrawer(GravityCompat.START)
//            else navDrawer.closeDrawer(
//                GravityCompat.START
//            )
//
//
//        }
//        return true
//    }




//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        binding.drawerLayout.closeDrawer(GravityCompat.START)
//        when (item.itemId) {
//
////
//            R.id.history -> {
//                startActivity(Intent(this,HistoryFragment::class.java))
//                return true
//            }
//
//            R.id.language -> {
//
//startActivity(Intent(this,LanguageFragment::class.java))
//                return true
//            }
//            R.id.logout -> {
//                logout()
//            }
//            R.id.share -> {
//                val intent= Intent()
//            intent.action=Intent.ACTION_SEND
//                    intent.putExtra(Intent.EXTRA_TEXT,"https://play.google.com/store/apps/details?id=com.blueray.valet_driver")
//                intent.type="text/plain"
//            startActivity(Intent.createChooser(intent,"Share To:"))
//
//            }
//
//            else -> {
//                return false
//            }
//        }
//        return true
//
//    }

