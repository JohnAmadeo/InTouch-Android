package com.example.android.intouch_android;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.android.intouch_android.ui.lettereditor.LetterEditorFragment;
import com.example.android.intouch_android.R;
import com.example.android.intouch_android.ui.sentletters.SentLettersFragment;
import com.example.android.intouch_android.model.Letter;

import androidx.navigation.Navigation;

public class MainActivity
        extends AppCompatActivity
        implements SentLettersFragment.OnListFragmentInteractionListener,
        LetterEditorFragment.OnFragmentInteractionListener,
        InmateSearchFragment.OnFragmentInteractionListener,
            BottomNavigationView.OnNavigationItemSelectedListener {

    private TextView mTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView= findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

    }

    // TODO: Android sets up this boilerplate but why do our fragments need to talk to the activity?
    public void onListFragmentInteraction(Letter item) {
        System.out.println(item.toString());
    }
    public void onFragmentInteraction(Uri uri) {
        System.out.println(uri.toString());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                mTextMessage.setText(R.string.title_home);
                return true;
            case R.id.navigation_dashboard:
                mTextMessage.setText(R.string.title_dashboard);
                return true;
            case R.id.navigation_notifications:
                mTextMessage.setText(R.string.title_notifications);
                return true;
        }
        return false;
    }

    @Override
    public boolean onSupportNavigateUp() {
        return Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp();
    }
}
