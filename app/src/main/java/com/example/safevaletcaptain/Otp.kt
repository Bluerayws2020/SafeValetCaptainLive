package com.example.safevaletcaptain


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.FragmentActivity
import com.example.safevaletcaptain.databinding.OtpActivatyBinding
import com.example.safevaletcaptain.fragments.ProgressDialogFragment
import com.example.safevaletcaptain.helpers.HelperUtils
import com.example.safevaletcaptain.helpers.ViewUtils.hide
import com.example.safevaletcaptain.helpers.ViewUtils.inVisible
import com.example.safevaletcaptain.helpers.ViewUtils.show
import com.example.safevaletcaptain.model.DriverModel
import com.example.safevaletcaptain.model.NetworkResults
import com.example.safevaletcaptain.viewmodel.DriverViewModel
import com.onesignal.OSSubscriptionObserver
import com.onesignal.OSSubscriptionStateChanges
import com.onesignal.OneSignal
import java.util.*

const val OTP_TYPE = "OTP_T"
const val PROGRESS_DIALOG = "progress_dialog"
const val MODIFIED_PHONE_NUMBER = "OLD_NUMBER"
const val CHANGE_PHONE_UID = "P_UID"
const val PHONENUMEBR = "PHONE"
const val PLAYERID = "PLAYERID"
private var playerId: String? = null

class Otp : AppCompatActivity(), OSSubscriptionObserver {

    private val otpViewModel by viewModels<DriverViewModel>()
    private lateinit var binding: OtpActivatyBinding
    private var countDownTimer: CountDownTimer? = null
//    private lateinit var otpInfo: OTPInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = OtpActivatyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var otpCode = ""
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        HelperUtils.setDefaultLanguage(this,HelperUtils.getLang(this))
        val phone = intent.getStringExtra(PHONENUMEBR)

        OneSignal.addSubscriptionObserver(this)
        playerId = OneSignal.getDeviceState()?.userId
        otpViewModel.getLoginWithOtpResponse().observe(this) { results ->
            binding.progressVerifyOtp.hide()
            when (results) {
                is NetworkResults.Success -> {
                    showMessage(results.data.status.message)
                    if (results.data.status.status == 1) {
//                        checkOTPType(otpInfo, results.data.data[0].uid)
                        saveUserData(
                            results.data.driver
                        )
                    } else {
                        binding.progressVerifyOtp.hide()
                        binding.verifyOtpBtn.show()
                    }
                }
                is NetworkResults.Error -> {
                    showMessage(getString(R.string.error_occurred))

                    results.exception.printStackTrace()
                }
            }
        }

        otpViewModel.getResendOtp().observe(this) { results ->
            when (results) {
                is NetworkResults.Success -> {
                    hideDialogProgress()
                    showMessage(results.data.msg.message)
                    if (results.data.msg.status == 1) {
                        binding.resendCodeTv.isEnabled = false
                        countDownTimer?.start()
                    } else {
                        binding.resendCodeTv.isEnabled = true
//                        binding.resendCodeTv.hide()
                        countDownTimer?.cancel()
                    }
                }
                is NetworkResults.Error -> {
                    hideDialogProgress()
                    showMessage(getString(R.string.error_occurred))
                    results.exception.printStackTrace()
                }
            }
        }

        binding.otpViewEt.setOtpCompletionListener { otpCode = it }

        binding.verifyOtpBtn.setOnClickListener {
            if (otpCode.isNotEmpty() && otpCode.length == 4) {
                it.inVisible()
                binding.progressVerifyOtp.show()
                otpViewModel.driverLoginWithOtp(
                    phone.toString(),
                    otpCode,
                    playerId.toString(),
                )
                Log.d("playerrId", playerId.toString())
            } else {
                showMessage(getString(R.string.correct_otp_required))
            }
        }

        countDownTimer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.resendCodeTv.text = getString(
                    R.string.resend_code_30,
                    (millisUntilFinished / 1000).toString(),

                )
                binding.resendCodeTv.isEnabled = false
                binding.resendCodeTv.setBackgroundColor(Color.parseColor("#eaebf0"));
            }

            override fun onFinish() {
                binding.resendCodeTv.setText(R.string.resend_code)
                binding.resendCodeTv.setBackgroundColor(Color.parseColor("#FF039BE5"));

                binding.resendCodeTv.isEnabled = true
            }

        }

        binding.resendCodeTv.setText(R.string.resend_code)
        binding.resendCodeTv.isEnabled = true
        binding.phoneNumberTv.apply {
            text = phone
            show()
        }
//        countDownTimer?.start()
        binding.resendCodeTv.setOnClickListener {
            showDialogProgress()
            otpViewModel.resendOtp(phone.toString())
        }
binding.phoneNumberTv.text = phone.toString()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        countDownTimer?.cancel()
        countDownTimer = null
        super.onDestroy()
    }

    override fun onBackPressed() {
        showMessage(getString(R.string.verification_required))
    }

    override fun attachBaseContext(newBase: Context?) {
        val language = "ar"
        val configuration = Configuration()
        configuration.setLocale(Locale.forLanguageTag(language))
        val context = newBase?.createConfigurationContext(configuration)
        super.attachBaseContext(context)
    }
    fun Activity.showMessage(message: String, gravity: Int? = null) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
        gravity?.let { toast.setGravity(it, 0, 0) }
        toast.show()
    }
    fun FragmentActivity.hideDialogProgress() {
        supportFragmentManager.findFragmentByTag(PROGRESS_DIALOG)?.let { fragment ->
            if (fragment.isAdded)
                (fragment as ProgressDialogFragment).dismiss()
        }
    }
    fun FragmentActivity.showDialogProgress() {
        ProgressDialogFragment().show(supportFragmentManager, PROGRESS_DIALOG)
    }
    private fun saveUserData(driver: DriverModel) {
        val sharedPreferences = getSharedPreferences(HelperUtils.SHARED_PREF, MODE_PRIVATE)
        sharedPreferences.edit().apply {
            putString("uid", driver.uid.toString())
            putString("user_type", driver.name)
            putString("company_id", driver.company_id)

            Log.d("UUID",driver.uid)
        }.apply()

        val intentSignIn = Intent(this, HomeActivity::class.java)
        startActivity(intentSignIn)
        finishAffinity()

    }

    override fun onOSSubscriptionChanged(p0: OSSubscriptionStateChanges?) {
        p0?.let {
            if (!it.from.isSubscribed && it.to.isSubscribed) {
                playerId = it.to.userId

            }
        }

    }
}