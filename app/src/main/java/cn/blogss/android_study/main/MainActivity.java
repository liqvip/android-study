package cn.blogss.android_study.main;

import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import cn.blogss.android_study.R;
import cn.blogss.android_study.discovery.view.DiscoveryFragment;
import cn.blogss.android_study.home.view.HomeFragment;
import cn.blogss.android_study.profile.view.ProfileFragment;
import cn.blogss.core.base.BaseActivity;

public class MainActivity extends BaseActivity {
    private BottomNavigationView bottomNavigationView;

    private FragmentManager fm;

    private Fragment homeFragment,discoveryFragment,profileFragment,preFragment,curFragment;

    private static String HOME_FRAGMENT_TAG = "home_fragment";

    private static String DISCOVERY_FRAGMENT_TAG = "discovery_fragment";

    private static String PROFILE_FRAGMENT_TAG = "profile_fragment";

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        fm= getSupportFragmentManager();
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                onTabItemSelected(item.getItemId());
                return true;
            }
        });
        onTabItemSelected(R.id.tab_menu_home);
    }

    private void onTabItemSelected(int itemId) {
        /*每个事务只能提交一次*/
        FragmentTransaction ft = fm.beginTransaction();
        switch (itemId){
            case R.id.tab_menu_home:
                if(homeFragment == null){
                    homeFragment = new HomeFragment();
                    ft.add(R.id.home_container,homeFragment,HOME_FRAGMENT_TAG);
                }
                curFragment = homeFragment;
                break;
            case R.id.tab_menu_discovery:
                if(discoveryFragment == null){
                    discoveryFragment = new DiscoveryFragment();
                    ft.add(R.id.home_container,discoveryFragment,DISCOVERY_FRAGMENT_TAG);
                }
                curFragment = discoveryFragment;
                break;
            case R.id.tab_menu_profile:
                if(profileFragment == null){
                    profileFragment = new ProfileFragment();
                    ft.add(R.id.home_container,profileFragment,PROFILE_FRAGMENT_TAG);
                }
                curFragment = profileFragment;
                break;
        }

        if(preFragment !=null && curFragment != null){
            ft.detach(preFragment).attach(curFragment);
        }

        preFragment = curFragment;
        ft.commit();
    }
}
