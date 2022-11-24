package cn.blogss.androidstudy.main

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import cn.blogss.androidstudy.R
import cn.blogss.androidstudy.databinding.ActivityMainBinding
import cn.blogss.androidstudy.discovery.view.DiscoveryFragment
import cn.blogss.androidstudy.home.view.HomeFragment
import cn.blogss.androidstudy.profile.view.ProfileFragment
import cn.blogss.helper.base.jetpack.BaseActivity
import cn.blogss.helper.base.jetpack.BaseViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.permissionx.guolindev.PermissionX

class MainActivity : BaseActivity<ActivityMainBinding, BaseViewModel>() {
    private lateinit var fm: FragmentManager
    private var homeFragment: HomeFragment? = null
    private var discoveryFragment: DiscoveryFragment? = null
    private var profileFragment: ProfileFragment? = null
    private var preFragment: Fragment? = null
    private var curFragment: Fragment? = null

    companion object {
        private const val HOME_FRAGMENT_TAG = "home_fragment"
        private const val DISCOVERY_FRAGMENT_TAG = "discovery_fragment"
        private const val PROFILE_FRAGMENT_TAG = "profile_fragment"
        private const val TAG = "MainActivity"
    }

    override fun initView() {
        fm = supportFragmentManager
        viewBinding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            onTabItemSelected(item.itemId)
            true
        }
        onTabItemSelected(R.id.tab_menu_home)
    }


    override fun getViewModel(): BaseViewModel? {
        return null
    }

    override fun getViewBinding(inflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(inflater)
    }

    override fun bindObserver() {
    }

    override fun initData() {
    }

    private fun onTabItemSelected(itemId: Int) {
        /*每个事务只能提交一次*/
        val ft = fm.beginTransaction()
        when (itemId) {
            R.id.tab_menu_home -> {
                if (homeFragment == null) {
                    homeFragment = HomeFragment()
                    ft.add(R.id.home_container, homeFragment!!, HOME_FRAGMENT_TAG)
                }
                curFragment = homeFragment
            }
            R.id.tab_menu_discovery -> {
                if (discoveryFragment == null) {
                    discoveryFragment = DiscoveryFragment()
                    ft.add(R.id.home_container, discoveryFragment!!, DISCOVERY_FRAGMENT_TAG)
                }
                curFragment = discoveryFragment
            }
            R.id.tab_menu_profile -> {
                if (profileFragment == null) {
                    profileFragment = ProfileFragment()
                    ft.add(R.id.home_container, profileFragment!!, PROFILE_FRAGMENT_TAG)
                }
                curFragment = profileFragment
            }
        }
        if (preFragment != null && curFragment != null) {
            ft.hide(preFragment!!).show(curFragment!!)
        }
        preFragment = curFragment
        ft.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 一次性申请必要的运行时权限
        PermissionX.init(this).permissions(mutableListOf(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.READ_CONTACTS))
            .onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(deniedList,"您需要去应用程序设置当中手动开启权限", "我已明白", "取消")
            }
            .onExplainRequestReason {scope, deniedList ->
                scope.showRequestReasonDialog(deniedList,"即将重新申请的权限是程序必须依赖的权限","我已明白","取消")
            }
            .request { allGranted, _, deniedList ->
                if(allGranted)
                    Toast.makeText(this,"所有申请的权限都已通过", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this,"您拒绝了如下权限：$deniedList", Toast.LENGTH_SHORT).show()
            }

    }

}