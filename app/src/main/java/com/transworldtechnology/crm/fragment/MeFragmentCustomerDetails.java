package com.transworldtechnology.crm.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.transworldtechnology.crm.R;
import com.transworldtechnology.crm.activity.MainActivity;

import java.util.ArrayList;

/**
 * Created by root on 25/2/16.
 */
public class MeFragmentCustomerDetails extends Fragment {
    public static final String KEY_COMPANY_NAME = "company_name";
    public static final String KEY_CUSTOMER_CODE = "customer_code";
    public static final String KEY_SALES_CUSTOMER_CODE = "cust_code";
    public static final String KEY_OPPORTUNITY_CODE = "opp_code";
    private MainActivity parentActivity;
    private View rootView;
    private TextView textCustomerNameDetails;
    private GridLayoutManager gridLayoutManager;
    private RecyclerView recyclerGridDashBoard;
    private ArrayList arrayGridDetails = new ArrayList();
    private MeAdapterRecyclerDetails adapterRecyclerDetails;
    private MeClick meClick;
    private Integer companyMasterId, marketingRepCode;
    private String companyName;
    private Integer salesCustomerCode,customerCode,opportunityCode;

    public static MeFragmentCustomerDetails getInstance(String companyName,Integer customerCode ,Integer salesCustomerCode,Integer opportunityCode) {
        MeFragmentCustomerDetails customerDetails = new MeFragmentCustomerDetails();
        Bundle args = new Bundle();
        args.putString(MainActivity.KEY_FRAGMENT_NAME, "MeFragmentCustomerDetails");
        args.putString(KEY_COMPANY_NAME, companyName);
        args.putInt(KEY_CUSTOMER_CODE, customerCode);
        args.putInt(KEY_SALES_CUSTOMER_CODE, salesCustomerCode);
        args.putInt(KEY_OPPORTUNITY_CODE, opportunityCode);
        Log.i("@Transworld", "Customer Code in instance" + salesCustomerCode);
        customerDetails.setArguments(args);
        return customerDetails;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arrayGridDetails = getAllDetails();
        meClick = new MeClick();
        parentActivity = (MainActivity) getActivity();
        parentActivity.setTitle("SmartSuite");
        parentActivity.showSearchView(false);
        parentActivity.isToolbarClickable();
        //parentActivity.showSearchView(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentActivity.getSupportActionBar().show();
        //  parentActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //   parentActivity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        parentActivity.showSearchView(false);
        parentActivity.clearListView();
        parentActivity.isVisibleFab(true);
        parentActivity.isToolbarClickable();
        rootView = inflater.inflate(R.layout.fragment_summary_list, container, false);
        parentActivity.setTitle("SmartSuite");
        initTextView();
        initGridCustomerDetails();
        companyName = getArguments().getString(KEY_COMPANY_NAME);
        customerCode = getArguments().getInt(KEY_CUSTOMER_CODE);
        opportunityCode = getArguments().getInt(KEY_OPPORTUNITY_CODE);
        salesCustomerCode = getArguments().getInt(KEY_SALES_CUSTOMER_CODE);
        return rootView;
    }

    private void initGridCustomerDetails() {
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerGridDashBoard = (RecyclerView) rootView.findViewById(R.id.my_recycler_view_customer_details);
        recyclerGridDashBoard.setHasFixedSize(true);
        recyclerGridDashBoard.setLayoutManager(gridLayoutManager);
        adapterRecyclerDetails = new MeAdapterRecyclerDetails(getContext(), arrayGridDetails);
        recyclerGridDashBoard.setAdapter(adapterRecyclerDetails);
    }

    private final ArrayList getAllDetails() {
        ArrayList<String> summaryList = new ArrayList();
        summaryList.add("Last 3 Follow Up");
        summaryList.add("Ledger A/C Details");
        summaryList.add("Last 3 Invoice");
        summaryList.add("Last 10 Transaction");
        //   summaryList.add("Payment Receipts");
        return summaryList;
    }

    private void initTextView() {
        textCustomerNameDetails = (TextView) rootView.findViewById(R.id.textCustomerNameDetails);
        textCustomerNameDetails.setText(getArguments().getString(KEY_COMPANY_NAME) + "["+getArguments().getInt(KEY_CUSTOMER_CODE) +"]" + "[" +getArguments().getInt(KEY_SALES_CUSTOMER_CODE)+"]" + "["+getArguments().getInt(KEY_OPPORTUNITY_CODE)+"]");
    }

