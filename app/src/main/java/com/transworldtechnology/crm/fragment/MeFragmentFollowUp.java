package com.transworldtechnology.crm.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.fasterxml.jackson.core.type.TypeReference;
import com.transworldtechnology.crm.R;
import com.transworldtechnology.crm.activity.MainActivity;
import com.transworldtechnology.crm.database.MeRepoFactory;
import com.transworldtechnology.crm.web.JsonMan;
import com.transworldtechnology.crm.web.MeConnectable;
import com.transworldtechnology.crm.web.MeIUrl;
import com.transworldtechnology.crm.web.MeNetworkChangeReceiver;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by root on 12/1/16.
 */
public class MeFragmentFollowUp extends Fragment
{
    public static final String KEY_COMPANY_NAME = "company_name";
    public static final String KEY_DATE = "date";
    public static final String KEY_TIME = "time";
    public static final String   KEY_IN_OUT = "inout";
    public static final String KEY_CUSTOMER_CODE = "customerCode";
    public static final String KEY_SALES_CUSTOMER_CODE = "salesCustomerCode";
    public static final String KEY_OPPORTUNITY_CODE = "opportunityCode";
    public static final String KEY_FOLLOWUP_TYPE = "followupType";
    public static final String KEY_STATUS = "status";
    public static final String KEY_SPOKEN_TO = "spokenTo";
    public static final String KEY_COMMENTS = "comments";
    public static final String KEY_CONTACT_ID = "contactId";
    public static final String KEY_CONTACT_ADDRESS = "contactAddress";
    public static final String KEY_CONTACT_CITY = "contactCity";
    public static final String KEY_CONTACT_DESIGNATION = "contactDesignation";
    public static final String KEY_CONTACT_PERSON = "contactPerson";
    private MainActivity parentActivity;
    private MeNetworkChangeReceiver networkChangeReceiver;
    private MeClick click;
    private ArrayAdapter<String> adapterSpokenTo;
    private  Map<String, Object> mapEntity=new HashMap<>();
    private ArrayList<String> listSpokenTo = new ArrayList<>();
    private List<Map<String, Object>> listSpokenToMap = new ArrayList<>();
    private MeDateListener dateListener;
    private MeTimeListener timeListener;
    private Calendar calendar;
    private View rootView;
    private ImageView imgCalendarLaunch;
    private TextView textClientName, textCurrentTime, textCurrentDate;
    private Button btnNext;
    private TextView textAddContact;
    private Spinner spinnerFollowUpType, spinnerStatus, spinnerSpokenTo;
    private EditText edtComments;
    private RadioButton rbIn, rbOut;
    private String dateToWeb, timeToWeb;

    public static MeFragmentFollowUp getInstance(String companyName, Integer customerCode, Integer salesCustomerCode, Integer opportunityCode) {
        Log.i("@Transworld", "Company Name - " + companyName);
        Log.i("@Transworld", "Customer Code - " + customerCode);
        Log.i("@Transworld", "Sales Cutomer Code - " + salesCustomerCode);
        Log.i("@Transworld", "Opportunity Code - " + opportunityCode);
        MeFragmentFollowUp meFragmentFollowUp = new MeFragmentFollowUp();
        Bundle args = new Bundle();
        args.putString(MainActivity.KEY_FRAGMENT_NAME, "MeFragmentFollowUp");
        args.putString(KEY_COMPANY_NAME, (companyName.length() > 0 ? companyName : ""));
        args.putInt(KEY_CUSTOMER_CODE, customerCode);
        args.putInt(KEY_SALES_CUSTOMER_CODE, salesCustomerCode);
        args.putInt(KEY_OPPORTUNITY_CODE, opportunityCode);
        meFragmentFollowUp.setArguments(args);
        return meFragmentFollowUp;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        parentActivity = (MainActivity) getActivity();
        parentActivity.showDrawerFab();
        parentActivity.isVisibleFab(false);
        networkChangeReceiver = new MeNetworkChangeReceiver();
        click = new MeClick();
        dateListener = new MeDateListener();
        timeListener = new MeTimeListener();
        calendar = Calendar.getInstance();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentActivity.getSupportActionBar().show();
        rootView = inflater.inflate(R.layout.fragment_follow_up, container, false);
        try
        {
            initTextView();
        } catch (Exception e) {
            e.printStackTrace();
        }

        initImageView();
        initButton();
        initSpinnerFollowUp();
        initSpinnerStatus();
        initSpinnerSpokenTo();
        initEditText();
        initRadioButton();
        parentActivity.setTitle("Follow-Up");
        return rootView;
    }

