package com.example.android.intouch_android;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.intouch_android.utils.ViewUtils;


public class signupPlaceholderUserFragment extends Fragment {
    public signupPlaceholderUserFragment() { }

    @Override
    public void onAttach(Context context) { super.onAttach(context); }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup_placeholder_user, container, false);
    }

    @Override
    public void onDetach() { super.onDetach(); }
}
