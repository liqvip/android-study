package cn.blogss.androidstudy.main

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import cn.blogss.androidstudy.R
import cn.blogss.androidstudy.databinding.ActivityMainBinding
import cn.blogss.androidstudy.discovery.view.DiscoveryFragment
import cn.blogss.androidstudy.home.view.HomeFragment
import cn.blogss.androidstudy.profile.view.ProfileFragment
import cn.blogss.helper.base.jetpack.BaseActivity
import cn.blogss.helper.base.jetpack.BaseViewModel

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
    }

}