package com.transworldtechnology.crm.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.kevinsawicki.http.HttpRequest;
import com.transworldtechnology.crm.R;
import com.transworldtechnology.crm.activity.MainActivity;
import com.transworldtechnology.crm.database.MeRepoFactory;
import com.transworldtechnology.crm.database.repository.MeRepoImplSaveFollowUp;
import com.transworldtechnology.crm.database.repository.MeRepoSaveFollowUp;
import com.transworldtechnology.crm.prefs.MePrefs;
import com.transworldtechnology.crm.web.JsonMan;
import com.transworldtechnology.crm.web.MeConnectable;
import com.transworldtechnology.crm.web.MeIUrl;
import com.transworldtechnology.crm.web.MeNetworkChangeReceiver;

import java.lang.reflect.Method;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by root on 18/1/16.
 */
public class MeFragmentAppointment extends Fragment
{
    private ArrayList<MeRecyclerItemAppointment> listAppointmentItems = new ArrayList<>();
    private MeNetworkChangeReceiver networkChangeReceiver;
    private Map<String, Object> appointmentList = new HashMap<>();
    private MainActivity parentActivity;
    private MeAdapterRecyclerAppointment adapterRecyclerAppointment;
    private RecyclerView.Adapter adapter;
    private Meclick meclick;
    private MeDateListener dateListener;
    private Calendar calendar;
    private String task = "m";
    private Integer companyMasterId;
    private Integer marketingRepCode;
    private String currentDateString, date, textviewDate, currentDate;
    private View rootView;
    private RelativeLayout relativeCurrentDate;
    private RecyclerView recyclerAppointment;
    private RecyclerView.LayoutManager layoutManager;
    private TextView textCurrentDate;
    private TextView textWeekly, textMonthly;
    private ProgressBar progressBar;
    private TextView textMobileNo;
    private Menu menu;
    private MenuItem itemFollowUp, itemfollowUptype;
    private TextView textFollowUpType;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String companyNameAppointment;

