package com.transworldtechnology.crm.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.transworldtechnology.crm.R;
import com.transworldtechnology.crm.activity.MainActivity;
import com.transworldtechnology.crm.database.MeRepoFactory;
import com.transworldtechnology.crm.database.repository.MeRepoLogin;
import com.transworldtechnology.crm.domain.MeAddCustomer;
import com.transworldtechnology.crm.prefs.MePrefs;
import com.transworldtechnology.crm.web.JsonMan;
import com.transworldtechnology.crm.web.MeConnectable;
import com.transworldtechnology.crm.web.MeIUrl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MeFragmentAddCustomer extends Fragment {

    public static final String KEY_FRAGMENT_NAME = "key_fragment_name";
    private MainActivity parentActivity;
    private MeRepoLogin repoLogin;
    private List<String> listCountry;
    private List<String> listState;
    private List<String> listCity;
    private ArrayAdapter<String> arrayAdapterCountry;
    private ArrayAdapter<String> arrayAdapterState;
    private ArrayAdapter<String> arrayAdapterCity;

    public static MeFragmentAddCustomer newInstance() {
        MeFragmentAddCustomer fragment = new MeFragmentAddCustomer();
        Bundle args = new Bundle();
        args.putString(KEY_FRAGMENT_NAME, "MeFragmentAddCustomer");
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentActivity = (MainActivity) getActivity();
        parentActivity.isVisibleFab(false);
        parentActivity.showSearchView(false);
        parentActivity.setTitle("Add Customer");
        parentActivity.clearListView();
        repoLogin = MeRepoFactory.getLoginRepository(parentActivity.getDbHelper());
        parentActivity.isToolbarClickable();
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_add_contact_submit, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);



            if (item.getItemId() == R.id.action_done) {
               // parentActivity.snack(getView(),"Submit Done");
                if (areAllFilled(getView())) {
                    if (parentActivity.isNetworkAvailable()) {
                        new MeTaskSaveCustomer().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                    else {
                        parentActivity.snack(getView(),"Unable to connect to the server:(");
                    }
                }
                else{
                    parentActivity.snack(getView(),"Please Entered Mandatory Fields !! :(");

                    String emailId = ((EditText) (getView().findViewById(R.id.edtEmailCustomer))).getText().toString();



                    String phoneNo = ((EditText) (getView().findViewById(R.id.edtPhoneNo))).getText().toString();
                    Log.i(MainActivity.TAG, "phoneNo - " + phoneNo);
                    if ((!isPhoneNumberValid(getView()) && !phoneNo.isEmpty()) ) {
                        parentActivity.snack(getView(), "Enter valid Phone No :(");
                    }
                    if(phoneNo.isEmpty()) {
                        parentActivity.snack(getView(), "Enter Phone No :(");
                    }

                    if ((!isMobileNumberValid(getView()) && !((EditText)getView().findViewById(R.id.edtMobileNo)).getText().toString().isEmpty())) {
                        parentActivity.snack(getView(), "Enter valid Mobile No :(");
                    }

                    if(((EditText)getView().findViewById(R.id.edtMobileNo)).getText().toString().isEmpty()) {
                        parentActivity.snack(getView(), "Enter Mobile No :(");
                    }


                    if ((!isEmailIdValid(getView()) && !emailId.isEmpty()) ) {
                        parentActivity.snack(getView(), "Enter valid email id :(");
                    }

                    if(emailId.isEmpty()){
                        parentActivity.snack(getView(), "Enter email id :(");
                    }

                    if (((EditText) (getView().findViewById(R.id.edtContactPerson))).getText().toString().isEmpty()) {
                        parentActivity.snack(getView(), "Enter Contact Person :(");
                    }

                    String companyName = ((EditText) (getView().findViewById(R.id.edtCompanyName))).getText().toString();
                    Log.i(MainActivity.TAG, "companyName - " + companyName);
                    if (companyName.isEmpty()) {
                        parentActivity.snack(getView(), "Enter Company name :(");
                    }
                }
            }

        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_customer, container, false);
        parentActivity.isToolbarClickable();
        initUI(rootView);
        initSpinnerCategory(rootView);
        initSpinnerCity(rootView);
        initSpinnerState(rootView);
        initSpinnerCountry(rootView);
        return rootView;
    }


    private final void initSpinnerCategory(View rootView) {
        Spinner spinnerInitials = (Spinner) rootView.findViewById(R.id.spinnerInitialsCategory);
        ArrayList<String> initialsList = new ArrayList<String>();
        initialsList.add("BarCode");
        initialsList.add("Camera");
        initialsList.add("Car");
        initialsList.add("Aerosoles");
        initialsList.add("Computer");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_textview, initialsList);
        spinnerInitials.setAdapter(arrayAdapter);
    }
    private final void initSpinnerCity(View rootView) {
        Spinner spinnerCity = (Spinner) rootView.findViewById(R.id.spinnerInitialsAddrCityCustomer);
        listCity = new ArrayList<>();
         arrayAdapterCity = new ArrayAdapter<String>(getContext(), R.layout.spinner_textview, listCity);
        spinnerCity.setAdapter(arrayAdapterCity);
    }
    private final void initSpinnerState(View rootView) {
        Spinner spinnerState = (Spinner) rootView.findViewById(R.id.spinnerInitialsAddrStateCustomer);
        listState = new ArrayList<>();


        arrayAdapterState = new ArrayAdapter<String>(getContext(), R.layout.spinner_textview, listState);
        spinnerState.setAdapter(arrayAdapterState);
    }

        private final void initSpinnerCountry(View rootView) {
        Spinner spinnerCountry = (Spinner) rootView.findViewById(R.id.spinnerInitialsAddrCountryCustomer);
        listCountry = new ArrayList<>();

            if (parentActivity.isNetworkAvailable()){
                try {
                    new MeTaskGetCountry(MeRepoFactory.getLoginRepository(parentActivity.getDbHelper()).getCompanyMasterId()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            arrayAdapterCountry = new ArrayAdapter<String>(getContext(), R.layout.spinner_textview, listCountry);
            spinnerCountry.setAdapter(arrayAdapterCountry);
    }

    private final Boolean areAllFilled(View rootView) {

        return (areCompanyNameFilled(rootView) && areContactPersonFilled(rootView) && areEmailMobileFilled(rootView) && isPhoneNumberValid(rootView));
    }

    private boolean areCompanyNameFilled(View rootView) {
        return ((EditText) rootView.findViewById(R.id.edtCompanyName)).getText().toString().length() > 0 && !(((EditText) rootView.findViewById(R.id.edtCompanyName)).getText().toString().contains(" "));

    }
    private final Boolean areContactPersonFilled(View rootView) {

        return ((EditText) rootView.findViewById(R.id.edtContactPerson)).getText().toString().length() > 0 && !(((EditText) rootView.findViewById(R.id.edtContactPerson)).getText().toString().startsWith(" "));
    }


    private final Boolean areEmailMobileFilled(View rootView) {
        return isEmailIdValid(rootView) && isMobileNumberValid(rootView);
    }

    private final Boolean isMobileNumberValid(View rootView) {
        return ((EditText) rootView.findViewById(R.id.edtMobileNo)).getText().toString().length() >= 10 && !(((EditText) rootView.findViewById(R.id.edtMobileNo)).getText().toString().startsWith(" "));
    }

    private final Boolean isPhoneNumberValid(View rootView) {
        return ((EditText) rootView.findViewById(R.id.edtPhoneNo)).getText().toString().length() >= 4 && !(((EditText) rootView.findViewById(R.id.edtPhoneNo)).getText().toString().startsWith(" "));
    }

    private final Boolean isEmailIdValid(View rootView) {

        if (((EditText) (rootView.findViewById(R.id.edtEmailCustomer))).getText().toString().startsWith(" ")) {
            ((EditText) (rootView.findViewById(R.id.edtEmailCustomer))).setText("");
        }
        String emailPattern = "(?:[a-z0-9'+_`-]+(?:\\.[a-z0-9'_`-])*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
        String email = ((EditText) (rootView.findViewById(R.id.edtEmailCustomer))).getText().toString();
        Log.i(MainActivity.TAG,"EmailId - "+email);
        if (email == null) {
            return false;
        } else {
            Log.i(MainActivity.TAG, "email -" + email.matches(emailPattern));
            return email.matches(emailPattern);
        }
    }

    private void initUI(final View rootView) {

        final Integer[] count = {0};
        final Integer[] countMob = {0};
        final Integer[] count1 = {0};

        rootView.findViewById(R.id.txtAddNewMobCustomer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                (rootView.findViewById(R.id.edtMobileNo2)).requestFocus();
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput((rootView.findViewById(R.id.edtMobileNo2)), InputMethodManager.SHOW_IMPLICIT);

                countMob[0]++;
                Log.i(MainActivity.TAG, "count[0] - " + countMob[0]);


                if (countMob[0] == 1) {

                    final RelativeLayout relativeLayoutMob2 = (RelativeLayout) rootView.findViewById(R.id.relativeLayoutMob2);
                    relativeLayoutMob2.setVisibility(View.VISIBLE);

                    rootView.findViewById(R.id.imageClosedTagM2).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final RelativeLayout relativeLayoutMob2 = (RelativeLayout) rootView.findViewById(R.id.relativeLayoutMob2);
                            relativeLayoutMob2.setVisibility(View.GONE);
                            countMob[0]--;
                            Log.i(MainActivity.TAG, "count[0] - " + countMob[0]);
                            if (countMob[0] == 0  || countMob[0] < 3) {
                                rootView.findViewById(R.id.txtAddNewMobCustomer).setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }

                if (countMob[0] == 2) {

                    //rootView.findViewById(R.id.txtAddNewMobCustomer).setVisibility(View.GONE);
                    final RelativeLayout relativeLayoutMob3 = (RelativeLayout) rootView.findViewById(R.id.relativeLayoutMob3);
                    relativeLayoutMob3.setVisibility(View.VISIBLE);

                    rootView.findViewById(R.id.imageClosedTagM3).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final RelativeLayout relativeLayoutMob3 = (RelativeLayout) rootView.findViewById(R.id.relativeLayoutMob3);
                            relativeLayoutMob3.setVisibility(View.GONE);
                            countMob[0]--;
                            Log.i(MainActivity.TAG, "count[0] - " + countMob[0]);
                            if (countMob[0] == 0 || countMob[0] < 3) {
                                rootView.findViewById(R.id.txtAddNewMobCustomer).setVisibility(View.VISIBLE);
                            }

                        }
                    });
                }

                if (countMob[0] == 3) {

                    final RelativeLayout relativeLayoutAltNoCustomer = (RelativeLayout) rootView.findViewById(R.id.relativeLayoutAltNoCustomer);
                    relativeLayoutAltNoCustomer.setVisibility(View.VISIBLE);

                    rootView.findViewById(R.id.txtAddNewMobCustomer).setVisibility(View.GONE);

                    rootView.findViewById(R.id.imageClosedTagAltNo).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final RelativeLayout relativeLayoutAltNoCustomer = (RelativeLayout) rootView.findViewById(R.id.relativeLayoutAltNoCustomer);
                            relativeLayoutAltNoCustomer.setVisibility(View.GONE);
                            countMob[0]--;
                            Log.i(MainActivity.TAG, "count[0] - " + countMob[0]);
                            if (countMob[0] == 0 || countMob[0] < 3) {
                                rootView.findViewById(R.id.txtAddNewMobCustomer).setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
            }
        });

        ((TextView)rootView.findViewById(R.id.textAddMoreField)).setPaintFlags( ((TextView)rootView.findViewById(R.id.textAddMoreField)).getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        rootView.findViewById(R.id.textAddMoreField).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootView.findViewById(R.id.textAddMoreField).setVisibility(View.GONE);

                ((Spinner) (rootView.findViewById(R.id.spinnerInitialsCategory))).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String item = (String) parent.getItemAtPosition(position);
                        ((TextView) parent.getChildAt(0)).setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                final LinearLayout LinearLayoutOtherField = (LinearLayout) rootView.findViewById(R.id.LinearLayoutOtherField);
                LinearLayoutOtherField.setVisibility(View.VISIBLE);
                final LinearLayout LinearLayoutFirstScreen = (LinearLayout) rootView.findViewById(R.id.LinearLayoutFirstScreen);
                LinearLayoutFirstScreen.setVisibility(View.VISIBLE);

            }
        });



            rootView.findViewById(R.id.textAddOtherFields).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final RelativeLayout relativeLayoutOtherFields = (RelativeLayout) rootView.findViewById(R.id.relativeLayoutOtherFields);
                    relativeLayoutOtherFields.setVisibility(View.VISIBLE);

                    rootView.findViewById(R.id.textAddOtherFields).setVisibility(View.GONE);
                    rootView.findViewById(R.id.textSubOtherFields).setVisibility(View.VISIBLE);

                    (rootView.findViewById(R.id.edtLeadReference)).requestFocus();
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput((rootView.findViewById(R.id.edtLeadReference)), InputMethodManager.SHOW_IMPLICIT);

                }
            });


        rootView.findViewById(R.id.textSubOtherFields).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootView.findViewById(R.id.textAddOtherFields).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.textSubOtherFields).setVisibility(View.GONE);

                final RelativeLayout relativeLayoutOtherFields = (RelativeLayout) rootView.findViewById(R.id.relativeLayoutOtherFields);
                relativeLayoutOtherFields.setVisibility(View.GONE);
            }
        });

        


        rootView.findViewById(R.id.imageDownAddrCustomer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ((Spinner) (rootView.findViewById(R.id.spinnerInitialsAddrCityCustomer))).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String item = (String) parent.getItemAtPosition(position);
                        ((TextView) parent.getChildAt(0)).setTextColor(ContextCompat.getColor(getContext(), R.color.black));

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });


                ((Spinner) (rootView.findViewById(R.id.spinnerInitialsAddrStateCustomer))).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String item = (String) parent.getItemAtPosition(position);
                        ((TextView) parent.getChildAt(0)).setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                        Log.i("@Transworld","Selection of Country:"+((Spinner)(rootView.findViewById(R.id.spinnerInitialsAddrCountryCustomer))).getSelectedItem());
                        String country = (String) ((Spinner)(rootView.findViewById(R.id.spinnerInitialsAddrCountryCustomer))).getSelectedItem();

                        if (parentActivity.isNetworkAvailable()){
                            try {
                                new MeTaskGetCity(MeRepoFactory.getLoginRepository(parentActivity.getDbHelper()).getCompanyMasterId(),country,item).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                ((Spinner) (rootView.findViewById(R.id.spinnerInitialsAddrCountryCustomer))).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String item = (String) parent.getItemAtPosition(position);
                        ((TextView) parent.getChildAt(0)).setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                        Log.i("@Transworld","Selected Item "+item);

                        if (parentActivity.isNetworkAvailable()){
                            try {
                                new MeTaskGetState(MeRepoFactory.getLoginRepository(parentActivity.getDbHelper()).getCompanyMasterId(),item).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        Log.i("@Transworld","Non Selected Item"+parent.getSelectedItem());
                    }
                });

                rootView.findViewById(R.id.textSelectCity).setVisibility(View.VISIBLE);
                final RelativeLayout relativeLayoutSpinnerAddrCityCustomer = (RelativeLayout) rootView.findViewById(R.id.relativeLayoutSpinnerAddrCityCustomer);
                relativeLayoutSpinnerAddrCityCustomer.setVisibility(View.VISIBLE);

                rootView.findViewById(R.id.textSelectState).setVisibility(View.VISIBLE);
                final RelativeLayout relativeLayoutSpinnerAddrStateCustomer = (RelativeLayout) rootView.findViewById(R.id.relativeLayoutSpinnerAddrStateCustomer);
                relativeLayoutSpinnerAddrStateCustomer.setVisibility(View.VISIBLE);

                rootView.findViewById(R.id.textSelectCountry).setVisibility(View.VISIBLE);
                final RelativeLayout relativeLayoutSpinnerAddrCountryCustomer = (RelativeLayout) rootView.findViewById(R.id.relativeLayoutSpinnerAddrCountryCustomer);
                relativeLayoutSpinnerAddrCountryCustomer.setVisibility(View.VISIBLE);

                rootView.findViewById(R.id.txtAddNewAddrCountryCustomer).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.txtAddNewAddrStateCustomer).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.txtAddNewAddrCityCustomer).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.txtAddNewAddrCountryCustomer).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rootView.findViewById(R.id.txtAddNewAddrCountryCustomer).setVisibility(View.GONE);
                        final RelativeLayout relativeLayoutSpinnerAddrCountryCustomer = (RelativeLayout) rootView.findViewById(R.id.relativeLayoutSpinnerAddrCountryCustomer);
                        relativeLayoutSpinnerAddrCountryCustomer.setVisibility(View.GONE);
                        final RelativeLayout relativeLayoutAddrCountry = (RelativeLayout) rootView.findViewById(R.id.relativeLayoutAddrCountry);
                        relativeLayoutAddrCountry.setVisibility(View.VISIBLE);

                        rootView.findViewById(R.id.edtAddrCountry).requestFocus();
                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput((rootView.findViewById(R.id.edtAddrCountry)), InputMethodManager.SHOW_IMPLICIT);

                        rootView.findViewById(R.id.imageClosedTagCountry).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final RelativeLayout relativeLayoutAddrCountry = (RelativeLayout) rootView.findViewById(R.id.relativeLayoutAddrCountry);
                                relativeLayoutAddrCountry.setVisibility(View.GONE);
                                final RelativeLayout relativeLayoutSpinnerAddrCountryCustomer = (RelativeLayout) rootView.findViewById(R.id.relativeLayoutSpinnerAddrCountryCustomer);
                                relativeLayoutSpinnerAddrCountryCustomer.setVisibility(View.VISIBLE);
                                rootView.findViewById(R.id.txtAddNewAddrCountryCustomer).setVisibility(View.VISIBLE);

                                rootView.findViewById(R.id.edtAddrCustomer).requestFocus();
                                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.showSoftInput((rootView.findViewById(R.id.edtAddrCustomer)), InputMethodManager.SHOW_IMPLICIT);
                            }
                        });

                        rootView.findViewById(R.id.txtAddCountry).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(((EditText) (rootView.findViewById(R.id.edtAddrCountry))).getText().toString().length() > 0) {
                                    if (parentActivity.isNetworkAvailable()) {
                                        try {
                                            new MeTaskAddCountry(MeRepoFactory.getLoginRepository(parentActivity.getDbHelper()).getCompanyMasterId(), ((EditText) (rootView.findViewById(R.id.edtAddrCountry))).getText().toString()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                else {
                                    parentActivity.snack(rootView,"Enter Country !!");
                                }

                            }
                        });
                    }
                });

                rootView.findViewById(R.id.txtAddNewAddrStateCustomer).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rootView.findViewById(R.id.txtAddNewAddrStateCustomer).setVisibility(View.GONE);
                        final RelativeLayout relativeLayoutSpinnerAddrStateCustomer = (RelativeLayout) rootView.findViewById(R.id.relativeLayoutSpinnerAddrStateCustomer);
                        relativeLayoutSpinnerAddrStateCustomer.setVisibility(View.GONE);
                        final RelativeLayout relativeLayoutAddrState = (RelativeLayout) rootView.findViewById(R.id.relativeLayoutAddrState);
                        relativeLayoutAddrState.setVisibility(View.VISIBLE);
                        rootView.findViewById(R.id.edtAddrState).requestFocus();
                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput((rootView.findViewById(R.id.edtAddrState)), InputMethodManager.SHOW_IMPLICIT);

                        rootView.findViewById(R.id.imageClosedTagState).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final RelativeLayout relativeLayoutAddrCountry = (RelativeLayout) rootView.findViewById(R.id.relativeLayoutAddrState);
                                relativeLayoutAddrCountry.setVisibility(View.GONE);
                                final RelativeLayout relativeLayoutSpinnerAddrCountryCustomer = (RelativeLayout) rootView.findViewById(R.id.relativeLayoutSpinnerAddrStateCustomer);
                                relativeLayoutSpinnerAddrCountryCustomer.setVisibility(View.VISIBLE);
                                rootView.findViewById(R.id.txtAddNewAddrStateCustomer).setVisibility(View.VISIBLE);

                                rootView.findViewById(R.id.edtAddrCustomer).requestFocus();
                                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.showSoftInput((rootView.findViewById(R.id.edtAddrCustomer)), InputMethodManager.SHOW_IMPLICIT);
                            }
                        });

                        rootView.findViewById(R.id.txtAddState).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(((EditText) (rootView.findViewById(R.id.edtAddrState))).getText().toString().length() > 0) {
                                    if (parentActivity.isNetworkAvailable()) {
                                        try {
                                            new MeTaskAddState(MeRepoFactory.getLoginRepository(parentActivity.getDbHelper()).getCompanyMasterId(), (String) ((Spinner) (rootView.findViewById(R.id.spinnerInitialsAddrCountryCustomer))).getSelectedItem(), ((EditText) (rootView.findViewById(R.id.edtAddrState))).getText().toString()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                else {
                                    parentActivity.snack(rootView,"Enter State !!");
                                }

                            }
                        });

                    }
                });

                rootView.findViewById(R.id.txtAddNewAddrCityCustomer).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rootView.findViewById(R.id.txtAddNewAddrCityCustomer).setVisibility(View.GONE);
                        final RelativeLayout relativeLayoutSpinnerAddrCityCustomer = (RelativeLayout) rootView.findViewById(R.id.relativeLayoutSpinnerAddrCityCustomer);
                        relativeLayoutSpinnerAddrCityCustomer.setVisibility(View.GONE);
                        final RelativeLayout relativeLayoutAddrCity = (RelativeLayout) rootView.findViewById(R.id.relativeLayoutAddrCity);
                        relativeLayoutAddrCity.setVisibility(View.VISIBLE);
                        rootView.findViewById(R.id.edtAddrCity).requestFocus();
                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput((rootView.findViewById(R.id.edtAddrCity)), InputMethodManager.SHOW_IMPLICIT);

                        rootView.findViewById(R.id.imageClosedTagCity).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final RelativeLayout relativeLayoutAddrCity = (RelativeLayout) rootView.findViewById(R.id.relativeLayoutAddrCity);
                                relativeLayoutAddrCity.setVisibility(View.GONE);
                                final RelativeLayout relativeLayoutSpinnerAddrCityCustomer = (RelativeLayout) rootView.findViewById(R.id.relativeLayoutSpinnerAddrCityCustomer);
                                relativeLayoutSpinnerAddrCityCustomer.setVisibility(View.VISIBLE);
                                rootView.findViewById(R.id.txtAddNewAddrCityCustomer).setVisibility(View.VISIBLE);

                                rootView.findViewById(R.id.edtAddrCustomer).requestFocus();
                                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.showSoftInput((rootView.findViewById(R.id.edtAddrCustomer)), InputMethodManager.SHOW_IMPLICIT);
                            }
                        });

                        rootView.findViewById(R.id.txtAddCity).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(((EditText) (rootView.findViewById(R.id.edtAddrCity))).getText().toString().length() > 0) {
                                    if (parentActivity.isNetworkAvailable()) {
                                        try {
                                            new MeTaskAddCity(MeRepoFactory.getLoginRepository(parentActivity.getDbHelper()).getCompanyMasterId(), (String) ((Spinner) (rootView.findViewById(R.id.spinnerInitialsAddrCountryCustomer))).getSelectedItem(), (String) ((Spinner) (rootView.findViewById(R.id.spinnerInitialsAddrStateCustomer))).getSelectedItem(), ((EditText) (rootView.findViewById(R.id.edtAddrCity))).getText().toString()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                else {
                                    parentActivity.snack(rootView,"Enter City !!");
                                }

                            }
                        });

                    }
                });
                rootView.findViewById(R.id.imageUpAddrCustomer).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.imageDownAddrCustomer).setVisibility(View.GONE);
            }
        });

        rootView.findViewById(R.id.imageUpAddrCustomer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rootView.findViewById(R.id.edtAddrCustomer).requestFocus();
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput((rootView.findViewById(R.id.edtAddrCustomer)), InputMethodManager.SHOW_IMPLICIT);

                rootView.findViewById(R.id.textSelectCity).setVisibility(View.GONE);
                final RelativeLayout relativeLayoutSpinnerAddrCityCustomer = (RelativeLayout) rootView.findViewById(R.id.relativeLayoutSpinnerAddrCityCustomer);
                relativeLayoutSpinnerAddrCityCustomer.setVisibility(View.GONE);

                rootView.findViewById(R.id.textSelectState).setVisibility(View.GONE);
                final RelativeLayout relativeLayoutSpinnerAddrStateCustomer = (RelativeLayout) rootView.findViewById(R.id.relativeLayoutSpinnerAddrStateCustomer);
                relativeLayoutSpinnerAddrStateCustomer.setVisibility(View.GONE);

                rootView.findViewById(R.id.textSelectCountry).setVisibility(View.GONE);
                final RelativeLayout relativeLayoutSpinnerAddrCountryCustomer = (RelativeLayout) rootView.findViewById(R.id.relativeLayoutSpinnerAddrCountryCustomer);
                relativeLayoutSpinnerAddrCountryCustomer.setVisibility(View.GONE);

                rootView.findViewById(R.id.txtAddNewAddrCountryCustomer).setVisibility(View.GONE);
                rootView.findViewById(R.id.txtAddNewAddrStateCustomer).setVisibility(View.GONE);
                rootView.findViewById(R.id.txtAddNewAddrCityCustomer).setVisibility(View.GONE);

                final RelativeLayout relativeLayoutAddrCountry = (RelativeLayout) rootView.findViewById(R.id.relativeLayoutAddrCountry);
                relativeLayoutAddrCountry.setVisibility(View.GONE);

                final RelativeLayout relativeLayoutAddrState = (RelativeLayout) rootView.findViewById(R.id.relativeLayoutAddrState);
                relativeLayoutAddrCountry.setVisibility(View.GONE);

                final RelativeLayout relativeLayoutAddrCity = (RelativeLayout) rootView.findViewById(R.id.relativeLayoutAddrCity);
                relativeLayoutAddrCountry.setVisibility(View.GONE);

                rootView.findViewById(R.id.imageUpAddrCustomer).setVisibility(View.GONE);
                rootView.findViewById(R.id.imageDownAddrCustomer).setVisibility(View.VISIBLE);
            }
        });
    }


    private final MeAddCustomer makeRequestObject(View rootView) throws Exception {

        MeAddCustomer makeRequestObject = new MeAddCustomer();

        makeRequestObject.setSalesCustomerCode(0);
        makeRequestObject.setOpportunityCode(0);
        makeRequestObject.setCompanyMasterId(repoLogin.getCompanyMasterId());
        makeRequestObject.setCompanyName(((EditText) (rootView.findViewById(R.id.edtCompanyName))).getText().toString().length() > 0 ? ((EditText) (rootView.findViewById(R.id.edtCompanyName))).getText().toString() : "");
        makeRequestObject.setContactPerson((((EditText) rootView.findViewById(R.id.edtContactPerson)).getText().toString()));
        makeRequestObject.setWebSite(((EditText) (rootView.findViewById(R.id.edtWebSite))).getText().toString().length() > 0 ? ((EditText) (rootView.findViewById(R.id.edtWebSite))).getText().toString() : "");
        makeRequestObject.seteMail(((EditText) (rootView.findViewById(R.id.edtEmailCustomer))).getText().toString().length() > 0 ? ((EditText) (rootView.findViewById(R.id.edtEmailCustomer))).getText().toString() : "");
        makeRequestObject.setMobileNo(((EditText) (rootView.findViewById(R.id.edtMobileNo))).getText().toString().length() > 0 ? ((EditText) (rootView.findViewById(R.id.edtMobileNo))).getText().toString() : "");
        makeRequestObject.setStdCode(((EditText) (rootView.findViewById(R.id.edtStdCode))).getText().toString().length() > 0 ? ((EditText) (rootView.findViewById(R.id.edtStdCode))).getText().toString() : "");
        makeRequestObject.setPhone(((EditText) (rootView.findViewById(R.id.edtPhoneNo))).getText().toString().length() > 0 ? ((EditText) (rootView.findViewById(R.id.edtPhoneNo))).getText().toString() : "");
        makeRequestObject.setAddress(((EditText) (rootView.findViewById(R.id.edtAddrCustomer))).getText().toString().length() > 0 ? ((EditText) (rootView.findViewById(R.id.edtAddrCustomer))).getText().toString() : "");
        //(spinnerStatus.getSelectedItem() != null) ? (String) spinnerStatus.getSelectedItem() : ""
        makeRequestObject.setCity((((Spinner)(rootView.findViewById(R.id.spinnerInitialsAddrCityCustomer))).getSelectedItem()!=null) ? (((Spinner) (rootView.findViewById(R.id.spinnerInitialsAddrCityCustomer))).getSelectedItem().toString()) : "");
        makeRequestObject.setState((((Spinner)(rootView.findViewById(R.id.spinnerInitialsAddrStateCustomer))).getSelectedItem()!=null) ? (((Spinner) (rootView.findViewById(R.id.spinnerInitialsAddrStateCustomer))).getSelectedItem().toString()) : "");
        makeRequestObject.setCountry((((Spinner)(rootView.findViewById(R.id.spinnerInitialsAddrCountryCustomer))).getSelectedItem()!=null) ? (((Spinner) (rootView.findViewById(R.id.spinnerInitialsAddrCountryCustomer))).getSelectedItem().toString()) : "");

        //makeRequestObject.setCity(((EditText) (rootView.findViewById(R.id.edtCity))).getText().toString().length() > 0 ? ((EditText) (rootView.findViewById(R.id.edtCity))).getText().toString() : "");
        makeRequestObject.setZip(((EditText) (rootView.findViewById(R.id.edtZipCode))).getText().toString().length() > 0 ? ((EditText) (rootView.findViewById(R.id.edtZipCode))).getText().toString() : "");
        makeRequestObject.setFax(((EditText) (rootView.findViewById(R.id.edtFaxNo))).getText().toString().length() > 0 ? ((EditText) (rootView.findViewById(R.id.edtFaxNo))).getText().toString() : "");

        //makeRequestObject.setDesignation(((EditText) (rootView.findViewById(R.id.edtDesignation))).getText().toString().length() > 0 ? ((EditText) (rootView.findViewById(R.id.edtDesignation))).getText().toString() : "");

        makeRequestObject.setMobileNo1(((EditText) (rootView.findViewById(R.id.edtMobileNo2))).getText().toString().length() > 0 ? ((EditText) (rootView.findViewById(R.id.edtMobileNo2))).getText().toString() : "");
        makeRequestObject.setMobileNo2(((EditText) (rootView.findViewById(R.id.edtMobileNo3))).getText().toString().length() > 0 ? ((EditText) (rootView.findViewById(R.id.edtMobileNo3))).getText().toString() : "");
        makeRequestObject.setTelephoneNo2(((EditText) (rootView.findViewById(R.id.edtAltNo))).getText().toString().length() > 0 ? ((EditText) (rootView.findViewById(R.id.edtAltNo))).getText().toString() : "");

        makeRequestObject.setLeadRef(((EditText) (rootView.findViewById(R.id.edtLeadReference))).getText().toString().length() > 0 ? ((EditText) (rootView.findViewById(R.id.edtLeadReference))).getText().toString() : "");
        makeRequestObject.setIntrestedProduct(((EditText) (rootView.findViewById(R.id.edtInterestedInProduct))).getText().toString().length() > 0 ? ((EditText) (rootView.findViewById(R.id.edtInterestedInProduct))).getText().toString() : "");
        makeRequestObject.setWeeklyOffon(((EditText) (rootView.findViewById(R.id.edtWeeklyOff))).getText().toString().length() > 0 ? ((EditText) (rootView.findViewById(R.id.edtWeeklyOff))).getText().toString() : "");
        makeRequestObject.setPotentialValue((((EditText) (rootView.findViewById(R.id.edtPotentialValue))).getText().toString().length() > 0) ? Double.parseDouble (((EditText) (rootView.findViewById(R.id.edtPotentialValue))).getText().toString()) : 0.0d);
        makeRequestObject.setNoOfUnits(((((EditText) (rootView.findViewById(R.id.edtNoOfUnits))).getText().toString().length() > 0) ? Integer.parseInt(((EditText) (rootView.findViewById(R.id.edtNoOfUnits))).getText().toString()) : 0));
        makeRequestObject.setCategory((((Spinner)(rootView.findViewById(R.id.spinnerInitialsCategory))).getSelectedItem()!=null) ? (((Spinner) (rootView.findViewById(R.id.spinnerInitialsCategory))).getSelectedItem().toString()) : "");
        makeRequestObject.setComments(((EditText) (rootView.findViewById(R.id.edtComments))).getText().toString().length() > 0 ? ((EditText) (rootView.findViewById(R.id.edtComments))).getText().toString() : "");


        Log.i(MainActivity.TAG, "MakeReqObj : " + makeRequestObject);
        return makeRequestObject;
    }

    private final class MeConnectorAddCustomer implements MeConnectable {
        public Map<String, Object> saveNewCustomer() throws Exception {
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), JsonMan.fromObject(makeRequestObject(getView())));
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(MeIUrl.URL_ADD_CUSTOMER)
                    .post(body)
                    .addHeader("Accept", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            String responsJson = response.body().string();
            Log.i(MainActivity.TAG, "Response Json Add Contact- " + responsJson);
            Log.i("@Transworld", MeIUrl.URL_ADD_CUSTOMER);
            Map<String, Object> mapEntity = JsonMan.parseAnything(responsJson, new TypeReference<Map<String, Object>>() {
            });
            Log.i(MainActivity.TAG, "Response Entity - " + mapEntity);
            String status = mapEntity.get("status").toString();
            Log.i(MainActivity.TAG, "Status :  " + status);
            if (status.equals("Contact Added Successfully")) {
                Log.i(MainActivity.TAG, "Status 2:  " + status);
                Log.i(MainActivity.TAG, "Fragment followup loaded");
            }
            return mapEntity;
        }
    }

    private final class MeTaskSaveCustomer extends AsyncTask<Void, Void, Map<String, Object>> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(parentActivity, "Contact", "saving contact");
        }

        @Override
        protected Map<String, Object> doInBackground(Void... params) {
            MeConnectorAddCustomer connector = new MeConnectorAddCustomer();
            Map<String, Object> mapEntity = null;
            try {
                mapEntity = connector.saveNewCustomer();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mapEntity;
        }

        @Override
        protected void onPostExecute(Map<String, Object> mapEntity) {
            super.onPostExecute(mapEntity);
            progressDialog.dismiss();
           // parentActivity.snack(getView(),"Customer Added Successfully !! :)");
            if (mapEntity != null) {
                String status = (String) mapEntity.get("status");
                String message = (String) mapEntity.get("message");
                if (status.equals("success")) {
                    parentActivity.snack(getView(), "" + message);
                    /*Log.i("@Transworld","From Frag - " +getArguments().get(KEY_FROM_FRAGMENT));
                    if (!getArguments().get(KEY_FROM_FRAGMENT).equals("nextFragment")) {
                        parentActivity.popBackStack(2);  //if come from followUpNext fragment
                    }
                    else {*/
                    parentActivity.popBackStack(1);
                  /*  }
                } else {
                    parentActivity.snack(getView(), message);
                }*/
                }
            }
        }
    }

    private final class MeConnectorGetCountry implements MeConnectable {
        public List<String> listGetCountry(Integer companyMasterId) throws Exception {
            OkHttpClient okHttpClient = new OkHttpClient();
            String urlPathVariable = MeIUrl.URL_GET_COUNTRY + companyMasterId;
            Log.i("@Transworld", "URL Path " + urlPathVariable);
            Request request = new Request.Builder()
                    .url(urlPathVariable)
                    .get()
                    .addHeader("Accept", "application/json")
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            String responsJson = response.body().string();
            Log.i("@Transworld", "Json Country To- " + responsJson);
            Map<String, Object> mapEntity = JsonMan.parseAnything(responsJson, new TypeReference<Map<String, Object>>() {
            });
            Log.i("@Transworld", "Map Entity - " + mapEntity);
            List<String> list = (List<String>) mapEntity.get("country");
            Log.i("@Transworld", "List Country - " + list);
            return list;
        }
    }

    private final class MeTaskGetCountry extends AsyncTask<Void,Void, List<String>>{
        private Integer companyMasterId;
        private MeConnectorGetCountry connectorGetCountry;

        public MeTaskGetCountry(Integer companyMasterId) {
            this.companyMasterId = companyMasterId;
            connectorGetCountry = new MeConnectorGetCountry();
        }

        @Override
        protected List<String> doInBackground(Void... params) {
            List<String> listOfCountry = null;
            try {
                listOfCountry = connectorGetCountry.listGetCountry(companyMasterId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ((listOfCountry != null) ? listOfCountry : new ArrayList<String>());
        }

        @Override
        protected void onPostExecute(List<String> list) {
            super.onPostExecute(list);
            Log.i("@Transworld","Map In Post " +list.toString());
            listCountry.clear();
            for (String country : list){
                listCountry.add(country);
            }
            arrayAdapterCountry.notifyDataSetChanged();
        }
    }

    private final class MeConnectorGetState implements MeConnectable {
        public List<String> listGetState(Integer companyMasterId,String country) throws Exception {
            OkHttpClient okHttpClient = new OkHttpClient();
            String urlPathVariable = MeIUrl.URL_GET_STATE + companyMasterId + "/" + country.replace(" ","%20");
            Log.i("@Transworld", "URL Path " + urlPathVariable);
            Request request = new Request.Builder()
                    .url(urlPathVariable)
                    .get()
                    .addHeader("Accept", "application/json")
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            String responsJson = response.body().string();
            Log.i("@Transworld", "Json State To- " + responsJson);
            Map<String, Object> mapEntity = JsonMan.parseAnything(responsJson, new TypeReference<Map<String, Object>>() {
            });
            Log.i("@Transworld", "Map Entity - " + mapEntity);
            List<String> list = (List<String>) mapEntity.get("state");
            Log.i("@Transworld", "List State - " + list);
            return list;
        }
    }

    private final class MeTaskGetState extends AsyncTask<Void,Void,List<String>>{
        private Integer companyMasterId;
        private String country;
        private MeConnectorGetState connectorGetState;
        public MeTaskGetState(Integer companyMasterId, String country) {
            this.companyMasterId = companyMasterId;
            this.country = country;
            connectorGetState = new MeConnectorGetState();
        }

        @Override
        protected List<String> doInBackground(Void... params) {
            List<String> listOfCountry = null;
            try {
                listOfCountry = connectorGetState.listGetState(companyMasterId,country);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ((listOfCountry != null) ? listOfCountry : new ArrayList<String>());
        }

        @Override
        protected void onPostExecute(List<String> list) {
            super.onPostExecute(list);
            Log.i("@Transworld","Map In Post " +list.toString());
            listState.clear();
            for (String country : list){
                listState.add(country);
            }
            arrayAdapterState.notifyDataSetChanged();
        }
    }

    private final class MeConnectorGetCity implements MeConnectable {
        public List<String> listGetCity(Integer companyMasterId,String country,String state) throws Exception {
            OkHttpClient okHttpClient = new OkHttpClient();
            String urlPathVariable = MeIUrl.URL_GET_CITY + companyMasterId + "/" + country.replace(" ","%20") + "/" +state.replace(" ","%20");
            Log.i("@Transworld", "URL Path " + urlPathVariable);
            Request request = new Request.Builder()
                    .url(urlPathVariable)
                    .get()
                    .addHeader("Accept", "application/json")
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            String responsJson = response.body().string();
            Log.i("@Transworld", "Json city To- " + responsJson);
            Map<String, Object> mapEntity = JsonMan.parseAnything(responsJson, new TypeReference<Map<String, Object>>() {
            });
            Log.i("@Transworld", "Map Entity - " + mapEntity);
            List<String> list = (List<String>) mapEntity.get("city");
            Log.i("@Transworld", "List city - " + list);
            return list;
        }
    }

    private final class MeTaskGetCity extends AsyncTask<Void,Void,List<String>>{
        private Integer companyMasterId;
        private String country;
        private String state;
        private MeConnectorGetCity connectorGetCity;

        public MeTaskGetCity(Integer companyMasterId, String country,String state) {
            this.companyMasterId = companyMasterId;
            this.country = country;
            this.state = state;
            connectorGetCity = new MeConnectorGetCity();
        }

        @Override
        protected List<String> doInBackground(Void... params) {
            List<String> listOfCountry = null;
            try {
                listOfCountry = connectorGetCity.listGetCity(companyMasterId,country,state);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ((listOfCountry != null) ? listOfCountry : new ArrayList<String>());
        }

        @Override
        protected void onPostExecute(List<String> list) {
            super.onPostExecute(list);
            Log.i("@Transworld","Map In Post " +list.toString());
            listCity.clear();
            for (String country : list){
                listCity.add(country);
            }
            arrayAdapterCity.notifyDataSetChanged();
        }
    }

    private final class MeConnectorAddCountry implements MeConnectable {
        public Map<String,Object> addCountry(Integer companyMasterId,String country) throws Exception {
            OkHttpClient okHttpClient = new OkHttpClient();
            String urlPathVariable = MeIUrl.URL_ADD_COUNTRY + companyMasterId + "/" + country.replace(" ","%20");
            Log.i("@Transworld", "URL Path " + urlPathVariable);
            Request request = new Request.Builder()
                    .url(urlPathVariable)
                    .get()
                    .addHeader("Accept", "application/json")
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            String responsJson = response.body().string();
            Log.i("@Transworld", "Json Country To- " + responsJson);
            Map<String, Object> mapEntity = JsonMan.parseAnything(responsJson, new TypeReference<Map<String, Object>>() {
            });
            Log.i("@Transworld", "Map Entity - " + mapEntity);

            return mapEntity;
        }
    }

    private final class MeConnectorAddState implements MeConnectable {
        public Map<String,Object> addState(Integer companyMasterId,String country,String state) throws Exception {
            OkHttpClient okHttpClient = new OkHttpClient();
            String urlPathVariable = MeIUrl.URL_ADD_STATE + companyMasterId + "/" + country.replace(" ","%20") + "/" +state.replace(" ","%20");
            Log.i("@Transworld", "URL Path " + urlPathVariable);
            Request request = new Request.Builder()
                    .url(urlPathVariable)
                    .get()
                    .addHeader("Accept", "application/json")
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            String responsJson = response.body().string();
            Log.i("@Transworld", "Json State To- " + responsJson);
            Map<String, Object> mapEntity = JsonMan.parseAnything(responsJson, new TypeReference<Map<String, Object>>() {
            });
            Log.i("@Transworld", "Map Entity - " + mapEntity);

            return mapEntity;
        }
    }

    private final class MeConnectorAddCity implements MeConnectable {
        public Map<String,Object> addCity(Integer companyMasterId,String country,String state,String city) throws Exception {
            OkHttpClient okHttpClient = new OkHttpClient();
            String urlPathVariable = MeIUrl.URL_ADD_CITY + companyMasterId + "/" + country.replace(" ","%20") + "/" +state.replace(" ","%20") + "/" +city.replace(" ","%20");
            Log.i("@Transworld", "URL Path " + urlPathVariable);
            Request request = new Request.Builder()
                    .url(urlPathVariable)
                    .get()
                    .addHeader("Accept", "application/json")
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            String responsJson = response.body().string();
            Log.i("@Transworld", "Json City To- " + responsJson);
            Map<String, Object> mapEntity = JsonMan.parseAnything(responsJson, new TypeReference<Map<String, Object>>() {
            });
            Log.i("@Transworld", "Map Entity - " + mapEntity);

            return mapEntity;
        }
    }

    private class MeTaskAddCountry extends AsyncTask<Void,Void,Map<String,Object>>{
        private Integer companyMasterId;
        private String country;
        private MeConnectorAddCountry connectorAddCountry;
        private ProgressDialog progressDialog;
        public MeTaskAddCountry(Integer companyMasterId, String country) {
            this.companyMasterId = companyMasterId;
            this.country = country;
            connectorAddCountry = new MeConnectorAddCountry();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(parentActivity,"Add Customer","Adding Country");
        }

        @Override
        protected Map<String, Object> doInBackground(Void... params) {
            Map<String,Object> map = null;
            try {
               map = connectorAddCountry.addCountry(companyMasterId,country);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return map;
        }

        @Override
        protected void onPostExecute(Map<String, Object> map) {
            super.onPostExecute(map);
            progressDialog.dismiss();
            if(map!=null){

                if (map.get("status").equals("success"))
                {
                    new MeTaskGetCountry(companyMasterId).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    final RelativeLayout relativeLayoutAddrCountry = (RelativeLayout) getView().findViewById(R.id.relativeLayoutAddrCountry);
                    relativeLayoutAddrCountry.setVisibility(View.GONE);
                    final RelativeLayout relativeLayoutSpinnerAddrCountryCustomer = (RelativeLayout) getView().findViewById(R.id.relativeLayoutSpinnerAddrCountryCustomer);
                    relativeLayoutSpinnerAddrCountryCustomer.setVisibility(View.VISIBLE);
                    getView().findViewById(R.id.txtAddNewAddrCountryCustomer).setVisibility(View.VISIBLE);

                }
                else {
                    parentActivity.snack(getView(), (String) map.get("message"));
                }

            }
        }
    }

    private class MeTaskAddState extends AsyncTask<Void,Void,Map<String,Object>>{
        private Integer companyMasterId;
        private String country;
        private String state;
        private MeConnectorAddState connectorAddState;
        private ProgressDialog progressDialog;
        public MeTaskAddState(Integer companyMasterId, String country,String state) {
            this.companyMasterId = companyMasterId;
            this.country = country;
            this.state = state;
            connectorAddState = new MeConnectorAddState();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(parentActivity,"Add Customer","Adding State");
        }

        @Override
        protected Map<String, Object> doInBackground(Void... params) {
            Map<String,Object> map = null;
            try {
                map = connectorAddState.addState(companyMasterId,country,state);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return map;
        }

        @Override
        protected void onPostExecute(Map<String, Object> map) {
            super.onPostExecute(map);
            progressDialog.dismiss();
            if(map!=null){
                String status = (String) map.get("status");
                if (status.equals("success"))
                {
                    new MeTaskGetState(companyMasterId,country).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    final RelativeLayout relativeLayoutAddrState = (RelativeLayout) getView().findViewById(R.id.relativeLayoutAddrState);
                    relativeLayoutAddrState.setVisibility(View.GONE);
                    final RelativeLayout relativeLayoutSpinnerAddrStateCustomer = (RelativeLayout) getView().findViewById(R.id.relativeLayoutSpinnerAddrStateCustomer);
                    relativeLayoutSpinnerAddrStateCustomer.setVisibility(View.VISIBLE);
                    getView().findViewById(R.id.txtAddNewAddrStateCustomer).setVisibility(View.VISIBLE);
                }
                else {
                    parentActivity.snack(getView(), (String) map.get("message"));
                }

            }
        }
    }

    private class MeTaskAddCity extends AsyncTask<Void,Void,Map<String,Object>>{
        private Integer companyMasterId;
        private String country;
        private String state;
        private String city;
        private MeConnectorAddCity connectorAddCity;
        private ProgressDialog progressDialog;
        public MeTaskAddCity(Integer companyMasterId, String country,String state,String city) {
            this.companyMasterId = companyMasterId;
            this.country = country;
            this.state = state;
            this.city = city;
            connectorAddCity = new MeConnectorAddCity();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(parentActivity,"Add Customer","Adding City");
        }

        @Override
        protected Map<String, Object> doInBackground(Void... params) {
            Map<String,Object> map = null;
            try {
                map = connectorAddCity.addCity(companyMasterId,country,state,city);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return map;
        }

        @Override
        protected void onPostExecute(Map<String, Object> map) {
            super.onPostExecute(map);
            progressDialog.dismiss();
            if(map!=null){
                String status = (String) map.get("status");
                if (status.equals("success"))
                {
                    new MeTaskGetCity(companyMasterId,country,state).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    final RelativeLayout relativeLayoutAddrCity = (RelativeLayout) getView().findViewById(R.id.relativeLayoutAddrCity);
                    relativeLayoutAddrCity.setVisibility(View.GONE);
                    final RelativeLayout relativeLayoutSpinnerAddrCityCustomer = (RelativeLayout) getView().findViewById(R.id.relativeLayoutSpinnerAddrCityCustomer);
                    relativeLayoutSpinnerAddrCityCustomer.setVisibility(View.VISIBLE);
                    getView().findViewById(R.id.txtAddNewAddrCityCustomer).setVisibility(View.VISIBLE);
                }
                else {
                    parentActivity.snack(getView(), (String) map.get("message"));
                }

            }
        }
    }
}
