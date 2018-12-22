package com.transworldtechnology.crm.fragment;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.fasterxml.jackson.core.type.TypeReference;
import com.transworldtechnology.crm.R;
import com.transworldtechnology.crm.activity.MainActivity;
import com.transworldtechnology.crm.database.MeRepoFactory;
import com.transworldtechnology.crm.database.repository.MeRepoContactDet;
import com.transworldtechnology.crm.database.repository.MeRepoImplContactDet;
import com.transworldtechnology.crm.database.repository.MeRepoLogin;
import com.transworldtechnology.crm.domain.MeAddContact;
import com.transworldtechnology.crm.web.JsonMan;
import com.transworldtechnology.crm.web.MeConnectable;
import com.transworldtechnology.crm.web.MeIUrl;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by root on 12/1/16.
 */
public class MeFragmentAddContact2 extends Fragment {
    public static final String KEY_FRAGMENT_NAME = "key_fragment_name";
    public static final String KEY_PARTIAL_MAP = "partialMap";
    private MainActivity parentActivity;
    private MeClick click;
    private MeRepoLogin repoLogin;
    private Map<String, Object> mapPartial;
    private View rootView;
    private EditText edtState, edtCountry, edtEmailId, edtEmail1, edtEmail2, edtMobileNo, edtMobileNo1, edtMobileNo2, edtPhoneNo, edtAltNo, edtFaxNo;
    private Button btnSave;
    private ImageView imageAddEmail, imageAddMobile;
    private RelativeLayout relativeEmail, relativeMobileNos;


    public static MeFragmentAddContact2 newInstance(HashMap<String, Object> mapAddContact1) {
        Log.i("@Transworld", mapAddContact1.toString());
        MeFragmentAddContact2 meFragmentAddContact2 = new MeFragmentAddContact2();
        Bundle args = new Bundle();
        args.putString(MainActivity.KEY_FRAGMENT_NAME, "MeFragmentAddContact2");
        args.putSerializable(KEY_PARTIAL_MAP, mapAddContact1);
        meFragmentAddContact2.setArguments(args);
        return meFragmentAddContact2;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        click = new MeClick();
        parentActivity = (MainActivity) getActivity();
        repoLogin = MeRepoFactory.getLoginRepository(parentActivity.getDbHelper());
        mapPartial = (Map<String, Object>) getArguments().get(KEY_PARTIAL_MAP);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentActivity.getSupportActionBar().show();
        rootView = inflater.inflate(R.layout.fragment_add_contact_2, container, false);
        initEditText();
        initButton();
        initRLayout();
        initImageView();
        parentActivity.setTitle("Add Contact");
        //  cleanIt();
        return rootView;
    }

    private String getDataFromURI() {
        Log.i("@Transworld", "Map Uri " + mapPartial.get(MeFragmentAddContact1.KEY_URI_CONTACT));
        String number = null;
        if ((mapPartial.get(MeFragmentAddContact1.KEY_URI_CONTACT)) != "") {
            Uri uri = (Uri) mapPartial.get(MeFragmentAddContact1.KEY_URI_CONTACT);
            number = null;
            if (uri != null) {
                Cursor cursor = null;
                cursor = getContext().getContentResolver().query(uri, new String[]{
                        ContactsContract.CommonDataKinds.Email.DATA
                }, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    number = cursor.getString(0).trim();
                }
            }
        }
        return number;
    }

    private final void initEditText() {
        edtState = (EditText) rootView.findViewById(R.id.edtState);
        edtCountry = (EditText) rootView.findViewById(R.id.edtCountry);
        edtEmailId = (EditText) rootView.findViewById(R.id.edtEmailId);
        edtEmail1 = (EditText) rootView.findViewById(R.id.edtEmail1);
        edtEmail2 = (EditText) rootView.findViewById(R.id.edtEmail2);
        edtMobileNo = (EditText) rootView.findViewById(R.id.edtMobileNo);
        edtMobileNo1 = (EditText) rootView.findViewById(R.id.edtMobileNo1);
        edtMobileNo2 = (EditText) rootView.findViewById(R.id.edtMobileNo2);
        edtPhoneNo = (EditText) rootView.findViewById(R.id.edtPhoneNo);
        edtAltNo = (EditText) rootView.findViewById(R.id.edtAltNo);
        edtFaxNo = (EditText) rootView.findViewById(R.id.edtFaxNo);
        edtMobileNo.setText((getDataFromURI() != null) ? getDataFromURI() : "");
    }