    public static MeFragmentAppointment getInstance()
    {
        MeFragmentAppointment meFragmentAppointment = new MeFragmentAppointment();
        Bundle args = new Bundle();
        args.putString(MainActivity.KEY_FRAGMENT_NAME, "MeFragmentAppointment");
        meFragmentAppointment.setArguments(args);
        return meFragmentAppointment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentActivity = (MainActivity) getActivity();
        parentActivity.showDrawerFab();
        parentActivity.showSearchView(true);
        parentActivity.initImageView();
        networkChangeReceiver = new MeNetworkChangeReceiver();
        parentActivity.setTitle("Search Company         ");
        meclick = new Meclick();
        dateListener = new MeDateListener();
        calendar = Calendar.getInstance();
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        parentActivity.getSupportActionBar().show();
        rootView = inflater.inflate(R.layout.fragment_appointment, container, false);
        parentActivity.toolbarClickable();
        parentActivity.showDrawerFab();
        parentActivity.initTextView();
        parentActivity.hideKeyboard(rootView);
        if (MePrefs.getKeyCompanyName(getContext()).equals("none")) {
            parentActivity.setTitle("Search Company         ");
        } else {
            parentActivity.setTitle(MePrefs.getKeyCompanyName(getContext()));
        }
        parentActivity.showSearchView(true);
        parentActivity.clearListView();
        initDBValue();
        initLayout();
        initTextViews();
        initMenuItem();
        initDate();
        initCardsAppointment();
        initSwipeRefresh();
        if (parentActivity.isNetworkAvailable())
        {
            new MeTaskAppointment(companyMasterId, marketingRepCode, currentDateString, task, "all").execute();
        }
        else
        {
            getAppointListMonthlyLocal();
        }
        return rootView;
    }
    private void initSwipeRefresh()
    {
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (parentActivity.isNetworkAvailable())
                    new MeTaskAppointment(companyMasterId, marketingRepCode, currentDateString, task, "all").execute();
                else
                {
                    parentActivity.snack(rootView, "Unable to connect to the server:(");
                    if (swipeRefreshLayout.isRefreshing())
                        swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        parentActivity.registerReceiver(networkChangeReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }
    @Override
    public void onPause()
    {
        super.onPause();
        parentActivity.unregisterReceiver(networkChangeReceiver);
    }

    private void getAppointListTodayLocal()
    {
        listAppointmentItems.clear();
        MeRepoSaveFollowUp repoSaveFollowUp = new MeRepoImplSaveFollowUp(parentActivity.getDbHelper());
        progressBar.setVisibility(View.GONE);
        //try {
        try
        {
            // for ( appointmentList:repoSaveFollowUp.getAppointmentsListToday(dateListener.getDateToWeb(), textFollowUpType.getText().toString())) {
            List<Map<String, Object>> listAppointments = repoSaveFollowUp.getAppointmentsListToday(dateListener.getDateToWeb(), textFollowUpType.getText().toString());
            if (listAppointments != null) {
                for (Map<String, Object> map : listAppointments) {
                    appointmentList = map;
                    MeRecyclerItemAppointment item = new MeRecyclerItemAppointment();
                    item.setTheSrno((Integer) appointmentList.get("theSrNo"));
                    item.setCompanyNameApp((String) appointmentList.get("companyName"));
                    item.setFollowUpType((String) appointmentList.get("followUpType"));
                    item.setContactPerson((String) appointmentList.get("contactPerson"));
                    item.setDate((String) appointmentList.get("appointmentDate"));
                    item.setTime((String) appointmentList.get("Time"));
                    listAppointmentItems.add(item);
                    Log.i("@Transworld", "Item in Recycler" + item.toString());
                }
                adapterRecyclerAppointment.notifyDataSetChanged();
                Log.i("@Transworld", "Map Appointment : " + listAppointments);
            } else {
                parentActivity.snack(rootView, "No Appointments for " + textCurrentDate.getText().toString() + " :(");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void getAppointListWeeklyLocal()
    {
        listAppointmentItems.clear();
        MeRepoSaveFollowUp repoSaveFollowUp = new MeRepoImplSaveFollowUp(parentActivity.getDbHelper());
        progressBar.setVisibility(View.GONE);
        //try {
        try {
            // for ( appointmentList:repoSaveFollowUp.getAppointmentsListToday(dateListener.getDateToWeb(), textFollowUpType.getText().toString())) {
            List<Map<String, Object>> listAppointments = repoSaveFollowUp.getAppointmentsListWeekly(dateListener.getDateToWeb(), textFollowUpType.getText().toString());
            if (listAppointments != null) {
                for (Map<String, Object> map : listAppointments) {
                    appointmentList = map;
                    MeRecyclerItemAppointment item = new MeRecyclerItemAppointment();
                    item.setTheSrno((Integer) appointmentList.get("theSrNo"));
                    item.setCompanyNameApp((String) appointmentList.get("companyName"));
                    item.setFollowUpType((String) appointmentList.get("followUpType"));
                    item.setContactPerson((String) appointmentList.get("contactPerson"));
                    item.setDate((String) appointmentList.get("appointmentDate"));
                    item.setTime((String) appointmentList.get("Time"));
                    listAppointmentItems.add(item);
                    Log.i("@Transworld", "Item in Recycler" + item.toString());
                }
                adapterRecyclerAppointment.notifyDataSetChanged();
                Log.i("@Transworld", "Map Appointment : " + listAppointments);
            } else
                parentActivity.snack(rootView, "No Appointments in this week :(");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getAppointListMonthlyLocal() {
        listAppointmentItems.clear();
        MeRepoSaveFollowUp repoSaveFollowUp = new MeRepoImplSaveFollowUp(parentActivity.getDbHelper());
        progressBar.setVisibility(View.GONE);
        //try {
        try {
            // for ( appointmentList:repoSaveFollowUp.getAppointmentsListToday(dateListener.getDateToWeb(), textFollowUpType.getText().toString())) {
            List<Map<String, Object>> listAppointments = repoSaveFollowUp.getAppointmentsListMonthly(dateListener.getDateToWeb(), textFollowUpType.getText().toString());
            if (listAppointments != null) {
                for (Map<String, Object> map : listAppointments) {
                    appointmentList = map;
                    MeRecyclerItemAppointment item = new MeRecyclerItemAppointment();
                    item.setTheSrno((Integer) appointmentList.get("theSrNo"));
                    item.setCompanyNameApp((String) appointmentList.get("companyName"));
                    item.setFollowUpType((String) appointmentList.get("followUpType"));
                    item.setContactPerson((String) appointmentList.get("contactPerson"));
                    item.setDate((String) appointmentList.get("appointmentDate"));
                    item.setTime((String) appointmentList.get("Time"));
                    listAppointmentItems.add(item);
                    Log.i("@Transworld", "Item in Recycler" + item.toString());
                }
                adapterRecyclerAppointment.notifyDataSetChanged();
                Log.i("@Transworld", "Map Appointment : " + listAppointments);
            } else
                parentActivity.snack(rootView, "No Appointments in this month :(");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private HashMap<String, Object> getAppointmentDetailsLocal(Integer theSrno)
    {
        HashMap<String, Object> appointmentDetailsMap = new HashMap<>();
        MeRepoSaveFollowUp saveFollowUp = new MeRepoImplSaveFollowUp(parentActivity.getDbHelper());
        try {
            List<HashMap<String, Object>> appointmentDetails = saveFollowUp.getAppointmentDetails(theSrno);
            if (appointmentDetails != null)
                for (HashMap<String, Object> map : appointmentDetails) {
                    appointmentDetailsMap = map;
                }
            Log.i("@Transworld", "APpointment Detils offline" + appointmentDetails);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appointmentDetailsMap;
    }

    private void initTextViews() {
        textFollowUpType = (TextView) rootView.findViewById(R.id.textFollowUpType);
        textFollowUpType.setText("All");
    }

    private void initLayout() {
        relativeCurrentDate = (RelativeLayout) rootView.findViewById(R.id.relativeCurrentDate);
    }

    private void initMenuItem() {
        itemfollowUptype = (MenuItem) rootView.findViewById(R.id.action_points);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
        if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
            try {
                Method m = menu.getClass().getDeclaredMethod(
                        "setOptionalIconsVisible", Boolean.TYPE);
                m.setAccessible(true);
                m.invoke(menu, true);
            } catch (NoSuchMethodException e) {
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        try {
            switch (item.getItemId()) {
                case R.id.action_search: {
                    parentActivity.clearSearchBar();
                    MePrefs.clearSharedPrefs(getContext());
                    parentActivity.showSearchView(true);
                }
                break;
                case R.id.action_points:
                    if (item.getTitle().equals("Action Points")) {
                        textFollowUpType.setText("Action Points");
                        Log.i("@Transworld", "Menu Item " + item.getTitle());
                    }
                    break;
                case R.id.action_Email:
                    if (item.getTitle().equals("Email")) {
                        textFollowUpType.setText("E-mail");
                        Log.i("@Transworld", "Menu Item " + item.getTitle());
                    }
                    break;
                case R.id.action_Letter:
                    if (item.getTitle().equals("Letter")) {
                        textFollowUpType.setText("Letter");
                    }
                    break;
                case R.id.action_Meeting:
                    if (item.getTitle().equals("Meeting")) {
                        textFollowUpType.setText("Meeting");
                    }
                    break;
                case R.id.action_phone_call:
                    if (item.getTitle().equals("Phone Call")) {
                        textFollowUpType.setText("Phone Call");
                    }
                    break;
                case R.id.action_visit:
                    if (item.getTitle().equals("Visit")) {
                        textFollowUpType.setText("Visit");
                    }
                    break;
                case R.id.action_select_all:
                    if (item.getTitle().equals("Select All")) {
                        textFollowUpType.setText("All");
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("@Transworld", "CompanyMasterId - " + companyMasterId);
        Log.i("@Transworld", "MarkertingRepCode - " + marketingRepCode);
        Log.i("@Transworld", "Date - " + dateListener.getDateToWeb());
        Log.i("@Transworld", "Task - " + task);
//        Log.i("@Transworld", "followuptype - " + textFollowUpType.getText().toString());
        if (parentActivity.isNetworkAvailable())
            new MeTaskAppointment(companyMasterId, marketingRepCode, dateListener.getDateToWeb(), task, textFollowUpType.getText().toString().replace(" ", "%20")).execute();
        else {
            getAppointListTodayLocal();
        }
        return true;
    }

    private void initDBValue() {
        try {
            companyMasterId = MeRepoFactory.getLoginRepository(parentActivity.getDbHelper()).getCompanyMasterId();
            marketingRepCode = MeRepoFactory.getLoginRepository(parentActivity.getDbHelper()).getEmployeeCode();
            Log.i("@Transworld", "Company Marketing rep Code -" + marketingRepCode);
            Log.i("@Transworld", "Company Master Id -" + companyMasterId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initDate() {
        date = calendar.get(Calendar.DAY_OF_MONTH) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.YEAR);
        Format formatter = new SimpleDateFormat("dd-MMM-yyyy");
        date = formatter.format(new Date());
        Log.i("@Transworld", "dateTotext -" + date);
        currentDateString = calendar.get(Calendar.YEAR) + "-" + ((calendar.get(Calendar.MONTH) + 1) > 9 ? (calendar.get(Calendar.MONTH) + 1) : "0" + (calendar.get(Calendar.MONTH) + 1)) + "-" + (calendar.get(Calendar.DAY_OF_MONTH) > 9 ? calendar.get(Calendar.DAY_OF_MONTH) : "0" + calendar.get(Calendar.DAY_OF_MONTH));
        textviewDate = calendar.get(Calendar.YEAR) + "-" + ((calendar.get(Calendar.MONTH) + 1) > 9 ? (calendar.get(Calendar.MONTH) + 1) : "0" + (calendar.get(Calendar.MONTH) + 1)) + "-" + (calendar.get(Calendar.DAY_OF_MONTH) > 9 ? calendar.get(Calendar.DAY_OF_MONTH) : "0" + calendar.get(Calendar.DAY_OF_MONTH));
        dateListener.setDateToWeb(currentDateString);
    }

    private void initCardsAppointment() {
        textCurrentDate = (TextView) rootView.findViewById(R.id.textCurrentDate);
        textCurrentDate.setOnClickListener(meclick);
        textWeekly = (TextView) rootView.findViewById(R.id.textWeekly);
        textWeekly.setOnClickListener(meclick);
        textMonthly = (TextView) rootView.findViewById(R.id.textMonthly);
        textMonthly.setOnClickListener(meclick);
        recyclerAppointment = (RecyclerView) rootView.findViewById(R.id.my_recycler_view_appointment);
        adapterRecyclerAppointment = new MeAdapterRecyclerAppointment(parentActivity);
        recyclerAppointment.setAdapter(adapterRecyclerAppointment);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerAppointment.setLayoutManager(layoutManager);
        // textMobileNo = (TextView) rootView.findViewById(R.id.textMobileNo);
        textCurrentDate.setText(date);
        currentDate = textCurrentDate.getText().toString();
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar1);
        progressBar.setProgress(0);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#303F9F"), android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    private final class MeDateListener implements DatePickerDialog.OnDateSetListener {
        private String dateToWeb, date, month;
        private String args[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            month = args[monthOfYear];
            date = "" + (dayOfMonth > 9 ? dayOfMonth : "0" + dayOfMonth) + "-" + month + "-" + year;
            // dateToWeb = "" + year + "-" + (monthOfYear + 1) + "-" + (dayOfMonth > 9 ? dayOfMonth : "0" + dayOfMonth);
            dateToWeb = year + "-" + ((monthOfYear + 1 > 9) ? (monthOfYear + 1) : "0" + (monthOfYear + 1) + "-" + (dayOfMonth > 9 ? dayOfMonth : "0" + dayOfMonth));
            textCurrentDate.setText(date);
            Log.i("@Transworld", "Date To Web New " + dateToWeb);
            //Calling WebService
            if (parentActivity.isNetworkAvailable())
                new MeTaskAppointment(companyMasterId, marketingRepCode, dateListener.getDateToWeb(), task, textFollowUpType.getText().toString().replace(" ", "%20")).execute();
            else
                getAppointListTodayLocal();
        }

        public String getDateToWeb() {
            return dateToWeb;
        }

        public void setDateToWeb(String dateToWeb) {
            this.dateToWeb = dateToWeb;
        }
    }

    private final class MeRecyclerItemAppointment {
        private Integer contactId;
        private String companyNameApp;
        private String date;
        private String time;
        private String contactPerson;
        private String followUpType;
        private String comments;
        //   private String mobileNo;
        private Integer theSrno;

        public MeRecyclerItemAppointment() {
        }

        public Integer getTheSrno() {
            return theSrno;
        }

        public void setTheSrno(Integer theSrno) {
            this.theSrno = theSrno;
        }

        public String getCompanyNameApp() {
            return companyNameApp;
        }

        public void setCompanyNameApp(String companyNameApp) {
            this.companyNameApp = companyNameApp;
        }

        public Integer getContactId() {
            return contactId;
        }

        public void setContactId(Integer contactId) {
            this.contactId = contactId;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getContactPerson() {
            return contactPerson;
        }

        public void setContactPerson(String contactPerson) {
            this.contactPerson = contactPerson;
        }

        public String getFollowUpType() {
            return followUpType;
        }

        public void setFollowUpType(String followUpType) {
            this.followUpType = followUpType;
        }

        @Override
        public String toString() {
            return "MeRecyclerItemAppointment{" +
                    "contactId=" + contactId +
                    ", companyNameApp='" + companyNameApp + '\'' +
                    ", date='" + date + '\'' +
                    ", time='" + time + '\'' +
                    ", contactPerson='" + contactPerson + '\'' +
                    ", followUpType='" + followUpType + '\'' +
                    '}';
        }

        public String getComments() {
            return comments;
        }

        public void setComments(String comments) {
            this.comments = comments;
        }
    }

    private final class MeAdapterRecyclerAppointment extends RecyclerView.Adapter<MeRecyclerViewHolder> {
        private Context context;
        private LayoutInflater inflater;

        public MeAdapterRecyclerAppointment(Context context) {
            this.context = context;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public MeRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_cardview_appointment, parent, false);
            MeRecyclerViewHolder holder = new MeRecyclerViewHolder(layoutView);
            return holder;
        }

        @Override
        public void onBindViewHolder(MeRecyclerViewHolder recyclerViewHolders, int i) {
            Boolean isPhoneCall = null, isEmail = null, isVisit = null, isActionPoints = null, isLetter = null, isMeeting = null;
            HashMap<String, Object> mapClickInfo = new HashMap<>();
            if (parentActivity.isNetworkAvailable()) {
                mapClickInfo.put("TheSrno", listAppointmentItems.get(i).getTheSrno());
                mapClickInfo.put("contactId", listAppointmentItems.get(i).getContactId());
                mapClickInfo.put("Time", listAppointmentItems.get(i).getTime());
                mapClickInfo.put("appointmentDate", listAppointmentItems.get(i).getDate());
                recyclerViewHolders.getCardViewAppointment().setTag(mapClickInfo);
                mapClickInfo = null;
                recyclerViewHolders.getTextCompanyNameApp().setText(listAppointmentItems.get(i).getCompanyNameApp());
                recyclerViewHolders.getTextContactPerson().setText(listAppointmentItems.get(i).getContactPerson());
                recyclerViewHolders.getTextDate().setText(listAppointmentItems.get(i).getDate());
                recyclerViewHolders.getTextTime().setText(listAppointmentItems.get(i).getTime());
                recyclerViewHolders.getTextComments().setText(listAppointmentItems.get(i).getComments());
                //   recyclerViewHolders.getTextMobileNo().setText(listAppointmentItems.get(i).getMobileNo());
                //  recyclerViewHolders.getTextMobileImage().setVisibility(View.GONE);
                isPhoneCall = listAppointmentItems.get(i).getFollowUpType().equals("Phone Call");
                isEmail = listAppointmentItems.get(i).getFollowUpType().equals("E-mail");
                isVisit = listAppointmentItems.get(i).getFollowUpType().equals("Visit");
                isActionPoints = listAppointmentItems.get(i).getFollowUpType().equals("Action Points");
                isLetter = listAppointmentItems.get(i).getFollowUpType().equals("Letter");
                isMeeting = listAppointmentItems.get(i).getFollowUpType().equals("Meeting");
            } else {
                mapClickInfo.put("TheSrno", listAppointmentItems.get(i).getTheSrno());
                recyclerViewHolders.getTextCompanyNameApp().setText(listAppointmentItems.get(i).getCompanyNameApp());
                recyclerViewHolders.getTextContactPerson().setText(listAppointmentItems.get(i).getContactPerson());
                recyclerViewHolders.getTextDate().setText(listAppointmentItems.get(i).getDate());
                recyclerViewHolders.getTextTime().setText(listAppointmentItems.get(i).getTime());
                recyclerViewHolders.getCardViewAppointment().setTag(mapClickInfo);
            }
            if ((listAppointmentItems.get(i).getFollowUpType().equals("Phone Call")) || listAppointmentItems.get(i).getFollowUpType().equals("phoneCall")) {
                recyclerViewHolders.textNameLetter.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_card_one));
                Log.i("@Transworld", "Char At " + listAppointmentItems.get(i).getCompanyNameApp().charAt(0));
                //  recyclerViewHolders.textNameLetter.setText(listAppointmentItems.get(i).getCompanyNameApp().substring(0,1));
                recyclerViewHolders.textNameLetter.setText("PC");
            } else if (listAppointmentItems.get(i).getFollowUpType().equals("E-mail")) {
                recyclerViewHolders.textNameLetter.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_card_two));
                Log.i("@Transworld", "Char At " + listAppointmentItems.get(i).getCompanyNameApp().charAt(0));
                //   recyclerViewHolders.textNameLetter.setText(listAppointmentItems.get(i).getCompanyNameApp().substring(0,1));
                recyclerViewHolders.textNameLetter.setText("E");
            } else if (listAppointmentItems.get(i).getFollowUpType().equals("Visit")) {
                recyclerViewHolders.textNameLetter.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_card_three));
                Log.i("@Transworld", "Char At " + listAppointmentItems.get(i).getCompanyNameApp().charAt(0));
                // recyclerViewHolders.textNameLetter.setText(listAppointmentItems.get(i).getCompanyNameApp().substring(0,1));
                recyclerViewHolders.textNameLetter.setText("V");
            } else if (listAppointmentItems.get(i).getFollowUpType().equals("Action Points")) {
                recyclerViewHolders.textNameLetter.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_card_four));
                Log.i("@Transworld", "Char At " + listAppointmentItems.get(i).getCompanyNameApp().charAt(0));
                // recyclerViewHolders.textNameLetter.setText(listAppointmentItems.get(i).getCompanyNameApp().substring(0,1));
                recyclerViewHolders.textNameLetter.setText("AP");
            } else if (listAppointmentItems.get(i).getFollowUpType().equals("Meeting")) {
                recyclerViewHolders.textNameLetter.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_card_five));
                Log.i("@Transworld", "Char At " + listAppointmentItems.get(i).getCompanyNameApp().charAt(0));
                // recyclerViewHolders.textNameLetter.setText(listAppointmentItems.get(i).getCompanyNameApp().substring(0,1));
                recyclerViewHolders.textNameLetter.setText("M");
            }
            else if (listAppointmentItems.get(i).getFollowUpType().equals("Letter")) {
                recyclerViewHolders.textNameLetter.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_card_two));
                Log.i("@Transworld", "Char At " + listAppointmentItems.get(i).getCompanyNameApp().charAt(0));
                //   recyclerViewHolders.textNameLetter.setText(listAppointmentItems.get(i).getCompanyNameApp().substring(0,1));
                recyclerViewHolders.textNameLetter.setText("L");
            }
        }

        @Override
        public int getItemCount() {
            return listAppointmentItems.size();
        }
    }

    private final class MeRecyclerViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private CardView cardViewAppointment;
        private TextView textCompanyNameApp;
        private TextView textDate;
        private TextView textTime;
        private TextView textContactPerson, textMobileImage;
        private ImageView imgFollowUpType;
        private TextView textMobileNo;
        private TextView textNameLetter;
        private TextView textComments;

        public MeRecyclerViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            textNameLetter = (TextView) itemView.findViewById(R.id.textNameLetter);
            textCompanyNameApp = (TextView) itemView.findViewById(R.id.textCompanyNameApp);
            textDate = (TextView) itemView.findViewById(R.id.textDate);
            textTime = (TextView) itemView.findViewById(R.id.textTime);
            textContactPerson = (TextView) itemView.findViewById(R.id.textContactPerson);
            //  imgFollowUpType = (ImageView) itemView.findViewById(R.id.imgFollowUpType);
            //     textMobileImage = (TextView) itemView.findViewById(R.id.textMobileImage);
            //   textMobileNo = (TextView) itemView.findViewById(R.id.textMobileNo);
            textComments = (TextView) itemView.findViewById(R.id.textComments);
          /*  textMobileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + textMobileNo.getText().toString()));
                    startActivity(intent);
                }
            });*/
            cardViewAppointment = (CardView) itemView.findViewById(R.id.card_view_appointment);
            cardViewAppointment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MePrefs.clearSharedPrefsAppointment(getContext());
                    HashMap<String, Object> mapClickInfo = (HashMap<String, Object>) cardViewAppointment.getTag();
                    Log.i("@Transworld", "MapClickInfo : " + mapClickInfo);
                    Integer theSrno = (Integer) mapClickInfo.get("TheSrno");
                    try {
                        companyMasterId = MeRepoFactory.getLoginRepository(parentActivity.getDbHelper()).getCompanyMasterId();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    parentActivity.runFragmentTransaction(R.id.frameMainContainer, MeFragmentAppointmentDetails.getInstance(getTextCompanyNameApp().getText().toString(), companyMasterId, theSrno, "Appointment"));
                }
            });
        }

