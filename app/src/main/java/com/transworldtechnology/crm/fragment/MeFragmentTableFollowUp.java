package com.transworldtechnology.crm.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.kevinsawicki.http.HttpRequest;
import com.transworldtechnology.crm.R;
import com.transworldtechnology.crm.activity.MainActivity;
import com.transworldtechnology.crm.database.MeRepoFactory;
import com.transworldtechnology.crm.web.JsonMan;
import com.transworldtechnology.crm.web.MeConnectable;
import com.transworldtechnology.crm.web.MeIUrl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by root on 26/2/16.
 */
public class MeFragmentTableFollowUp extends Fragment {
    public static final String KEY_COMPANY_NAME = "company_name";
    public static final String KEY_CUSTOMER_CODE = "cust_code";
    public static final String KEY_SALES_CUSTOMER_CODE = "sales_cust_code";
    public static final String KEY_OPPORTUNITY_CODE = "opp_code";
    private MainActivity parentActivity;
    private View rootView;
    private ArrayList<MeRecyclerItemLastFollowUp> listFollowUpItems = new ArrayList<>();
    private MeAdapterRecyclerLastFollowUp adapterRecyclerLastFollowUp;
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewFollowUp;
    private RecyclerView.LayoutManager layoutManager;
    private Button btnGo;

    private String companyName;
    private TextView textCompanyNameFollowup, textHeader;

