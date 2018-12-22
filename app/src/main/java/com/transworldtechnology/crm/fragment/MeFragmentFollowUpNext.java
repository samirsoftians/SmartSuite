package com.transworldtechnology.crm.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.kevinsawicki.http.HttpRequest;
import com.transworldtechnology.crm.R;
import com.transworldtechnology.crm.activity.MainActivity;
import com.transworldtechnology.crm.compress.MeTaskImageCompression;
import com.transworldtechnology.crm.connector.GoogleApiConnector;
import com.transworldtechnology.crm.database.MeRepoFactory;
import com.transworldtechnology.crm.database.repository.MeRepoContactDet;
import com.transworldtechnology.crm.database.repository.MeRepoFollowUpDocs;
import com.transworldtechnology.crm.database.repository.MeRepoImplContactDet;
import com.transworldtechnology.crm.database.repository.MeRepoImplFollowUpDocs;
import com.transworldtechnology.crm.database.repository.MeRepoImplSaveFollowUp;
import com.transworldtechnology.crm.database.repository.MeRepoSaveFollowUp;
import com.transworldtechnology.crm.domain.MeFollowUp;
import com.transworldtechnology.crm.domain.MeFollowUpDocs;
import com.transworldtechnology.crm.prefs.MePrefs;
import com.transworldtechnology.crm.web.JsonMan;
import com.transworldtechnology.crm.web.MeConnectable;
import com.transworldtechnology.crm.web.MeIUrl;
import com.transworldtechnology.crm.web.MeNetworkChangeReceiver;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.changer.polypicker.Config;
import nl.changer.polypicker.ImagePickerActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by root on 13/1/16.
 */
public class MeFragmentFollowUpNext extends Fragment
{
    public static final String KEY_MAP_PARTIAL = "mapPartial";
    private static final int INTENT_REQUEST_GET_IMAGES = 1501;
    public static final String KEY_COMPANY_NAME = "company_name";
    public static final String KEY_DATE = "date";
    public static final String KEY_TIME = "time";
    public static final String KEY_IN_OUT = "inout";
    public static final String KEY_FROM_FRAGMENT = "fromFragment";
    public static final String KEY_CUSTOMER_CODE = "customerCode";
    public static final String KEY_SALES_CUSTOMER_CODE = "salesCustomerCode";
    public static final String KEY_OPPORTUNITY_CODE = "opportunityCode";
    private MainActivity parentActivity;
    private MeNetworkChangeReceiver networkChangeReceiver;
    private MeDateListener dateListenerNextFollowUp;
    private MeDateListener dateListenerAmtExcpt;
    private MeClick click;
    private HashMap<String, Object> mapPartial;
    private MeWatcherSpecialEmail watcherEmail;
    private List<Map<String, Object>> listSpecialEmailMap = new ArrayList<>();
    private ArrayList<String> listSpecialEmail = new ArrayList<>();
    private ArrayAdapter<String> adapterSpecialEmail;
    private ArrayList<String> listAssignTo = new ArrayList<>();
    private List<Map<String, Object>> listAssignToMap = new ArrayList<>();
    private ArrayAdapter<String> adapterAssignTo;
    private Uri uriFileCapturedFollowup;
    private View rootView;
    private Button btnSubmit;
    private Spinner spinnerFollowUpTypeNext, spinnerAssignTo;
    private CheckBox chkPaymentFollowUp;
    private RelativeLayout relativeAmount;
    private TextView textClientName, textPaymentDate, textNextDate, textNextTime, textDocPath, textSelectAssignTo, textViewImage;
    private EditText edtAmountExpected, edtPreparation, edtRemarks;
    private MultiAutoCompleteTextView autoSpecialEmail;
    private ImageView imageCalendar, imgAttachFile, imgAmtExpctOn;
    private LocationManager locationManager;
    private String provider;
    private ArrayList<String> uriList = new ArrayList<>();
    private File[] multipartDocs;
    private RelativeLayout relativeAssignTo;
    private ArrayList<String> imageList = new ArrayList<String>();
    private Map<String, Object> followUpDocsMap = new HashMap<>();
    private ArrayList<String> listSpokenTo = new ArrayList<>();
    private List<Map<String, Object>> listSpokenToMap = new ArrayList<>();
    private MeDateListenerFollowUp dateListener;
    private MeTimeListenerFollowUp timeListener;
    private MeTimeListener timeListenerNextFollowUp;
    private Calendar calendar;
    private ImageView imgCalendarLaunch;
    private TextView textCurrentTime, textCurrentDate;
    private Button btnNext;
    private TextView textAddContact;
    private Spinner spinnerFollowUpType, spinnerStatus, spinnerSpokenTo;
    private EditText edtComments;
    private RadioButton rbIn, rbOut;
    private String dateToWeb, timeToWebFollowUp,timeToWebNextFollowUp;
    private ArrayAdapter<String> adapterSpokenTo;
    private TextView textAddNewFollowUp;
    private LinearLayout layoutNextFollowUp;
    private GoogleApiConnector googleApiConnector;