    private final class MeRecyclerItemSummary {
        private Integer imgId;
        private String companyName;

        public MeRecyclerItemSummary(Integer imgId, String companyName) {
            this.imgId = imgId;
            this.companyName = companyName;
        }

        public MeRecyclerItemSummary() {
        }

        public Integer getImgId() {
            return imgId;
        }

        public void setImgId(Integer imgId) {
            this.imgId = imgId;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }
    }

    private final class MeAdapterRecyclerDetails extends RecyclerView.Adapter<MeRecyclerViewHolder> {
        private ArrayList<String> summaryList;
        private Context context;
        private LayoutInflater inflater;

        public MeAdapterRecyclerDetails(Context context, ArrayList<String> summaryList) {
            this.context = context;
            this.summaryList = summaryList;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public MeRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_cardview_customer_details, parent, false);
            MeRecyclerViewHolder holder = new MeRecyclerViewHolder(layoutView);
            return holder;
        }

        @Override
        public void onBindViewHolder(MeRecyclerViewHolder recyclerViewHolders, int i) {
            recyclerViewHolders.textLastSummary.setText(summaryList.get(i).toString());
            if (summaryList.get(i).toString().equalsIgnoreCase("Ledger A/C Details"))
                recyclerViewHolders.imgSummaryLogo.setImageResource(R.drawable.layer_shape_ledger_details);
            if (summaryList.get(i).toString().equalsIgnoreCase("Last 3 Follow Up"))
                recyclerViewHolders.imgSummaryLogo.setImageResource(R.drawable.layer_shape_followup);
            if (summaryList.get(i).toString().equalsIgnoreCase("Last 3 Invoice"))
                recyclerViewHolders.imgSummaryLogo.setImageResource(R.drawable.layer_shape_ledger_details);
            if (summaryList.get(i).toString().equalsIgnoreCase("Last 10 Transaction"))
                recyclerViewHolders.imgSummaryLogo.setImageResource(R.drawable.layer_shape_ledger_details);
         /*   if (summaryList.get(i).toString().equalsIgnoreCase("Payment Receipts"))
                recyclerViewHolders.imgSummaryLogo.setImageResource(R.drawable.layer_shape_ledger_details);*/
        }

        @Override
        public int getItemCount() {
            return summaryList.size();
        }
    }

    private final class MeRecyclerViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private TextView textLastSummary;
        private ImageView imgSummaryLogo;

        public MeRecyclerViewHolder(View itemView) {
            super(itemView);
            textLastSummary = (TextView) itemView.findViewById(R.id.textLastSummary);
            imgSummaryLogo = (ImageView) itemView.findViewById(R.id.imgSummaryLogo);
            view = itemView;
            view.setOnClickListener(meClick);
        }
    }

    private class MeClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String fragmentName;
            TextView textView = (TextView) v.findViewById(R.id.textLastSummary);
            if (textView.getText().toString().equalsIgnoreCase("Last 3 Invoice")) {
                fragmentName = "invoice";
                parentActivity.runFragmentTransaction(R.id.frameMainContainer, MeFragmentTableInvoice.getInstance(companyName,customerCode, salesCustomerCode,opportunityCode));
                //      parentActivity.runFragmentTransaction(R.id.frameMainContainer, MeFragmentSelectDate.getInstance(companyName, fragmentName));
            }
            if (textView.getText().toString().equalsIgnoreCase("Ledger A/C Details")) {
                fragmentName = "ledger";
                parentActivity.runFragmentTransaction(R.id.frameMainContainer, MeFragmentSelectDate.newInstance(companyName, customerCode, salesCustomerCode,opportunityCode));
            }
            if (textView.getText().toString().equalsIgnoreCase("Last 3 Follow Up")) {
                parentActivity.runFragmentTransaction(R.id.frameMainContainer, MeFragmentTableFollowUp.getInstance(companyName,customerCode, salesCustomerCode,opportunityCode));
            }
            if (textView.getText().toString().equalsIgnoreCase("Last 10 Transaction")) {
                Log.i("@Transworld", "Customer Code in if" + salesCustomerCode);
                parentActivity.runFragmentTransaction(R.id.frameMainContainer, MeFragmentTableTransaction.getInstance(companyName,customerCode, salesCustomerCode,opportunityCode));
            }
            if (textView.getText().toString().equalsIgnoreCase("Payment Receipts")) {
                parentActivity.snack(rootView, "In Progress");
            }
        }
    }
}