    private final void initTextView() throws Exception {
        textClientName = (TextView) rootView.findViewById(R.id.textClientName);
        Bundle args = getArguments();
        Integer s = args.getInt(KEY_SALES_CUSTOMER_CODE);
        Log.i("@Transworld", "s : " + s);
        if (args != null) {
            textClientName.setText(args.getString(KEY_COMPANY_NAME) + " [" + (s == 0 ? args.getInt(KEY_CUSTOMER_CODE) : args.getInt(KEY_SALES_CUSTOMER_CODE)) + "] [" + args.getInt(KEY_OPPORTUNITY_CODE) + "]");
            // textClientName.setText(args.getString(KEY_COMPANY_NAME));
        }
        String currentDate = "" + (calendar.get(Calendar.DAY_OF_MONTH) > 9 ? calendar.get(Calendar.DAY_OF_MONTH) : "0" + calendar.get(Calendar.DAY_OF_MONTH)) + "-" + new SimpleDateFormat("MMM").format(calendar.getTime()) + "-" + calendar.get(Calendar.YEAR);
        dateToWeb = "" + calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
        textCurrentDate = (TextView) rootView.findViewById(R.id.textCurrentDate);
        textCurrentDate.setText(currentDate);
        textCurrentDate.setOnClickListener(click);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String currentTime = sdf.format(calendar.getTime());
        timeToWeb = "" + calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);
        textCurrentTime = (TextView) rootView.findViewById(R.id.textCurrentTime);
        textCurrentTime.setText(currentTime);
        textCurrentTime.setOnClickListener(click);
    }

    private final void initSpinnerStatus()
    {
        spinnerStatus = (Spinner) rootView.findViewById(R.id.spinnerStatus);
        ArrayList<String> statusList = new ArrayList<String>();
        statusList.add("Bill Submission");
        statusList.add("Business Started");
        statusList.add("Bussiness Stopped/Restart Bussiness");
        statusList.add("Follow Up Collection");
        statusList.add("New Bussiness Started");
        statusList.add("New Enquiry/Bussiness Follow Up");
        statusList.add("Other");
        statusList.add("Payment Receipt");
        statusList.add("Quotation");
        statusList.add("Quotation Follow");
        statusList.add("Rate Revision Follow");
        statusList.add("Follow Up Collection");
        ArrayAdapter<String> arrayAdapterStatus = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, statusList);
        arrayAdapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(arrayAdapterStatus);
    }

    private final void initSpinnerFollowUp() {
        spinnerFollowUpType = (Spinner) rootView.findViewById(R.id.spinnerFollowUpType);
        ArrayList<String> followUpTypeList = new ArrayList<String>();
        followUpTypeList.add("Action Points");
        followUpTypeList.add("E-mail");
        followUpTypeList.add("Letter");
        followUpTypeList.add("Meeting");
        followUpTypeList.add("Phone Call");
        followUpTypeList.add("Visit");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, followUpTypeList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFollowUpType.setAdapter(arrayAdapter);
    }

    private final void initSpinnerSpokenTo() {
        spinnerSpokenTo = (Spinner) rootView.findViewById(R.id.spinnerSpokenTo);
        try {
            Integer salesCustomerCode = (Integer) getArguments().get(KEY_SALES_CUSTOMER_CODE);
            Integer customerCode = (Integer) getArguments().get(KEY_CUSTOMER_CODE);
            listSpokenTo.clear();
            adapterSpokenTo = new ArrayAdapter<>(parentActivity, android.R.layout.simple_list_item_1, listSpokenTo);
            spinnerSpokenTo.setAdapter(adapterSpokenTo);
            if (parentActivity.isNetworkAvailable())
                new MeTaskSpokenTo(MeRepoFactory.getLoginRepository(parentActivity.getDbHelper()).getCompanyMasterId(), salesCustomerCode, customerCode).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            else
                contactListLocally();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final void contactListLocally() {
        adapterSpokenTo.clear();
        List<String> list = new ArrayList<>();
        try {
            for (String contactPerson : MeRepoFactory.getContactList(parentActivity.getDbHelper()).getContactPerson())
                list.add(contactPerson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapterSpokenTo = new ArrayAdapter<>(parentActivity, android.R.layout.simple_list_item_1, list);
        spinnerSpokenTo.setAdapter(adapterSpokenTo);
        adapterSpokenTo.notifyDataSetChanged();
    }

    private final void initImageView()
    {
        textAddContact = (TextView) rootView.findViewById(R.id.textAddContact);
        textAddContact.setOnClickListener(click);
    }

    private final void initEditText() {
        edtComments = (EditText) rootView.findViewById(R.id.edtComments);
    }

    private final void initButton() {
        btnNext = (Button) rootView.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(click);
    }

    private final void initRadioButton() {
        rbIn = (RadioButton) rootView.findViewById(R.id.rbIn);
        rbOut = (RadioButton) rootView.findViewById(R.id.rbOut);
    }

    private final HashMap<String, Object> makePartialMap() throws Exception {
        HashMap<String, Object> mapData = new HashMap<>();
        String clientName = null;
        if (textClientName.getText().toString().length() > 0) {
            clientName = textClientName.getText().toString();
        }
        // mapData.put(KEY_COMPANY_NAME, (textClientName.getText().toString().length() > 0 ? textClientName.getText().toString() : ""));
        mapData.put(KEY_COMPANY_NAME, clientName.split("\\[")[0]);
        mapData.put(KEY_DATE, (dateListener.getDateToWeb() != null ? dateListener.getDateToWeb() : dateToWeb));
        mapData.put(KEY_TIME, (timeListener.getTimeToWeb() != null ? timeListener.getTimeToWeb() : timeToWeb));
        mapData.put(KEY_IN_OUT, (rbIn.isChecked() ? "In" : rbOut.getText()));
        mapData.put(KEY_CUSTOMER_CODE, getArguments().getInt(KEY_CUSTOMER_CODE));
        mapData.put(KEY_SALES_CUSTOMER_CODE, getArguments().getInt(KEY_SALES_CUSTOMER_CODE));
        mapData.put(KEY_FOLLOWUP_TYPE, (spinnerFollowUpType.getSelectedItem() != null ? (String) spinnerFollowUpType.getSelectedItem() : ""));
        mapData.put(KEY_STATUS, (spinnerStatus.getSelectedItem() != null) ? (String) spinnerStatus.getSelectedItem() : "");
        mapData.put(KEY_SPOKEN_TO, (spinnerSpokenTo.getSelectedItem() != null) ? (String) spinnerSpokenTo.getSelectedItem() : "");
        mapData.put(KEY_COMMENTS, edtComments.getText().toString().length() > 0 ? edtComments.getText().toString() : "");
        if (listSpokenToMap.size() != 0) {
            Log.i("@Transworld", "List Spoken to - " + listSpokenTo);
            Log.i("@Transworld", "Spiner Spoken To Pos -  - " + spinnerSpokenTo.getSelectedItemPosition());
            Map<String, Object> mapAtPos = listSpokenToMap.get(spinnerSpokenTo.getSelectedItemPosition());
            mapData.put(KEY_CONTACT_ID, (mapAtPos.get("contactId") != null) ? mapAtPos.get("contactId") : "");
            mapData.put(KEY_CONTACT_PERSON, (mapAtPos.get("contactPerson") != null) ? mapAtPos.get("contactPerson") : "");
            mapData.put(KEY_CONTACT_ADDRESS, (mapAtPos.get("address") != null) ? mapAtPos.get("address") : "");
            mapData.put(KEY_CONTACT_CITY, (mapAtPos.get("city") != null) ? mapAtPos.get("city") : "");
            mapData.put(KEY_CONTACT_DESIGNATION, (mapAtPos.get("designation") != null) ? mapAtPos.get("designation") : "");
            Log.i("@Transworld", "Map At Pos - " + mapAtPos.toString());
            Log.i("@Transworld", "Total Map Data - " + mapData.toString());
            Log.i("@Transworld", "Map Data  - " + mapData);
        }
        return mapData;
    }

   /* private void saveFollowUp() {
        MeRepoSaveFollowUp repoSaveFollowUp = new MeRepoImplSaveFollowUp(parentActivity.getDbHelper());
        try {
            repoSaveFollowUp.saveFollowUpLocally(addNewSpokenToLocally(mapEntity));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private Map<String, Object> addNewSpokenToLocally(Map<String,Object> mapEntity) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("spokenTo",mapEntity.get("spokenTo"));
        Log.i("@Transworld","Map of SpokenTo -"+map);
        return map;

    }*/


    private final Boolean isCommentsFilled() {
        return edtComments.getText().toString().length() > 0 && !(edtComments.getText().toString().startsWith(" "));
    }

    private final Boolean isSpokenToSelected() {
        return spinnerSpokenTo != null && spinnerSpokenTo.getSelectedItem() != null;
    }

    private final Boolean isFollowUpDateValid() {
        DateFormat dateFormat = new SimpleDateFormat("kk:mm:ss");
        Date date = new Date();
        String currentDate = dateFormat.format(date);
        return (currentDate.compareTo(timeListener.getTimeToWeb()) > 0) || currentDate.equals(timeListener.getTimeToWeb());
    }

    private final Boolean areAllFilled() {
        return isCommentsFilled() && isSpokenToSelected();
    }

    private final class MeClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Integer customerCode = getArguments().getInt(KEY_CUSTOMER_CODE);
            Integer salesCustomerCode = getArguments().getInt(KEY_SALES_CUSTOMER_CODE);
            Log.i("@Transworld", "CUSTOMER CODE : " + customerCode);
            Log.i("@Transworld", "SALES CUSTOMER CODE : " + salesCustomerCode
            );
            if (v.getId() == textCurrentDate.getId()) {
                DatePickerDialog datePicker = new DatePickerDialog(parentActivity, dateListener, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                datePicker.getDatePicker().setMaxDate(new Date().getTime());
                datePicker.show();
            }
            if (v.getId() == textCurrentTime.getId()) {
                TimePickerDialog timePicker = new TimePickerDialog(parentActivity, timeListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                timePicker.show();
            }
            if (v.getId() == textAddContact.getId()) {
                Log.i("@Transworld", "Customer Code - " + customerCode);
                Log.i("@Transworld", "Sales Cutomer Code - " + salesCustomerCode);
                //parentActivity.runFragmentTransaction(R.id.frameMainContainer, MeFragmentAddContact1.getInstance(textClientName.getText().toString().split("\\[")[0], customerCode, salesCustomerCode));
               // parentActivity.runFragmentTransaction(R.id.frameMainContainer,MeFragmentAddContacts.newInstance(textClientName.getText().toString().split("\\[")[0], customerCode, salesCustomerCode));
            }
            if (v.getId() == btnNext.getId()) {
                if (areAllFilled()) {
                    // if (isFollowUpDateValid()) {
                    try {
                     //   parentActivity.runFragmentTransaction(R.id.frameMainContainer, MeFragmentFollowUpNext.getInstance(makePartialMap()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //        }
                    //else
                    //      parentActivity.snack(rootView, "Set FollowUp time not greater than Current time");
                } else {
                    parentActivity.snack(rootView, !isSpokenToSelected() ? "You missed to select Spoken To :(" : "You missed comments :(");
                }
            }
        }
    }

    private final class MeDateListener implements DatePickerDialog.OnDateSetListener {
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        private String dateToWeb;

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String month = months[monthOfYear];
            String date = "" + (dayOfMonth > 9 ? dayOfMonth : "0" + dayOfMonth) + "-" + month + "-" + year;
            dateToWeb = year + "-" + (monthOfYear + 1) + "-" + (dayOfMonth > 9 ? dayOfMonth : "0" + dayOfMonth);
            textCurrentDate.setText(date);
            Log.e("date",date);
        }

        public String getDateToWeb() {
            return dateToWeb;
        }
    }

    private final class MeTimeListener implements TimePickerDialog.OnTimeSetListener {
        private String timeToWeb;

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String time = "" + (hourOfDay > 9 ? hourOfDay : "0" + hourOfDay) + ":" + (minute > 9 ? minute : "0" + minute);
            timeToWeb = "" + (hourOfDay > 9 ? hourOfDay : "0" + hourOfDay) + ":" + (minute > 9 ? minute : "0" + minute) + ":" + calendar.get(Calendar.SECOND);
            textCurrentTime.setText(time);
        }

        public String getTimeToWeb() {
            return timeToWeb;
        }
    }

    private final class MeConnectorSpokenTo implements MeConnectable {
        public List<Map<String, Object>> spokenToList(Integer companyMasterId, Integer customerCode, Integer salesCustomerCode) throws Exception {
            OkHttpClient client = new OkHttpClient();
            String urlPathVariable = MeIUrl.URL_ADD_CONTACT_SPOKEN_TO + companyMasterId + "/" + customerCode + "/" + salesCustomerCode;
            Log.i("@Transworld", "Url Spoken To - " + urlPathVariable);
            Request request = new Request.Builder()
                    .url(urlPathVariable)
                    .get()
                    .addHeader("Accept", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            String responsJson = response.body().string();
            Log.i("@Transworld", "Json Spoken To- " + responsJson);
            mapEntity = JsonMan.parseAnything(responsJson, new TypeReference<Map<String, Object>>() {
            });
            Log.i("@Transworld", "Map Entity - " + mapEntity);
            listSpokenToMap = (List<Map<String, Object>>) mapEntity.get("spokenTo");
            Log.i("@Transworld", "List Spoken To - " + listSpokenToMap);
            return listSpokenToMap;
        }
    }

    private class MeTaskSpokenTo extends AsyncTask<Void, Void, List<Map<String, Object>>> {
        private MeConnectorSpokenTo connector;
        private Integer companyMasterId, customerCode, salesCustomerId;

        public MeTaskSpokenTo(Integer companyMasterId, Integer customerCode, Integer salesCustomerCode) {
            this.connector = new MeConnectorSpokenTo();
            this.companyMasterId = companyMasterId;
            this.customerCode = customerCode;
            this.salesCustomerId = salesCustomerCode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            adapterSpokenTo.clear();
            listSpokenTo.clear();
        }

        @Override
        protected List<Map<String, Object>> doInBackground(Void... params) {
            try {
                listSpokenToMap = connector.spokenToList(companyMasterId, customerCode, salesCustomerId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ((listSpokenToMap != null) ? listSpokenToMap : new ArrayList<Map<String, Object>>());
        }

        @Override
        protected void onPostExecute(List<Map<String, Object>> map) {
            super.onPostExecute(map);
            listSpokenTo.clear();
            for (Map<String, Object> mapItem : map) {
                listSpokenTo.add((String) mapItem.get("contactPerson"));
            }
            adapterSpokenTo.notifyDataSetChanged();
        }
    }
}