    /*   public static MeFragmentFollowUpNext getInstance(HashMap<String, Object> mapPartial) {
           MeFragmentFollowUpNext meFragmentFollowUp = new MeFragmentFollowUpNext();
           Bundle args = new Bundle();
           args.putString(MainActivity.KEY_FRAGMENT_NAME, "MeFragmentFollowUpNext");
           args.putSerializable(KEY_MAP_PARTIAL, mapPartial);
           meFragmentFollowUp.setArguments(args);
           return meFragmentFollowUp;
       }
   */
    public static MeFragmentFollowUpNext getInstance(String companyName, Integer customerCode, Integer salesCustomerCode, Integer opportunityCode, String fromFragment) {
        Log.i("@Transworld", "Company Name - " + companyName);
        Log.i("@Transworld", "Customer Code - " + customerCode);
        Log.i("@Transworld", "Sales Cutomer Code - " + salesCustomerCode);
        Log.i("@Transworld", "Opportunity Code - " + opportunityCode);
        MeFragmentFollowUpNext meFragmentFollowUp = new MeFragmentFollowUpNext();
        Bundle args = new Bundle();
        args.putString(MainActivity.KEY_FRAGMENT_NAME, "MeFragmentFollowUp");
        args.putString(KEY_COMPANY_NAME, (companyName.length() > 0 ? companyName : ""));
        args.putString(KEY_FROM_FRAGMENT, fromFragment);
        args.putInt(KEY_CUSTOMER_CODE, customerCode);
        args.putInt(KEY_SALES_CUSTOMER_CODE, salesCustomerCode);
        args.putInt(KEY_OPPORTUNITY_CODE, opportunityCode);
        meFragmentFollowUp.setArguments(args);
        return meFragmentFollowUp;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentActivity = (MainActivity) getActivity();
        parentActivity.isVisibleFab(false);
        parentActivity.isToolbarClickable();
        parentActivity.showSearchView(false);
        googleApiConnector = new GoogleApiConnector();
        googleApiConnector.buildGoogleApiClient(getContext());
        googleApiConnector.connectGoogleApi();
        // parentActivity.buildApiClient();
        networkChangeReceiver = new MeNetworkChangeReceiver();
        click = new MeClick();
        dateListenerAmtExcpt = new MeDateListener();
        dateListenerNextFollowUp = new MeDateListener();
        timeListenerNextFollowUp = new MeTimeListener();
        dateListener = new MeDateListenerFollowUp();
        timeListener = new MeTimeListenerFollowUp();
        calendar = Calendar.getInstance();
        mapPartial = (HashMap<String, Object>) getArguments().getSerializable(KEY_MAP_PARTIAL);
        watcherEmail = new MeWatcherSpecialEmail();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentActivity.getSupportActionBar().show();
        rootView = inflater.inflate(R.layout.fragment_add_follow_up_, container, false);
        parentActivity.setTitle("Add Follow-Up");
        parentActivity.hideKeyboard(rootView);
        parentActivity.isToolbarClickable();
        initImages();
        try {
            initTextViewFollowUp();
        } catch (Exception e) {
            e.printStackTrace();
        }
        initSpinnerStatusFollowUp();
        initSpinnerFollowUp();
        initSpinnerSpokenToFollowUp();
        initImageViewFollowUp();
        initEditTextFollowUp();
        initRadioButtonFollowUp();
        isSpokenToSelectedFollowUp();
        //isFollowUpDateValidFollowUp();
        initTextViews();
        initSpinners();
        initCheckBoxes();
        initButtons();
        initAutoComplete();
        initEditText();
        initRelativeLayout();
        initSpinnerAssignTo();
        return rootView;
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

    private final void initTextViewFollowUp() throws Exception {
        textAddNewFollowUp = (TextView) rootView.findViewById(R.id.textAddNewFollowUp);
        textAddNewFollowUp.setPaintFlags(textAddNewFollowUp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        final TextView textNextFollowUp = (TextView) rootView.findViewById(R.id.textNextFollowUp);
        final LinearLayout layoutNextFollowUp = (LinearLayout) rootView.findViewById(R.id.layoutNextFollowUp);
        final LinearLayout layoutFollowUp = (LinearLayout) rootView.findViewById(R.id.layoutFollowUp);
        textAddNewFollowUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (areAllFilled()) {
                    if (textAddNewFollowUp.getText().toString().equals("Add Next Follow Up")) {
                        layoutNextFollowUp.setVisibility(View.VISIBLE);
                        textAddNewFollowUp.setVisibility(View.GONE);
                        textNextFollowUp.setText(Html.fromHtml("<B>Next Follow Up</B>"));
                        //  textNextFollowUp.setText("View Follow Up");
                    }
                    //else
                    //      parentActivity.snack(rootView, "Set FollowUp time not greater than Current time");
                } else {
                    if(getView()!=null)
                    parentActivity.snack(rootView, !isSpokenToSelected() ? "You missed to select Spoken To :(" : "You missed comments :(");
                }
            }
        });
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
        Log.e("current date",currentDate);
        textCurrentDate.setOnClickListener(click);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String currentTime = sdf.format(calendar.getTime());
        timeToWebFollowUp = "" + calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);
        textCurrentTime = (TextView) rootView.findViewById(R.id.textCurrentTime);
        textCurrentTime.setText(currentTime);
        textCurrentTime.setOnClickListener(click);
    }

    private final void initSpinnerStatusFollowUp() {
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
        ArrayAdapter<String> arrayAdapterStatus = new ArrayAdapter<String>(getContext(), R.layout.spinner_textview, statusList);
        //   arrayAdapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(arrayAdapterStatus);
    }

    private final void initSpinnerFollowUp()
    {
        spinnerFollowUpType = (Spinner) rootView.findViewById(R.id.spinnerFollowUpType);
        ArrayList<String> followUpTypeList = new ArrayList<String>();
        followUpTypeList.add("Action Points");
        followUpTypeList.add("E-mail");
        followUpTypeList.add("Letter");
        followUpTypeList.add("Meeting");
        followUpTypeList.add("Phone Call");
        followUpTypeList.add("Visit");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_textview, followUpTypeList);
        // arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFollowUpType.setAdapter(arrayAdapter);
    }
    private final void initSpinnerSpokenToFollowUp()
    {
        spinnerSpokenTo = (Spinner) rootView.findViewById(R.id.spinnerSpokenTo);
        try {
            Integer salesCustomerCode = (Integer) getArguments().get(KEY_SALES_CUSTOMER_CODE);
            Integer customerCode = (Integer) getArguments().get(KEY_CUSTOMER_CODE);
            listSpokenTo.clear();
            adapterSpokenTo = new ArrayAdapter<>(parentActivity, R.layout.spinner_textview, listSpokenTo);
            spinnerSpokenTo.setAdapter(adapterSpokenTo);
            if (parentActivity.isNetworkAvailable())
                new MeTaskSpokenTo(MeRepoFactory.getLoginRepository(parentActivity.getDbHelper()).getCompanyMasterId(), salesCustomerCode, customerCode).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            else
                contactListLocally();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void contactListLocally()
    {
        adapterSpokenTo.clear();
        MeRepoContactDet repoContactDet = new MeRepoImplContactDet(parentActivity.getDbHelper());
        getArguments().getInt(KEY_CUSTOMER_CODE);
        try {
            repoContactDet.saveCode(getArguments().getInt(KEY_SALES_CUSTOMER_CODE), getArguments().getInt(KEY_CUSTOMER_CODE));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            for (String contactPerson : MeRepoFactory.getContactList(parentActivity.getDbHelper()).getContactPerson())
                listSpokenTo.add(contactPerson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("@Transworld", "From DataBAse:" + listSpokenTo);
        adapterSpokenTo = new ArrayAdapter<>(parentActivity, R.layout.spinner_textview, listSpokenTo);
        spinnerSpokenTo.setAdapter(adapterSpokenTo);
        adapterSpokenTo.notifyDataSetChanged();
    }
    private final void initImageViewFollowUp()
    {
        textAddContact = (TextView) rootView.findViewById(R.id.textAddContact);
        textAddContact.setOnClickListener(click);
    }

    private final void initEditTextFollowUp() {
        edtComments = (EditText) rootView.findViewById(R.id.edtComments);
    }

    private final void initRadioButtonFollowUp() {
        rbIn = (RadioButton) rootView.findViewById(R.id.rbIn);
        rbOut = (RadioButton) rootView.findViewById(R.id.rbOut);
    }

    private final Boolean isCommentsFilled() {
        //   edtComments.getText().toString().replaceAll("[\\t\\n\\r]",""); //!(edtComments.getText().toString().startsWith(" ")) ;
        return edtComments.getText().toString().length() > 0 && !(edtComments.getText().toString().startsWith(" ")) && !(edtComments.getText().toString().startsWith("\n"));
    }


    private final Boolean isSpokenToSelectedFollowUp() {
        return spinnerSpokenTo != null && spinnerSpokenTo.getSelectedItem() != null;
    }

    private final Boolean isFollowUpDateValidFollowUp() {
        DateFormat dateFormat = new SimpleDateFormat("kk:mm:ss");
        Date date = new Date();
        String currentDate = dateFormat.format(date);
        return (currentDate.compareTo(timeListenerNextFollowUp.getTimeToWeb()) > 0) || currentDate.equals(timeListenerNextFollowUp.getTimeToWeb());
    }

    private final Boolean isSpokenToSelected() {
        return spinnerSpokenTo != null && spinnerSpokenTo.getSelectedItem() != null;
    }

    private final Boolean areAllFilled() {
        return isCommentsFilled() && isSpokenToSelected();
    }
    //next Follow up

    private void initRelativeLayout() {
        relativeAssignTo = (RelativeLayout) rootView.findViewById(R.id.relativeAssignTo);
        String userLevel = null;
        try {
            userLevel = MeRepoFactory.getLoginRepository(parentActivity.getDbHelper()).getUserLevel();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("@Transworld", "USER LEVEL : " + userLevel);
        if (userLevel.equals("Level1") || userLevel.equals("Level2")) {
            textSelectAssignTo.setVisibility(View.GONE);
            relativeAssignTo.setVisibility(View.GONE);
        }
    }

    private final void initEditText() {
        edtAmountExpected = (EditText) rootView.findViewById(R.id.edtAmountExpected);
        edtPreparation = (EditText) rootView.findViewById(R.id.edtPreparation);
        edtRemarks = (EditText) rootView.findViewById(R.id.edtRemarks);
    }

    private final void initImages() {
        imageCalendar = (ImageView) rootView.findViewById(R.id.imgAmtExpctOn);
        imageCalendar.setOnClickListener(click);
        imgAttachFile = (ImageView) rootView.findViewById(R.id.imgAttachFile);
        imgAttachFile.setOnClickListener(click);
    }

    private final void initTextViews() {
        textNextDate = (TextView) rootView.findViewById(R.id.textNextDate);
        textNextDate.setOnClickListener(click);
        textNextTime = (TextView) rootView.findViewById(R.id.textNextTime);
        textNextTime.setOnClickListener(click);
        textPaymentDate = (TextView) rootView.findViewById(R.id.textPaymentDate);
        textDocPath = (TextView) rootView.findViewById(R.id.textDocPath);
        textViewImage = (TextView) rootView.findViewById(R.id.textViewImage);
        textViewImage.setOnClickListener(click);
      /*  if(uriList.size() > 0) {
            textDocPath.setText(uriList.size() + " Files Attached");
        }*/
        //textDocPath.setOnClickListener(click);
        textSelectAssignTo = (TextView) rootView.findViewById(R.id.textSelectAssignTo);
    }

    private final void initSpinners() {
        spinnerFollowUpTypeNext = (Spinner) rootView.findViewById(R.id.spinnerFollowUpTypeNext);
        ArrayList<String> followUpTypeList = new ArrayList<String>();
        followUpTypeList.add("Action Points");
        followUpTypeList.add("E-mail");
        followUpTypeList.add("Letter");
        followUpTypeList.add("Meeting");
        followUpTypeList.add("Phone Call");
        followUpTypeList.add("Visit");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(parentActivity, R.layout.spinner_textview, followUpTypeList);
        spinnerFollowUpTypeNext.setAdapter(arrayAdapter);
    }

    private final void initSpinnerAssignTo() {
        spinnerAssignTo = (Spinner) rootView.findViewById(R.id.spinnerAssignTo);
        listAssignTo.clear();
        adapterAssignTo = new ArrayAdapter<String>(parentActivity, R.layout.spinner_textview, listAssignTo);
        spinnerAssignTo.setAdapter(adapterAssignTo);
        Integer companyMasterId = null;
        try {
            companyMasterId = MeRepoFactory.getLoginRepository(parentActivity.getDbHelper()).getCompanyMasterId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (textSelectAssignTo.getVisibility() == View.VISIBLE && relativeAssignTo.getVisibility() == View.VISIBLE) {
            if (parentActivity.isNetworkAvailable())
                new MeTaskAssignTo(companyMasterId).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            else {
                assignToListFromLocal();
            }
        }
    }

    private final void initButtons() {
        btnSubmit = (Button) rootView.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(click);
    }

    private final void initCheckBoxes() {
        chkPaymentFollowUp = (CheckBox) rootView.findViewById(R.id.chkPaymentFollowUp);
        relativeAmount = (RelativeLayout) rootView.findViewById(R.id.relativeAmount);
        relativeAmount.setVisibility(View.GONE);
        chkPaymentFollowUp.setOnClickListener(click);
    }

    private final void initAutoComplete() {
        autoSpecialEmail = (MultiAutoCompleteTextView) rootView.findViewById(R.id.autoSpecialEmail);
        autoSpecialEmail.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        autoSpecialEmail.setThreshold(1);
        autoSpecialEmail.setTextColor(Color.BLACK);
        autoSpecialEmail.addTextChangedListener(watcherEmail);
    }

    private final MeFollowUp makeFullNewFollowUpLocal() throws Exception {
        MeFollowUp followUp = new MeFollowUp();
        String clientName = null;
        if (textClientName.getText().toString().length() > 0) {
            clientName = textClientName.getText().toString();
        }
        MeRepoContactDet repoContactDet = new MeRepoImplContactDet(parentActivity.getDbHelper());
        repoContactDet.saveContactPerson((String) spinnerSpokenTo.getSelectedItem());
        followUp.setProspCustName(clientName.split("\\[")[0]);
        followUp.setCompanyMasterId(MeRepoFactory.getLoginRepository(parentActivity.getDbHelper()).getCompanyMasterId());
        followUp.setFollowUpDate((dateListener.getDateToWeb() != null ? dateListener.getDateToWeb() : dateToWeb));
        followUp.setFollowUpTime((timeListener.getTimeToWeb() != null ? timeListener.getTimeToWeb() : timeToWebFollowUp));
        followUp.setFollowUpInOut("" + (rbIn.isChecked() ? "In" : rbOut.getText()));
        Integer salesCustCode = getArguments().getInt(KEY_SALES_CUSTOMER_CODE);
        Integer custCode = getArguments().getInt(KEY_CUSTOMER_CODE);
        followUp.setProspCustCode((salesCustCode != 0) ? salesCustCode : custCode);
        followUp.setFollowUpType((spinnerFollowUpType.getSelectedItem() != null ? (String) spinnerFollowUpType.getSelectedItem() : ""));
        followUp.setStatus((spinnerStatus.getSelectedItem() != null) ? (String) spinnerStatus.getSelectedItem() : "");
        followUp.setSpokenTo((spinnerSpokenTo.getSelectedItem() != null) ? (String) spinnerSpokenTo.getSelectedItem() : "");
        followUp.setContactPerson((spinnerSpokenTo.getSelectedItem() != null) ? (String) spinnerSpokenTo.getSelectedItem() : "");
        followUp.setComments(edtComments.getText().toString().length() > 0 ? edtComments.getText().toString().trim() : "");
        Log.i("@Transworld", "City : " + repoContactDet.getCity() + " ," + "Designation" + repoContactDet.getDesignation());
        followUp.setCity(repoContactDet.getCity());
        followUp.setDesignation(repoContactDet.getDesignation());
        followUp.setAddress(repoContactDet.getAddress());
        followUp.setContactId(repoContactDet.getContactId());
        Log.i("@Transworld", "List Spoken to Local - " + listSpokenTo);
        Log.i("@Transworld", "Spiner Spoken Selected  - " + spinnerSpokenTo.getSelectedItem());
         /*   ArrayList<Integer> mapAtPos = (Integer)spinnerSpokenTo.getSelectedItemPosition();
            followUp.setAddress((mapAtPos.get("address") != null) ? (String) mapAtPos.get("address") : "");
            followUp.setCity((mapAtPos.get("city") != null) ? (String) mapAtPos.get("city") : "");
            followUp.setDesignation((mapAtPos.get("designation") != null) ? (String) mapAtPos.get("designation") : "");
            followUp.setContactPerson((mapAtPos.get("contactPerson") != null) ? (String) mapAtPos.get("contactPerson") : "");
            followUp.setContactId((Integer) ((mapAtPos.get("contactId") != null) ? mapAtPos.get("contactId") : ""));
            Log.i("@Transworld", "Map At Pos - " + mapAtPos.toString());*/
        followUp.setMarketingRepCode(MeRepoFactory.getLoginRepository(parentActivity.getDbHelper()).getEmployeeCode());
        followUp.setNextFollowUpDate(dateListenerNextFollowUp.getDateToWeb());
        followUp.setNextFollowUpTime(timeListenerNextFollowUp.getTimeToWeb());
        followUp.setNextFollowUpType(spinnerFollowUpTypeNext.getSelectedItem() != null ? (String) spinnerFollowUpTypeNext.getSelectedItem() : "");
        followUp.setAmountExpectedOn((chkPaymentFollowUp.isChecked() == true) ? dateListenerAmtExcpt.getDateToWeb() : "");
        Log.i("@Transworld", "Date to web for expected date - " + dateListenerAmtExcpt.getDateToWeb());
        Log.i("@Transworld", "Amount Excpy :  " + edtAmountExpected.getText().toString());
        Log.i("@Transworld", "chkFollowUp : " + chkPaymentFollowUp.isChecked());
        followUp.setAmountExpected((chkPaymentFollowUp.isChecked() == true) ? edtAmountExpected.getText().toString() : "");
        followUp.setPreparation(edtPreparation.getText().toString());
        followUp.setRemarks(edtRemarks.getText().toString());
        followUp.setPaymentFollowUp(chkPaymentFollowUp.isChecked() ? "YES" : "NO");
        multipartDocs = new File[uriList.size()];
        for (Integer i = 0; i < uriList.size(); i++) {
            multipartDocs[i] = new File(uriList.get(i));
            Log.i("@Transworld", "URL For Loop  " + uriList.get(i));
        }
        //   LocationService locationService = new LocationService();
        //  Log.i("@Transworld","Lat : "+ googleApiConnector.getLatitude()+","+"Long : "+googleApiConnector.getLongitude() +","+"Source : "+googleApiConnector.getEnabledLocationProvider(getContext()));
        followUp.setMultipartFile(multipartDocs);
        followUp.setLocLat(googleApiConnector.getLatitude());
        followUp.setLocLong(googleApiConnector.getLongitude());
        followUp.setLocSource(googleApiConnector.getEnabledLocationProvider(getContext()));
        Log.i("@Transworld", "Followup Object - " + followUp);
        return followUp;
    }

    private final MeFollowUp makeFullNewFollowUp() throws Exception {
        MeFollowUp followUp = new MeFollowUp();
        String clientName = null;
        if (textClientName.getText().toString().length() > 0) {
            clientName = textClientName.getText().toString();
        }
        followUp.setProspCustName(clientName.split("\\[")[0]);
        followUp.setCompanyMasterId(MeRepoFactory.getLoginRepository(parentActivity.getDbHelper()).getCompanyMasterId());
        followUp.setFollowUpDate((dateListener.getDateToWeb() != null ? dateListener.getDateToWeb() : dateToWeb));
        followUp.setFollowUpTime((timeListener.getTimeToWeb() != null ? timeListener.getTimeToWeb() : timeToWebFollowUp));
        followUp.setFollowUpInOut("" + (rbIn.isChecked() ? "In" : rbOut.getText()));
        Integer salesCustCode = getArguments().getInt(KEY_SALES_CUSTOMER_CODE);
        Integer custCode = getArguments().getInt(KEY_CUSTOMER_CODE);
        followUp.setProspCustCode((salesCustCode != 0) ? salesCustCode : custCode);
        followUp.setFollowUpType((spinnerFollowUpType.getSelectedItem() != null ? (String) spinnerFollowUpType.getSelectedItem() : ""));
        followUp.setStatus((spinnerStatus.getSelectedItem() != null) ? (String) spinnerStatus.getSelectedItem() : "");
        followUp.setSpokenTo((spinnerSpokenTo.getSelectedItem() != null) ? (String) spinnerSpokenTo.getSelectedItem() : "");
        followUp.setComments(edtComments.getText().toString().length() > 0 ? edtComments.getText().toString().trim() : "");
        if (listSpokenToMap.size() != 0) {
            Log.i("@Transworld", "List Spoken to - " + listSpokenTo);
            Log.i("@Transworld", "Spiner Spoken To Pos -  - " + spinnerSpokenTo.getSelectedItemPosition());
            Map<String, Object> mapAtPos = listSpokenToMap.get(spinnerSpokenTo.getSelectedItemPosition());
            followUp.setAddress((mapAtPos.get("address") != null) ? (String) mapAtPos.get("address") : "");
            followUp.setCity((mapAtPos.get("city") != null) ? (String) mapAtPos.get("city") : "");
            followUp.setDesignation((mapAtPos.get("designation") != null) ? (String) mapAtPos.get("designation") : "");
            followUp.setContactPerson((mapAtPos.get("contactPerson") != null) ? (String) mapAtPos.get("contactPerson") : "");
            followUp.setContactId((Integer) ((mapAtPos.get("contactId") != null) ? mapAtPos.get("contactId") : ""));
            Log.i("@Transworld", "Map At Pos - " + mapAtPos.toString());
        }
        followUp.setMarketingRepCode(MeRepoFactory.getLoginRepository(parentActivity.getDbHelper()).getEmployeeCode());
        followUp.setNextFollowUpDate(dateListenerNextFollowUp.getDateToWeb());
        followUp.setNextFollowUpTime(timeListenerNextFollowUp.getTimeToWeb());
        followUp.setNextFollowUpType(spinnerFollowUpTypeNext.getSelectedItem() != null ? (String) spinnerFollowUpTypeNext.getSelectedItem() : "");
        followUp.setAmountExpectedOn((chkPaymentFollowUp.isChecked() == true) ? dateListenerAmtExcpt.getDateToWeb() : "");
        Log.i("@Transworld", "Date to web for expected date - " + dateListenerAmtExcpt.getDateToWeb());
        Log.i("@Transworld", "Amount Excpy :  " + edtAmountExpected.getText().toString());
        Log.i("@Transworld", "chkFollowUp : " + chkPaymentFollowUp.isChecked());
        followUp.setAmountExpected((chkPaymentFollowUp.isChecked() == true) ? edtAmountExpected.getText().toString() : "");
        followUp.setPreparation(edtPreparation.getText().toString());
        followUp.setRemarks(edtRemarks.getText().toString());
        followUp.setPaymentFollowUp(chkPaymentFollowUp.isChecked() ? "YES" : "NO");
        multipartDocs = new File[uriList.size()];
        for (Integer i = 0; i < uriList.size(); i++) {
            multipartDocs[i] = new File(uriList.get(i));
            Log.i("@Transworld", "URL For Loop  " + uriList.get(i));
        }
        //   LocationService locationService = new LocationService();
        //  Log.i("@Transworld","Lat : "+ googleApiConnector.getLatitude()+","+"Long : "+googleApiConnector.getLongitude() +","+"Source : "+googleApiConnector.getEnabledLocationProvider(getContext()));
        followUp.setMultipartFile(multipartDocs);
        followUp.setLocLat(googleApiConnector.getLatitude());
        followUp.setLocLong(googleApiConnector.getLongitude());
        followUp.setLocSource(googleApiConnector.getEnabledLocationProvider(getContext()));
        Log.i("@Transworld", "Followup Object - " + followUp);
        return followUp;
    }

    private final Boolean isNextFolloupDateFilled() {
        return textNextDate.getText().toString().length() > 0;
    }

    private final Boolean isNextFolloupTimeFilled() {
        return textNextTime.getText().toString().length() > 0;
    }

    private final Boolean isAmountExpectedEntered() {
        return (edtAmountExpected.getText().toString().length() > 0) && (!edtAmountExpected.getText().toString().startsWith(".") && (!edtAmountExpected.getText().toString().startsWith(" ")));
    }

    private final Boolean isNextPaymentDateSelected() {
        return textPaymentDate.getText().toString().length() > 0;
    }

    private final Boolean isAmountValid(){
        return  !edtAmountExpected.getText().toString().startsWith(".");
    }

    private final Boolean areAllField() {
        return chkPaymentFollowUp.isChecked() ? isNextFolloupDateFilled() && isNextFolloupTimeFilled() && isAmountExpectedEntered() && isNextPaymentDateSelected() : isNextFolloupDateFilled() && isNextFolloupTimeFilled();
    }

    private final Boolean isPaymentDetailsFilled() {
        return chkPaymentFollowUp.isChecked() ? isNextPaymentDateSelected() && isAmountExpectedEntered() : !(isNextPaymentDateSelected() && isAmountExpectedEntered());
    }

    private final Boolean isTimeValid() {
        Date inTime = null;
        Date outTime = null;
        Date prevDate = null;
        Date nextDate = null;
        String in = null;
        String out = null;
        String prev = null;
        String next = null;
        DateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
          /*  Log.i("@Transworld", "dateListener.getDateToWeb() " + (dateFormat.parse(dateListener.getDateToWeb()).equals(null) ? dateToWeb : dateFormat.parse(dateListener.getDateToWeb())));
            Log.i("@Transworld", "dateFormat.parse(dateListenerNextFollowUp.getDateToWeb() " + dateFormat.parse(dateListenerNextFollowUp.getDateToWeb()));
            Log.i("@Transworld", "timeFormat.parse(timeListener.getTimeToWeb() " + (timeListener.getTimeToWeb().equals(null) ? timeToWebFollowUp : timeListener.getTimeToWeb()));
            Log.i("@Transworld", "timeListenerNextFollowUp.getTimeToWeb() " + timeListenerNextFollowUp.getTimeToWeb());*/
            prevDate = dateFormat.parse(dateListener.getDateToWeb()==null ? dateToWeb : dateListener.getDateToWeb());
            nextDate = dateFormat.parse(dateListenerNextFollowUp.getDateToWeb());
            inTime = timeFormat.parse(timeListener.getTimeToWeb()==null ? timeToWebFollowUp : timeListener.getTimeToWeb());
            outTime = timeFormat.parse(timeListenerNextFollowUp.getTimeToWeb());
            in = timeFormat.format(inTime);
            out = timeFormat.format(outTime);
            prev = dateFormat.format(prevDate);
            next = dateFormat.format(nextDate);
            if (prevDate.equals(nextDate)) {
                if (in.compareTo(out) >= 0) {
                    return false;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.i("@Transworld", "isTimeValid" + prev + "  " + next);
        return true;
    }

    public final void openInGallery(String imageId) {
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI.buildUpon().appendPath(imageId).build();
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private final void openImageIntent() {
        // Determine Uri of camera image to save.
        final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "MyDir" + File.separator);
        root.mkdirs();
        final String fname = "" + System.currentTimeMillis();
        final File sdImageMainDirectory = new File(root, fname);
        uriFileCapturedFollowup = Uri.fromFile(sdImageMainDirectory);
        // Camera.
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = parentActivity.getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uriFileCapturedFollowup);
            cameraIntents.add(intent);
        }
    }

    private void getImages() {
        Intent intent = new Intent(parentActivity, ImagePickerActivity.class);
        Config config = new Config.Builder()
                .setTabBackgroundColor(R.color.white)    // set tab background color. Default white.
                .setTabSelectionIndicatorColor(R.color.blue)
                .setCameraButtonColor(R.color.green)
                .setSelectionLimit(3)
                .build();
        ImagePickerActivity.setConfig(config);
        Log.i("@Transworld", "URI COUNT in getImage() - " + uriList.size());
        startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);
    }

    private Date getDateFromString(String dateStr) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = formatter.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("@Transworld", "URI COUNT OnActivity Result - " + uriList.size());
        /*if(uriList.size() > 0) {
            textDocPath.setText(uriList.size() + " Files Attached");
        }
        else {
            textDocPath.setText("0 Files Attached");
        }*/
        uriList.clear();
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == INTENT_REQUEST_GET_IMAGES) {
                Parcelable[] parcelableUris = data.getParcelableArrayExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);
                if (parcelableUris == null) {
                    return;
                }
                Uri[] uris = new Uri[parcelableUris.length];
                System.arraycopy(parcelableUris, 0, uris, 0, parcelableUris.length);
                if (uris != null) {
                    for (Uri uri : uris) {
                        String imgName = uri.toString();
                        String img[] = imgName.split("/");
                        imageList.add(img[img.length - 1]);
                        Log.i("@Transworld", "On Activity Result " + uri);
                        //Image Compression Logic
                    /*    MeTaskImageCompression taskImageCompression = new MeTaskImageCompression(getActivity(), uri);
                        taskImageCompression.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, uri.toString());*/
                        uriList.add(uri.toString());
                    }
                }
            }
        }
        if (uriList.size() > 0) {
            textDocPath.setText(uriList.size() + " Files Attached");
            textViewImage.setVisibility(View.VISIBLE);
        } else {
            textDocPath.setText("0 Files Attached");
            textViewImage.setVisibility(View.INVISIBLE);
        }
        followUpDocsMap.put("docCount", uriList.size());//store to SQLite
        Log.i("@Transworld", "URI COUNT OnActivity Result Exit - " + uriList.size());
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public final String getPath(Context context, Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public final String getDataColumn(Context context, Uri uri, String selection,
                                      String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public final boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public final boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public final boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private Boolean saveFollowUp() {
        MeRepoSaveFollowUp repoSaveFollowUp = new MeRepoImplSaveFollowUp(parentActivity.getDbHelper());
        try {
            repoSaveFollowUp.saveFollowUpLocally(addNewFollowUpLocally(makeFullNewFollowUpLocal()));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void saveFollowUpLocalServer() {
        MeRepoSaveFollowUp repoSaveFollowUp = new MeRepoImplSaveFollowUp(parentActivity.getDbHelper());
        try {
            repoSaveFollowUp.saveFollowUpLocallyServer(addNewFollowUpLocally(makeFullNewFollowUp()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final void assignToListFromLocal() {
        List<String> list = new ArrayList<>();
        try {
            for (String assignTo : MeRepoFactory.getMarketingRepMaster(parentActivity.getDbHelper()).getAssignToList()) {
                if (assignTo != null) {
                    list.add(assignTo);
                }
                Log.i("@Transworld", "List Assign To" + list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapterAssignTo = new ArrayAdapter<>(parentActivity, R.layout.spinner_textview, list);
        spinnerAssignTo.setAdapter(adapterAssignTo);
        adapterAssignTo.notifyDataSetChanged();
    }

    private final void specialEmailsListLocally() {
        //     adapterSpecialEmail.clear();
        List<String> list = new ArrayList<>();
        try {
            for (String specialEmail : MeRepoFactory.getLoginRepository(parentActivity.getDbHelper()).getEmployeeNames())
                list.add(specialEmail);
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapterSpecialEmail = new ArrayAdapter<>(parentActivity, R.layout.spinner_textview, list);
        autoSpecialEmail.setAdapter(adapterSpecialEmail);
        adapterSpecialEmail.notifyDataSetChanged();
    }

    private Map<String, Object> addNewFollowUpLocally(MeFollowUp followUp) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("prospCustCode", "" + followUp.getProspCustCode());
        map.put("remarks", "" + followUp.getRemarks());
        map.put("companyMasterId", "" + followUp.getCompanyMasterId());
        map.put("followUpDate", "" + followUp.getFollowUpDate().toString());
        map.put("followUpTime", "" + followUp.getFollowUpTime().toString());
        map.put("status", "" + followUp.getStatus());
        map.put("followUpType", "" + followUp.getFollowUpType());
        map.put("followUpInOut", "" + followUp.getFollowUpInOut());
        map.put("comments", "" + followUp.getComments());
        map.put("nextFollowUpDate", "" + followUp.getNextFollowUpDate().toString());
        map.put("nextFollowUpTime", "" + followUp.getNextFollowUpTime().toString());
        map.put("nextFollowUpType", "" + followUp.getNextFollowUpType());
        map.put("preparation", "" + followUp.getPreparation());
        map.put("marketingRepCode", "" + followUp.getMarketingRepCode());
        map.put("city", "" + followUp.getCity());
        map.put("contactId", "" + followUp.getContactId());
        map.put("contactPerson", "" + followUp.getContactPerson());
        map.put("designation", "" + followUp.getDesignation());
        map.put("address", "" + followUp.getAddress());
        map.put("prospCustName", "" + followUp.getProspCustName());
        map.put("paymentFollowUp", "" + followUp.getPaymentFollowUp());
        map.put("spokenTo", "" + followUp.getSpokenTo());
        map.put("amountExpected", "" + followUp.getAmountExpected());
        map.put("amountExpectedOn", followUp.getAmountExpectedOn());
        map.put("locLat", googleApiConnector.getLatitude());
        map.put("locLong", googleApiConnector.getLongitude());
        map.put("locSource", googleApiConnector.getEnabledLocationProvider(getContext()));
        map.put("timeZone", "" + System.currentTimeMillis());
        if (listAssignToMap.size() != 0) {
            Log.i("@Transworld", "List Assign To Map: " + listAssignToMap);
            Log.i("@Transworld", "Seleted Item position: " + spinnerAssignTo.getSelectedItemPosition());
            Map<String, Object> mapAtPos = listAssignToMap.get(spinnerAssignTo.getSelectedItemPosition());
            map.put("assignToUserName", "" + mapAtPos.get("empname"));
            map.put("assignToUserId", "" + mapAtPos.get("empcode"));
            Log.i("@Transworld", "AssignTo Person: " + mapAtPos.get("empcode") + " || " + mapAtPos.get("empname"));
        } else {
            map.put("assignToUserName", "-");
            map.put("assignToUserId", "-");
        }
        if (followUpDocsMap.get("docCount") != null) {
            if ((Integer) followUpDocsMap.get("docCount") == 1) {
                map.put("docPath1", "" + multipartDocs[0]);
            }
            if ((Integer) followUpDocsMap.get("docCount") == 2) {
                map.put("docPath1", "" + multipartDocs[0]);
                map.put("docPath2", "" + multipartDocs[1]);
            }
            if ((Integer) followUpDocsMap.get("docCount") == 3) {
                map.put("docPath1", "" + multipartDocs[0]);
                map.put("docPath2", "" + multipartDocs[1]);
                map.put("docPath3", "" + multipartDocs[2]);
            }
        }
        Log.i("@Transworld", "Map to Sqlite " + map);
        return map;
    }

    private void addListToDialog(List<String> imageList) {
        final CharSequence[] images = imageList.toArray(new String[imageList.size()]);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setTitle("Images");
        dialogBuilder.setItems(images, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                String selectedText = images[item].toString();  //Selected item in listview
            }
        });
        //Create alert dialog object via builder
        AlertDialog alertDialogObject = dialogBuilder.create();
        //Show the dialog
        alertDialogObject.show();
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
                parentActivity.runFragmentTransaction(R.id.frameMainContainer, MeFragmentAddContacts.newInstance(textClientName.getText().toString().split("\\[")[0], customerCode, salesCustomerCode, "nextFragment"));
            }
            if (v.getId() == chkPaymentFollowUp.getId()) {
                if (chkPaymentFollowUp.isChecked())
                    relativeAmount.setVisibility(View.VISIBLE);
                else
                    relativeAmount.setVisibility(View.GONE);
            } else if (v.getId() == textNextDate.getId()) {
                dateListenerNextFollowUp.setTextView(textNextDate);
                DatePickerDialog datePicker = new DatePickerDialog(parentActivity, dateListenerNextFollowUp, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
               // datePicker.getDatePicker().setMaxDate(new Date().getTime());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                try {
                    date = sdf.parse(dateToWeb);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long startDate = date.getTime();

                datePicker.getDatePicker().setMinDate(startDate);
                // datePicker.getDatePicker().setMinDate(getDateFromString(dateListener.getDateToWeb()).getTime());
                datePicker.show();
            } else if (v.getId() == textNextTime.getId()) {
                timeListenerNextFollowUp.setTextView(textNextTime);
                new TimePickerDialog(parentActivity, timeListenerNextFollowUp, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
            } else if (v.getId() == imageCalendar.getId()) {
                dateListenerAmtExcpt.setTextView(textPaymentDate);
                new DatePickerDialog(parentActivity, dateListenerAmtExcpt, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            } else if (v.getId() == btnSubmit.getId()) {
                //   btnSubmit.setBackground(getContext().getApplicationContext().getResources().getDrawable(R.drawable.ripple_button));
                if (areAllField() && areAllFilled()) {
                    if (isTimeValid()) {
                        if (parentActivity.isNetworkAvailable()) {
                            new MeTaskAddFollowUp().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            Log.i("@Transworld", "From Frag - " + getArguments().get(KEY_FROM_FRAGMENT));
                        } else {
                            saveFollowUp();//save follow up locally when network not available with flag 1
                            //saveFollowUpDocsLocally();
                            parentActivity.snack(rootView, "Follow Up Saved Locally");
                            if (getArguments().get(KEY_FROM_FRAGMENT).equals("AppointmentFragment")) {
                                parentActivity.popBackStack(2);  //if come from followUpNext fragment
                            } else
                                parentActivity.popBackStack(1);
                        }
                    } else {
                        if(getView()!=null)
                        parentActivity.snack(rootView, "Set NextFollowUp time greater than FollowUp time");
                    }
                } else if (areAllField()) {
                    if(getView()!=null)
                    parentActivity.snack(rootView, !isSpokenToSelected() ? "You missed to select Spoken To :(" : "You missed comments :(");
                } else {
                    if(getView()!=null)
                    parentActivity.snack(rootView, isPaymentDetailsFilled() ? "You missed date & time details" :  "Enter proper payment/date :(");
                }
            } else if (v.getId() == imgAttachFile.getId()) {
                Log.i("@Transworld", "URI COUNT Before getImage() - " + uriList.size());
                imageList.clear();
                getImages();
                Log.i("@Transworld", "URI COUNT after getImage() - " + uriList.size());
            } else if (v.getId() == textViewImage.getId()) {
                Log.i("@Transworld", "imageList" + imageList);
                addListToDialog(imageList);
            }
        }
    }

    private final class MeDateListener implements DatePickerDialog.OnDateSetListener {
        String[] s = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        private TextView textView;
        private String dateToWeb;

        public MeDateListener() {
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
           /* String month = s[monthOfYear];
            Log.e("next follow up-month",month);
            String date = "" + (dayOfMonth > 9 ? dayOfMonth : "0" + dayOfMonth) + "-" + month + "-" + year;
            Log.e("next follow up-date", String.valueOf(dayOfMonth));
            dateToWeb = year + "-" + ((monthOfYear + 1 > 9) ? (monthOfYear + 1) : "0" + (monthOfYear + 1) + "-" + (dayOfMonth > 9 ? dayOfMonth : "0" + dayOfMonth));
            Log.i("@Transworld", "Date To Web: " + dateToWeb);*/
            String month = s[monthOfYear];
            Log.e("month FollowUp :",month);
            String date = "" + (dayOfMonth > 9 ? dayOfMonth : "0" + dayOfMonth) + "-" + month + "-" + year;

            dateToWeb = year + "-" + (monthOfYear + 1) + "-" + (dayOfMonth > 9 ? dayOfMonth : "0" + dayOfMonth);
            Log.e("Month Year", String.valueOf(monthOfYear));
            Log.e("day Month ", String.valueOf(dayOfMonth));
            Log.e("date web",dateToWeb);
            textCurrentDate.setText(date);
            Log.e("current",date);
            if (textView.getId() == textPaymentDate.getId()) textView.setVisibility(View.VISIBLE);
            textView.setText(date);
        }

        public String getDateToWeb() {
            return dateToWeb;
        }

        public void setTextView(TextView textView) {
            this.textView = textView;
        }
    }

    private final class MeTimeListener implements TimePickerDialog.OnTimeSetListener {

        private TextView textView;

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String time = "" + (hourOfDay > 9 ? hourOfDay : "0" + hourOfDay) + ":" + (minute > 9 ? minute : "0" + minute);
            timeToWebNextFollowUp = "" + (hourOfDay > 9 ? hourOfDay : "0" + hourOfDay) + ":" + (minute > 9 ? minute : "0" + minute) + ":" + calendar.get(Calendar.SECOND);
            textView.setText(time);
        }

        public String getTimeToWeb() {
            return timeToWebNextFollowUp;
        }

        public void setTextView(TextView textView) {
            this.textView = textView;
        }
    }

    private final class MeConnectorFollowUp implements MeConnectable {
        public Map<String, Object> addNewFollowUp(MeFollowUp followUp) throws Exception {
            Log.i("@Transworld", "Connector Followup - " + followUp.toString());
            Map<String, Object> mapEntity = null;
            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.addFormDataPart("prospCustCode", "" + followUp.getProspCustCode());
            builder.addFormDataPart("remarks", "" + followUp.getRemarks());
            builder.addFormDataPart("companyMasterId", "" + followUp.getCompanyMasterId());
            builder.addFormDataPart("followUpDate", "" + followUp.getFollowUpDate().toString());
            builder.addFormDataPart("followUpTime", "" + followUp.getFollowUpTime().toString());
            builder.addFormDataPart("status", "" + followUp.getStatus());
            builder.addFormDataPart("followUpType", "" + followUp.getFollowUpType());
            builder.addFormDataPart("followUpInOut", "" + followUp.getFollowUpInOut());
            builder.addFormDataPart("comments", "" + followUp.getComments());
            builder.addFormDataPart("nextFollowUpDate", "" + followUp.getNextFollowUpDate().toString());
            builder.addFormDataPart("nextFollowUpTime", "" + followUp.getNextFollowUpTime().toString());
            builder.addFormDataPart("nextFollowUpType", "" + followUp.getNextFollowUpType());
            builder.addFormDataPart("preparation", "" + followUp.getPreparation());
            builder.addFormDataPart("marketingRepCode", "" + followUp.getMarketingRepCode());
            builder.addFormDataPart("city", "" + followUp.getCity());
            builder.addFormDataPart("contactId", "" + followUp.getContactId());
            builder.addFormDataPart("contactPerson", "" + followUp.getContactPerson());
            builder.addFormDataPart("designation", "" + followUp.getDesignation());
            builder.addFormDataPart("address", "" + followUp.getAddress());
            builder.addFormDataPart("prospCustName", "" + followUp.getProspCustName());
            builder.addFormDataPart("paymentFollowUp", "" + followUp.getPaymentFollowUp());
            builder.addFormDataPart("spokenTo", "" + followUp.getSpokenTo());
            if (chkPaymentFollowUp.isChecked() == true) {
                builder.addFormDataPart("amountExpected", "" + followUp.getAmountExpected());
                builder.addFormDataPart("amountExpectedOn", followUp.getAmountExpectedOn());
            }
            String empEmailId = MeRepoFactory.getLoginRepository(parentActivity.getDbHelper()).getEmpEmailId();
            Log.i("@transworld", "Email to Id -" + empEmailId);
            String toCC = autoSpecialEmail.getText().toString();
            Log.i("@transworld", "Email To CC -" + toCC);

            /*String empName=MeRepoFactory.getLoginRepository(parentActivity.getDbHelper()).getEmpName();
            Log.i("@transworld","empName -"+empName);*/
            builder.addFormDataPart("locSource", "" + googleApiConnector.getEnabledLocationProvider(getContext()));
            builder.addFormDataPart("locLat", "" + googleApiConnector.getLatitude());
            builder.addFormDataPart("locLong", "" + googleApiConnector.getLongitude());
            Log.i("@Transworld", "GetLat : " + googleApiConnector.getLatitude() + "," + "GetLong : " + googleApiConnector.getLongitude() + "," + "GetSource : " + googleApiConnector.getEnabledLocationProvider(getContext()));
//            builder.addFormDataPart("emailToId", MeRepoFactory.getLoginRepository(parentActivity.getDbHelper()).getEmpEmailId());
            //if field empty then it send blank
            // builder.addFormDataPart("emailToCc", autoSpecialEmail.getText().toString());
            // builder.addFormDataPart("empName",MeRepoFactory.getLoginRepository(parentActivity.getDbHelper()).getEmpName());
            Long currentTime = System.currentTimeMillis();
            Log.i("@Transworld", "Timezone : " + currentTime);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss");
            Date date = new Date(currentTime);
            Log.i("@Transworld", "Timezone After Format : " + sdf.format(date).toString());
            Long time = date.getTime();
            Log.i("@Transworld", "Timezone to web : " + time);
            builder.addFormDataPart("timeZone", "" + System.currentTimeMillis());
            Log.i("@Transworld", "MULTIPART Length : " + multipartDocs.length);
            for (File file : multipartDocs) {
                if (file.exists()) {
                    builder.addFormDataPart("multipartFile", "" + (file.getName()), RequestBody.create(MediaType.parse("image/*"), file));
                }
            }
            //    Log.i("@Transworld", "Source : " + parentActivity.getEnabledLocationProvider() + "\n Lat : " + parentActivity.getLatitude() + "\n Long : " + parentActivity.getLongitude());
            if (listAssignToMap.size() != 0) {
                Log.i("@Transworld", "List Assign To Map: " + listAssignToMap);
                Log.i("@Transworld", "Seleted Item position: " + spinnerAssignTo.getSelectedItemPosition());
                Map<String, Object> mapAtPos = listAssignToMap.get(spinnerAssignTo.getSelectedItemPosition());
                builder.addFormDataPart("assignToUserName", "" + mapAtPos.get("MarketingRepName"));
                builder.addFormDataPart("assignToUserId", "" + mapAtPos.get("MarketingRepCode"));
                Log.i("@Transworld", "AssignTo Person: " + mapAtPos.get("MarketingRepCode") + " || " + mapAtPos.get("MarketingRepName"));
            } else {
                builder.addFormDataPart("assignToUserName", "-");
                builder.addFormDataPart("assignToUserId", "-");
            }
            builder.setType(MultipartBody.FORM);
            RequestBody body = builder.build();
            Request request = new Request.Builder()
                    .url(MeIUrl.URL_ADD_NEW_FOLLOW_UP)
                    .post(body)
                    .addHeader("Accept", "application/json")
                    .build();
            Log.i("@Transworld", "req Body " + builder.toString());
            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(request).execute();
            String responseJson = response.body().string();
            Log.i("@Transworld", "Response Json Follow Up" + responseJson);
            if (response.code() == 200) {
                mapEntity = JsonMan.parseAnything(responseJson, new TypeReference<Map<String, Object>>() {
                });
                Log.i("@Transworld", "Map Entity Follow Up" + mapEntity);
            }
            return mapEntity;
        }
    }

    private final class MeTaskAddFollowUp extends AsyncTask<Void, Void, Map<String, Object>> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(parentActivity, "Please wait...", "Saving Follow Up");
        }

        @Override
        protected Map<String, Object> doInBackground(Void... params) {
            MeConnectorFollowUp connector = new MeConnectorFollowUp();
            Map<String, Object> mapEntity = null;
            try {
                mapEntity = connector.addNewFollowUp(makeFullNewFollowUp());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mapEntity;
        }

        @Override
        protected void onPostExecute(Map<String, Object> mapEntity) {
            super.onPostExecute(mapEntity);
            progressDialog.dismiss();
            if (mapEntity != null) {
                String status = (String) mapEntity.get("status");
                String message = (String) mapEntity.get("message");
                Log.i("@transworldtechnology", message);
                if (status.equals("success")) {
                    MePrefs.clearSharedPrefsTemp(getContext());
                    if(getView()!=null) {
                        parentActivity.snack(rootView, "Follow-Up Saved");
                    }
                    //parentActivity.snack(rootView, "" + message);
                    //   saveFollowUpLocalServer();//save follow local and server with flag 0
                    //  parentActivity.snack(rootView, "Follow Up Saved Locally & Server");
                    if (followUpDocsMap.get("docCount") != null) {
                        try {
                            new MeTaskSaveToFtp(MeRepoFactory.getLoginRepository(parentActivity.getDbHelper()).getCompanyMasterId()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (getArguments().get(KEY_FROM_FRAGMENT).equals("AppointmentFragment")) {
                        parentActivity.popBackStack(2);  //if come from followUpNext fragment
                    } else
                        parentActivity.popBackStack(1);
                } else {
                    if(getView()!=null) {
                        parentActivity.snack(rootView, "" + message);
                    }
                }
            }
            /* else {
                parentActivity.snack(rootView, "Problem while adding Follow Up :(");
            }*/
        }
    }

    private final class MeWatcherSpecialEmail implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (autoSpecialEmail.isPerformingCompletion()) {
                return;
            }
            String[] multEmail = s.toString().split(",");
            if (multEmail.length > 0 && !multEmail.toString().equals(" ")) {
                try {
                    if (parentActivity.isNetworkAvailable())
                        new MeTaskSearchEmail(multEmail[multEmail.length - 1]).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    else
                        specialEmailsListLocally();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

    private final class MeConnectorSearchEmail implements MeConnectable {
        public List<Map<String, Object>> listOfSpecialEmail(String employeeName) throws Exception {
            OkHttpClient client = new OkHttpClient();
            String urlPathVariable = MeIUrl.URL_SPECIAL_EMAIL_LIST + employeeName;
            Log.i("@Transworld", "Url Special Email - " + urlPathVariable);
            Request request = new Request.Builder()
                    .url(urlPathVariable)
                    .get()
                    .addHeader("Accept", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            String responsJson = response.body().string();
            Log.i("@Transworld", "Json Special Email - " + responsJson);
            Map<String, Object> mapEntity = JsonMan.parseAnything(responsJson, new TypeReference<Map<String, Object>>() {
            });
            Log.i("@Transworld", "Map Entity - " + mapEntity);
            listSpecialEmailMap = (List<Map<String, Object>>) mapEntity.get("specialEmails");
            Log.i("@Transworld", "List Special Email - " + listSpecialEmail);
            return listSpecialEmailMap;
        }
    }

    private final class MeTaskSearchEmail extends AsyncTask<Void, Void, List<Map<String, Object>>> {
        private String employeeName;
        private MeConnectorSearchEmail connector;

        public MeTaskSearchEmail(String employeeName) {
            this.employeeName = employeeName;
            connector = new MeConnectorSearchEmail();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Map<String, Object>> doInBackground(Void... params) {
            try {
                listSpecialEmailMap = connector.listOfSpecialEmail(employeeName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listSpecialEmailMap;
        }

        @Override
        protected void onPostExecute(List<Map<String, Object>> maps) {
            super.onPostExecute(maps);
            listSpecialEmail.clear();
            if (maps != null) {
                for (Map<String, Object> mapItem : maps) {
                    listSpecialEmail.add((String) mapItem.get("empName"));
                }
            }
            adapterSpecialEmail = new ArrayAdapter<>(parentActivity, R.layout.spinner_textview, listSpecialEmail);
            autoSpecialEmail.setAdapter(adapterSpecialEmail);
            adapterSpecialEmail.notifyDataSetChanged();
        }
    }

    private void sentEmailTo() {
        Log.i("@Transworld", "ListSppecialEmailMap : " + listSpecialEmailMap);
    }

    private class MeConnectorAssignTo implements MeConnectable {
        public List<Map<String, Object>> listAssignTo(Integer companyMasterId) throws Exception {
            OkHttpClient okHttpClient = new OkHttpClient();
            String urlPathVariable = MeIUrl.URL_ASSIGN_TO_LIST + companyMasterId;
            Log.i("@Transworld", "URL Path " + urlPathVariable);
            Request request = new Request.Builder()
                    .url(urlPathVariable)
                    .get()
                    .addHeader("Accept", "application/json")
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            String responsJson = response.body().string();
            Log.i("@Transworld", "Json Assign To- " + responsJson);
            Map<String, Object> mapEntity = JsonMan.parseAnything(responsJson, new TypeReference<Map<String, Object>>() {
            });
            Log.i("@Transworld", "Map Entity - " + mapEntity);
            listAssignToMap = (List<Map<String, Object>>) mapEntity.get("UserNames");
            Log.i("@Transworld", "List Assign To  To - " + listAssignToMap);
            return listAssignToMap;
        }
    }

    private class MeTaskAssignTo extends AsyncTask<Void, Void, List<Map<String, Object>>> {
        private MeConnectorAssignTo connectorAssignTo;
        private Integer companyMasterId;

        public MeTaskAssignTo(Integer companyMasterId) {
            this.companyMasterId = companyMasterId;
            connectorAssignTo = new MeConnectorAssignTo();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listAssignTo.clear();
            adapterAssignTo.clear();
        }

        @Override
        protected List<Map<String, Object>> doInBackground(Void... params) {
            try {
                listAssignToMap = connectorAssignTo.listAssignTo(companyMasterId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ((listAssignToMap != null ? listAssignToMap : new ArrayList<Map<String, Object>>()));
        }

        @Override
        protected void onPostExecute(List<Map<String, Object>> maps) {
            super.onPostExecute(maps);
            listAssignTo.clear();
            for (Map<String, Object> mapItems : maps) {
                listAssignTo.add((String) mapItems.get("MarketingRepName"));
                mapItems.get("MarketingRepCode");
            }
            adapterAssignTo.notifyDataSetChanged();
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
            Map<String, Object> mapEntity = JsonMan.parseAnything(responsJson, new TypeReference<Map<String, Object>>() {
            });
            Log.i("@Transworld", "Map Entity - " + mapEntity);
            listSpokenToMap = (List<Map<String, Object>>) mapEntity.get("spokenTo");
            Log.i("@Transworld", "List Spoken To - " + listSpokenToMap);
            return listSpokenToMap;
        }
    }

    private class MeDateListenerFollowUp implements DatePickerDialog.OnDateSetListener {
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        private String dateToWeb;

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String month = months[monthOfYear];
            Log.e("month Next FollowUp :",month);
            String date = "" + (dayOfMonth > 9 ? dayOfMonth : "0" + dayOfMonth) + "-" + month + "-" + year;

            dateToWeb = year + "-" + (monthOfYear + 1) + "-" + (dayOfMonth > 9 ? dayOfMonth : "0" + dayOfMonth);
            Log.e("Month of Year", String.valueOf(monthOfYear));
            Log.e("dayOfMonth ", String.valueOf(dayOfMonth));
            Log.e("date to web",dateToWeb);
            textCurrentDate.setText(date);
            Log.e("date current",date);
        }

        public String getDateToWeb() {
            return dateToWeb;
        }
    }

    private class MeTimeListenerFollowUp implements TimePickerDialog.OnTimeSetListener {


        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String time = "" + (hourOfDay > 9 ? hourOfDay : "0" + hourOfDay) + ":" + (minute > 9 ? minute : "0" + minute);
            timeToWebFollowUp = "" + (hourOfDay > 9 ? hourOfDay : "0" + hourOfDay) + ":" + (minute > 9 ? minute : "0" + minute) + ":" + calendar.get(Calendar.SECOND);
            textCurrentTime.setText(time);
        }

        public String getTimeToWeb() {
            return timeToWebFollowUp;
        }
    }

    private final class MeConnectorSaveToFtp implements MeConnectable {
        public Map<String, Object> uploadToFtpServer(Integer companyMasterId) throws Exception {
            String urlWithPathVariables = MeIUrl.URL_UPLOAD_TO_FTP_SERVER + companyMasterId;
            Log.i("@Transworld", MeIUrl.URL_UPLOAD_TO_FTP_SERVER + companyMasterId);
            String jsonMap = HttpRequest.get(urlWithPathVariables).accept("application/json").body();
            Log.i("@Transworld", "JSON - " + jsonMap);
            Map<String, Object> mapEntity = JsonMan.parseAnything(jsonMap, new TypeReference<Map<String, Object>>() {
            });
            Log.i("@Transworld", "Map Entity " + mapEntity.toString());
            return mapEntity;
        }
    }

    private class MeTaskSaveToFtp extends AsyncTask<Void, Void, Map<String, Object>> {
        Integer companyMasterId;
        private MeConnectorSaveToFtp connector;

        public MeTaskSaveToFtp(Integer companyMasterId) {
            this.companyMasterId = companyMasterId;
            connector = new MeConnectorSaveToFtp();
        }

        @Override
        protected Map<String, Object> doInBackground(Void... params) {
            Map<String, Object> mapEntity = null;
            try {
                mapEntity = connector.uploadToFtpServer(companyMasterId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mapEntity;
        }

        @Override
        protected void onPostExecute(Map<String, Object> stringObjectMap) {
            super.onPostExecute(stringObjectMap);
            Log.i("@Transworld", "Save To FTP Successfully");
        }
    }
}