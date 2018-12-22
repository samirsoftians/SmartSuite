package com.transworldtechnology.crm.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.kevinsawicki.http.HttpRequest;
import com.transworldtechnology.crm.R;
import com.transworldtechnology.crm.activity.MainActivity;
import com.transworldtechnology.crm.database.repository.MeRepoImplSaveFollowUp;
import com.transworldtechnology.crm.database.repository.MeRepoSaveFollowUp;
import com.transworldtechnology.crm.prefs.MePrefs;
import com.transworldtechnology.crm.web.JsonMan;
import com.transworldtechnology.crm.web.MeConnectable;
import com.transworldtechnology.crm.web.MeIUrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MeFragmentAppointmentDetails extends Fragment
{
    public static final String KEY_FRAGMENT_NAME = "key_fragment_name";
    public static final String KEY_COMPANY_MASTER_ID = "company_master_id";
    public static final String KEY_COMPANY_NAME = "company_name";
    public static final String KEY_SR_NO = "sr_no";
    public static final String KEY_FROM_FRAGMENT = "fromFragment";
    private MainActivity parentActivity;
    private static Boolean isPop;
    private LocationManager locationManager;
    private static final int INTENT_ENABLED_GPS_ON = 1217;
    public static MeFragmentAppointmentDetails getInstance(String companyNameAppointment, Integer companyMasterId, Integer theSrno,String fromFragment)
    {
        MeFragmentAppointmentDetails meFragmentAppointmentDetails = new MeFragmentAppointmentDetails();
        Bundle args = new Bundle();
        isPop = true;
        args.putString(KEY_FRAGMENT_NAME, "MeFragmentAppointmentDetails");
        args.putInt(KEY_COMPANY_MASTER_ID, companyMasterId);
        args.putString(KEY_COMPANY_NAME, companyNameAppointment);
        args.putInt(KEY_SR_NO, theSrno);
        args.putString(KEY_FROM_FRAGMENT,fromFragment);
        meFragmentAppointmentDetails.setArguments(args);
        return meFragmentAppointmentDetails;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentActivity = (MainActivity) getActivity();
        parentActivity.isVisibleFab(false);
        parentActivity.showSearchView(false);
        parentActivity.isToolbarClickable();
        locationManager = (LocationManager) parentActivity.getSystemService(Context.LOCATION_SERVICE);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.custom_appointments_details, container, false);
        parentActivity.clearListView();
        parentActivity.isToolbarClickable();
        if (!getArguments().get(KEY_FROM_FRAGMENT).equals("Appointment")){
            rootView.findViewById(R.id.btnTakeFollowUp).setVisibility(View.GONE);
        }
        if (isPop == false) {
            showAppointmentsDetails(rootView, (HashMap<String, Object>) MePrefs.getKeyAppointmentDetails(getContext()));
            parentActivity.setTitle(MePrefs.getKeyCompanyNameTemp(getContext()));
        } else {
            if (parentActivity.isNetworkAvailable()) {
                new MeTaskAppointmentDetails(getArguments().getInt(KEY_COMPANY_MASTER_ID), getArguments().getInt(KEY_SR_NO)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                showAppointmentsDetails(rootView, getAppointmentDetailsLocal(getArguments().getInt(KEY_SR_NO)));
                //MePrefs.saveAppointment(getContext(), mapFollowUpData);
            }
            parentActivity.setTitle(getArguments().getString(KEY_COMPANY_NAME));
        }
        isPop = false;
        return rootView;
    }

    private HashMap<String, Object> getAppointmentDetailsLocal(Integer theSrno) {
        Log.i("@Transworld", "Appointment Details offline MEthod" + theSrno);
        HashMap<String, Object> appointmentDetailsMap = new HashMap<>();
        MeRepoSaveFollowUp saveFollowUp = new MeRepoImplSaveFollowUp(parentActivity.getDbHelper());
        try {
            List<HashMap<String, Object>> appointmentDetails = saveFollowUp.getAppointmentDetails(theSrno);
            if (appointmentDetails != null)
                for (HashMap<String, Object> map : appointmentDetails) {
                    appointmentDetailsMap = map;
                }
            Log.i("@Transworld", "APpointment Detils offline" + appointmentDetails);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return appointmentDetailsMap;
    }

    private void showAppointmentsDetails(final View rootView, final HashMap<String, Object> map) {
        ((LinearLayout)rootView.findViewById(R.id.layoutNextFollowUp)).setVisibility(View.VISIBLE);
        ((Button)rootView.findViewById(R.id.btnTakeFollowUp)).setVisibility(View.VISIBLE);

        ((TextView) rootView.findViewById(R.id.txtFollowUpEnteredBy)).setText(""+map.get("enteredBy"));
        ((TextView) rootView.findViewById(R.id.txtViewContactPerson)).setText(""+map.get("contactPerson"));
        ((TextView) rootView.findViewById(R.id.txtNextFollowUpTypeApp)).setText(Html.fromHtml(""+ map.get("nextFollowUpType")));
        ((TextView) rootView.findViewById(R.id.txtAppointmentDate)).setText(Html.fromHtml(map.get("nextFollowUpDate") + " " + map.get("nextFollowUpTime")));
        if (map.get("preparation").equals(""))
            ((TextView) rootView.findViewById(R.id.txtPreparation)).setText(Html.fromHtml("-"));
        else
            ((TextView) rootView.findViewById(R.id.txtPreparation)).setText(Html.fromHtml(""+map.get("preparation")));
        if (map.get("remarks").equals(""))
            ((TextView) rootView.findViewById(R.id.txtRemarks)).setText(Html.fromHtml("-"));
        else
            ((TextView) rootView.findViewById(R.id.txtRemarks)).setText(Html.fromHtml("" + map.get("remarks")));
        if (map.get("paymentFollowUp").equals("YES")) {
            ((TextView) rootView.findViewById(R.id.txtPayment)).setText(Html.fromHtml("Rs." + map.get("amountExpected")));
            ((TextView) rootView.findViewById(R.id.txtPaymentDate)).setText(Html.fromHtml("" + map.get("amountExpectedOn")));
        } else {
            ((TextView) rootView.findViewById(R.id.txtPayment)).setText(Html.fromHtml("-"));
            ((TextView) rootView.findViewById(R.id.txtPaymentDate)).setText(Html.fromHtml("-"));
        }
        rootView.findViewById(R.id.txtPrevFollowUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootView.findViewById(R.id.txtHideDetails).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.txtPrevFollowUp).setVisibility(View.GONE);
                final LinearLayout linearLayoutPrevFollowUp = (LinearLayout) rootView.findViewById(R.id.linearLayoutPrevFollowUp);
                linearLayoutPrevFollowUp.setVisibility(View.VISIBLE);
                ((TextView) rootView.findViewById(R.id.txtCommentsPrev)).setText(Html.fromHtml("" + map.get("comments")));
                // ((TextView)rootView.findViewById(R.id.txtViewContactPerson)).setText((String) map.get("contactPerson"));
                ((TextView) rootView.findViewById(R.id.txtFollowUpDateTime)).setText(Html.fromHtml("" + map.get("followUpDate") + " " + map.get("followUpTime")));
                ((TextView) rootView.findViewById(R.id.txtFollowUpTypePrev)).setText(Html.fromHtml(map.get("followUpType") + "(" + map.get("followUpInOut") + ")"));
                ((TextView) rootView.findViewById(R.id.txtStatusPrev)).setText(Html.fromHtml("" + map.get("status")));
//                if (map.get("assignedBy"))
//                ((TextView) rootView.findViewById(R.id.txtFollowUpAssignedBy)).setText(Html.fromHtml("<b>Assigned By &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp:</b>"+map.get("assignedBy")));
            }
        });
        rootView.findViewById(R.id.txtHideDetails).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootView.findViewById(R.id.txtHideDetails).setVisibility(View.GONE);
                rootView.findViewById(R.id.txtPrevFollowUp).setVisibility(View.VISIBLE);
                final LinearLayout linearLayoutPrevFollowUp = (LinearLayout) rootView.findViewById(R.id.linearLayoutPrevFollowUp);
                linearLayoutPrevFollowUp.setVisibility(View.GONE);
            }
        });
        rootView.findViewById(R.id.btnTakeFollowUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (parentActivity.isNetworkAvailable()) {
                    MePrefs.clearSharedPrefs(getContext());
                    new MeTaskTakeFollowUpFromCompanyName(getArguments().getInt(KEY_COMPANY_MASTER_ID), getArguments().getString(KEY_COMPANY_NAME)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }

                else {
                    if(getView()!=null)
                    parentActivity.snack(rootView,"Sorry!!Customer Data Not Available");
                }
            }
        });
    }
    private final class MeConnectorAppointmentDetails implements MeConnectable
    {
        private String jsonMap;

        public MeConnectorAppointmentDetails()
        {

        }
        public Map<String, Object> listOfAppointmentDetails(Integer companyMasterId, Integer theSrno) throws Exception
        {
            Map<String, Object> mapEntity = new HashMap<>();
            jsonMap = HttpRequest.get(MeIUrl.URL_APPOINTMENT_LIST_DETAILS + companyMasterId + "/" + theSrno)
                    .body();
            Log.i("@Transworld", MeIUrl.URL_APPOINTMENT_LIST_DETAILS + companyMasterId + "/" + theSrno);
            Log.i("@Transworld", "Appointmnet Details - " + jsonMap);
            mapEntity = JsonMan.parseAnything(jsonMap, new TypeReference<Map<String, Object>>() {
            });
            return mapEntity;
        }
    }

    private class MeTaskAppointmentDetails extends AsyncTask<Void, Void, Map<String, Object>> {
        Integer companyMasterId, theSrno;
        ProgressDialog progressDialog;
        private MeConnectorAppointmentDetails meConnectorAppointmentDetails;

        public MeTaskAppointmentDetails(Integer companyMasterId, Integer theSrno) {
            this.companyMasterId = companyMasterId;
            this.theSrno = theSrno;
            meConnectorAppointmentDetails = new MeConnectorAppointmentDetails();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(parentActivity, "Follow Up Info.", "Loading...");
        }

        @Override
        protected Map<String, Object> doInBackground(Void... params) {
            Map<String, Object> mapEntity = null;
            Map<String, Object> mapFollowUpData = null;
            try {
                mapFollowUpData = meConnectorAppointmentDetails.listOfAppointmentDetails(companyMasterId, theSrno);
                if (mapFollowUpData != null) {
                    mapEntity = (Map<String, Object>) mapFollowUpData.get("FollowUpData");
                    Log.i("@Transworld", "FollowUpData Details : " + mapEntity);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mapFollowUpData;
        }

        @Override
        protected void onPostExecute(Map<String, Object> stringObjectMap) {
            progressDialog.dismiss();
            super.onPostExecute(stringObjectMap);
            if (stringObjectMap != null) {
                if (stringObjectMap.get("status").equals("success")) {
                    HashMap<String, Object> mapFollowUpData = (HashMap<String, Object>) stringObjectMap.get("FollowUpData");
                    MePrefs.saveAppointment(getContext(), mapFollowUpData);
                    showAppointmentsDetails(getView(), mapFollowUpData);
                }
            }
        }
    }

    private final class MeConnectorTakeFollowUp implements MeConnectable {
        public List<Map<String, Object>> takeFollowUpFromCompanyName(Integer companyMasterId, String companyName, List<Map<String, Object>> listTakeFollowUpMap) throws Exception {
            OkHttpClient client = new OkHttpClient();
            String urlPathVariable = MeIUrl.URL_TAKE_FOLLOWUP_FROM_COMPANY_NAME + companyMasterId + "/" + companyName.replace(" ", "%20");
            Log.i("@Transworld", "Url Take followUp from company name - " + urlPathVariable);
            Request request = new Request.Builder()
                    .url(urlPathVariable)
                    .get()
                    .addHeader("Accept", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            String responsJson = response.body().string();
            Log.i("@Transworld", "Json Take followUp from company name- " + responsJson);
            Map<String, Object> mapEntity = JsonMan.parseAnything(responsJson, new TypeReference<Map<String, Object>>() {
            });
            Log.i("@Transworld", "Map Entity - " + mapEntity);
            listTakeFollowUpMap = (List<Map<String, Object>>) mapEntity.get("companies");
            Log.i("@Transworld", "List Take followUp - " + listTakeFollowUpMap);
            return listTakeFollowUpMap;
        }
    }

    private final class MeTaskTakeFollowUpFromCompanyName extends AsyncTask<Void, Void, List<Map<String, Object>>> {
        private MeConnectorTakeFollowUp connector;
        private ArrayList<Integer> listTakeFollowUp = new ArrayList<>();
        private Integer companyMasterId;
        private String companyName;
        private Integer opportunityCode;
        private Integer customerCode;
        private Integer salesCustomerCode;
        private ProgressDialog progressDialog;

        public MeTaskTakeFollowUpFromCompanyName(Integer companyMasterId, String companyName) {
            this.connector = new MeConnectorTakeFollowUp();
            ;
            this.companyMasterId = companyMasterId;
            this.companyName = companyName;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(parentActivity, "", "please wait..");
        }

        @Override
        protected List<Map<String, Object>> doInBackground(Void... params) {
            List<Map<String, Object>> listTakeFollowUpMap = new ArrayList<>();
            try {
                listTakeFollowUpMap = connector.takeFollowUpFromCompanyName(companyMasterId, companyName, listTakeFollowUpMap);
                Log.i("@Transworld", "listTakeFollowUpMap" + listTakeFollowUpMap.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listTakeFollowUpMap;
        }

        @Override
        protected void onPostExecute(List<Map<String, Object>> map) {
            super.onPostExecute(map);
            progressDialog.dismiss();
            //Log.i("@Transworld","listTakeFollowUpMap"+map.toString());

            if (map != null) {
                MePrefs.saveCompanyInfoTemp(getContext(), map, 0);
                for (Map<String, Object> mapItem : map) {
                    opportunityCode = (Integer) mapItem.get("opportunityCode");
                    customerCode = (Integer) mapItem.get("customerCode");
                    salesCustomerCode = (Integer) mapItem.get("salesCustomerCode");
                }
                Log.i("@Transworld", "listTakeFollowUp - " + listTakeFollowUp);

                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    buildAlertMessageNoGps();
                } else {
                    runFragmentFollowUp();
                   // parentActivity.runFragmentTransaction(R.id.frameMainContainer, MeFragmentFollowUpNext.getInstance(companyName, customerCode, salesCustomerCode, opportunityCode, "AppointmentFragment"));
                }
                //parentActivity.runFragmentTransaction(R.id.frameMainContainer, MeFragmentFollowUpNext.getInstance(companyName, customerCode, salesCustomerCode, opportunityCode, "AppointmentFragment"));
            }
        }
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
                        dialog.dismiss();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("@Transworld", "Result Code before if: " + resultCode);
        if (resultCode == Activity.RESULT_OK || resultCode == Activity.RESULT_FIRST_USER) {
            Log.i("@Transworld", "Result Code After if: " + resultCode);
            Log.i("@Transworld", "Location Manager : " + locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER));
        }
        if (locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)) {
            runFragmentFollowUp();
        }
    }

    private void runFragmentFollowUp() {
        parentActivity.runFragmentTransaction(R.id.frameMainContainer, MeFragmentFollowUpNext.getInstance(MePrefs.getKeyCompanyNameTemp(getContext()),
                MePrefs.getKeyCustomerCodeTemp(getContext()), MePrefs.getKeySaleCustCodeTemp(getContext()), MePrefs.getKeyOpportunityCodeTemp(getContext()), "AppointmentFragment"));
    }
}