    private final void initButton() {
        btnSave = (Button) rootView.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(click);
    }

    private final void initImageView() {
        imageAddEmail = (ImageView) rootView.findViewById(R.id.imageAddEmail);
        imageAddEmail.setOnClickListener(click);
        imageAddMobile = (ImageView) rootView.findViewById(R.id.imageAddMobile);
        imageAddMobile.setOnClickListener(click);
    }

    private final void initRLayout() {
        relativeEmail = (RelativeLayout) rootView.findViewById(R.id.relativeEmail);
        relativeEmail.setVisibility(View.GONE);
        relativeMobileNos = (RelativeLayout) rootView.findViewById(R.id.relativeMobileNos);
        relativeMobileNos.setVisibility(View.GONE);
    }

    private final void addContactsLocal(MeAddContact meAddContact) throws Exception {
        if (meAddContact != null) {
            MeRepoContactDet repoContactDet = new MeRepoImplContactDet(parentActivity.getDbHelper());
            repoContactDet.addContactsLocally(meAddContact);
        }
    }

    private final MeAddContact makeRequestObject() throws Exception {
        Log.i("@Transworld", "MapPartial AddContact2" + mapPartial);
        MeAddContact makeRequestObject = new MeAddContact();
        makeRequestObject.setCustomerCode((Integer) mapPartial.get(MeFragmentFollowUp.KEY_CUSTOMER_CODE));
        makeRequestObject.setSalesCustomerCode((Integer) mapPartial.get(MeFragmentFollowUp.KEY_SALES_CUSTOMER_CODE));
        makeRequestObject.setCompanyMasterId(repoLogin.getCompanyMasterId());
        makeRequestObject.setCompanyName((String) mapPartial.get(MeFragmentFollowUp.KEY_COMPANY_NAME));
        makeRequestObject.setContactPerson((String) mapPartial.get(MeFragmentAddContact1.KEY_CONTACT_PERSON));
        makeRequestObject.setSalutation((String) mapPartial.get(MeFragmentAddContact1.KEY_SALUTATION));
        makeRequestObject.setFirstName((String) mapPartial.get(MeFragmentAddContact1.KEY_FIRST_NAME));
        makeRequestObject.setMiddleName((String) mapPartial.get(MeFragmentAddContact1.KEY_MIDDLE_NAME));
        makeRequestObject.setLastName((String) mapPartial.get(MeFragmentAddContact1.KEY_LAST_NAME));
        makeRequestObject.setAddress((String) mapPartial.get(MeFragmentAddContact1.KEY_ADDRESS));
        makeRequestObject.setCity((String) mapPartial.get(MeFragmentAddContact1.KEY_CITY));
        makeRequestObject.setZipcode((String) mapPartial.get(MeFragmentAddContact1.KEY_ZIP_CODE));
        makeRequestObject.setDesignation((String) mapPartial.get(MeFragmentAddContact1.KEY_DESIGNATION));
        makeRequestObject.setMobileNo(edtMobileNo.getText().toString().length() > 0 ? edtMobileNo.getText().toString() : "");
        makeRequestObject.setMobileno1(edtMobileNo1.getText().toString().length() > 0 ? edtMobileNo1.getText().toString() : "");
        makeRequestObject.setMobileno2(edtMobileNo2.getText().toString().length() > 0 ? edtMobileNo2.getText().toString() : "");
        makeRequestObject.setEmail(edtEmailId.getText().toString().length() > 0 ? edtEmailId.getText().toString() : "");
        makeRequestObject.setPhone(edtPhoneNo.getText().toString().length() > 0 ? edtPhoneNo.getText().toString() : "");
        makeRequestObject.setFax(edtFaxNo.getText().toString().length() > 0 ? edtFaxNo.getText().toString() : "");
        makeRequestObject.setState(edtState.getText().toString().length() > 0 ? edtState.getText().toString() : "");
        makeRequestObject.setAlternetNo(edtAltNo.getText().toString().length() > 0 ? edtAltNo.getText().toString() : "");
        makeRequestObject.setEmail1(edtEmail1.getText().toString().length() > 0 ? edtEmail1.getText().toString() : "");
        makeRequestObject.setEmail2(edtEmail2.getText().toString().length() > 0 ? edtEmail2.getText().toString() : "");
        makeRequestObject.setCountry(edtCountry.getText().toString().length() > 0 ? edtCountry.getText().toString() : "");
        makeRequestObject.setEntryBy("ERP");
        Log.i("@Transworld ", "MakeReqObj - " + makeRequestObject);
        return makeRequestObject;
    }