        public TextView getTextMobileImage() {
            return textMobileImage;
        }

        public void setTextMobileImage(TextView textMobileImage) {
            this.textMobileImage = textMobileImage;
        }

        public View getView() {
            return view;
        }

        public void setView(View view) {
            this.view = view;
        }

        public TextView getTextCompanyNameApp() {
            return textCompanyNameApp;
        }

        public void setTextCompanyNameApp(TextView textCompanyNameApp) {
            this.textCompanyNameApp = textCompanyNameApp;
        }

        public TextView getTextDate() {
            return textDate;
        }

        public void setTextDate(TextView textDate) {
            this.textDate = textDate;
        }

        public TextView getTextTime() {
            return textTime;
        }

        public void setTextTime(TextView textTime) {
            this.textTime = textTime;
        }

        public TextView getTextContactPerson() {
            return textContactPerson;
        }

        public void setTextContactPerson(TextView textContactPerson) {
            this.textContactPerson = textContactPerson;
        }

        public ImageView getImgFollowUpType() {
            return imgFollowUpType;
        }

        public void setImgFollowUpType(ImageView imgFollowUpType) {
            this.imgFollowUpType = imgFollowUpType;
        }

        public TextView getTextMobileNo() {
            return textMobileNo;
        }

        public void setTextMobileNo(TextView textMobileNo) {
            this.textMobileNo = textMobileNo;
        }

