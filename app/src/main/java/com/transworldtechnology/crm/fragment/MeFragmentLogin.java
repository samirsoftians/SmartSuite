package com.transworldtechnology.crm.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.kevinsawicki.http.HttpRequest;
import com.transworldtechnology.crm.R;
import com.transworldtechnology.crm.activity.MainActivity;
import com.transworldtechnology.crm.database.repository.MeRepoImplLogin;
import com.transworldtechnology.crm.database.repository.MeRepoLogin;
import com.transworldtechnology.crm.web.JsonMan;
import com.transworldtechnology.crm.web.MeConnectable;
import com.transworldtechnology.crm.web.MeIUrl;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by root on 11/1/16.
 */
public class MeFragmentLogin extends Fragment
{

    public static final String KEY_FRAGMENT_NAME = "key_fragment_name";
    private static final Integer INTENT_ENABLED_GPS = 1111;
    private MeClick click;
    private MainActivity parentActivity;
    private View rootView;
    private Button btnLogin;
    private EditText edtUserName, edtPassword;
    private LocationManager locationManager;

    public static MeFragmentLogin getInstance()
    {
        MeFragmentLogin meFragmentLogin = new MeFragmentLogin();
        Bundle args = new Bundle();
        args.putString(KEY_FRAGMENT_NAME, "MeFragmentLogin");
        meFragmentLogin.setArguments(args);
        return meFragmentLogin;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        parentActivity = (MainActivity) getActivity();
        parentActivity.isVisibleFab(false);
        parentActivity.showSearchView(false);
        locationManager = (LocationManager) parentActivity.getSystemService(Context.LOCATION_SERVICE);
        click = new MeClick();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        parentActivity.getSupportActionBar().show();
        rootView = inflater.inflate(R.layout.fragment_login_user, container, false);
        initEditText();
        initButton();
        return rootView;
    }

    private final void initEditText()
    {
        edtUserName = (EditText) rootView.findViewById(R.id.edtUserName);
        edtPassword = (EditText) rootView.findViewById(R.id.edtPassword);
    }

    private final void initButton()
    {

        btnLogin = (Button) rootView.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(click);

    }
    private final Boolean isUserNameValid()
    {
        Boolean isValid = false;
        final Drawable drawableError = ContextCompat.getDrawable(getContext(), R.drawable.ic_error);
        drawableError.setBounds(0, 0, 50, 50);
        if (edtUserName.getText().length() < 1) {
            edtUserName.setError("Username required", drawableError);
            isValid = false;
        } else {
            edtUserName.setError(null);
            edtUserName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_done, 0);
            isValid = true;
        }
        return isValid;
    }

    private final Boolean isPasswordValid()
    {
        Boolean isValid = false;
        final Drawable drawableError = ContextCompat.getDrawable(getContext(), R.drawable.ic_error);
        drawableError.setBounds(0, 0, 50, 50);
        if (edtPassword.getText().length() < 1) {
            edtPassword.setError("Password required", drawableError);
            isValid = false;
        } else {
            edtPassword.setError(null);
            edtPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_done, 0);
            isValid = true;
        }
        return isValid;
    }

    private final void saveUserCrediantials(Map<String, Object> map) {
        MeRepoLogin repoLogin = new MeRepoImplLogin(parentActivity.getDbHelper());
        try {
            if (map != null) {
                repoLogin.saveUserCrediantials(edtUserName.getText().toString(),
                        edtPassword.getText().toString(),
                        (Integer) map.get("employeeCode"),
                        (Integer) map.get("companyMasterId"),
                        (String) map.get("role"),
                        (Integer) map.get("userTypeCode"),
                        (String) map.get("activeStatus"),
                        (String) map.get("userLevel"),
                        (String) map.get("companyCode"),
                        (String) map.get("empEmailId"),
                        (String) map.get("imei"),
                        (String)map.get("EmpName")

                );
                repoLogin = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void buildAlertMessageNoGps()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setTitle("GPS")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), INTENT_ENABLED_GPS);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.dismiss();
                        new MeTaskLogin(edtUserName.getText().toString(), edtPassword.getText().toString(), parentActivity.getDeviceUniqueId()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
        if (locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER))
        {
            if (parentActivity.isNetworkAvailable())
            {
                new MeTaskLogin(edtUserName.getText().toString(), edtPassword.getText().toString(), parentActivity.getDeviceUniqueId()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
            else {
                if(getView()!=null)
                parentActivity.snack(rootView, "Unable to connect to the server:(");
            }
        }
    }

    private final class MeClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.getId() == btnLogin.getId())
            {
                if (isUserNameValid() && isPasswordValid() && parentActivity.isNetworkAvailable())
                {
                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                    {
                        Log.i("@Transworld", "In Inner LOOP");
                        buildAlertMessageNoGps();
                    }
                    else
                    {
                        if (parentActivity.isNetworkAvailable())
                        {
                            new MeTaskLogin(edtUserName.getText().toString(), edtPassword.getText().toString(), parentActivity.getDeviceUniqueId()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        }
                        else
                        {
                            if(getView()!=null)
                            parentActivity.  snack(rootView, "Unable to connect to the server:(");
                        }
                    }
                }
            }
        }
    }

    private final class MeConnectorLogin implements MeConnectable
    {
        public Map<String, Object> loginUser(String userName, String password, String imei) throws Exception
        {

            Map<String, Object> mapEntity = new HashMap<>();
            mapEntity.put("userName", userName);
            mapEntity.put("password", password);
            mapEntity.put("imei", imei);
            Log.i("@Transworld", "Map Entity - " + mapEntity);
            String jsonMap = HttpRequest.post(MeIUrl.URL_LOGIN)
                    .contentType(HttpRequest.CONTENT_TYPE_JSON)
                    .accept("application/json")
                    .form(mapEntity)
                    .body();
            Log.i("@Transworld", "Url Login - " + MeIUrl.URL_LOGIN);
            Log.i("@Transworld", "Response Json - " + jsonMap);
            mapEntity = JsonMan.parseAnything(jsonMap, new TypeReference<Map<String, Object>>() {
            });
            return mapEntity;
        }
    }

    private final class MeTaskLogin extends AsyncTask<Void, Void, Map<String, Object>>
    {
        private ProgressDialog progressDialog;
        private MeConnectorLogin connector;
        private String userName, password;
        private String imei;

        public MeTaskLogin(String userName, String password, String imei)
        {
            this.userName = userName;
            this.password = password;
            this.imei = imei;
            connector = new MeConnectorLogin();
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(parentActivity, "Login", "Checking credentials");
        }

        @Override
        protected Map<String, Object> doInBackground(Void... params)
        {
            Map<String, Object> mapEntity = null;
            try
            {
                mapEntity = connector.loginUser(userName, password, imei);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return mapEntity;
        }
        @Override
        protected void onPostExecute(Map<String, Object> map)
        {
            super.onPostExecute(map);
            progressDialog.dismiss();
            if (map != null) {
                if (map.get("status").equals("success"))
                {
                    saveUserCrediantials(map);
                    parentActivity.runFragmentTransaction(R.id.frameMainContainer, MeFragmentAppointment.getInstance());
                    parentActivity.startGetLocations();
                }
                else
                {
                    if(getView()!=null)
                    parentActivity.snack(rootView, "Invalid Credentials :(");
                }
            }
        }
    }
}
