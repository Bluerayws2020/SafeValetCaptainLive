package com.example.safevaletcaptain.model

import com.google.gson.annotations.SerializedName

sealed class NetworkResults<out R> {
    data class Success<out T>(val data: T) : NetworkResults<T>()
    data class Error(val exception: Exception) : NetworkResults<Nothing>()
}

data class LoginDriverModel(

    @SerializedName("msg") val status: MessageModel,
    @SerializedName("data") val driver: DriverModel,

    )

data class MessageModelRespose(

    @SerializedName("msg") val msg: MessageModel,
    )

data class MessageModel(
    @SerializedName("status") val status: Int,
    @SerializedName("message") val message: String,
    @SerializedName("newStatus") val newStatus: Int


)


data class DriverModel(
    @SerializedName("uid") val uid: String,
    @SerializedName("type") val type: String,
    @SerializedName("name") val name: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("station_responsible") val station_responsible: String,
    @SerializedName("company_id") val company_id: String,
    @SerializedName("driver_image") val driver_image: String,
    @SerializedName("driver_active") val driver_active: String,
    @SerializedName("manager") val manager: Int
    )


data class MainRideResponse(
    @SerializedName("msg") var msg :MessageModel,
    @SerializedName("data") var ride_data :RideData

    )
data class RideData(
 @SerializedName("myRides") var myRides:List<RideModel>,
    @SerializedName("globalRides") var globalRides:List<RideModel>

)
data class RideModel(
    @SerializedName("car No.") var car_number :String,
   @SerializedName("key No.") var key_number :String,
    @SerializedName("ride_id") var ride_id :String,
    @SerializedName("lat") var lat :String,
@SerializedName("lon") var lon :String,
@SerializedName("image") var image :String




)

data class DriverResponseINFO (

    @SerializedName("msg") var msg : MessageModel,
    @SerializedName("data") var customerInfo: CustomerInfo? = null ,

@SerializedName("lat") var lat: String ? = null,
    @SerializedName("lon") var lon: String? = null ,
    @SerializedName("keyNumber") var keyNumber: String? = null ,

    @SerializedName("param") var param: String? = null ,
    @SerializedName("driver_active") var driver_active: String? = null ,


    )

data class DriverResponseWorkOrNot (

    @SerializedName("msg") var msg : MessageModel,

    @SerializedName("lat") var lat: String ? = null,
    @SerializedName("lon") var lon: String? = null ,
    @SerializedName("keyNumber") var keyNumber: String? = null ,

    @SerializedName("param") var param: String? = null ,
    @SerializedName("driver_active") var driver_active: String? = null ,


    )
data class CustomerInfo (

    @SerializedName("ride_id") var rideId      : String? = null,
    @SerializedName("carID") var carID       : String? = null,
    @SerializedName("carNickName") var carNickName : String? = null,
    @SerializedName("carNumber") var carNumber   : String? = null

)


data class TakeCarMainModel (

    @SerializedName("msg"  ) var msg  : MessageModel,
    @SerializedName("data" ) var takeCarmodel :TakeCarModel,

)
data class TakeCarModel (

    @SerializedName("image" ) var image : String? = null,
    @SerializedName("lat"   ) var lat   : String? = null,
    @SerializedName("lon"   ) var lon   : String? = null

)

data class RideHistory (

    @SerializedName("msg"  ) var msg  : MessageModel,
    @SerializedName("data"   ) var ride_history   : ArrayList<RideHistoryData>

)
data class RideHistoryData (

@SerializedName("type"                       ) var type                     : String?    = null,
@SerializedName("carName"                    ) var carName                  : String?    = null,
@SerializedName("carID"                      ) var carID                    : String?    = null,
@SerializedName("date"                       ) var date                     : String?    = null,
@SerializedName("dropStart"                  ) var dropStart                : DropStart? = DropStart(),
@SerializedName("dropEnd"                    ) var dropEnd                  : DropEnd?   = DropEnd(),
@SerializedName("driver1"                    ) var driver1                  : String?    = null,
@SerializedName("phone1"                     ) var phone1                   : String?    = null,
@SerializedName("station_responsible1"       ) var stationResponsible1      : String?    = null,
@SerializedName("station_responsible_phone1" ) var stationResponsiblePhone1 : String?    = null,
@SerializedName("driver_image1"              ) var driverImage1             : String?    = null,
@SerializedName("time1"                      ) var time1                    : String?    = null,
@SerializedName("driver2"                    ) var driver2                  : String?    = null,
@SerializedName("phone2"                     ) var phone2                   : String?    = null,
@SerializedName("station_responsible2"       ) var stationResponsible2      : String?    = null,
@SerializedName("station_responsible_phone2" ) var stationResponsiblePhone2 : String?    = null,
@SerializedName("driver_image2"              ) var driverImage2             : String?    = null,
@SerializedName("time2"                      ) var time2                    : String?    = null,

@SerializedName("drop"                      ) var drop                    : DropEnd?    = null,

    @SerializedName("totalTime") var totalTime:String?    = null


)
data class DropEnd (

    @SerializedName("lat" ) var lat : String? = null,
    @SerializedName("lon" ) var lon : String? = null

)
data class DropStart (

    @SerializedName("lat" ) var lat : String? = null,
    @SerializedName("lon" ) var lon : String? = null



)



//station Name
data class StationResponse (

    @SerializedName("msg"  ) var msg  : MessageModel?  ,
    @SerializedName("data" ) var data : StationObject,

)

data class  StationObject (
    @SerializedName("stations" ) var stations : List<StationsName>
)
data class StationsName(

    @SerializedName("id"   ) var id   : String? = null,
    @SerializedName("name" ) var name : String? = null

){
    override fun toString(): String {
        return name.toString()
    }
}

data class NotficationResponse (

    @SerializedName("msg"  ) var msg  :MessageModel,
    @SerializedName("data" ) var notficationData : ArrayList<ArrayList<NotficationDataResponse>> = arrayListOf()

)

data class NotficationDataResponse (


    @SerializedName("id"      ) var id     : String? = null,
    @SerializedName("bodyEN"  ) var bodyEN : String? = null,
    @SerializedName("bodyAR"  ) var bodyAR : String? = null,
    @SerializedName("date"    ) var date   : String? = null,
    @SerializedName("time"    ) var time   : String? = null,
    @SerializedName("uid"     ) var uid    : String? = null,
    @SerializedName("flag"    ) var flag   : String? = null,
    @SerializedName("ride_id" ) var rideId : String? = null,
    @SerializedName("msg_id"  ) var msgId  : String? = null
)