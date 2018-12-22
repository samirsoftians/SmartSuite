package com.transworldtechnology.crm.fragment;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.transworldtechnology.crm.R;
import com.transworldtechnology.crm.activity.MainActivity;
import com.transworldtechnology.crm.web.MeNetworkChangeReceiver;

import java.util.ArrayList;

/**
 * Created by root on 16/5/16.
 */
public class MeFragmentAgingReportSelect extends Fragment {
    private View rootView;
    private MainActivity parentActivity;
    private MeNetworkChangeReceiver networkChangeReceiver;

    public static MeFragmentAgingReportSelect getInstance() {
        MeFragmentAgingReportSelect meFragmentAgingReportSelect = new MeFragmentAgingReportSelect();
        Bundle args = new Bundle();
        args.putString(MainActivity.KEY_FRAGMENT_NAME, "MeFragmentAgingReport");
        meFragmentAgingReportSelect.setArguments(args);
        return meFragmentAgingReportSelect;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentActivity = (MainActivity) getActivity();
        parentActivity.isVisibleFab(false);
        parentActivity.showSearchView(false);
        networkChangeReceiver = new MeNetworkChangeReceiver();
    }

    @Override
    public void onResume() {
        super.onResume();
        parentActivity.registerReceiver(networkChangeReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    @Override
    public void onPause() {
        super.onPause();
        parentActivity.unregisterReceiver(networkChangeReceiver);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_aging_report_select,container,false);
        initSpinnerMin();
        initSpinnerMax();
        initSpinnerRecords();
        initSpinnerSortBy();
        initSpinnerActiveSince();
        initSpinnerGroup();
        initButton();
        return rootView;
    }

    private void initButton() {
        Button btnAgingGo = (Button) rootView.findViewById(R.id.btnAgingGo);
        btnAgingGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    private void initSpinnerRecords() {
        Spinner spinnerRecords = (Spinner) rootView.findViewById(R.id.spinnerRecords);
        ArrayList<String> recordList = new ArrayList<String>();
        recordList.add("10");
        recordList.add("50");
        recordList.add("100");
        recordList.add("All");
        ArrayAdapter<String> arrayAdapterStatus = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, recordList);
        arrayAdapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRecords.setAdapter(arrayAdapterStatus);
    }

    private void initSpinnerSortBy() {
        Spinner spinnerSortBy = (Spinner) rootView.findViewById(R.id.spinnerSortBy);
        ArrayList<String> sortByList = new ArrayList<String>();
        sortByList.add("Amount");
        sortByList.add("Last Transactions");
        ArrayAdapter<String> arrayAdapterStatus = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, sortByList);
        arrayAdapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSortBy.setAdapter(arrayAdapterStatus);
    }

    private void initSpinnerActiveSince() {
        Spinner spinnerActiveSince = (Spinner) rootView.findViewById(R.id.spinnerActiveSince);
        ArrayList<String> activeSinceList = new ArrayList<String>();
        activeSinceList.add("1yr");
        activeSinceList.add("2yrs");
        activeSinceList.add("3yrs");
        activeSinceList.add("All");
        ArrayAdapter<String> arrayAdapterStatus = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, activeSinceList);
        arrayAdapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerActiveSince.setAdapter(arrayAdapterStatus);
    }

    private void initSpinnerGroup() {
        Spinner spinnerGroup = (Spinner) rootView.findViewById(R.id.spinnerGroup);
        ArrayList<String> groupList = new ArrayList<String>();
        groupList.add("Compressor");
        groupList.add("DGSet");
        groupList.add("DigitalEquipment");
        groupList.add("Endoscope");
        groupList.add("All");
        ArrayAdapter<String> arrayAdapterStatus = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, groupList);
        arrayAdapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGroup.setAdapter(arrayAdapterStatus);
    }

    private void initSpinnerMax() {
        Spinner spinnerBalMax = (Spinner) rootView.findViewById(R.id.spinnerBalMax);
        ArrayList<String> maxList = new ArrayList<String>();
        maxList.add("0");
        maxList.add(">10,000");
        maxList.add(">50,000");
        maxList.add(">1,00,000");
        maxList.add(">10,00,000");
        ArrayAdapter<String> arrayAdapterStatus = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, maxList);
        arrayAdapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBalMax.setAdapter(arrayAdapterStatus);
    }

    private void initSpinnerMin() {
        Spinner spinnerBalMin = (Spinner) rootView.findViewById(R.id.spinnerBalMin);
        ArrayList<String> minList = new ArrayList<String>();
        minList.add("<10,000");
        minList.add("<50,000");
        minList.add("<1,00,000");
        minList.add("<10,00,000");
        ArrayAdapter<String> arrayAdapterStatus = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, minList);
        arrayAdapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBalMin.setAdapter(arrayAdapterStatus);
    }
}
