package com.example.android.intouch_android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.example.android.intouch_android.utils.BackPressListener;
import com.example.android.intouch_android.utils.BackPressSetter;

import androidx.navigation.Navigation;

public class MainActivity extends AppCompatActivity implements BackPressSetter {
    private final String LOG_TAG = this.getClass().getSimpleName();
    private Fragment selectedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onBackPressed() {
        if (selectedFragment instanceof BackPressListener) {
            ((BackPressListener) selectedFragment).onBackPressed();
        }
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp();
    }

    @Override
    public void setSelectedFragment(Fragment fragment) {
        selectedFragment = fragment;
    }
}


