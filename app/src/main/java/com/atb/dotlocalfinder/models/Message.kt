package com.example.atb_app.models

/*import com.squareup.moshi.Json
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers*/

//@JsonClass(generateAdapter = true)
/*data class Message<T>(
    val FID: String,
    val RID: String,
    val ARG: T?
)*/


data class Message(
    val FID: String,
    val RID: String
    //val ARG: String
)

data class Arg(

    var STA: String,
    var AP: String

)


//@Serializable
data class Echo(

    val FID: String,
    val RID: String,
    val ARG: Arg?,
    val ERR: String?
)

data class NetworkList(

    val FID: String,
    val RID: String,
    val ARG: List<Networks>?,
    val ERR: String?
)

data class Networks(

    val rssi: String,
    val ssid: String,
    val bssid: String,
    val channel: String,
    val secure: String,
    val hidden: String
)

/*

{
    "FID": "E1F16E10",
    "RID": "00004442",
    "ARG": [
    {
        "rssi": -58,
        "ssid": "ATB2100X",
        "bssid": "7C-87-CE-30-DD-C5",
        "channel": 6,
        "secure": 0,
        "hidden": false
    },
    {
        "rssi": -63,
        "ssid": "",
        "bssid": "02-92-BF-14-7F-09",
        "channel": 6,
        "secure": 3,
        "hidden": false
    },
    {
        "rssi": -64,
        "ssid": "ATB_Electronics",
        "bssid": "F4-92-BF-14-7F-09",
        "channel": 6,
        "secure": 3,
        "hidden": false
    },
    {
        "rssi": -64,
        "ssid": "",
        "bssid": "FE-92-BF-14-7F-09",
        "channel": 6,
        "secure": 3,
        "hidden": false
    },
    {
        "rssi": -64,
        "ssid": "ATB_Guest",
        "bssid": "FA-92-BF-14-7F-09",
        "channel": 6,
        "secure": 4,
        "hidden": false
    },
    {
        "rssi": -92,
        "ssid": "ATB_Electronics",
        "bssid": "68-D7-9A-C4-C0-61",
        "channel": 1,
        "secure": 3,
        "hidden": false
    },
    {
        "rssi": -92,
        "ssid": "ATB_Guest",
        "bssid": "6E-D7-9A-C4-C0-61",
        "channel": 1,
        "secure": 4,
        "hidden": false
    }
    ]
}
*/


/*
data class UserProfile(
    @Json(name = "results") val results: List<Echo>
    //@Json(name = "info") val info: List<Info>
)

interface SearchUserProfileApi {
    //@GET("v1/images/search")

*/
/*    @Headers("Accept: application/json", "Content-type: application/json")
    @GET("/api/")*//*

    fun getUserProfile(): Call<UserProfile>  //Для варианта без корутин
    //suspend fun getUserProfile(): Response<UserProfile>

}*/
