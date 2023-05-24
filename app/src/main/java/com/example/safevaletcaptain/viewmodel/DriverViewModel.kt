package com.example.safevaletcaptain.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.safevaletcaptain.helpers.HelperUtils
import com.example.safevaletcaptain.model.LoginDriverModel
import com.example.safevaletcaptain.model.MessageModel
import com.example.safevaletcaptain.model.MessageModelRespose
import com.example.safevaletcaptain.model.NetworkResults
import com.example.safevaletcaptain.repo.NetworkRepository
import kotlinx.coroutines.launch

class DriverViewModel(application: Application): AndroidViewModel(application) {

    private val deviceId = HelperUtils.getAndroidID(application.applicationContext)
    private val repo = NetworkRepository
    private val language = HelperUtils.getLang(application.applicationContext)

    private val loginDriverMessageLiveData = MutableLiveData<NetworkResults<MessageModelRespose>>()
    private val logoutCustomerLiveData = MutableLiveData<NetworkResults<MessageModelRespose>>()

    private val loginDriverWithOtpMessageLiveData = MutableLiveData<NetworkResults<LoginDriverModel>>()
    private val resendOtpLiveData = MutableLiveData<NetworkResults<MessageModelRespose>>()

    fun driverLogin(phone: String, deviceId: String){
        viewModelScope.launch{
            loginDriverMessageLiveData.value = repo.driverLogin(phone, deviceId)
        }
    }


    fun driverLoginWithOtp(phone: String,otp:String, deviceId: String){
        viewModelScope.launch{
            loginDriverWithOtpMessageLiveData.value = repo.driverLoginWithOtp(phone,otp,deviceId,language)
        }
    }
//    user_kind 1 for driver 2 for Clint  ,user_kind:String
    fun resendOtp(phone: String){
        viewModelScope.launch{
            resendOtpLiveData.value = repo.resendOtp(phone,"1")
        }
    }


    fun logoutUser(userId: String){
        viewModelScope.launch{
            logoutCustomerLiveData.value = repo.logoutUser(userId)
        }
    }


    fun getLoginResponse() = loginDriverMessageLiveData
    fun getLoginWithOtpResponse() = loginDriverWithOtpMessageLiveData
    fun getResendOtp() = resendOtpLiveData
    fun getCustomerLogoutResponse() = logoutCustomerLiveData

}