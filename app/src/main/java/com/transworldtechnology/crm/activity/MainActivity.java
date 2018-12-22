package com.transworldtechnology.crm.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.kevinsawicki.http.HttpRequest;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.transworldtechnology.crm.R;
import com.transworldtechnology.crm.database.MeDatabase;
import com.transworldtechnology.crm.database.MeHelper;
import com.transworldtechnology.crm.database.MeRepoFactory;
import com.transworldtechnology.crm.database.repository.MeRepoContactDet;
import com.transworldtechnology.crm.database.repository.MeRepoImplContactDet;
import com.transworldtechnology.crm.database.repository.MeRepoImplProspectiveCustMaster;
import com.transworldtechnology.crm.database.repository.MeRepoImplSaveFollowUp;
import com.transworldtechnology.crm.database.repository.MeRepoProspectiveCustMaster;
import com.transworldtechnology.crm.database.repository.MeRepoSaveFollowUp;
import com.transworldtechnology.crm.fragment.MeFragmentAgingReportSelect;
import com.transworldtechnology.crm.fragment.MeFragmentAppointment;
import com.transworldtechnology.crm.fragment.MeFragmentBottomSheet;
import com.transworldtechnology.crm.fragment.MeFragmentCustomerDetails;
import com.transworldtechnology.crm.fragment.MeFragmentFollowUpNext;
import com.transworldtechnology.crm.fragment.MeFragmentLogin;
import com.transworldtechnology.crm.fragment.MeFragmentNetworkUnavailable;
import com.transworldtechnology.crm.fragment.MeFragmentSplash;
import com.transworldtechnology.crm.prefs.MePrefs;
import com.transworldtechnology.crm.receiver.LocationService;
import com.transworldtechnology.crm.send.MeTaskSender;
import com.transworldtechnology.crm.sync.MeTaskSyncer;
import com.transworldtechnology.crm.upload.MeTaskUploader;
import com.transworldtechnology.crm.web.JsonMan;
import com.transworldtechnology.crm.web.MeConnectable;
import com.transworldtechnology.crm.web.MeIUrl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{

    Double latitudenew,lonngitude;

    public static final String KEY_FRAGMENT_NAME = "key_fragment_name";
    public static final Integer GALLERY_PICTURE = 1515;
    public static final Integer RESULT_CROP = 4544;
    public static final int MY_PERMISSIONS_REQUESTS = 5;
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    private static final String DIALOG_ERROR = "dialog_error";
    private MeHelper helper;
    private FragmentManager manager;
    private FragmentTransaction txn;
    private Integer companyMId = -1, empCode;
    private Menu menu;
    private MeSearchClick meSearchClick;
    private List<Map<String, Object>> listCompanies = new ArrayList<>();
    private ArrayList<String> listCompanySearch = new ArrayList<>();
    private ArrayAdapter<String> adapterAutoCompanySearch;
    private GoogleApiClient googleApiClient;
    private Location location;
    private boolean resolvingError = false;
    private String userNameDb = null, passwordDb = null;
    private ActionBarDrawerToggle toggle;
    private SearchView searchViewCustomer;
    private CircularImageView imageViewAddPic;
    private ListView listView;
    private Integer position = -1;
    //private static String fragment = "followup";
    public static final String TAG = "smartsuite";
    private String selectedImagePath = null;
    private MenuItem item1;
    final Handler handler=new Handler();
    Double latitude,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MePrefs.clearSharedPrefs(getApplicationContext());
        MePrefs.clearSharedPrefsTemp(getApplicationContext());
        meSearchClick = new MeSearchClick();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        initSearchView();
        initFabButton();
        initToolBar();
        helper = new MeHelper(this, MeDatabase.DB_NAME, null, MeDatabase.DB_VERSION);
        try
        {
            companyMId = MeRepoFactory.getLoginRepository(getDbHelper()).getCompanyMasterId();
            userNameDb = MeRepoFactory.getLoginRepository(getDbHelper()).getUserName();
            passwordDb = MeRepoFactory.getLoginRepository(getDbHelper()).getPassword();
            empCode = MeRepoFactory.getLoginRepository(getDbHelper()).getEmployeeCode();
        }
        catch (Exception e)
        {

            e.printStackTrace();

        }
        runFragmentStart();
        checkPlayServices();
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        toggle.setDrawerIndicatorEnabled(false);
        navigationView.setNavigationItemSelectedListener(this);
        Timer myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                UpdateGUI();
                Log.e("VehicleInfoActivity", "Timer executed");
            }
        }, 0, 60*1000);
    }

    private void initToolBar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MePrefs.getKeySelectedCompany(getApplicationContext()).equals("none, none"))
                {
                    Log.i(MainActivity.TAG, "In If ");
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage(MePrefs.getKeyCompanyName(getApplicationContext()) + ", " + MePrefs.getKeySelectedCompany(getApplicationContext()))
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });
    }

    public void isToolbarClickable()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setClickable(false);
    }

    public void toolbarClickable() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setClickable(true);
    }

    public void runFragmentStart()
    {
        if ((companyMId == -1) && (isNetworkAvailable())) {
            runFragmentTransaction(R.id.frameMainContainer, MeFragmentLogin.getInstance());
        } else {
            if (isNetworkAvailable())
            {
                try
                {
                    new MeTaskLogin(userNameDb, passwordDb, getDeviceUniqueId()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
                catch (Exception e)
                {
                    Toast toast = Toast.makeText(MainActivity.this, "Internet not available,Please check your internet connectivity and try again!", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        }
        if ((companyMId == -1) && (!isNetworkAvailable()))
        {
            runFragmentTransaction(R.id.frameMainContainer, MeFragmentNetworkUnavailable.getInstance());
        }
        if ((companyMId != -1) && (!isNetworkAvailable())) {
            runFragmentTransaction(R.id.frameMainContainer, MeFragmentSplash.getInstance());
            //buildAlertMessage();
        }
    }
    public void initImageView()
    {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        imageViewAddPic = (CircularImageView) findViewById(R.id.imageViewAddPic);
        if (MePrefs.getKeyImageName(getApplicationContext()).equals("none"))
        {
            imageViewAddPic.setImageResource(R.drawable.layer_list_add_pic);
        }
        else
        {
            File imgFile = new File(MePrefs.getKeyImageName(getApplicationContext()));
            if (imgFile.exists())
            {
                Bitmap profilePic = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                int nh = (int) (profilePic.getHeight() * (512.0 / profilePic.getWidth()));
                Bitmap scaled = Bitmap.createScaledBitmap(profilePic, 512, nh, true);
                imageViewAddPic.setImageBitmap(scaled);
            }
            else
            {

                imageViewAddPic.setImageResource(R.drawable.layer_list_add_pic);

            }
        }
        imageViewAddPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent pictureActionIntent = null;
                pictureActionIntent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pictureActionIntent, GALLERY_PICTURE);
            }
        });
    }

    public final void initTextView()
    {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = LayoutInflater.from(this).inflate(R.layout.nav_header_main, null);
        //navigationView.addHeaderView(header);
        TextView textUserName = (TextView) findViewById(R.id.textUserName);
        try
        {
            Log.i("@Transworld", "Username - " + MeRepoFactory.getLoginRepository(getDbHelper()).getEmpName());
            textUserName.setText(MeRepoFactory.getLoginRepository(getDbHelper()).getEmpName());
            Log.i("@Transworld", "textUserName - " + textUserName.getText().toString());
            navigationView.addHeaderView(textUserName);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void initFabButton()
    {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
      //  fragment = "followup";
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                new MeFragmentBottomSheet().show(getSupportFragmentManager(), "sample");

            }
        });
    }

    private void initSearchView()
    {
        searchViewCustomer = (SearchView) findViewById(R.id.searchViewCustomer);
        listView = (ListView) findViewById(R.id.listView);
        adapterAutoCompanySearch = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, listCompanySearch);
        listView.setAdapter(adapterAutoCompanySearch);
        listView.setTextFilterEnabled(true);
        searchViewCustomer.setOnCloseListener(meSearchClick);
        searchViewCustomer.setOnQueryTextListener(meSearchClick);
        searchViewCustomer.setOnSuggestionListener(meSearchClick);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainActivity.this.position = position;
                Log.i("@Transworld", "Position = " + position);
                clearSearchBar();
                if (!isNetworkAvailable()) {
                    MePrefs.saveCompanyInfoLocal(getApplicationContext(), (String) ((TextView) view).getText());
                    setTitle(MePrefs.getKeyCompanyName(getApplicationContext()));

                } else {
                    MePrefs.clearSharedPrefsTemp(getApplicationContext());
                    MePrefs.saveCompanyInfo(getApplicationContext(), listCompanies, position);
                    setTitle((String) listCompanies.get(position).get("companyName"));
                    runFragmentSummary();
                }
               // Log.i("@Transworld", "FragmentName: " + fragment);
                findViewById(R.id.frameMainContainer).setVisibility(View.VISIBLE);
             //   fragment = "followup";
                hideKeyboard(view);
                clearListView();
                listView.setVisibility(View.GONE);
            }
        });
    }

    public void clearListView() {
        listCompanySearch.clear();
        adapterAutoCompanySearch.notifyDataSetChanged();
    }

    private void runFragmentSummary() {
        Log.i("@Transworld", "position - " + position);
       // itemDisable();
        if (position != -1 || (!(listCompanies.isEmpty()))) {
            String companyName = MePrefs.getKeyCompanyName(getApplicationContext()).equals("none") ? (String) listCompanies.get(position).get("companyName") : MePrefs.getKeyCompanyName(getApplicationContext());
            //  String companyName = (String) listCompanies.get(position).get("companyName");
          /*  Integer customerCode =
                    (Integer) listCompanies.get(position).get("customerCode");*/
            Log.i("@Transworld", "Company Name in runFragmentSummary - " + companyName);
            Integer customerCode = MePrefs.getKeyCustomerCode(getApplicationContext()).equals(-1) ? (Integer) listCompanies.get(position).get("customerCode") : MePrefs.getKeyCustomerCode(getApplicationContext());
            Integer salesCustomerCode = MePrefs.getKeySaleCustCode(getApplicationContext()).equals(-1) ? (Integer) listCompanies.get(position).get("salesCustomerCode") : MePrefs.getKeySaleCustCode(getApplicationContext());
            Integer opportunityCode = MePrefs.getKeyOpportunityCode(getApplicationContext()).equals(-1) ? (Integer) listCompanies.get(position).get("opportunityCode") : MePrefs.getKeyOpportunityCode(getApplicationContext());
            runFragmentTransaction(R.id.frameMainContainer, MeFragmentCustomerDetails.getInstance(companyName.length() > 0 ? companyName : "", customerCode, salesCustomerCode, opportunityCode));
        } else {
            if (getCurrentFocus()!=null) {
                snack(getCurrentFocus(), "Please Select Customer");
            }
        }
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }

    @Override
    public void onBackPressed() {
        Log.i(MainActivity.TAG,"Back Pressed : "+getSupportFragmentManager().getBackStackEntryCount());
        if (getSupportFragmentManager().getBackStackEntryCount() > 1 ){//&& (getSupportFragmentManager().findFragmentByTag(KEY_FRAGMENT_NAME)!=null)) {
            removeFragmentFromBackStack();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUESTS) {
            // need to implement for marshmellow
        }
    }

    public final void removeFragmentFromBackStack() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
     //   Log.i(MainActivity.TAG, "FragmentName : " + getSupportFragmentManager().findFragmentByTag(KEY_FRAGMENT_NAME));
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        Log.i("@Transworld","Fragment By Id  : "+getSupportFragmentManager().findFragmentById(R.id.frameMainContainer));
        Log.i("@Transworld","Fragment By Name  : "+getSupportFragmentManager().findFragmentByTag(KEY_FRAGMENT_NAME));
        Log.i("@Transworld","Error "+getSupportFragmentManager()
                .getBackStackEntryAt(getSupportFragmentManager()
                        .getBackStackEntryCount() - 1)
                .getName());


        if (getSupportFragmentManager()
                .getBackStackEntryAt(getSupportFragmentManager()
                        .getBackStackEntryCount() - 1)
                .getName()!=null){
            Log.i(MainActivity.TAG,"Not Null");
            if (((getSupportFragmentManager()
                    .getBackStackEntryAt(getSupportFragmentManager()
                            .getBackStackEntryCount() - 1)
                    .getName()
                    .equals("MeFragmentAppointment"))
                    || getSupportFragmentManager()
                    .getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1)
                    .getName()
                    .equals("MeFragmentLogin"))
                    ) {
                dialogExitApp();
                // }
            }
            else {
                super.onBackPressed();
            }

        }

        else {
            runFragmentTransaction(R.id.frameMainContainer,MeFragmentAppointment.getInstance());
        }



    }

    public final void showDrawerFab() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        toggle.setDrawerIndicatorEnabled(true);
        fab.setVisibility(View.VISIBLE);
    }

    public final void isVisibleFab(Boolean isVisible)
    {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (isVisible)
            fab.setVisibility(View.VISIBLE);
        else
            fab.setVisibility(View.GONE);
    }

    public final void popBackStack(Integer count)
    {
        for (Integer i = 0; i < count; i++) {
            getSupportFragmentManager().popBackStackImmediate();
        }
    }
    public final Fragment runFragmentTransaction(Integer containerId, Fragment fragment) {
        final String backStateName = fragment.getArguments().getString(KEY_FRAGMENT_NAME);
        FragmentManager manager = getSupportFragmentManager();
        Boolean fragmentPopped = manager.popBackStackImmediate(backStateName, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        Log.i(MainActivity.TAG,"Fragment Popped : "+fragmentPopped);
        if (!fragmentPopped) {
            FragmentTransaction txn = manager.beginTransaction();
            txn.replace(containerId, fragment, backStateName);
            txn.addToBackStack(backStateName);
            txn.commit();
        } else {
            //not Popped
            FragmentTransaction txn = manager.beginTransaction();
            txn.replace(containerId, fragment, backStateName);
            txn.addToBackStack(null);
            txn.commit();
        }
        return fragment;
    }

    public final String getDeviceUniqueId() {
        TelephonyManager telephonyManager;
        WifiManager wifiManager;
        WifiInfo wifiInfo = null;
        String imei = null;
        String macId = null;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.CALL_PHONE,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.CAMERA
                    },
                    MY_PERMISSIONS_REQUESTS);
        } else {
            telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            imei = telephonyManager.getDeviceId();
            wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            wifiInfo = wifiManager.getConnectionInfo();
            macId = wifiInfo.getMacAddress();
        }
        Log.i("@Transworld", "Imei - " + imei);
        Log.i("@Transworld", "MacId - " + macId);
        return (((imei != null) && !imei.equals("")) ? imei : macId);
    }

    public final MeHelper getDbHelper() {
        return helper;
    }
    public void snack(View rootView, String message) {
        Snackbar snackBar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT);
        snackBar.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.snack));
        snackBar.setDuration(Snackbar.LENGTH_LONG);
        snackBar.show();
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;
        menu.findItem(R.id.action_Email).setVisible(false);
        menu.findItem(R.id.action_Letter).setVisible(false);
        menu.findItem(R.id.action_Meeting).setVisible(false);
        menu.findItem(R.id.action_phone_call).setVisible(false);
        menu.findItem(R.id.action_select_all).setVisible(false);
        menu.findItem(R.id.action_points).setVisible(false);
        menu.findItem(R.id.action_visit).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(false);
        invalidateOptionsMenu();
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        if (listView.getVisibility() == View.VISIBLE)
        {
            menu.clear();
        }
        return super.onPrepareOptionsMenu(menu);
    }

    public final boolean isNetworkAvailable()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected())
        {

            return true;
        }
        else
        {
            return false;
        }
    }

    public void showErrorDialog(int errorCode)
    {
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getSupportFragmentManager(), "errordialog");
    }
    public void onDialogDismissed() {
        resolvingError = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_RESOLVE_ERROR) {
            resolvingError = false;
            if (resultCode == RESULT_OK) {
                if (!googleApiClient.isConnecting() &&
                        !googleApiClient.isConnected()) {
                    googleApiClient.connect();
                }
            }
        }
        if (requestCode == GALLERY_PICTURE) {
            if (resultCode == RESULT_OK) {
                if (data.getData() != null) {
                    Uri selectedImageUri = data.getData();
                    if (selectedImageUri != null) {
                        selectedImagePath = (getPath(selectedImageUri) != null ? getPath(selectedImageUri) : "");
                        MePrefs.saveImage(getApplicationContext(), selectedImagePath);
                        Log.i("@Transworld", "Image Path : " + selectedImagePath);
                        cropImage(selectedImagePath);
                    }
                }
            }
        }
        if (requestCode == RESULT_CROP)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                Bundle extras = data.getExtras();
                Bitmap selectedBitmap = extras.getParcelable("data");
                // Set The Bitmap Data To ImageView
                Bitmap profilePic = BitmapFactory.decodeFile(selectedImagePath);
                int nh = (int) (profilePic.getHeight() * (512.0 / profilePic.getWidth()));
                Bitmap scaled = Bitmap.createScaledBitmap(profilePic, 512, nh, true);
                imageViewAddPic.setImageBitmap(scaled);
                imageViewAddPic.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        }
    }

    private void cropImage(String selectedImagePath) {
        try {
            Intent cropImageIntent = new Intent("com.android.camera.action.CROP");
            File file = new File(selectedImagePath);
            Uri contentUri = Uri.fromFile(file);
            cropImageIntent.setDataAndType(contentUri, "image/*");
            cropImageIntent.putExtra("crop", "true");     // set crop properties
            // indicate aspect of desired crop
            cropImageIntent.putExtra("aspectX", 1);
            cropImageIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropImageIntent.putExtra("outputX", 100);
            cropImageIntent.putExtra("outputY", 100);
            cropImageIntent.putExtra("return-data", true);    // retrieve data on return
            startActivityForResult(cropImageIntent, RESULT_CROP);
        } catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private String getPath(Uri uri) {
        Cursor cursor = null;
        try {
            String[] projection = {MediaStore.Images.Media.DATA};
            cursor = getContentResolver().query(uri, projection, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        REQUEST_RESOLVE_ERROR).show();
            }
            return false;
        }
        return true;
    }
    public static class ErrorDialogFragment extends DialogFragment {
        public ErrorDialogFragment() {
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GoogleApiAvailability.getInstance().getErrorDialog(
                    this.getActivity(), errorCode, REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            ((MainActivity) getActivity()).onDialogDismissed();
        }
    }
    private class MeConnectorLogin implements MeConnectable
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
            mapEntity = JsonMan.parseAnything(jsonMap, new TypeReference<Map<String, Object>>()
            {

            });
            return mapEntity;
        }
    }

    private final class MeTaskLogin extends AsyncTask<Void, Void, Map<String, Object>> {
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
        protected void onPreExecute() {
            super.onPreExecute();
            runFragmentTransaction(R.id.frameMainContainer, MeFragmentSplash.getInstance());
        }
        @Override
        protected Map<String, Object> doInBackground(Void... params) {
            Map<String, Object> mapEntity = null;
            try {
                mapEntity = connector.loginUser(userName, password, imei);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mapEntity;
        }
        @Override
        protected void onPostExecute(Map<String, Object> map) {
            super.onPostExecute(map);
            if (map != null) {
                if ((map.get("status").equals("success"))) {
                    startGetLocations();
                    MeRepoContactDet repoContactDet = new MeRepoImplContactDet(getDbHelper());
                    MeRepoSaveFollowUp repoSaveFollowUp = new MeRepoImplSaveFollowUp(getDbHelper());
                    try {
                        if (!repoContactDet.checkDataContacts().equals(0) || !repoSaveFollowUp.checkDataFollowUp().equals(0)) {
                            MeTaskUploader taskUploader = new MeTaskUploader(MainActivity.this, "onLaunching");
                            taskUploader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    runFragmentTransaction(R.id.frameMainContainer, MeFragmentAppointment.getInstance());
                } else {
                    runFragmentTransaction(R.id.frameMainContainer, MeFragmentLogin.getInstance());
                }
            }
        }
    }

    public final void startGetLocations()
    {
        Intent intent = new Intent(this, LocationService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                , 1000 * 60 , pendingIntent);

  /*alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
    + (i * 1000), 8000, pendingIntent);*/
    }
    private void itemDisable()
    {
        NavigationView navigationView= (NavigationView) findViewById(R.id.nav_view);
        Menu menuNav=navigationView.getMenu();
        MenuItem nav_item2 = menuNav.findItem(R.id.nav_summary);
        nav_item2.setEnabled(false);
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.nav_send_location)
        {
            if (isNetworkAvailable())
            {

                Log.i("SS"," IN THE NW ON ");

                dialogStartLocation();
            } else {
                if (getCurrentFocus()!=null) {
                    snack(getCurrentFocus(), "Unable to connect to the server:(");
                }
            }
        } else if (id == R.id.nav_aging_report)
        {
            runFragmentTransaction(R.id.frameMainContainer, MeFragmentAgingReportSelect.getInstance());
        } else if (id == R.id.nav_appointment) {
            runFragmentTransaction(R.id.frameMainContainer, MeFragmentAppointment.getInstance());
        } else if (id == R.id.nav_summary) {
            Log.i("@Transworld", "Add Summary");
           // Log.i("@Transworld", "FragmentNAme: " + fragment);
            if (isNetworkAvailable()) {
           //     fragment = "summary";
                if ((MePrefs.getKeyCompanyName(getApplicationContext()).equals("none")) && (position == -1)) {
                    if (getCurrentFocus()!=null) {
                        snack(getCurrentFocus(), "Please Select Customer");
                    }
                } else {
                    Log.i("@Transworld", "Company Name - " + MePrefs.getKeyCompanyName(getApplicationContext()));
                    runFragmentSummary();
                }
            } else {
                if (getCurrentFocus()!=null) {
                    snack(getCurrentFocus(), "Unable to connect to the server:(");
                }

            }
        } else if (id == R.id.nav_sync) {
            Log.i("@Transworld", "Sync");
            if (isNetworkAvailable()) {
                MeTaskSyncer taskSyncer = new MeTaskSyncer(MainActivity.this);
                taskSyncer.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                if (getCurrentFocus()!=null) {
                    snack(getCurrentFocus(), "Unable to connect to the server:(");
                }
            }
        } else if (id == R.id.nav_upload) {
            Log.i("@Transworld", "Uploads");
            if (isNetworkAvailable()) {

                dialogUploadData();

            }
        }else if (id == R.id.nav_checkin) {
                Log.i("@Transworld", "Uploads");
                if (isNetworkAvailable()) {
                    Intent i = new Intent(MainActivity.this, EmpCheckIn.class);
                    startActivity(i);


                } else {
                    if (getCurrentFocus() != null) {
                        snack(getCurrentFocus(), "Unable to connect to the server:(");
                    }
                }
            } else if (id == R.id.nav_checkout) {
                Log.i("@Transworld", "Uploads");
                if (isNetworkAvailable()) {

                    Intent i = new Intent(MainActivity.this, EmpCheckOut.class);
                    startActivity(i);
                    // dialogUploadData();

                }

            else {
                if (getCurrentFocus()!=null) {
                    snack(getCurrentFocus(), "Unable to connect to the server:(");
                }
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void dialogExitApp() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Sure to Exit?")
                .setCancelable(false)
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.dismiss();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void dialogUploadData()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Do you really want to Upload Data?")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        MeRepoContactDet repoContactDet = new MeRepoImplContactDet(getDbHelper());
                        MeRepoSaveFollowUp repoSaveFollowUp = new MeRepoImplSaveFollowUp(getDbHelper());
                        try {
                            if (!repoContactDet.checkDataContacts().equals(0) || !repoSaveFollowUp.checkDataFollowUp().equals(0)) {
                                MeTaskUploader taskUploader = new MeTaskUploader(MainActivity.this, "forcefully");
                                taskUploader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            }
                            else
                            {
                                if (getCurrentFocus()!=null)
                                {
                                    snack(getCurrentFocus(), "No Data Available To Upload!");
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void dialogStartLocation()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Do you want to Send Locations to Server?")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id)
                    {
                        Log.i("SS"," IN THE MeTask Sender  ");

                        MeTaskSender taskSender = new MeTaskSender(MainActivity.this);
                        taskSender.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                        new MyLocationListener();


                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private final class MeSearchClick implements SearchView.OnSuggestionListener, SearchView.OnQueryTextListener, SearchView.OnCloseListener {
        @Override
        public boolean onSuggestionSelect(int position) {
            return false;
        }

        @Override
        public boolean onSuggestionClick(int position) {
            return false;
        }

        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            findViewById(R.id.frameMainContainer).setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            listView.setTextFilterEnabled(false);
            if (TextUtils.isEmpty(s)) {
                listView.clearTextFilter();
                listView.setVisibility(View.GONE);
                findViewById(R.id.frameMainContainer).setVisibility(View.VISIBLE);
            } else {
                listView.setFilterText(s.toString());
                listView.setVisibility(View.VISIBLE);
                findViewById(R.id.frameMainContainer).setVisibility(View.GONE);
                try {
                    if (isNetworkAvailable()) {
                        new MeTaskSearchCompany(s.replace(" ", "%20"), MeRepoFactory.getLoginRepository(helper).getCompanyMasterId()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        Log.i("@Transworld", "before Search Company ");
                        searchCompanyLocally(s);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //Search Company Offline
            }
            return true;
        }
        @Override
        public boolean onClose()
        {
            searchViewCustomer.clearFocus();
            searchViewCustomer.setQuery("", false);
            searchViewCustomer.setFocusable(false);
            searchViewCustomer.onActionViewCollapsed();
            listCompanySearch.clear();
            findViewById(R.id.frameMainContainer).setVisibility(View.VISIBLE);
            //  showSearchView(false);
            findViewById(R.id.listView).setVisibility(View.GONE);
            return false;
        }
    }
    private void searchCompanyLocally(String s) {
        listCompanySearch.clear();
        Log.i("@Transworld", "In Search Company ");
        MeRepoProspectiveCustMaster repoProspectiveCustMaster = new MeRepoImplProspectiveCustMaster(getDbHelper());
        try {
            repoProspectiveCustMaster.saveCompanyName(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            for (String listItem : MeRepoFactory.getSearchCompanyInfo(getDbHelper()).getCompanyInfo())
                listCompanySearch.add(listItem);
            Log.i("@Transworld", "List listCompanySearch " + listCompanySearch);
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapterAutoCompanySearch = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, listCompanySearch) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setTextColor(Color.BLACK);
                return view;
            }
        };
        listView.setAdapter(adapterAutoCompanySearch);
        adapterAutoCompanySearch.notifyDataSetChanged();
    }
    public void clearSearchBar()
    {
        searchViewCustomer.clearFocus();
        searchViewCustomer.setQuery("", false);
        searchViewCustomer.setFocusable(false);
        searchViewCustomer.onActionViewCollapsed();
    }

    public final void showSearchView(Boolean isVisible)
    {
        SearchView searchViewCustomer = (SearchView) findViewById(R.id.searchViewCustomer);
        if (isVisible) {
            searchViewCustomer.setVisibility(View.VISIBLE);
            findViewById(R.id.frameMainContainer).setVisibility(View.VISIBLE);
        } else {
            searchViewCustomer.setVisibility(View.GONE);
            findViewById(R.id.frameMainContainer).setVisibility(View.VISIBLE);
            adapterAutoCompanySearch.notifyDataSetChanged();
        }
    }

    private final class MeConnectorSearchCompany implements MeConnectable
    {
        public Map<String, Object> findByCompanyName(String companyRelatedThing, Integer companyMasterId) throws Exception {
            String urlWithPathVariables = MeIUrl.URL_SEARCH_COMPANY + companyRelatedThing + "/" + companyMasterId;
            Log.i("@Transworld", MeIUrl.URL_SEARCH_COMPANY + companyRelatedThing + "/" + companyMasterId);
            String jsonMap = HttpRequest.get(urlWithPathVariables).accept("application/json").body();
            Log.i("@Transworld", "JSON - " + jsonMap);
            Map<String, Object> mapEntity = JsonMan.parseAnything(jsonMap, new TypeReference<Map<String, Object>>() {
            });
            Log.i("@Transworld", "Map Entity " + mapEntity.toString());
            return mapEntity;
        }
    }

    private final class MeTaskSearchCompany extends AsyncTask<Void, Void, List<Map<String, Object>>> {
        private Integer companyMasterId;
        private String companyRelatedThing;
        private MeConnectorSearchCompany connector;

        public MeTaskSearchCompany(String companyRelatedThing, Integer companyMasterId) {
            this.companyRelatedThing = companyRelatedThing;
            this.companyMasterId = companyMasterId;
            connector = new MeConnectorSearchCompany();
        }

        @Override
        protected void onPreExecute()
        {

            super.onPreExecute();
        }

        @Override
        protected List<Map<String, Object>> doInBackground(Void... params) {
            try
            {
                //  Thread.sleep(3000);
                Map<String, Object> mapList = connector.findByCompanyName(companyRelatedThing, companyMasterId);
                Map<String, Object> mapCompanies = null;
                if (mapList != null) {
                    mapCompanies = (Map<String, Object>) mapList.get("companies");
                }
                if (mapCompanies != null) {
                    listCompanies = (List<Map<String, Object>>) mapCompanies.get("matchingCompanies");
                }
                Log.i("@Transworld", "List Compnies - " + listCompanies.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listCompanies;
        }
        @Override
        protected void onPostExecute(List<Map<String, Object>> stringObjectMap)
        {
            super.onPostExecute(stringObjectMap);
            listCompanySearch.clear();
            // adapterAutoCompanySearch.clear();
            if (stringObjectMap != null) {
                for (Map<String, Object> mapItem : stringObjectMap) {
                    listCompanySearch.add((String) mapItem.get("companyInfo"));

                   /* searchViewCustomer.setSearchableInfo(
                            searchManager.getSearchableInfo(listCompanySearch.add((String) mapItem.get("companyInfo")));*/
                }
                adapterAutoCompanySearch = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, listCompanySearch) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent)
                    {
                        View view = super.getView(position, convertView, parent);
                        TextView tv = (TextView) view.findViewById(android.R.id.text1);
                        tv.setTextColor(Color.BLACK);
                        return view;
                    }
                };
                listView.setAdapter(adapterAutoCompanySearch);
                adapterAutoCompanySearch.notifyDataSetChanged();
            }
        }
    }

    private boolean UpdateGUI() {
        //i++;
        //tv.setText(String.valueOf(i));
        handler.post(myRunnable);
        return true;
    }

    final Runnable myRunnable = new Runnable() {
        public void run() {
            MeTaskSender taskSender = new MeTaskSender(MainActivity.this);
            taskSender.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new MyLocationListener();

        }
    };



    class MyLocationListener implements LocationListener
    {


        @Override
        public void onLocationChanged(Location location)
        {

            latitude=location.getLatitude();
            longitude=location.getLongitude();


            String LOCATION=" Latitude "+latitude+" LOngitude "+longitude;

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
}