    public static MeFragmentTableFollowUp getInstance(String companyName, Integer customerCode, Integer salesCustomerCode, Integer opportunityCode) {
        MeFragmentTableFollowUp fragmentLedgerTable = new MeFragmentTableFollowUp();
        Bundle args = new Bundle();
        args.putString(MainActivity.KEY_FRAGMENT_NAME, "MeFragmentTableFollowUp");
        args.putString(KEY_COMPANY_NAME, companyName);
        args.putInt(KEY_CUSTOMER_CODE, customerCode);
        args.putInt(KEY_SALES_CUSTOMER_CODE,salesCustomerCode);
        args.putInt(KEY_OPPORTUNITY_CODE,opportunityCode);
        fragmentLedgerTable.setArguments(args);
        return fragmentLedgerTable;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentActivity = (MainActivity) getActivity();
        parentActivity.setTitle("Last Follow Ups");
        parentActivity.showSearchView(false);
        parentActivity.isVisibleFab(false);
        parentActivity.isToolbarClickable();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentActivity.getSupportActionBar().show();
        parentActivity.isToolbarClickable();
        rootView = inflater.inflate(R.layout.fragment_last_follow_up, container, false);

        Integer companyMasterId = null, marketingRepCode = null;
        //  initProgressBar();
        try {
            companyMasterId = MeRepoFactory.getLoginRepository(parentActivity.getDbHelper()).getCompanyMasterId();
            //     marketingRepCode = MeRepoFactory.getLoginRepository(parentActivity.getDbHelper()).getEmployeeCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        companyName = getArguments().getString(KEY_COMPANY_NAME);
        Log.i("@Transworld", "Company NameLast Follow Up" + companyName);
        initTextView();
        initCardFollowUp();
        if (parentActivity.isNetworkAvailable())
        new MeTaskFollowUp(companyMasterId, companyName).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        return rootView;
    }

    /* private void initProgressBar() {
         progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar1);
         progressBar.setProgress(0);
         progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#303F9F"), android.graphics.PorterDuff.Mode.MULTIPLY);
     }
 */
    private void initTextView() {
        textCompanyNameFollowup = (TextView) rootView.findViewById(R.id.textCompanyNameFollowup);
        textCompanyNameFollowup.setText(getArguments().getString(KEY_COMPANY_NAME) + "["+getArguments().getInt(KEY_CUSTOMER_CODE) +"]" + "[" +getArguments().getInt(KEY_SALES_CUSTOMER_CODE)+"]" + "["+getArguments().getInt(KEY_OPPORTUNITY_CODE)+"]");
     //   textHeader = (TextView) rootView.findViewById(R.id.textHeader);
       // textHeader.setPaintFlags(textHeader.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    private void initCardFollowUp() {
        //  ada = new MeAdapterRecyclerAppointment(parentActivity);
        recyclerViewFollowUp = (RecyclerView) rootView.findViewById(R.id.my_recycler_view_last_follow_up);
        adapterRecyclerLastFollowUp = new MeAdapterRecyclerLastFollowUp(parentActivity);
        recyclerViewFollowUp.setAdapter(adapterRecyclerLastFollowUp);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewFollowUp.setLayoutManager(layoutManager);
    }

    private class MeRecyclerItemLastFollowUp {
        private String followUpDate;
        private String followUpType;
        private String followUpTime;
        private String mobileNo;
        private String ContactPerson;
        private Integer contactId;
        private Integer theSrNo;
        private String status;
        private String preparation;
        private String nextFollowUpDate;
        private String nextFollowUpTime;

        public String getNextFollowUpTime() {
            return nextFollowUpTime;
        }

        public void setNextFollowUpTime(String nextFollowUpTime) {
            this.nextFollowUpTime = nextFollowUpTime;
        }

        public String getNextFollowUpDate() {
            return nextFollowUpDate;
        }

        public void setNextFollowUpDate(String nextFollowUpDate) {
            this.nextFollowUpDate = nextFollowUpDate;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPreparation() {
            return preparation;
        }

        public void setPreparation(String preparation) {
            this.preparation = preparation;
        }

        public MeRecyclerItemLastFollowUp() {
        }

        public Integer getTheSrNo() {
            return theSrNo;
        }

        public void setTheSrNo(Integer theSrNo) {
            this.theSrNo = theSrNo;
        }

        public String getFollowUpDate() {
            return followUpDate;
        }

        public void setFollowUpDate(String followUpDate) {
            this.followUpDate = followUpDate;
        }

        public String getFollowUpType() {
            return followUpType;
        }

        public void setFollowUpType(String followUpType) {
            this.followUpType = followUpType;
        }

        public String getFollowUpTime() {
            return followUpTime;
        }

        public void setFollowUpTime(String followUpTime) {
            this.followUpTime = followUpTime;
        }

        public String getMobileNo() {
            return mobileNo;
        }

        public void setMobileNo(String mobileNo) {
            this.mobileNo = mobileNo;
        }




        public String getContactPerson() {
            return ContactPerson;
        }

        public void setContactPerson(String contactPerson) {
            ContactPerson = contactPerson;
        }

        public Integer getContactId() {
            return contactId;
        }

        public void setContactId(Integer contactId) {
            this.contactId = contactId;
        }
    }

    private final class MeAdapterRecyclerLastFollowUp extends RecyclerView.Adapter<MeRecyclerViewHolder>
    {
        private Context context;
        private LayoutInflater inflater;

        public MeAdapterRecyclerLastFollowUp(Context context) {
            this.context = context;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public MeRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_cardview_last_follow_up, parent, false);
            MeRecyclerViewHolder holder = new MeRecyclerViewHolder(layoutView);
            return holder;
        }

        @Override
        public void onBindViewHolder(MeRecyclerViewHolder recyclerViewHolders, int i) {
            HashMap<String, Object> mapClickInfo = new HashMap<>();
            mapClickInfo.put("contactId", listFollowUpItems.get(i).getContactId());
            mapClickInfo.put("TheSrno", listFollowUpItems.get(i).getTheSrNo());
            mapClickInfo.put("Time", listFollowUpItems.get(i).getNextFollowUpTime());
            mapClickInfo.put("appointmentDate", listFollowUpItems.get(i).getNextFollowUpDate());
            recyclerViewHolders.getCardViewLastFollowUp().setTag(mapClickInfo);
            mapClickInfo = null;
            recyclerViewHolders.getTextContactPersonFollow().setText(listFollowUpItems.get(i).getContactPerson());

            recyclerViewHolders.getTextFollowupDate().setText(listFollowUpItems.get(i).getFollowUpDate());
            recyclerViewHolders.getTextFollowupTime().setText(listFollowUpItems.get(i).getFollowUpTime());
            recyclerViewHolders.getTextStatus().setText(listFollowUpItems.get(i).getStatus());
            recyclerViewHolders.getTextPreparation().setText(listFollowUpItems.get(i).getPreparation());
            recyclerViewHolders.getTextFollowupType().setText(listFollowUpItems.get(i).getFollowUpType());

             if(i%3==0){
                recyclerViewHolders.textNameLetter.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_card_three));
                //Log.i("@Transworld","Char At " + listFollowUpItems.get(i).getContactPerson().split("' '|\\.")[1]);
               // recyclerViewHolders.textNameLetter.setText(listFollowUpItems.get(i).getContactPerson().split(".")[1]);
            }
            else if (i%2==0){
                recyclerViewHolders.textNameLetter.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_card_four));
                //Log.i("@Transworld","Char At " + listFollowUpItems.get(i).getContactPerson().split(".")[1]);
               // recyclerViewHolders.textNameLetter.setText(listFollowUpItems.get(i).getContactPerson().split(".")[1]);
            }
            else
            {
                recyclerViewHolders.textNameLetter.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_card_five));
              //  Log.i("@Transworld","Char At " + listFollowUpItems.get(i).getContactPerson().split(".")[1]);
              //  recyclerViewHolders.textNameLetter.setText(listFollowUpItems.get(i).getContactPerson().split(".")[1]);
            }


        }

        @Override
        public int getItemCount() {
            return listFollowUpItems.size();
        }
    }

    private final class MeRecyclerViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private CardView cardViewLastFollowUp;
        private TextView textContactPersonFollow;
        private TextView textFollowupDate;
        private TextView textFollowupTime;
        private TextView textStatus;
        private TextView textNameLetter;
        private TextView textFollowupType;

        public TextView getTextFollowupType() {
            return textFollowupType;
        }

        public void setTextFollowupType(TextView textFollowupType) {
            this.textFollowupType = textFollowupType;
        }

        public TextView getTextNameLetter() {
            return textNameLetter;
        }

        public void setTextNameLetter(TextView textNameLetter) {
            this.textNameLetter = textNameLetter;
        }

        public TextView getTextPreparation() {
            return textPreparation;
        }

        public void setTextPreparation(TextView textPreparation) {
            this.textPreparation = textPreparation;
        }

        public TextView getTextStatus() {
            return textStatus;
        }

        public void setTextStatus(TextView textStatus) {
            this.textStatus = textStatus;
        }

        private TextView textPreparation;

        public MeRecyclerViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            cardViewLastFollowUp = (CardView) itemView.findViewById(R.id.card_view_last_followup);
            textNameLetter = (TextView) itemView.findViewById(R.id.textNameLetter);
            textContactPersonFollow = (TextView) itemView.findViewById(R.id.textContactPersonFollow);
            textFollowupDate = (TextView) itemView.findViewById(R.id.textFollowupDate);
            textFollowupTime = (TextView) itemView.findViewById(R.id.textFollowupTime);
            textFollowupType = (TextView) itemView.findViewById(R.id.textFollowupType);
            textStatus = (TextView) itemView.findViewById(R.id.textStatus);
            textPreparation = (TextView) itemView.findViewById(R.id.textPreparation);
            cardViewLastFollowUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HashMap<String, Object> mapClickInfo = (HashMap<String, Object>) cardViewLastFollowUp.getTag();
                    //   Integer contactId = listAppointmentItems.get(v.getId()).getContactId();
                    Log.i("@Transworld", "View Table In Contact Id : " + mapClickInfo);
                    //HashMap<String, Object> mapClickInfo = (HashMap<String, Object>) getArguments().getSerializable(KEY_MAP_CLICK_INFO);
                    Log.i("@Transworld", "Last Follow up MapClickInfo : " + mapClickInfo);
                    Integer theSrno = (Integer) mapClickInfo.get("TheSrno");
                    Integer contactId = (Integer) mapClickInfo.get("contactId");
                    String time = (String) mapClickInfo.get("Time");
                    String appointmentDate = (String) mapClickInfo.get("appointmentDate");
                   String splitDate = appointmentDate.split(",")[1];
                   Log.i("@Transworld", "appointmentDate Date : " + appointmentDate  +"   " +splitDate );
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                    Date date = null;
                    try {
                        date = sdf.parse(splitDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String dateToWeb = sdf.format(date);
                    Integer companyMasterId = null;
                    try {
                        companyMasterId = MeRepoFactory.getLoginRepository(parentActivity.getDbHelper()).getCompanyMasterId();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (parentActivity.isNetworkAvailable())
                    //new MeTaskAppointmentDetails(companyMasterId, theSrno).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    parentActivity.runFragmentTransaction(R.id.frameMainContainer,MeFragmentAppointmentDetails.getInstance(companyName,companyMasterId,theSrno,"Last Follow Up"));
                }
            });
        }

        public View getView() {
            return view;
        }

        public void setView(View view) {
            this.view = view;
        }

        public CardView getCardViewLastFollowUp() {
            return cardViewLastFollowUp;
        }

        public void setCardViewLastFollowUp(CardView cardViewLastFollowUp) {
            this.cardViewLastFollowUp = cardViewLastFollowUp;
        }

        public TextView getTextContactPersonFollow() {
            return textContactPersonFollow;
        }

        public void setTextContactPersonFollow(TextView textContactPersonFollow) {
            this.textContactPersonFollow = textContactPersonFollow;
        }

        public TextView getTextFollowupDate() {
            return textFollowupDate;
        }

        public void setTextFollowupDate(TextView textFollowupDate) {
            this.textFollowupDate = textFollowupDate;
        }

        public TextView getTextFollowupTime() {
            return textFollowupTime;
        }

        public void setTextFollowupTime(TextView textFollowupTime) {
            this.textFollowupTime = textFollowupTime;
        }


    }

    private class MeTaskFollowUp extends AsyncTask<Void, Void, List<Map<String, Object>>> {
        private MeConnectorFollowUp connectorFollowUp;
        private Integer companyMasterId;

        private String companyName;
        private ProgressDialog progressDialog;

        public MeTaskFollowUp(Integer companyMasterId, String companyName) {
            this.companyMasterId = companyMasterId;
            this.companyName = companyName;
            connectorFollowUp = new MeConnectorFollowUp();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listFollowUpItems.clear();
            progressDialog = ProgressDialog.show(parentActivity, "Last Follow Up", "Loading...");
        }

        @Override
        protected List<Map<String, Object>> doInBackground(Void... params)
        {
            List<Map<String, Object>> listFollowUp = new ArrayList<>();
            try {
                Map<String, Object> mapEntity = connectorFollowUp.listOfLastFollowUp(companyMasterId, companyName);
                if (mapEntity != null) {
                    listFollowUp = (List<Map<String, Object>>) mapEntity.get("followup");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listFollowUp;
        }

        @Override
        protected void onPostExecute(List<Map<String, Object>> maps)
        {
            super.onPostExecute(maps);
            progressDialog.dismiss();
            textCompanyNameFollowup.setText(getArguments().getString(KEY_COMPANY_NAME) + "["+getArguments().getInt(KEY_CUSTOMER_CODE) +"]" + "[" +getArguments().getInt(KEY_SALES_CUSTOMER_CODE)+"]" + "["+getArguments().getInt(KEY_OPPORTUNITY_CODE)+"]");
            if (maps != null && maps.size() > 0) {
                for (Map<String, Object> mapItem : maps) {

                    MeRecyclerItemLastFollowUp item = new MeRecyclerItemLastFollowUp();
                    item.setFollowUpDate((String) mapItem.get("followUpDate"));
                    item.setFollowUpType((String) mapItem.get("followUpType"));
                    item.setContactId((Integer) mapItem.get("contactId"));

                    Log.i("@Transworld", "Follow Yp Type" + mapItem.get("followUpType"));
                    item.setTheSrNo((Integer) mapItem.get("TheSrno"));
                    item.setFollowUpTime((String) mapItem.get("followUpTime"));
                    item.setNextFollowUpTime((String) mapItem.get("nextFollowUpTime"));
                    item.setContactPerson((String) mapItem.get("ContactPerson"));
                    item.setNextFollowUpDate((String) mapItem.get("appointmentDate"));

                    Log.i("@Transworld","nextFollowUpDate"+mapItem.get("appointmentDate"));
                    item.setStatus((String) mapItem.get("status"));
                    item.setPreparation((String) mapItem.get("preparation"));
                    item.setFollowUpType((String) mapItem.get("followUpType"));
                    item.setMobileNo((String) mapItem.get("mobileno"));
                    Log.i("@Transworld", "Just Inserted Item -" + item.toString());
                    listFollowUpItems.add(item);
                }
                adapterRecyclerLastFollowUp.notifyDataSetChanged();
            } else {
                if(getView()!=null)
                parentActivity.snack(rootView, "No Follow Up for this customers:(");
            }

        }
    }

    private final class MeConnectorFollowUp implements MeConnectable
    {
        private String jsonMap;

        public Map<String, Object> listOfLastFollowUp(Integer companyMasterId, String companyName) throws Exception {
            Map<String, Object> mapEntity = new HashMap<>();
            jsonMap = HttpRequest.get(MeIUrl.URL_LAST_FOLLOW_UP + companyMasterId + "/" + companyName.replace(" ", "%20"))
                    .body();
            Log.i("@Transworld", MeIUrl.URL_LAST_FOLLOW_UP + companyMasterId + "/" + companyName.replace(" ", "%20"));
            Log.i("@Transworld", "Last Follow Up" + jsonMap);
            mapEntity = JsonMan.parseAnything(jsonMap, new TypeReference<Map<String, Object>>() {
            });
            return mapEntity;
        }
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
        private Integer companyMasterId, contactId, theSrno;
        private String date, time;
        private ProgressDialog progressDialog;
        private MeConnectorAppointmentDetails meConnectorAppointmentDetails;

        public MeTaskAppointmentDetails(Integer companyMasterId, Integer theSrno) {
            this.companyMasterId = companyMasterId;
            this.theSrno = theSrno;
            this.date = date;
            this.time = time;
            meConnectorAppointmentDetails = new MeConnectorAppointmentDetails();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(parentActivity, "Last Follow Up Info.", "Loading...");
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
                    FragmentManager fragmentManager = parentActivity.getSupportFragmentManager();
                    MeDialogFragment dialogFragment = MeDialogFragment.getInstanseWithContact(mapFollowUpData);
                    dialogFragment.show(fragmentManager, MeDialogFragment.TAG_APPOINTMENT_LIST);
                }
            } else {
                if(getView()!=null)
                parentActivity.snack(rootView, "No Follow up Details available:(");
            }
        }
    }
}
