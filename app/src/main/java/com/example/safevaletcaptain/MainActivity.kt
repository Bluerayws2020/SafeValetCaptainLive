package com.example.safevaletcaptain

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import com.example.safevaletcaptain.databinding.ActivityMainBinding
import com.example.safevaletcaptain.helpers.ContextWrapper
import com.example.safevaletcaptain.helpers.HelperUtils
import com.example.safevaletcaptain.helpers.HelperUtils.SHARED_PREF
import java.util.*


class MainActivity : AppCompatActivity() , View.OnClickListener   {

    private lateinit var binding: ActivityMainBinding
    private var navController: NavController? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        HelperUtils.setDefaultLanguage(this,HelperUtils.getLang(this))
        supportActionBar?.hide()




//        check if uid != null

        val shared = getSharedPreferences(SHARED_PREF, MODE_PRIVATE)
        val uid = shared.getString("uid", "")

        Log.d("Clint UID ",uid.toString())
        if (!uid.isNullOrEmpty()) {

            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }, 3000)


        }else{
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }, 3000)

        }



    }


    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }
    override fun attachBaseContext(newBase: Context?) {
        val lang = HelperUtils.getLang(newBase!!)
        val local = Locale(lang)
        val newContext = ContextWrapper.wrap(newBase, local)
        super.attachBaseContext(newContext)
    }
}