    private final Boolean isMobileNumberValid() {
        return edtMobileNo.getText().toString().length() >= 10 && !(edtMobileNo.getText().toString().startsWith(" "));
    }

    private final Boolean isEmailIdValid() {
        String emailPattern = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
        String email = edtEmailId.getText().toString();
        if (edtEmailId.getText().toString() == null) {
            return false;
        } else {
            Log.i("@Transworld ","email -"+email.matches(emailPattern));
            return email.matches(emailPattern);
        }
    }

    private final Boolean isEmailId() {
        return edtEmailId.getText().toString().length() > 0;
    }

    private final Boolean areAllFilled() {
        Log.i("@Transworld ","isEmailIdValid -"+isEmailIdValid());
        Log.i("@Transworld ","isMobileNumberValid -"+isMobileNumberValid());

        return isEmailIdValid() || isMobileNumberValid();
    }


    private final void cleanIt() {
        edtState.setText("");
        edtCountry.setText("");
        edtEmailId.setText("");
        edtEmail1.setText("");
        edtEmail2.setText("");
        edtMobileNo.setText("");
        edtMobileNo1.setText("");
        edtMobileNo2.setText("");
        edtPhoneNo.setText("");
        edtAltNo.setText("");
        edtFaxNo.setText("");
    }

    private final class MeConnectorAddContact implements MeConnectable {
        public Map<String, Object> saveNewContact() throws Exception {
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), JsonMan.fromObject(makeRequestObject()));
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(MeIUrl.URL_ADD_CONTACT)
                    .post(body)
                    .addHeader("Accept", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            String responsJson = response.body().string();
            Log.i("@Transworld", "Response Json Add Contact- " + responsJson);
            Map<String, Object> mapEntity = JsonMan.parseAnything(responsJson, new TypeReference<Map<String, Object>>() {
            });
            Log.i("@Transworld", "Response Entity - " + mapEntity);
            String status = mapEntity.get("status").toString();
            Log.i("@Transworld", "Status :  " + status);
            if (status.equals("added contact successfully")) {
                Log.i("@Transworld", "Status 2:  " + status);
                Log.i("@Transworld", "Fragment followup loaded");
            }
            return mapEntity;
        }
    }

    private final class MeTaskSaveContact extends AsyncTask<Void, Void, Map<String, Object>> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(parentActivity, "Contact", "saving contact");
        }

        @Override
        protected Map<String, Object> doInBackground(Void... params) {
            MeConnectorAddContact connector = new MeConnectorAddContact();
            Map<String, Object> mapEntity = null;
            try {
                mapEntity = connector.saveNewContact();
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
                if (status.equals("success")) {
                    parentActivity.snack(rootView, "" + message);
                    parentActivity.popBackStack(2);
                } else {
                    parentActivity.snack(rootView, message);
                }
            } else {
                parentActivity.snack(rootView, "something went wrong :(");
            }
        }
    }

    private final class MeClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.imageAddEmail: {
                    if (relativeEmail.getVisibility() == View.VISIBLE)
                        relativeEmail.setVisibility(View.GONE);
                    else
                        relativeEmail.setVisibility(View.VISIBLE);
                    break;
                }
                case R.id.imageAddMobile: {
                    if (relativeMobileNos.getVisibility() == View.VISIBLE)
                        relativeMobileNos.setVisibility(View.GONE);
                    else
                        relativeMobileNos.setVisibility(View.VISIBLE);
                    break;
                }
                case R.id.btnSave:
                {

                    if (areAllFilled()) {
                        if (parentActivity.isNetworkAvailable())
                            new MeTaskSaveContact().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        else {
                            try {
                                addContactsLocal(makeRequestObject());
                                parentActivity.snack(rootView, "Contact Saved Locally");
                                parentActivity.popBackStack(2);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        if (edtEmailId.getText().toString().startsWith(" ")) {
                            edtEmailId.setText("");
                        }
                        Log.i("@Transworld","isEmailIdValid()-"+isEmailIdValid());
                        if(!isEmailIdValid()){
                            parentActivity.snack(rootView, "Enter valid email id:(");
                        }
                        parentActivity.snack(rootView, "Not a valid email id / mobile number:(");

                    }
                    break;
                }
            }
        }
    }
}
