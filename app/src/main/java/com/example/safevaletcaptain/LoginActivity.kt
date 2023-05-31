package com.example.safevaletcaptain

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.safevaletcaptain.databinding.ActivityLoginBinding
import com.example.safevaletcaptain.helpers.HelperUtils
import com.example.safevaletcaptain.helpers.ViewUtils.hide
import com.example.safevaletcaptain.helpers.ViewUtils.inVisible
import com.example.safevaletcaptain.helpers.ViewUtils.isInputEmpty
import com.example.safevaletcaptain.helpers.ViewUtils.show
import com.example.safevaletcaptain.model.DriverModel
import com.example.safevaletcaptain.model.NetworkResults
import com.example.safevaletcaptain.viewmodel.DriverViewModel
import com.onesignal.OSSubscriptionObserver
import com.onesignal.OSSubscriptionStateChanges
import com.onesignal.OneSignal
import retrofit2.HttpException

class LoginActivity : AppCompatActivity(), OSSubscriptionObserver {         //, View.OnClickListener {
    init {
        Log.d("ayham", "LoginActivity")
    }
    private lateinit var binding: ActivityLoginBinding
    private val driverVM by viewModels<DriverViewModel>()

    private var playerId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        HelperUtils.setDefaultLanguage(this,HelperUtils.getLang(this))
        driverVM.getLoginResponse().observe(this) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.msg.status == 1) {
                        showMessage(result.data.msg.message.toString())

                        val intentSignIn = Intent(this, Otp::class.java)
                        intentSignIn.putExtra(PHONENUMEBR, binding.driverPhone.text.toString());
                        intentSignIn.putExtra(PLAYERID,  playerId.toString());




                        startActivity(intentSignIn)
                        finishAffinity()
                        hideProgress()

                    } else {
                        showMessage(result.data.msg.message.toString())
                        hideProgress()

                    }
                }
                is NetworkResults.Error -> {
                    result.exception.printStackTrace()
                    hideProgress()
                    if (result.exception is HttpException)
                        showMessage(result.exception.message())
                    else
                        showMessage("No Internet connection")
                }
            }
        }

        binding.signInBtn.setOnClickListener {
            HelperUtils.hideKeyBoard(this)
            if (isInputValid()) {
                binding.progressBarLogin.show()
                it.inVisible()
                driverVM.driverLogin(
                    binding.driverPhone.text.toString(),
        playerId.toString()
                )
                Log.d("playerIdsss",playerId.toString())


            }
        }

    }
    override fun onOSSubscriptionChanged(p0: OSSubscriptionStateChanges?) {
        p0?.let {
//            if (!it.from.isSubscribed && it.to.isSubscribed) {
                playerId = it.to.userId
//            }
            Log.d("LIOOOOL",playerId.toString())
        }
    }

    private fun isInputValid(): Boolean {
        var status = true


        if (binding.driverPhone.isInputEmpty()) {
            status = false
            binding.driverPhone.error = "Required" //getString(R.string.required)
        }

        return status
    }

    private fun saveUserData(driver: DriverModel) {
        val sharedPreferences = getSharedPreferences(HelperUtils.SHARED_PREF, MODE_PRIVATE)
        sharedPreferences.edit().apply {
            putString("uid", driver.uid.toString())
            putString("user_type", driver.name)
            putString("phone", driver.company_id)

            Log.d("UUID",driver.uid)
        }.apply()
    }


    private fun showMessage(message: String?) =
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()

    private fun hideProgress() {
        binding.signInBtn.show()
        binding.progressBarLogin.hide()
    }
}