package com.example.androiduistudy.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import com.example.androiduistudy.R
import com.example.androiduistudy.base.BaseBindingViewFragment
import com.example.androiduistudy.bean.LocationInfo
import com.example.androiduistudy.databinding.FragmentRadarBinding
import com.example.androiduistudy.util.CustomLog
import com.example.androiduistudy.util.GeoUtils
import java.util.*
import kotlin.math.abs


class RadarFragment : BaseBindingViewFragment<FragmentRadarBinding>(R.layout.fragment_radar) ,
    SensorEventListener {
    companion object{
        const val TAG = "RadarFragment"
    }
    private lateinit var sensorManager: SensorManager
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    private lateinit var locationInfo: LocationInfo
    private var locationInfoLiveData = MutableLiveData<LocationInfo>()
    private var hasInitialized = false//飞机经纬度是否初始化
    private var lastDegree:Float = 999f
    var lat1:Double = 0.0
    var lon1:Double = 0.0
    var lon2:Double = 0.015
    var lat2:Double = 0.0

    override fun initBinding(layoutInflater: LayoutInflater): FragmentRadarBinding? {
        return FragmentRadarBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {

        locationInfo = LocationInfo()
        locationInfoLiveData.observe(viewLifecycleOwner){ it ->
            binding.locationInfo = ""+
                "手机经纬度:(${it.lon1}，${it.lat1})\n" +
                    "飞机经纬度:(${it.lon2},${it.lat2})\n" +
                    "两点距离(米):${(GeoUtils.calculateDistance(it.lon1, it.lat1, it.lon2, it.lat2)*1000).toInt()}\n"+
                    "与正北方向夹角:${GeoUtils.getAngle(GeoUtils.MyLatLng(it.lon1, it.lat1),GeoUtils.MyLatLng(it.lon2, it.lat2)).toInt()}\n"+
                    "方向角:${it.degree}"
        }



        val angle = GeoUtils.getAngle(GeoUtils.MyLatLng(0.15,0.15),GeoUtils.MyLatLng(0.0,0.0))
        CustomLog.d(TAG,"角度；${angle}")
    }

    override fun onResume() {
        super.onResume()
        //地址监听
        locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = object :LocationListener{
            override fun onLocationChanged(location: Location) {
                lat1 = location.latitude
                lon1 = location.longitude
                if (!hasInitialized){
                    val random = Random()
                    var randomValue = 0.02 + random.nextDouble() * (0.02 - 0.01)
//                    var randomValue = 0.0002 + random.nextDouble() * (0.0002 - 0.0001)//测试用  较近范围
                    if(random.nextBoolean()){
                        randomValue = -randomValue
                    }
                    lat2 = lat1 + randomValue
                    if(random.nextBoolean()){
                        randomValue = -randomValue
                    }
                    lon2 = lon1 + randomValue
                    hasInitialized = true
                }
                binding.layoutRadarMap.setLatLng(lon1,lat1,lon2,lat2)
                CustomLog.d("起点经纬度：($lon1,$lat1)")
                CustomLog.d("终点经纬度：($lon2,$lat2)")
//                binding.layoutRadarMap.setLatLng(0.0,0.0,0.0,0.015)//测试用，正北方向点
                locationInfo.lon1 = lon1
                locationInfo.lat1 = lat1
                locationInfo.lon2 = lon2
                locationInfo.lat2 = lat2
                locationInfoLiveData.value = locationInfo
            }

            /** 位置提供者被禁用时调用，例如禁用了 GPS 定位功能，那么该方法就会被触发 如果禁用时没有重写该方法会导致闪退*/
            override fun onProviderDisabled(provider: String) {
                CustomLog.d(TAG,provider)
            }

            /** 位置提供者被启用时调用，之前被禁用的位置提供者（如打开了 GPS 定位功能），该方法就会被触发。如果重新启用时没有重写该方法会导致闪退*/
            override fun onProviderEnabled(provider: String) {
                CustomLog.d(TAG,provider)
            }
        }

        //判断GPS或网络定位是否启用
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            //检查是否授予了访问精确位置和粗略位置的权限
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //请求位置更新，这里使用GPS，最小时间间隔0，最小距离0，及对应的位置监听器
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
            }
        }

        //方向角监听器实现
        sensorManager = requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION)
        sensorManager.registerListener(this, rotationVectorSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onDestroy() {
        super.onDestroy()
        // 取消注册传感器监听器
        sensorManager.unregisterListener(this)
        locationManager?.removeUpdates(locationListener)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ORIENTATION) {
            val degree = event?.values?.get(0) ?:0F
            binding.layoutRadarMap.setDegree(degree)
            locationInfo.degree = degree
            locationInfoLiveData.value = locationInfo
            if (abs(lastDegree) <999f && abs(lastDegree - degree) > 5f){
                val startDegree = degree / 360 * 8f
                binding.layoutRadarMap.setNavDirectionDegree(startDegree)
                lastDegree = degree
            }
            if(lastDegree == 999f){
                lastDegree = degree
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        // 当传感器精度发生变化时的回调
    }
}