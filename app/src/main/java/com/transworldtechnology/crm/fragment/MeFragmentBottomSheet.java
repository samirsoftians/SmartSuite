package com.transworldtechnology.crm.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.design.widget.BottomSheetDialogFragment;
import android.widget.TextView;

import com.transworldtechnology.crm.R;
import com.transworldtechnology.crm.activity.MainActivity;
import com.transworldtechnology.crm.prefs.MePrefs;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragmentBottomSheet extends BottomSheetDialogFragment {
    private View rootView;
    private MainActivity parentActivity;
    private LocationManager locationManager;
    private static final int INTENT_ENABLED_GPS_ON = 1215;
    public MeFragmentBottomSheet()
    {
        // Required empty public constructor
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        parentActivity = (MainActivity) getActivity();
        locationManager = (LocationManager) parentActivity.getSystemService(Context.LOCATION_SERVICE);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.bottom_sheet_activity_main, container, false);
        initGridText();
        return rootView;
    }

    private void initGridText() {
        TextView textAddFollowUp = (TextView) rootView.findViewById(R.id.textAddFollowUp);
        TextView textAddCustomer = (TextView) rootView.findViewById(R.id.textAddCustomer);
    /*    TextView textSearchCompany = (TextView) rootView.findViewById(R.id.textSearchCompany);

        textSearchCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentActivity.closeSearchBar();
                MePrefs.clearSharedPrefs(getContext());
                parentActivity.showSearchView(true);
                getView().setVisibility(View.GONE);
                dismiss();
            }
        });
*/
        textAddCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               parentActivity.runFragmentTransaction(R.id.frameMainContainer,MeFragmentAddCustomer.newInstance());
                dismiss();
            }
        });
        textAddFollowUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MePrefs.getKeyCompanyName(getContext()).equals("none")) {
                    dismiss();
                    parentActivity.snack(parentActivity.getCurrentFocus(), "Please Select Customer");
                } else {
                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        buildAlertMessageNoGps();
                    } else {
                        dismiss();
                        runFragmentFollowUp();
                    }
                }
            }
        });
    }

    private void runFragmentFollowUp() {
        parentActivity.runFragmentTransaction(R.id.frameMainContainer,
                MeFragmentFollowUpNext.getInstance(MePrefs.getKeyCompanyName(getContext()),
                        MePrefs.getKeyCustomerCode(getContext()),
                        MePrefs.getKeySaleCustCode(getContext()),
                        MePrefs.getKeyOpportunityCode(getContext()),"BottomSheet"));
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setTitle("GPS")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                       startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), INTENT_ENABLED_GPS_ON);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dismiss();
                        dialog.dismiss();

                        runFragmentFollowUp();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("@Transworld", "Result Code before if: " + resultCode);
        if (resultCode == Activity.RESULT_OK || resultCode == Activity.RESULT_FIRST_USER) {
            Log.i("@Transworld", "Result Code After if: " + resultCode);
            Log.i("@Transworld", "Location Manager : " + locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER));
        }
        if (locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)) {
            dismiss();
            runFragmentFollowUp();
        }
    }
}