        public CardView getCardViewAppointment() {
            return cardViewAppointment;
        }

        public void setCardViewAppointment(CardView cardViewAppointment) {
            this.cardViewAppointment = cardViewAppointment;
        }

        public TextView getTextComments() {
            return textComments;
        }

        public void setTextComments(TextView textComments) {
            this.textComments = textComments;
        }
    }

    private final class MeConnectorAppointment implements MeConnectable {
        private String task;
        private String jsonMap;

        public MeConnectorAppointment(String task) {
            this.task = task;
        }

        public Map<String, Object> listOfAppointmentToday(Integer companyMasterId, Integer marketingRepCode, String date, String followUptype) throws Exception {
            Map<String, Object> mapEntity = new HashMap<>();
          /*  if (task == "c") {
                jsonMap = HttpRequest.get(MeIUrl.URL_APPOINTMENT_LIST__TYPE + companyMasterId + "/" + marketingRepCode + "/" + date + "/" + followUptype.replace(" ", "%20"))
                        .body();
                Log.i("@Transworld", MeIUrl.URL_APPOINTMENT_LIST__TYPE + companyMasterId + "/" + marketingRepCode + "/" + date + "/" + followUptype.replace(" ", "%20")
                );
                Log.i("@Transworld", "Today's Appointment AcionPoints -" + jsonMap);
            }*/
            if (task == "m") {
                jsonMap = HttpRequest.get(MeIUrl.URL_MONTH_APPOINTMENT_LIST + companyMasterId + "/" + marketingRepCode + "/" + date + "/" + followUptype.replace(" ", "%20"))
                        .body();
                Log.i("@Transworld", MeIUrl.URL_MONTH_APPOINTMENT_LIST + companyMasterId + "/" + marketingRepCode + "/" + date + "/" + followUptype.replace(" ", "%20")
                );
                Log.i("@Transworld", "Monthly Appointment" + jsonMap);
            }
            /*if (task == "w") {
                jsonMap = HttpRequest.get(MeIUrl.URL_WEEK_APPOINTMENT_LIST + companyMasterId + "/" + marketingRepCode + "/" + date + "/" + followUptype.replace(" ", "%20"))
                        .body();
                Log.i("@Transworld", MeIUrl.URL_WEEK_APPOINTMENT_LIST + companyMasterId + "/" + marketingRepCode + "/" + date + "/" + followUptype.replace(" ", "%20")
                );
                Log.i("@Transworld", "Weekly Appointment" + jsonMap);
            }*/
            mapEntity = JsonMan.parseAnything(jsonMap, new TypeReference<Map<String, Object>>() {
            });
            return mapEntity;
        }
    }

    private class MeTaskAppointment extends AsyncTask<Void, Void, List<Map<String, Object>>> {
        private MeConnectorAppointment connectorAppointment;
        private Integer companyMasterId;
        private Integer marketingRepCode;
        private String date, task, followUptype;

        public MeTaskAppointment(Integer companyMasterId, Integer marketingRepCode, String date, String task, String followUptype) {
            this.companyMasterId = companyMasterId;
            this.date = date;
            this.marketingRepCode = marketingRepCode;
            this.task = task;
            this.followUptype = followUptype;
            connectorAppointment = new MeConnectorAppointment(task);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (swipeRefreshLayout.isRefreshing())
                progressBar.setVisibility(View.INVISIBLE);
            else
                progressBar.setVisibility(View.VISIBLE);
            recyclerAppointment.setVisibility(View.INVISIBLE);
            listAppointmentItems.clear();
            adapterRecyclerAppointment.notifyDataSetChanged();
            //   rootView.findViewById(R.id.swipeRefreshLayout).setEnabled(true);
        }

        @Override
        protected List<Map<String, Object>> doInBackground(Void... params) {
            List<Map<String, Object>> listAppointmets = new ArrayList<>();
            try {
                Map<String, Object> mapEntity = connectorAppointment.listOfAppointmentToday(companyMasterId, marketingRepCode, date, followUptype);
                if (mapEntity != null) {
                    listAppointmets = (List<Map<String, Object>>) mapEntity.get("appointments");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listAppointmets;
        }

        @Override
        protected void onPostExecute(List<Map<String, Object>> stringObjectMap) {
            super.onPostExecute(stringObjectMap);
            progressBar.setVisibility(View.GONE);
            recyclerAppointment.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(false);
            if (stringObjectMap != null && stringObjectMap.size() > 0) {
                for (Map<String, Object> mapItem : stringObjectMap) {
                    MeRecyclerItemAppointment item = new MeRecyclerItemAppointment();
                    item.setContactId((Integer) mapItem.get("contactId"));
                    Log.i("@Transworld", "Contact Id  : " + mapItem.get("contactId"));
                    item.setTheSrno((Integer) mapItem.get("TheSrno"));
                    item.setCompanyNameApp((String) mapItem.get("companyName"));
                    item.setFollowUpType((String) mapItem.get("followUpType"));
                    item.setContactPerson((String) mapItem.get("ContactPerson"));
                    item.setDate((String) mapItem.get("appointmentDate"));
                    item.setTime((String) mapItem.get("Time"));
                    item.setComments((String) mapItem.get("Status"));
                    //      item.setMobileNo((String) mapItem.get("mobileno"));
                    Log.i("@Transworld", "Just Inserted Item -" + item.toString());
                    listAppointmentItems.add(item);
                }
                adapterRecyclerAppointment.notifyDataSetChanged();
            } else {
                adapterRecyclerAppointment.notifyDataSetChanged();
                if (task == "c") {
                    parentActivity.snack(rootView, "No Appointments for " + textCurrentDate.getText().toString() + " :(");
                }
                if (task == "w") {
                    parentActivity.snack(rootView, "No Appointments in this week :(");
                }
                if (task == "m" && getView()!=null) {
                    parentActivity.snack(rootView, "No Appointments in this month :(");
                }
            }
        }
    }

    private final class Meclick implements View.OnClickListener {
        public void onClick(View v) {
            if (v.getId() == textCurrentDate.getId()) {
                task = "c";
                relativeCurrentDate.setBackgroundColor(Color.parseColor("#d2e5f6"));
                textCurrentDate.setBackgroundColor(Color.parseColor("#d2e5f6"));
                textCurrentDate.setTextColor(Color.parseColor("#303F9F"));
                textWeekly.setBackgroundColor(Color.parseColor("#303F9F"));
                textWeekly.setTextColor(Color.parseColor("#d2e5f6"));
                textMonthly.setBackgroundColor(Color.parseColor("#303F9F"));
                textMonthly.setTextColor(Color.parseColor("#d2e5f6"));
                new DatePickerDialog(parentActivity, dateListener, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
            if (v.getId() == textWeekly.getId()) {
                task = "w";
                textWeekly.setBackgroundColor(Color.parseColor("#d2e5f6"));
                textWeekly.setTextColor(Color.parseColor("#303F9F"));
                textMonthly.setBackgroundColor(Color.parseColor("#303F9F"));
                textMonthly.setTextColor(Color.parseColor("#d2e5f6"));
                relativeCurrentDate.setBackgroundColor(Color.parseColor("#303F9F"));
                textCurrentDate.setBackgroundColor(Color.parseColor("#303F9F"));
                textCurrentDate.setTextColor(Color.parseColor("#d2e5f6"));
                if (parentActivity.isNetworkAvailable())
                    new MeTaskAppointment(companyMasterId, marketingRepCode, currentDateString, task, textFollowUpType.getText().toString().replace(" ", "%20")).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                else
                    getAppointListWeeklyLocal();
            }
            if (v.getId() == textMonthly.getId()) {
                task = "m";
                textMonthly.setBackgroundColor(Color.parseColor("#d2e5f6"));
                textMonthly.setTextColor(Color.parseColor("#303F9F"));
                textWeekly.setBackgroundColor(Color.parseColor("#303F9F"));
                textWeekly.setTextColor(Color.parseColor("#d2e5f6"));
                relativeCurrentDate.setBackgroundColor(Color.parseColor("#303F9F"));
                textCurrentDate.setBackgroundColor(Color.parseColor("#303F9F"));
                textCurrentDate.setTextColor(Color.parseColor("#d2e5f6"));
                if (parentActivity.isNetworkAvailable())
                    new MeTaskAppointment(companyMasterId, marketingRepCode, currentDateString, task, textFollowUpType.getText().toString().replace(" ", "%20")).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                else
                    getAppointListMonthlyLocal();
            }
        }
    }

}
