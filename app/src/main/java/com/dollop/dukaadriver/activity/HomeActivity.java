package com.dollop.dukaadriver.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.dollop.dukaadriver.R;
import com.dollop.dukaadriver.UtilityTools.MarshMallowPermission;
import com.dollop.dukaadriver.UtilityTools.SavedData;
import com.dollop.dukaadriver.UtilityTools.SessionManager;
import com.dollop.dukaadriver.UtilityTools.Utils;
import com.dollop.dukaadriver.ui.jobs.CourierJobFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    public SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_home);

        sessionManager = new SessionManager(this);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_order, R.id.navigation_earning,
                R.id.navigation_jobs
                , R.id.navigation_profile)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);


       /* if (sessionManager.is_DRIVER()) {
            navView.getMenu().findItem(R.id.navigation_jobs).setVisible(false);

        } else {
            navView.getMenu().findItem(R.id.navigation_order).setVisible(false);
        }

        if (sessionManager.is_COMPANY_DRIVER()) {
            navView.getMenu().findItem(R.id.navigation_earning).setVisible(false);
        }*/

        MarshMallowPermission marshMallowPermission = new MarshMallowPermission(HomeActivity.this);

        if (!marshMallowPermission.checkPermissionForLocation()) {
            marshMallowPermission.requestPermissionForLocation();
        }
        Utils.E("getDriver::"+SavedData.getDriver());
        Utils.E("getCourierDriver::"+SavedData.getCourierDriver());
        Utils.E("getCourier::"+SavedData.getCourier());

        if (SavedData.getDriver()) {
            navView.getMenu().findItem(R.id.navigation_home).setVisible(true);
            navView.getMenu().findItem(R.id.navigation_order).setVisible(true);
            navView.getMenu().findItem(R.id.navigation_jobs).setVisible(false);
            navView.getMenu().findItem(R.id.navigation_earning).setVisible(true);
            navView.getMenu().findItem(R.id.navigation_profile).setVisible(true);
        }
        if (SavedData.getCourierDriver()) {
            navView.getMenu().findItem(R.id.navigation_home).setVisible(true);
            navView.getMenu().findItem(R.id.navigation_order).setVisible(true);
            navView.getMenu().findItem(R.id.navigation_jobs).setVisible(false);
            navView.getMenu().findItem(R.id.navigation_earning).setVisible(false);
            navView.getMenu().findItem(R.id.navigation_profile).setVisible(true);
        }
        if (SavedData.getCourier()) {
            navView.getMenu().findItem(R.id.navigation_home).setVisible(true);
            navView.getMenu().findItem(R.id.navigation_order).setVisible(false);
            navView.getMenu().findItem(R.id.navigation_jobs).setVisible(true);
            navView.getMenu().findItem(R.id.navigation_earning).setVisible(true);
            navView.getMenu().findItem(R.id.navigation_profile).setVisible(true);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.navigation_jobs:
                replaceFragmentWithoutBack(new CourierJobFragment(), "");
                break;

            default:
                break;
        }

        return true;
    }


    public void replaceFragmentWithoutBack(Fragment newFragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.nav_host_fragment, newFragment, tag);
        ft.addToBackStack(tag);
        ft.commit();
    }


}
