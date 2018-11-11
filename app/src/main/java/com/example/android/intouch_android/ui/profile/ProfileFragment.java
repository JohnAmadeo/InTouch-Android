package com.example.android.intouch_android.ui.profile;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.intouch_android.R;
import com.example.android.intouch_android.utils.ViewUtils;

import androidx.navigation.Navigation;

public class ProfileFragment extends Fragment {
    private View mParentView;

    public ProfileFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewUtils.setActionBarVisible(getActivity(), true);
        ViewUtils.setupActionBarOptions(getActivity(), "InTouch", false);

        mParentView = inflater.inflate(R.layout.fragment_profile, container, false);

        ViewUtils.setupBottomNavigation(
                getActivity(),
                mParentView,
                R.id.navigation_profile,
                R.id.profileFragment
        );

        return mParentView;
    }

    @Override
    public void onAttach(Context context) { super.onAttach(context); }

    @Override
    public void onDetach() { super.onDetach(); }
}
