package com.transworldtechnology.crm.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.transworldtechnology.crm.R;
import com.transworldtechnology.crm.activity.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by root on 12/1/16.
 */
public class MeFragmentAddContact1 extends Fragment
{
    public static final String KEY_FRAGMENT_NAME = "key_fragment_name";
    public static final String KEY_SALUTATION = "salutation";
    public static final String KEY_FIRST_NAME = "firstName";
    public static final String KEY_MIDDLE_NAME = "middleName";
    public static final String KEY_LAST_NAME = "lastName";
    public static final String KEY_CONTACT_PERSON = "contactPerson";
    public static final String KEY_DESIGNATION = "designation";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_CITY = "city";
    public static final String KEY_ZIP_CODE = "zipCode";
    public static final String KEY_URI_CONTACT = "uriContact";
    private static final Integer INTENT_REQUEST_GET_CONTACT = 1501;
    private MainActivity parentActivity;
    private MeClick click;
    private View rootView;
    private Button btnNextContact;
    private Spinner spinnerInitials;
    private EditText edtFirstName, edtMiddleName, edtLastName, edtDesignation, edtAddress, edtCity, edtZipCode;
    private ImageView imgAddContactFromAddress;
    private Uri uriContact;

    public static MeFragmentAddContact1 getInstance(String companyName, Integer customerCode, Integer salesCustomerCode) {
        MeFragmentAddContact1 meFragmentAddContact1 = new MeFragmentAddContact1();
        Bundle args = new Bundle();
        args.putString(MainActivity.KEY_FRAGMENT_NAME, "MeFragmentAddContact1");
        args.putInt(MeFragmentFollowUp.KEY_CUSTOMER_CODE, customerCode);
        args.putInt(MeFragmentFollowUp.KEY_SALES_CUSTOMER_CODE, salesCustomerCode);
        args.putString(MeFragmentFollowUp.KEY_COMPANY_NAME, companyName);
        meFragmentAddContact1.setArguments(args);
        return meFragmentAddContact1;



    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentActivity = (MainActivity) getActivity();
        click = new MeClick();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentActivity.getSupportActionBar().show();
        rootView = inflater.inflate(R.layout.fragment_add_contact_1, container, false);
        parentActivity.setTitle("Add Contact");
        initSpinner();
        initEditText();
        initButton();
        initImageView();
        cleanIt();
        return rootView;
    }

    private void initImageView() {
        imgAddContactFromAddress = (ImageView) rootView.findViewById(R.id.imgAddContactFromAddress);
        imgAddContactFromAddress.setOnClickListener(click);
    }

    private final void initSpinner() {
        spinnerInitials = (Spinner) rootView.findViewById(R.id.spinnerInitials);
        ArrayList<String> initialsList = new ArrayList<String>();
        initialsList.add("Mr.");
        initialsList.add("Ms.");
        initialsList.add("Mrs.");
        initialsList.add("Dr.");
        initialsList.add("M/s.");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, initialsList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerInitials.setAdapter(arrayAdapter);
    }

    private final void initEditText() {
        edtFirstName = (EditText) rootView.findViewById(R.id.edtFirstName);
        edtMiddleName = (EditText) rootView.findViewById(R.id.edtMiddleName);
        edtLastName = (EditText) rootView.findViewById(R.id.edtLastName);
        edtAddress = (EditText) rootView.findViewById(R.id.edtAddress);
        edtCity = (EditText) rootView.findViewById(R.id.edtCity);
        edtDesignation = (EditText) rootView.findViewById(R.id.edtDesignation);
        edtZipCode = (EditText) rootView.findViewById(R.id.edtZipCode);
    }

    private final void initButton() {
        btnNextContact = (Button) rootView.findViewById(R.id.btnNextContact);
        btnNextContact.setOnClickListener(click);
    }

    private final Boolean isFirstNameFilled() {
        Log.i("@Transworld", "" + edtFirstName.getText().toString().startsWith(" "));
        return edtFirstName.getText().toString().length() > 0 && !(edtFirstName.getText().toString().contains(" "));
    }

    private final Boolean isMiddleNameFilled() {
        return edtMiddleName.getText().toString().length() > 0;
    }

    private final Boolean isLastNameFilled() {
        return edtLastName.getText().toString().length() > 0 && !(edtLastName.getText().toString().contains(" "));
    }

    private final Boolean areAllFilled() {
        return isFirstNameFilled() && isLastNameFilled();
    }

