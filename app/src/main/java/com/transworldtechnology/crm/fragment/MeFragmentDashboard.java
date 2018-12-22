package com.transworldtechnology.crm.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.kevinsawicki.http.HttpRequest;
import com.transworldtechnology.crm.R;
import com.transworldtechnology.crm.activity.MainActivity;
import com.transworldtechnology.crm.database.MeRepoFactory;
import com.transworldtechnology.crm.database.repository.MeRepoImplProspectiveCustMaster;
import com.transworldtechnology.crm.database.repository.MeRepoLogin;
import com.transworldtechnology.crm.database.repository.MeRepoProspectiveCustMaster;
import com.transworldtechnology.crm.upload.MeTaskUploader;
import com.transworldtechnology.crm.web.JsonMan;
import com.transworldtechnology.crm.web.MeConnectable;
import com.transworldtechnology.crm.web.MeIUrl;
import com.transworldtechnology.crm.web.MeNetworkChangeReceiver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by root on 12/1/16.
 */
public class MeFragmentDashboard extends Fragment {
    private static final int INTENT_ENABLED_GPS = 1111;
    private LocationManager locationManager;
    private MainActivity parentActivity;
    private MeNetworkChangeReceiver networkChangeReceiver;
    private PackageManager pm;
    private MeClick meClick;
    private MeItemClick itemClick;
    private MeAdapterRecyclerFollower adapterRecyclerFollower;
    private MeWatcherCompanySearch watcherCompanySearch;
    private List<Map<String, Object>> listCompanies = new ArrayList<>();
    private ArrayList<String> listCompanySearch = new ArrayList<>();
    private ArrayAdapter<String> adapterAutoCompanySearch;
    private ArrayList arrayGridDashboard = new ArrayList();
    private MeRepoLogin repoLogin;
    private MeRepoProspectiveCustMaster repoSearchCompany;
    private View rootView;
    private RecyclerView recyclerGridDashBoard;
    private GridLayoutManager gridLayoutManager;
    private ProgressBar progressBarSync;
    private AutoCompleteTextView autoSearchCompany;
    private Integer position = -1;
    private Boolean isNetworkAvail = false;
    private String selection;
    // private Boolean flag = true;

