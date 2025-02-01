package com.jm.appsolve_fe

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.provider.Settings
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.jm.appsolve_fe.retrofit.LWRetrofitClient
import com.jm.appsolve_fe.retrofit.currentBookmarkLocationWeatherResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class GetCurrentLocation(private val context: Context) {

    private lateinit var currentTemperature: TextView

    // currentTemperature TextView 전달 함수
    fun setCurrentLocationTemperature(tv: TextView) {
        currentTemperature = tv
    }

    private var fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    // getCurrentLocation 함수에서 위도/경도를 콜백으로 전달하고, 주소 업데이트도 가능하도록 개선
    fun getCurrentLocation(callback: (latitude: Double, longitude: Double) -> Unit) {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                if (ActivityCompat.checkSelfPermission(
                        context, Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(
                        context, Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }

                fusedLocationClient.lastLocation.addOnCompleteListener { task ->
                    val location: Location? = task.result
                    if (location != null) {
                        val latitude = location.latitude
                        val longitude = location.longitude
                        callback(latitude, longitude) // 위도와 경도 콜백으로 전달
                        getWeatherData(latitude, longitude) // 날씨 데이터 가져오기
                        getAddressFromLocation(latitude, longitude)
                    } else {
                        Toast.makeText(context, "위치 정보를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(context, "위치 서비스를 활성화하세요.", Toast.LENGTH_SHORT).show()
                context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
        } else {
            requestPermission()
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            (context as FragmentActivity),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
            PERMISSION_REQUEST_ACCESS_LOCATION
        )
    }
    fun getWeatherData(latitude: Double, longitude: Double) {
        val token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxNCIsImlhdCI6MTczODMxMDM4OCwiZXhwIjoxNzQwOTAyMzg4fQ.Pma0mIzOiSPvUto6Ya8qs1Cncoz5W2QBkOiZiGkiD40"

        // 날씨 API 호출
        LWRetrofitClient.service.currentBookmarkLocationWeather("Bearer $token", latitude, longitude)
            .enqueue(object : Callback<currentBookmarkLocationWeatherResponse> {
                override fun onResponse(
                    call: Call<currentBookmarkLocationWeatherResponse>,
                    response: Response<currentBookmarkLocationWeatherResponse>
                ) {
                    if (response.isSuccessful) {
                        val weatherData = response.body()
                        if (weatherData?.success == true) {

                            val temperature = weatherData.result
                            // °C -> ° 로 변경
                            val cleanedTemperature = temperature.replace("°C", "°")
                            // currentTemperature가 초기화된 후에만 텍스트 설정
                            if (::currentTemperature.isInitialized) {
                                currentTemperature.text = cleanedTemperature
                            }
                            val activity = context as Activity
                            val tvlocationTemperature = activity.findViewById<TextView>(R.id.tv_locationtemperature)
                            tvlocationTemperature?.text = cleanedTemperature

                            Log.d("WeatherAPI", "성공, $cleanedTemperature")
                        } else {
                            Log.e("WeatherAPI", "Error: Success is false")
                        }
                    } else {
                        Log.e("WeatherAPI", "Error code: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<currentBookmarkLocationWeatherResponse>, t: Throwable) {
                    Toast.makeText(context, "API 호출 실패", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun getAddressFromLocation(latitude: Double, longitude: Double) {
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses != null && addresses.isNotEmpty()) {
                val address: Address = addresses[0]
                val countryName = address.countryName ?: ""
                val thoroughfare = address.thoroughfare ?: "" // 도로명
                val subLocality = address.subLocality ?: "" // 구 이름
                val locality = address.locality ?: "" // 시 이름
                val adminArea = address.adminArea ?: "" // 도, 광역시

                val activity = context as Activity
                val tvAdminarea = activity.findViewById<TextView>(R.id.tv_adminarea)
                val tvLocality = activity.findViewById<TextView>(R.id.tv_locality)
                val tvThoroughfare = activity.findViewById<TextView>(R.id.tv_thoroughfare)

                val tv_1st_address = activity.findViewById<TextView>(R.id.tv_1st_address)
                val tv_2nd_address = activity.findViewById<TextView>(R.id.tv_2nd_address)
                val tv_3rd_address = activity.findViewById<TextView>(R.id.tv_3rd_address)

                // 광역시 처리
                if (adminArea.endsWith("시")) {
                    tvAdminarea?.text = adminArea // 광역시일 때는 adminArea를 설정
                    tv_1st_address?.text = adminArea // 광역시일 때는 adminArea를 설정
                } else {
                    tvAdminarea?.text = locality // 광역시가 아니면 locality 설정
                    tv_1st_address?.text = locality // 광역시가 아니면 locality 설정
                }

                tvLocality?.text = subLocality
                tvThoroughfare?.text = thoroughfare

                tv_2nd_address?.text = subLocality
                tv_3rd_address?.text = thoroughfare

                // 최종 주소 생성
                val finalAddress = when {
                    subLocality.isNotEmpty() -> "$countryName $adminArea $locality $subLocality $thoroughfare"
                    locality.isNotEmpty() -> "$countryName $adminArea $locality $thoroughfare"
                    else -> "주소를 찾을 수 없음"
                }

                Toast.makeText(context, finalAddress, Toast.LENGTH_LONG).show()

            } else {
                Toast.makeText(context, "주소를 찾을 수 없음", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "주소 변환 실패", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 100
    }
}
