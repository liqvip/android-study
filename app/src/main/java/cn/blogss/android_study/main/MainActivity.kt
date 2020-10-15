package cn.blogss.android_study.main

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import cn.blogss.android_study.R
import cn.blogss.android_study.discovery.view.DiscoveryFragment
import cn.blogss.android_study.home.view.HomeFragment
import cn.blogss.android_study.profile.view.ProfileFragment
import cn.blogss.core.base.BaseActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : BaseActivity() {
    private var bottomNavigationView: BottomNavigationView? = null
    private var homeContainer: FrameLayout? = null
    private var fm: FragmentManager? = null
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

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        fm = supportFragmentManager
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        homeContainer = findViewById(R.id.home_container)
        bottomNavigationView!!.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            onTabItemSelected(item.itemId)
            true
        })
        onTabItemSelected(R.id.tab_menu_home)
    }

    private fun onTabItemSelected(itemId: Int) {
        /*每个事务只能提交一次*/
        val ft = fm!!.beginTransaction()
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
            ft.detach(preFragment!!).attach(curFragment!!)
        }
        preFragment = curFragment
        ft.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTag(TAG)
        super.onCreate(savedInstanceState)
    }
}