    public static MeFragmentDashboard getInstance() {
        MeFragmentDashboard meFragmentDashboard = new MeFragmentDashboard();
        Bundle args = new Bundle();
        args.putString(MainActivity.KEY_FRAGMENT_NAME, "MeFragmentDashboard");
        meFragmentDashboard.setArguments(args);
        return meFragmentDashboard;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arrayGridDashboard = getAllItemList();
        parentActivity = (MainActivity) getActivity();
        networkChangeReceiver = new MeNetworkChangeReceiver();
        watcherCompanySearch = new MeWatcherCompanySearch();
        itemClick = new MeItemClick();
        meClick = new MeClick();
        repoLogin = MeRepoFactory.getLoginRepository(parentActivity.getDbHelper());
        repoSearchCompany = MeRepoFactory.getSearchCompanyInfo(parentActivity.getDbHelper());
        locationManager = (LocationManager) parentActivity.getSystemService(Context.LOCATION_SERVICE);
        pm = parentActivity.getPackageManager();

        //    setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentActivity.getSupportActionBar().show();
        rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        if (pm.hasSystemFeature(PackageManager.FEATURE_LOCATION))
            Log.i("@Transworld", "GPS AVAILABLE");
        else
            Toast.makeText(parentActivity, "GPS hardware not found!!!", Toast.LENGTH_SHORT).show();
        initMultiAutoCompanySearch();
        Log.i("@Transworld", "Auto Search" + autoSearchCompany.getText().toString());
        if (autoSearchCompany.getText().toString().length() > 0) {
            cleanIt();
        }

        initGridDashBoard();
        initProgressBar();
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_sync, menu);
        //  menu.findItem(R.id.action_sync).setVisible(true);
        Log.i("@transworld", "Create Option Menu");
        /*if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
            try {
                Method m = menu.getClass().getDeclaredMethod(
                        "setOptionalIconsVisible", Boolean.TYPE);
                m.setAccessible(true);
                m.invoke(menu, true);
            } catch (NoSuchMethodException e) {
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        Log.i("@Transworld", "Menu Clicked");
        try {
            /*if (item.getItemId() == R.id.action_sync) {
                if (parentActivity.isNetworkAvailable()) {
                    MeTaskSyncer taskSyncer = new MeTaskSyncer(parentActivity);
                    taskSyncer.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    parentActivity.snack(rootView, "Unable to connect to the server:(");
                }
            }*/
            if (item.getItemId() == R.id.action_upload) {
                if (parentActivity.isNetworkAvailable()) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
                    builder.setMessage("Do you really want to Upload Data?")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                    //MeTaskUploader taskUploader = new MeTaskUploader(parentActivity);
                                   // taskUploader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                    dialog.cancel();
                                }
                            });
                    final AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    parentActivity.snack(rootView, "Unable to connect to the server:(");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }



   /* @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (parentActivity.isNetworkAvailable()) {
        } else
            parentActivity.snack(rootView, "Unable to connect to the Server");


    }*/

    private void initMultiAutoCompanySearch() {
        autoSearchCompany = (AutoCompleteTextView) rootView.findViewById(R.id.autoSearchCompany);
        autoSearchCompany.addTextChangedListener(watcherCompanySearch);
        autoSearchCompany.setThreshold(1);
        adapterAutoCompanySearch = new ArrayAdapter<>(parentActivity, android.R.layout.simple_list_item_1, listCompanySearch);
        autoSearchCompany.setAdapter(adapterAutoCompanySearch);
        autoSearchCompany.setOnItemClickListener(itemClick);
    }

    private void initProgressBar() {
        progressBarSync = (ProgressBar) rootView.findViewById(R.id.progressBarSync);
        progressBarSync.setProgress(20);
        progressBarSync.getIndeterminateDrawable().setColorFilter(Color.parseColor("#303F9F"), android.graphics.PorterDuff.Mode.MULTIPLY);
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
//        flag = false;
    }

    private void initGridDashBoard() {
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        parentActivity.setTitle("SmartSuite");
        recyclerGridDashBoard = (RecyclerView) rootView.findViewById(R.id.my_recycler_view_follower);
        recyclerGridDashBoard.setHasFixedSize(true);
        recyclerGridDashBoard.setLayoutManager(gridLayoutManager);
        adapterRecyclerFollower = new MeAdapterRecyclerFollower(getContext(), arrayGridDashboard);
        recyclerGridDashBoard.setAdapter(adapterRecyclerFollower);
    }

    private final ArrayList getAllItemList() {
        ArrayList<String> followerList = new ArrayList<>();
        followerList.add("Add Follow Ups");
        followerList.add("Appointments");
        followerList.add("Summary");
        return followerList;
    }

    private final void cleanIt() {
        autoSearchCompany.setText("");
    }

    private final void searchCompanyLocally(String str) throws Exception {
        //      adapterAutoCompanySearch.clear();
        // List<String> list = new ArrayList<>();
        MeRepoProspectiveCustMaster repoProspectiveCustMaster = new MeRepoImplProspectiveCustMaster(parentActivity.getDbHelper());
        repoProspectiveCustMaster.saveCompanyName(str);
        try {
            for (String listItem : MeRepoFactory.getSearchCompanyInfo(parentActivity.getDbHelper()).getCompanyInfo())
                listCompanySearch.add(listItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapterAutoCompanySearch = new ArrayAdapter<>(parentActivity, android.R.layout.simple_list_item_1, listCompanySearch);
        autoSearchCompany.setAdapter(adapterAutoCompanySearch);
        adapterAutoCompanySearch.notifyDataSetChanged();
    }

    private void buildAlertMessageNoGps() {
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
                        runFragmentAddFollowUp();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("@Transworld", "Result Code before if: " + resultCode);
        if (resultCode == Activity.RESULT_OK || resultCode == Activity.RESULT_FIRST_USER) {
            Log.i("@Transworld", "Result Code After if: " + resultCode);
            Log.i("@Transworld", "Location Manager : " + locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER));
        }
        if (locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)) {
            runFragmentAddFollowUp();
        }
    }

    private void runFragmentAddFollowUp() {
        if (parentActivity.isNetworkAvailable()) {
            if (position != -1) {
                Integer opportunityCode = (Integer) listCompanies.get(position).get("opportunityCode");
                Integer customerCode = (Integer) listCompanies.get(position).get("customerCode");
                Integer salesCustomerCode = (Integer) listCompanies.get(position).get("salesCustomerCode");
                String companyName = (String) listCompanies.get(position).get("companyName");
                cleanIt();
                parentActivity.runFragmentTransaction(R.id.frameMainContainer, MeFragmentFollowUp.getInstance(companyName.length() > 0 ? companyName : "", customerCode, salesCustomerCode, opportunityCode));
            }
            //position = -1;
        } else {
            try {
                if (position != -1) {
                    Integer opportunityCode = MeRepoFactory.getSearchCompanyInfo(parentActivity.getDbHelper()).getOpportunityCode();
                    Log.i("@Transworld", "Opp Code " + opportunityCode);
                    Integer customerCode = MeRepoFactory.getSearchCompanyInfo(parentActivity.getDbHelper()).getCustomerCode();
                    Integer salesCustomerCode = MeRepoFactory.getSearchCompanyInfo(parentActivity.getDbHelper()).getSalesCustomerCode();
                    String companyName = MeRepoFactory.getSearchCompanyInfo(parentActivity.getDbHelper()).getCompanyName();
                    Log.i("@Transworld ", "customerCode " + customerCode + " SalesCustomerCode " + salesCustomerCode);
                    parentActivity.runFragmentTransaction(R.id.frameMainContainer, MeFragmentFollowUp.getInstance(companyName.length() > 0 ? companyName : "", customerCode, salesCustomerCode, opportunityCode));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private final class MeRecyclerItemFollower {
        private Integer imgId;
        private String followerName;

        public MeRecyclerItemFollower() {
        }

        public MeRecyclerItemFollower(Integer imgId) {
            this.imgId = imgId;
        }

        public MeRecyclerItemFollower(String followerName) {
            this.followerName = followerName;
        }

        public String getFollowerName() {
            return followerName;
        }

        public void setFollowerName(String followerName) {
            this.followerName = followerName;
        }

        public Integer getImgId() {
            return imgId;
        }

        public void setImgId(Integer imgId) {
            this.imgId = imgId;
        }
    }

    private final class MeAdapterRecyclerFollower extends RecyclerView.Adapter<MeRecyclerViewHolder> {
        private ArrayList<String> followerList;
        private Context context;
        private LayoutInflater inflater;
        private MainActivity mainActivity;

        public MeAdapterRecyclerFollower(Context context, ArrayList<String> followerList) {
            this.context = context;
            this.followerList = followerList;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public MeRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_cardview_follower, parent, false);
            MeRecyclerViewHolder holder = new MeRecyclerViewHolder(layoutView);
            return holder;
        }

        @Override
        public void onBindViewHolder(MeRecyclerViewHolder recyclerViewHolders, int i) {
            recyclerViewHolders.textFollowerName.setText(followerList.get(i).toString());
            Log.i("@Transworld", "Follow Up Type" + followerList.get(i).toString());
            if (followerList.get(i).toString().equalsIgnoreCase("Add Follow Ups"))
                recyclerViewHolders.imgFollowerLogo.setImageResource(R.drawable.layer_shape_followup);
            if (followerList.get(i).toString().equalsIgnoreCase("Appointments"))
                recyclerViewHolders.imgFollowerLogo.setImageResource(R.drawable.layer_shape_add_contact);
            if (followerList.get(i).toString().equalsIgnoreCase("Summary"))
                recyclerViewHolders.imgFollowerLogo.setImageResource(R.drawable.layer_shape_details);
          /*  if (followerList.get(i).toString().equalsIgnoreCase("Ledger A/C Details"))
                recyclerViewHolders.imgFollowerLogo.setImageResource(R.drawable.layer_shape_ledger_details);*/
        }

        @Override
        public int getItemCount() {
            return followerList.size();
        }
    }

    private final class MeRecyclerViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private TextView textFollowerName;
        private ImageView imgFollowerLogo;

        public MeRecyclerViewHolder(View itemView) {
            super(itemView);
            textFollowerName = (TextView) itemView.findViewById(R.id.textFollowerName);
            imgFollowerLogo = (ImageView) itemView.findViewById(R.id.imgFollowerLogo);
            view = itemView;
            view.setOnClickListener(meClick);
        }
    }

    private final class MeConnectorSearchCompany implements MeConnectable {
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
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Map<String, Object>> doInBackground(Void... params) {
            try {
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
        protected void onPostExecute(List<Map<String, Object>> stringObjectMap) {
            super.onPostExecute(stringObjectMap);
            listCompanySearch.clear();
            // adapterAutoCompanySearch.clear();
            if (stringObjectMap != null) {
                for (Map<String, Object> mapItem : stringObjectMap) {
                    listCompanySearch.add((String) mapItem.get("companyInfo"));
                }
                adapterAutoCompanySearch = new ArrayAdapter<>(parentActivity, android.R.layout.simple_list_item_1, listCompanySearch);
                autoSearchCompany.setAdapter(adapterAutoCompanySearch);
                adapterAutoCompanySearch.notifyDataSetChanged();
            } else {
                parentActivity.snack(rootView, "something went wrong :(");
            }
        }
    }

    private final class MeWatcherCompanySearch implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (autoSearchCompany.isPerformingCompletion()) {
                return;
            }
            if (listCompanySearch.size() > 0) {
                listCompanySearch.clear();
                adapterAutoCompanySearch.notifyDataSetInvalidated();
            }
            String
                    str = s.toString().replace(" ", "%20");
            if (str.length() > 0 && !str.equals("") && !str.equals(" ")) {
                try {
                    if (parentActivity.isNetworkAvailable()) {
                        new MeTaskSearchCompany(str, repoLogin.getCompanyMasterId()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        // saveData();
                        searchCompanyLocally(str);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

    private final class MeItemClick implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            MeFragmentDashboard.this.position = position;
            if (!parentActivity.isNetworkAvailable()) {
                selection = (String) parent.getItemAtPosition(position);
                MeRepoProspectiveCustMaster repoProspectiveCustMaster = new MeRepoImplProspectiveCustMaster(parentActivity.getDbHelper());
                String str[] = selection.split(",");
                try {
                    repoProspectiveCustMaster.saveCompanyName(str[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("@Transworld", "Selection: " + str[0]);
            }
            parentActivity.hideKeyboard(rootView);
           /* InputMethodManager in = (InputMethodManager) parentActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);*/
        }
    }

    private final class MeClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            TextView text = (TextView) v.findViewById(R.id.textFollowerName);
            if (text.getText().toString().equalsIgnoreCase("Add Follow Ups")) {
                if (parentActivity.isNetworkAvailable()) {
                    if ((position != -1) && (!(listCompanies.isEmpty())) && (!autoSearchCompany.getText().equals(""))) {
                        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            Log.i("@Transworld", "In Inner LOOP");
                            buildAlertMessageNoGps();
                        } else {
                            runFragmentAddFollowUp();
                        }
                    } else {
                        parentActivity.snack(rootView, "Please Select Customer");
                    }
                } else {
                    runFragmentAddFollowUp();
                }
            }
            if (text.getText().toString().equalsIgnoreCase("Appointments")) {
                cleanIt();
                position = -1;
                parentActivity.runFragmentTransaction(R.id.frameMainContainer, MeFragmentAppointment.getInstance());
            }
            if (text.getText().toString().equalsIgnoreCase("Summary")) {
                if (parentActivity.isNetworkAvailable()) {
                    Log.i("@Transworld","in Summary - ");
                    if (position != -1 && (!(listCompanies.isEmpty()))) {
                        Log.i("@Transworld","listCompanies - "+listCompanies.toString());
                        String companyName = (String) listCompanies.get(position).get("companyName");
                        Integer customerCode = (Integer) listCompanies.get(position).get("customerCode");
                        Integer salesCustomerCode = (Integer) listCompanies.get(position).get("salesCustomerCode");
                        Log.i("@Transworld","companyName - "+companyName);
                       /* if ((companyName.length() > 0) && (!autoSearchCompany.getText().equals("")))
                            parentActivity.runFragmentTransaction(R.id.frameMainContainer, MeFragmentCustomerDetails.getInstance(companyName.length() > 0 ? companyName : "", salesCustomerCode));
                        else
                            parentActivity.snack(rootView, "Please Select Customer");
                    } else {*/
                        parentActivity.snack(rootView, "Please Select Customer");
                    }
                }
            }
        }
    }
}
