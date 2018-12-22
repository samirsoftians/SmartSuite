package com.transworldtechnology.crm.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.transworldtechnology.crm.R;
import com.transworldtechnology.crm.activity.MainActivity;

/**
 * Created by root on 1/6/16.
 */
public class MeFragmentNetworkUnavailable extends Fragment {

    private View rootView;
    private MainActivity parentActivity;

    public static MeFragmentNetworkUnavailable getInstance() {
        MeFragmentNetworkUnavailable fragmentNetworkUnavailable = new MeFragmentNetworkUnavailable();

        Bundle args = new Bundle();
        args.putString(MainActivity.KEY_FRAGMENT_NAME, "MeFragmentNetworkUnavailable");
        fragmentNetworkUnavailable.setArguments(args);
        return fragmentNetworkUnavailable;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentActivity = (MainActivity) getActivity();
        parentActivity.isVisibleFab(false);
        parentActivity.showSearchView(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentActivity.getSupportActionBar().hide();
        rootView = inflater.inflate(R.layout.fragment_network_unavailable, container, false);
        initButton();
        return rootView;
    }

    private void initButton() {
        Button btnRetry = (Button) rootView.findViewById(R.id.btnRetry);
        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentActivity.runFragmentStart();
            }
        });
    }
}