    private void cleanIt() {
        edtFirstName.setText("");
        edtMiddleName.setText("");
        edtLastName.setText("");
        edtAddress.setText("");
        edtCity.setText("");
        edtDesignation.setText("");
        edtZipCode.setText("");
    }

    private HashMap<String, Object> makePartialMap() {
        HashMap<String, Object> mapAddContact1 = new HashMap<>();
        String salutation = (String) spinnerInitials.getSelectedItem();
        String companyName = (String) getArguments().get(MeFragmentFollowUp.KEY_COMPANY_NAME);
        String firstName = edtFirstName.getText().toString().length() > 0 ? edtFirstName.getText().toString().trim() : "";
        String middleName = edtMiddleName.getText().toString().length() > 0 ? edtMiddleName.getText().toString().trim() : "";
        String lastName = edtLastName.getText().toString().length() > 0 ? edtLastName.getText().toString().trim() : "";
        mapAddContact1.put(KEY_SALUTATION, salutation);
        mapAddContact1.put(KEY_FIRST_NAME, firstName);
        mapAddContact1.put(KEY_MIDDLE_NAME, middleName);
        mapAddContact1.put(KEY_LAST_NAME, lastName);
        mapAddContact1.put(KEY_CONTACT_PERSON, salutation + firstName + " " + middleName + " " + lastName);
        mapAddContact1.put(KEY_DESIGNATION, edtDesignation.getText().toString().length() > 0 ? edtDesignation.getText().toString() : "");
        mapAddContact1.put(KEY_ADDRESS, edtAddress.getText().toString().length() > 0 ? edtAddress.getText().toString() : "");
        mapAddContact1.put(KEY_CITY, edtCity.getText().toString().length() > 0 ? edtCity.getText().toString() : "");
        mapAddContact1.put(KEY_ZIP_CODE, edtZipCode.getText().toString().length() > 0 ? edtZipCode.getText().toString() : "");
        mapAddContact1.put(MeFragmentFollowUp.KEY_CUSTOMER_CODE, getArguments().getInt(MeFragmentFollowUp.KEY_CUSTOMER_CODE));
        mapAddContact1.put(MeFragmentFollowUp.KEY_SALES_CUSTOMER_CODE, getArguments().getInt(MeFragmentFollowUp.KEY_SALES_CUSTOMER_CODE));
        mapAddContact1.put(MeFragmentFollowUp.KEY_COMPANY_NAME, ((companyName != null && !companyName.equals("")) ? companyName : ""));
        mapAddContact1.put(KEY_URI_CONTACT, (uriContact != null ? uriContact : ""));
        Log.i("@Transworld", "Customer Code Hash" + mapAddContact1);
        return mapAddContact1;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == INTENT_REQUEST_GET_CONTACT) {
                if (data != null) {
                    uriContact = data.getData();
                    Log.i("@Transworld", " Contact URI : " + uriContact);
                    if (uriContact != null) {
                        Cursor cursor;
                        cursor = getContext().getContentResolver().query(uriContact, new String[]{
                                ContactsContract.PhoneLookup.DISPLAY_NAME,
                                ContactsContract.CommonDataKinds.Email.DATA
                        }, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            String fullName = cursor.getString(0);
                            String name[] = new String[0];
                            String email = cursor.getString(1);
                            Log.i("@Transworld", " Name : " + fullName);
                            Log.i("@Transworld", " Email : " + email);
                            if (fullName.contains(" ")) {
                                name = fullName.split(" ");
                                edtFirstName.setText((name[0] != null) ? name[0] : "");
                                edtLastName.setText((name[1] != null) ? name[1] : "");
                            } else {
                                edtFirstName.setText(fullName);
                            }
                        }
                    }
                }
            }
        }
    }

    private final class MeClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.getId() == btnNextContact.getId()) {
                if (edtFirstName.getText().toString().startsWith(" "))
                    edtFirstName.setText("");
                if (edtLastName.getText().toString().startsWith(" "))
                    edtLastName.setText("");
                if (areAllFilled())
                    parentActivity.runFragmentTransaction(R.id.frameMainContainer, MeFragmentAddContact2.newInstance(makePartialMap()));
                else {
                    parentActivity.snack(rootView, !isFirstNameFilled() ? "You missed first name :(" : "You missed last name :(");
                }
            }
            if (v.getId() == imgAddContactFromAddress.getId()) {
                cleanIt();
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                startActivityForResult(intent, INTENT_REQUEST_GET_CONTACT);
            }
        }
    }
}
