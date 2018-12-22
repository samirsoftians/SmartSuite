package com.transworldtechnology.crm.fragment;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.kevinsawicki.http.HttpRequest;
import com.transworldtechnology.crm.R;
import com.transworldtechnology.crm.activity.MainActivity;
import com.transworldtechnology.crm.database.MeRepoFactory;
import com.transworldtechnology.crm.web.JsonMan;
import com.transworldtechnology.crm.web.MeConnectable;
import com.transworldtechnology.crm.web.MeIUrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by root on 26/2/16.
 */
public class MeFragmentTableTransaction extends Fragment {
    public static final String KEY_CUSTOMER_CODE = "cust_code";
    public static final String KEY_COMPANY_NAME = "company_name";
    public static final String KEY_SALES_CUSTOMER_CODE = "sales_cust_code";
    public static final String KEY_OPPORTUNITY_CODE = "opp_code";
    private MainActivity parentActivity;
    private View rootView;
    private Button btnGo;
    private TextView textDateValueTr, textIdValueTr, textCreditValueTr, textDebitValueTr, textBalanceValueTr, textCustomerNameTable;
    private TableLayout tableLayoutTransaction;
    private String companyName;
    private Integer customerCode,opportunityCode,salesCustomerCode;

    public static MeFragmentTableTransaction getInstance(String companyName, Integer customerCode, Integer salesCustomerCode, Integer opportunityCode) {
        MeFragmentTableTransaction fragmentLedgerTable = new MeFragmentTableTransaction();
        Bundle args = new Bundle();
        args.putString(MainActivity.KEY_FRAGMENT_NAME, "MeFragmentTableTransaction");
        args.putInt(KEY_CUSTOMER_CODE, customerCode);
        args.putInt(KEY_SALES_CUSTOMER_CODE,salesCustomerCode);
        args.putInt(KEY_OPPORTUNITY_CODE,opportunityCode);
        args.putString(KEY_COMPANY_NAME, companyName);
        fragmentLedgerTable.setArguments(args);
        return fragmentLedgerTable;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentActivity = (MainActivity) getActivity();
        parentActivity.setTitle("Last 10 Transaction");
        parentActivity.showSearchView(false);
        parentActivity.isVisibleFab(false);
        parentActivity.isToolbarClickable();
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        parentActivity.getSupportActionBar().show();
        parentActivity.isToolbarClickable();
        rootView = inflater.inflate(R.layout.custom_table_transaction, container, false);
        customerCode = getArguments().getInt(KEY_CUSTOMER_CODE);
        opportunityCode = getArguments().getInt(KEY_OPPORTUNITY_CODE);
        salesCustomerCode = getArguments().getInt(KEY_SALES_CUSTOMER_CODE);
        companyName = getArguments().getString(KEY_COMPANY_NAME);
        Integer companyMasterId = null;
        try {
            companyMasterId = MeRepoFactory.getLoginRepository(parentActivity.getDbHelper()).getCompanyMasterId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        initTableLayout();
        initTextView();
        if (parentActivity.isNetworkAvailable()) {
            if(!salesCustomerCode.equals("0")){
                new MeTaskTransaction(companyMasterId, salesCustomerCode).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
            else {
                new MeTaskTransaction(companyMasterId, customerCode).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }

        }
        return rootView;
    }

    private void initTextView() {
        textCustomerNameTable = (TextView) rootView.findViewById(R.id.textCustomerNameTable);
        textCustomerNameTable.setText(companyName+"["+customerCode+"]"+"["+salesCustomerCode+"]"+"["+opportunityCode+"]");
    }

    private void initTableLayout() {
        tableLayoutTransaction = (TableLayout) rootView.findViewById(R.id.tableLayoutTransaction);
    }

    private void addTableData(List<Map<String, Object>> maps) {
        Log.i(MainActivity.TAG,"Map Size Transaction "+maps.size());
        if (maps.size() > 0) {
            for (Map<String, Object> mapItem : maps) {
                textCreditValueTr = new TextView(parentActivity);
                textDateValueTr = new TextView(parentActivity);
                textDebitValueTr = new TextView(parentActivity);
                textBalanceValueTr = new TextView(parentActivity);
                textIdValueTr = new TextView(parentActivity);
                TableRow tableRowValue = new TableRow(parentActivity);
                tableRowValue.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                TableLayout.LayoutParams tableRowParams =
                        new TableLayout.LayoutParams
                                (TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
                textDateValueTr.setText("" + mapItem.get("transactionDate"));

                textIdValueTr.setText("" + mapItem.get("transactionId"));
                textCreditValueTr.setText(String.format("%.2f",mapItem.get("creditAmount")));
                textDebitValueTr.setText(String.format("%.2f",mapItem.get("debitAmount")));
                textBalanceValueTr.setText(String.format("%.2f",mapItem.get("balance")));

                textDateValueTr.setGravity(Gravity.RIGHT);
                textIdValueTr.setGravity(Gravity.LEFT);
                textCreditValueTr.setGravity(Gravity.RIGHT);
                textDebitValueTr.setGravity(Gravity.RIGHT);
                textBalanceValueTr.setGravity(Gravity.RIGHT);

                textDateValueTr.setPadding(10, 0, 13, 0);
                textIdValueTr.setPadding(10, 0, 10, 0);
                textCreditValueTr.setPadding(10, 0, 10, 0);
                textDebitValueTr.setPadding(10, 0, 10, 0);
                textBalanceValueTr.setPadding(10, 0, 10, 0);

                textDateValueTr.setTextColor(ContextCompat.getColor(parentActivity, R.color.black));
                textDateValueTr.setTypeface(Typeface.DEFAULT_BOLD);
                textIdValueTr.setTextColor(ContextCompat.getColor(parentActivity, R.color.black));
                textCreditValueTr.setTextColor(ContextCompat.getColor(parentActivity, R.color.black));
                textDebitValueTr.setTextColor(ContextCompat.getColor(parentActivity, R.color.black));
                textBalanceValueTr.setTextColor(ContextCompat.getColor(parentActivity, R.color.black));
                tableRowValue.setPadding(10, 10, 10, 10);
                tableRowValue.addView(textDateValueTr);
                tableRowValue.addView(textIdValueTr);
                tableRowValue.addView(textCreditValueTr);
                tableRowValue.addView(textDebitValueTr);
                tableRowValue.addView(textBalanceValueTr);
                tableRowParams.setMargins(0, 0, 20, 0);
                tableRowValue.setLayoutParams(tableRowParams);
                tableLayoutTransaction.addView(tableRowValue, new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.WRAP_CONTENT,
                        TableLayout.LayoutParams.MATCH_PARENT));
            }
        } else {
            if(getView()!=null)
            parentActivity.snack(rootView, "No Transaction for this customers:(");
        }
    }

    private class MeTaskTransaction extends AsyncTask<Void, Void, List<Map<String, Object>>> {
        ProgressDialog progressDialog;
        private MeConnectorTransaction connectorTransaction;
        private Integer companyMasterId;
        private Integer customerCode;

        public MeTaskTransaction(Integer companyMasterId, Integer customerCode) {
            this.companyMasterId = companyMasterId;
            this.customerCode = customerCode;
            connectorTransaction = new MeConnectorTransaction();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(parentActivity, "Transaction Info.", "Loading...");
        }

        @Override
        protected List<Map<String, Object>> doInBackground(Void... params) {
            List<Map<String, Object>> listTransaction = new ArrayList<>();
            try {
                Map<String, Object> mapEntity = connectorTransaction.getLastTransaction(companyMasterId, customerCode);
                if (mapEntity != null) {
                    listTransaction = (List<Map<String, Object>>) mapEntity.get("transaction");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listTransaction;
        }

        @Override
        protected void onPostExecute(List<Map<String, Object>> maps) {
            super.onPostExecute(maps);
            progressDialog.dismiss();
            addTableData(maps);
        }
    }

    private final class MeConnectorTransaction implements MeConnectable {
        private String jsonMap;

        public Map<String, Object> getLastTransaction(Integer companyMasterId, Integer customerCode) throws Exception {
            Map<String, Object> mapEntity = new HashMap<>();
            jsonMap = HttpRequest.get(MeIUrl.URL_LAST_TRANSACTION + companyMasterId + "/" + customerCode)
                    .body();
            Log.i("@Transworld", MeIUrl.URL_LAST_TRANSACTION + companyMasterId + "/" + customerCode);
            Log.i("@Transworld", "Last Transaction" + jsonMap);
            mapEntity = JsonMan.parseAnything(jsonMap, new TypeReference<Map<String, Object>>() {
            });
            return mapEntity;
        }
    }
}
