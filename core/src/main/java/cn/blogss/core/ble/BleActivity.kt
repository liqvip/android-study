package cn.blogss.core.ble

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import cn.blogss.core.databinding.ActivityBleBinding

/**
 * 权限1:用于请求连接、接受连接和传输数据
 * 权限2:允许程序管理蓝牙设备，如打开、关闭蓝牙，扫描蓝牙设备等。
 * 权限3:系统会将蓝牙扫描视为一种位置信息获取行为，即使只是用于发现附近的蓝牙设备，也需要获取定位权限
 *
 * <uses-permission android:name="android.permission.BLUETOOTH" />
 * <uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
 * <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
 */
class BleActivity: AppCompatActivity() {
    private lateinit var vb: ActivityBleBinding
    private val vm: BleViewModel by viewModels()

    companion object {
        private const val REQUEST_BLE_PERMISSION_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initData()
    }

    private fun initView() {
        vb = ActivityBleBinding.inflate(layoutInflater)
        setContentView(vb.root)
    }

    private fun initData() {
        // 请求蓝牙和定位权限
        requestBlePermissions()
    }

    private fun requestBlePermissions() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                ),
            REQUEST_BLE_PERMISSION_CODE)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}