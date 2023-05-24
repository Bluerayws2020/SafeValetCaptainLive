package com.example.safevaletcaptain.fragments

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import com.example.safevaletcaptain.HomeActivity
import com.example.safevaletcaptain.MainActivity
import com.example.safevaletcaptain.R
import com.example.safevaletcaptain.databinding.ActivityNotficationBinding
import com.example.safevaletcaptain.databinding.ChooseLanguageBinding
import com.example.safevaletcaptain.databinding.FragmentHomeBinding
import com.example.safevaletcaptain.databinding.LangActivatyBinding
import com.example.safevaletcaptain.helpers.HelperUtils
import java.util.*

class LanguageFragment: AppCompatActivity()
{

    private lateinit var binding: LangActivatyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = LangActivatyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding.toolbarInclude.toolbar.title = resources.getString(R.string.Lang)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        HelperUtils.setDefaultLanguage(this,HelperUtils.getLang(this))

        binding.toolbarInclude.notficationBtn.setOnClickListener{



            startActivity(Intent(this,Notfication::class.java))

        }
        HelperUtils.setDefaultLanguage(this,HelperUtils.getLang(this))

        if (HelperUtils.getLang(this) == "ar"){
            binding.toolbarInclude.menuNotfication .setImageDrawable(resources.getDrawable(R.drawable.ar_back))

        }else {
            binding.toolbarInclude.menuNotfication .setImageDrawable(resources.getDrawable(R.drawable.back))

        }
        HelperUtils.setDefaultLanguage(this,HelperUtils.getLang(this))

        binding.toolbarInclude.menuNotfication.setOnClickListener{
            onBackPressed()
        }

        binding.arabicBtn.setOnClickListener {
            var language = "ar"


            HomeActivity.Lang = "ar"
            HelperUtils.changeLang()

            val sharedPreferences = getSharedPreferences(HelperUtils.SHARED_PREF, MODE_PRIVATE)
            sharedPreferences?.edit()?.putString("lang", language)?.apply()
            val intentSplash = Intent(this, HomeActivity::class.java)
            startActivity(intentSplash)

        }
        binding.englishBtn.setOnClickListener {


            var language = "en"

            HomeActivity.Lang = "en"
            HelperUtils.changeLang()


            val sharedPreferences = getSharedPreferences(HelperUtils.SHARED_PREF, MODE_PRIVATE)
            sharedPreferences?.edit()?.putString("lang", language)?.apply()
            val intentSplash = Intent(this, HomeActivity::class.java)
            startActivity(intentSplash)



        }





    }




}

