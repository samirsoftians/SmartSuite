package com.transworldtechnology.crm.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.transworldtechnology.crm.R;
import com.transworldtechnology.crm.activity.MainActivity;
import com.transworldtechnology.crm.database.MeRepoFactory;

/**
 * Created by root on 28/3/16.
 */
public class MeFragmentSplash extends Fragment {
    private View rootView;
    private MainActivity parentActivity;
    private Animation animationBlink,animationZoomIn,animationBounce;
    private Integer companyMId = -1;

    public static MeFragmentSplash getInstance() {
        MeFragmentSplash meFragmentSpash = new MeFragmentSplash();

        Bundle args = new Bundle();
        args.putString(MainActivity.KEY_FRAGMENT_NAME, "MeFragmentSplash");
        meFragmentSpash.setArguments(args);
        return meFragmentSpash;
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
        rootView = inflater.inflate(R.layout.fragment_welcome_page, container, false);
        animationBlink = AnimationUtils.loadAnimation(getContext(),
                R.anim.anim_blink);
        animationZoomIn = AnimationUtils.loadAnimation(getContext(),R.anim.anim_zoom_in);
        animationBounce = AnimationUtils.loadAnimation(getContext(),R.anim.anim_bounce);

        TextView textViewSmart = (TextView) rootView.findViewById(R.id.textViewSmart);
        TextView textViewLaunching = (TextView) rootView.findViewById(R.id.textViewLaunching);

        textViewSmart.startAnimation(animationZoomIn);
        textViewLaunching.startAnimation(animationBounce);
        try {
            companyMId = MeRepoFactory.getLoginRepository(parentActivity.getDbHelper()).getCompanyMasterId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if ((companyMId != -1) && (!parentActivity.isNetworkAvailable())){
            textViewSmart.setVisibility(View.GONE);
            textViewLaunching.setVisibility(View.GONE);
            buildAlertMessage();
        }
        return rootView;
    }

    private void buildAlertMessage() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("No Internet connection, Proceed with offline mode")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        parentActivity.runFragmentTransaction(R.id.frameMainContainer, MeFragmentAppointment.getInstance());
                        // runFragmentTransaction(R.id.frameMainContainer, MeFragmentDashboard.getInstance());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                        parentActivity.runFragmentTransaction(R.id.frameMainContainer, MeFragmentNetworkUnavailable.getInstance());
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
