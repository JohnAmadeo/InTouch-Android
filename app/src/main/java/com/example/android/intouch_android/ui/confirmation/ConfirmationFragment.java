package com.example.android.intouch_android.ui.confirmation;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.android.intouch_android.R;
import com.example.android.intouch_android.utils.ViewUtils;
import com.example.android.intouch_android.viewmodel.ConfirmationViewModel;

import androidx.navigation.Navigation;


public class ConfirmationFragment extends Fragment {
    public ConfirmationFragment() { }

    @Override
    public void onAttach(Context context) { super.onAttach(context); }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewUtils.setActionBarVisible(getActivity(), false);
        setHasOptionsMenu(false);
        ViewUtils.setBottomNavigationVisible(getActivity(), false);

        // Inflate the layout for this fragment
        View parentView = inflater.inflate(R.layout.fragment_confirmation, container, false);
        ImageButton closeButton = parentView.findViewById(R.id.confirmation_close_button);

        ConfirmationViewModel viewModel =
                ViewModelProviders.of(this).get(ConfirmationViewModel.class);

        closeButton.setOnClickListener(view -> {
            if (viewModel.isPlaceholderUser()) {
                Navigation.findNavController(parentView).navigate(R.id.signupPlaceholderUserFragment);
            } else {
                Navigation.findNavController(parentView).navigate(R.id.sentLettersFragment);
            }
        });

        return parentView;
    }

    @Override
    public void onDetach() { super.onDetach(); }
}
