package com.transworldtechnology.crm.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.transworldtechnology.crm.pdf.MeItextPdf;
import com.transworldtechnology.crm.web.JsonMan;
import com.transworldtechnology.crm.web.MeConnectable;
import com.transworldtechnology.crm.web.MeIUrl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;



/**
 * Created by root on 25/2/16.
 */
public class  MeFragmentLedgerTable extends Fragment {
    public static final String KEY_CUSTOMER_CODE = "cust_code";
    public static final String KEY_SALES_CUSTOMER_CODE = "sales_cust_code";
    public static final String KEY_COMPANY_NAME = "company_name";
    public static final String KEY_START_DATE = "start_date";
    public static final String KEY_END_DATE = "end_date";
    private MainActivity parentActivity;
    private Toolbar toolBar;
    private MeItextPdf meItextPdf;
    private TableLayout tableLayoutLedger;
    private View rootView;
    private Calendar calendar;
    private TextView textCompanyNameLedger, textDuration, textHeader;
    private TextView textSrNo;
    private TextView textTrDate, textTrId, textDebit, textCredit, textBalance, textDesc, textDescId, textSign, textTotal;
    private TextView textSrNoValue, textTrDateValue, textTrIdValue, textDebitValue, textCreditValue, textBalanceOutStandingValue, textDebitAdd, textCreditAdd, textBalanceOutStandingAdd;
    private TextView textDescValue, textDescIdValue, textSignValue;
    private Button btnGo;
    private Integer companyMasterId, customerCode, salesCustomerCode;
    private String toDate, fromDate, date1, date2;
    private String companyName;
    private List<Map<String, Object>> mapLedgerAccountDetails = new ArrayList<>();

    private File myFileLedger;

    public static MeFragmentLedgerTable getInstance(String companyName, Integer customerCode,Integer salesCustomerCode, String startDate, String endDate) {
        MeFragmentLedgerTable fragmentLedgerTable = new MeFragmentLedgerTable();
        Bundle args = new Bundle();
        args.putString(MainActivity.KEY_FRAGMENT_NAME, "MeFragmentLedgerTable");
        Log.i("@Transworld", "companyName -" + companyName);
        args.putString(KEY_COMPANY_NAME, companyName);
        args.putInt(KEY_CUSTOMER_CODE, customerCode);
        args.putInt(KEY_SALES_CUSTOMER_CODE,salesCustomerCode);
        args.putString(KEY_START_DATE, startDate);
        args.putString(KEY_END_DATE, endDate);
        Log.i("@Transworld", "Customer Code in instance" + customerCode);
        Log.i("@Transworld", "startDate in MeFragmentLedgerTable -" + startDate);
        Log.i("@Transworld", "endDate in MeFragmentLedgerTable -" + endDate);
        fragmentLedgerTable.setArguments(args);
        return fragmentLedgerTable;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentActivity = (MainActivity) getActivity();
        parentActivity.setTitle("Ledger Account Details");
        parentActivity.showSearchView(false);
        parentActivity.isVisibleFab(false);
        calendar = Calendar.getInstance();
        parentActivity.isToolbarClickable();
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main2, menu);
     //   Log.i("@Transworld", "mapLedgerAccountDetails- " + mapLedgerAccountDetails);

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        try {
            Log.i("@Transworld", "mapLedgerAccountDetails in onOptionsItemSelected- " + mapLedgerAccountDetails);

            if(mapLedgerAccountDetails!=null) {
                if (item.getItemId() == R.id.action_send_pdf) {
                    attachPdfToMail(myFileLedger);
                }
            }
            else {
                if(getView()!=null)
                parentActivity.snack(rootView, "No Ledger Records Found :(");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentActivity.getSupportActionBar().show();
        parentActivity.isToolbarClickable();
        rootView = inflater.inflate(R.layout.custom_table_ledger_details, container, false);
        initTextViews();
        initTableLayout();

        try {
            companyName = getArguments().getString(KEY_COMPANY_NAME);
            companyMasterId = MeRepoFactory.getLoginRepository(parentActivity.getDbHelper()).getCompanyMasterId();
            customerCode = getArguments().getInt(KEY_CUSTOMER_CODE);
            salesCustomerCode = getArguments().getInt(KEY_SALES_CUSTOMER_CODE);
            toDate = getArguments().getString(KEY_START_DATE);
            fromDate = getArguments().getString(KEY_END_DATE);


            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date d1 = formatter.parse(toDate);
            Date d2 = formatter.parse(fromDate);

            formatter = new SimpleDateFormat("dd-MMM-yyyy");
            date1 = formatter.format(d1);
            date2 = formatter.format(d2);
            Log.i("@Transworld", "date1 after" + date1);
            Log.i("@Transworld", "date2 after" + date2);

            Log.i("@Transworld", "toDate -" + toDate);
            Log.i("@Transworld", "fromDate -" + fromDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String headerText = textHeader.getText().toString();
        String text = headerText + "\n" + companyName + "\n" + "From " + date1 + " to " + date2;
        textCompanyNameLedger.setText(companyName);
        textDuration.setText("From " + date1 + " to " + date2);

        meItextPdf = new MeItextPdf(text);

        if (parentActivity.isNetworkAvailable()) {
            if(!salesCustomerCode.equals("0")){
                new MeTaskLedgerAccountDetails(companyMasterId, salesCustomerCode,toDate,fromDate).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
            else {
                new MeTaskLedgerAccountDetails(companyMasterId, customerCode,toDate,fromDate).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }

        }

        /*if (parentActivity.isNetworkAvailable()) {

            new MeTaskLedgerAccountDetails(companyMasterId, customerCode, toDate, fromDate).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }*/
        return rootView;
    }

    private void initTableLayout() {
        tableLayoutLedger = (TableLayout) rootView.findViewById(R.id.tableLayoutLedger);
    }


    private void initTextViews() {
        textCompanyNameLedger = (TextView) rootView.findViewById(R.id.textCompanyNameLedger);
        //textCompanyNameLedger.setText(companyName);
        textHeader = (TextView) rootView.findViewById(R.id.textHeader);
        textHeader.setPaintFlags(textHeader.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        textDuration = (TextView) rootView.findViewById(R.id.textDuration);
        //textDuration.setText("From " + date1 + " to " + date2);
        textSrNo = (TextView) rootView.findViewById(R.id.textSrNo);
        textTrDate = (TextView) rootView.findViewById(R.id.textTrDate);
        textTrId = (TextView) rootView.findViewById(R.id.textTrId);
        textDebit = (TextView) rootView.findViewById(R.id.textDebit);
        textCredit = (TextView) rootView.findViewById(R.id.textCredit);
        textBalance = (TextView) rootView.findViewById(R.id.textBalance);
        textDesc = (TextView) rootView.findViewById(R.id.textDesc);
        textDescId = (TextView) rootView.findViewById(R.id.textDescId);
        textSign = (TextView) rootView.findViewById(R.id.textSign);

    }

    private void attachPdfToMail(File myFileLedger) {
        Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:")); // it's not ACTION_SEND
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(myFileLedger));
        intent .putExtra(Intent.EXTRA_SUBJECT, "Ledger_Statement");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
        startActivity(intent);
    }

    private void getLedgerRecords(List<Map<String, Object>> maps) {
        TableRow tableRowValue = null, tableRowTotal;
        TableLayout.LayoutParams tableRowParams = null, tableRowParamsTotal;
        Integer index = 1;
        Double creditAmount = null, debitAmount, balance = 0.0, addOfCredit = 0.0, addOfDebit = 0.0, addOfBalance = 0.0;

        if (maps.size() > 0) {
            for (Map<String, Object> map : maps) {
                textSrNoValue = new TextView(parentActivity);
                textTrDateValue = new TextView(parentActivity);
                textTrIdValue = new TextView(parentActivity);
                textDebitValue = new TextView(parentActivity);
                textCreditValue = new TextView(parentActivity);
                textBalanceOutStandingValue = new TextView(parentActivity);
                textDescValue = new TextView(parentActivity);
                textDescIdValue = new TextView(parentActivity);
                textSignValue = new TextView(parentActivity);
                tableRowValue = new TableRow(parentActivity);
                tableRowValue.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                tableRowParams =
                        new TableLayout.LayoutParams
                                (TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
                textSrNoValue.setText("" + index);
                textTrDateValue.setText("" + map.get("transactionDate"));
                textTrIdValue.setText("" + map.get("transactionId"));
                debitAmount = (Double) map.get("debitAmount");
                creditAmount = (Double) map.get("creditAmount");
                if (debitAmount != null ) {
                    addOfDebit = addOfDebit + debitAmount + (creditAmount < 0 ? creditAmount : 0.00);
                }
                textDebitValue.setText(String.format("%.2f",map.get("debitAmount")));

                if (creditAmount != null && creditAmount >0) {
                    addOfCredit = addOfCredit + creditAmount;
                }
                textCreditValue.setText(String.format("%.2f", map.get("creditAmount")));
                balance = (Double) map.get("balance");
                if (balance != null) {
                    addOfBalance = addOfBalance + balance;
                }
                textBalanceOutStandingValue.setText(String.format("%.2f", map.get("balance")));

                textDescValue.setText("" + map.get("description"));
                textDescIdValue.setText("" + map.get("descriptionId"));
                textSignValue.setText("" + map.get("sign"));

                textSrNoValue.setGravity(Gravity.RIGHT);
                textTrDateValue.setGravity(Gravity.RIGHT);
                textTrIdValue.setGravity(Gravity.LEFT);
                textDebitValue.setGravity(Gravity.RIGHT);
                textCreditValue.setGravity(Gravity.RIGHT);
                textBalanceOutStandingValue.setGravity(Gravity.RIGHT);
                textDescValue.setGravity(Gravity.LEFT);
                textDescIdValue.setGravity(Gravity.LEFT);
                textSignValue.setGravity(Gravity.LEFT);

                textSrNoValue.setPadding(10, 0, 10, 0);
                textTrDateValue.setPadding(10, 0, 10, 0);
                textTrIdValue.setPadding(10, 0, 10, 0);
                textDebitValue.setPadding(10, 0, 10, 0);
                textCreditValue.setPadding(10, 0, 10, 0);
                textBalanceOutStandingValue.setPadding(10, 0, 10, 0);
                textDescValue.setPadding(10, 0, 10, 0);
                textDescIdValue.setPadding(10, 0, 10, 0);
                textSignValue.setPadding(10, 0, 10, 0);





                textSrNoValue.setTextColor(ContextCompat.getColor(parentActivity, R.color.black));
                textSrNoValue.setTypeface(Typeface.DEFAULT_BOLD);
                textTrDateValue.setTextColor(ContextCompat.getColor(parentActivity, R.color.black));
                textTrIdValue.setTextColor(ContextCompat.getColor(parentActivity, R.color.black));
                textDebitValue.setTextColor(ContextCompat.getColor(parentActivity, R.color.black));
                textCreditValue.setTextColor(ContextCompat.getColor(parentActivity, R.color.black));
                textBalanceOutStandingValue.setTextColor(ContextCompat.getColor(parentActivity, R.color.black));
                textDescValue.setTextColor(ContextCompat.getColor(parentActivity, R.color.black));
                textDescIdValue.setTextColor(ContextCompat.getColor(parentActivity, R.color.black));
                textSignValue.setTextColor(ContextCompat.getColor(parentActivity, R.color.black));
                tableRowValue.setPadding(15, 10, 20, 10);
                tableRowValue.addView(textSrNoValue);
                tableRowValue.addView(textTrDateValue);
                tableRowValue.addView(textTrIdValue);
                tableRowValue.addView(textDebitValue);
                tableRowValue.addView(textCreditValue);
                tableRowValue.addView(textBalanceOutStandingValue);
                tableRowValue.addView(textDescValue);
                tableRowValue.addView(textDescIdValue);
                tableRowValue.addView(textSignValue);
                tableRowParams.setMargins(0, 0, 15, 0);
                tableRowValue.setLayoutParams(tableRowParams);
                tableLayoutLedger.addView(tableRowValue, new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.WRAP_CONTENT,
                        TableLayout.LayoutParams.MATCH_PARENT));
                index++;
            }

            if(maps.size()>1) {
                tableRowTotal = new TableRow(parentActivity);
                tableRowTotal.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT));
                tableRowParamsTotal =
                        new TableLayout.LayoutParams
                                (TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
                TableRow.LayoutParams params = (TableRow.LayoutParams) tableRowTotal.getLayoutParams();
                params.span = 3;
                textTotal = new TextView(parentActivity);
                textDebitAdd = new TextView(parentActivity);
                textCreditAdd = new TextView(parentActivity);
                textBalanceOutStandingAdd = new TextView(parentActivity);
                textTotal.setText("Total:");
                textTotal.setTextSize(15);
                textDebitAdd.setText(String.format("%.2f", addOfDebit));
                textDebitAdd.setPadding(10, 0, 15, 0);
                textCreditAdd.setText(String.format("%.2f", addOfCredit));
                if (maps.size() > 0) {
                    for (Map<String, Object> map : maps) {
                        textBalanceOutStandingAdd.setText(String.format("%.2f", map.get("balance")));
                    }
                }
                //  textBalanceOutStandingAdd.setText(String.format("%.2f", addOfBalance));
                textDebitAdd.setGravity(Gravity.RIGHT);
                textCreditAdd.setGravity(Gravity.RIGHT);
                textBalanceOutStandingAdd.setGravity(Gravity.RIGHT);

                textDebitAdd.setPadding(10, 0, 10, 0);
                textCreditAdd.setPadding(10, 0, 10, 0);
                textBalanceOutStandingAdd.setPadding(10, 0, 10, 0);

                textTotal.setTextColor(ContextCompat.getColor(parentActivity, R.color.black));
                textDebitAdd.setTextColor(ContextCompat.getColor(parentActivity, R.color.black));
                textCreditAdd.setTextColor(ContextCompat.getColor(parentActivity, R.color.black));
                textBalanceOutStandingAdd.setTextColor(ContextCompat.getColor(parentActivity, R.color.black));
                tableRowTotal.setPadding(15, 10, 15, 10);
                textTotal.setLayoutParams(params);
                tableRowTotal.addView(textTotal);
                tableRowTotal.addView(textDebitAdd);
                tableRowTotal.addView(textCreditAdd);
                tableRowTotal.addView(textBalanceOutStandingAdd);
                tableLayoutLedger.addView(tableRowTotal, new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.WRAP_CONTENT,
                        TableLayout.LayoutParams.MATCH_PARENT));
            }
        } else {
            if(getView()!=null)
            parentActivity.snack(rootView, "No Ledger Details for this customers:(");
        }
    }

    private final class MeConnectorLedgerDetails implements MeConnectable {
        private String jsonMap;

        public MeConnectorLedgerDetails() {
        }

        public Map<String, Object> listOfLedgerAccountDetails(Integer companyMasterId, Integer customerCode, String toDate, String fromDate) throws Exception {
            jsonMap = HttpRequest.get(MeIUrl.URL_LEDGER_STATEMENT + companyMasterId + "/" + customerCode + "/" + toDate + "/" + fromDate)
                    .body();
            Log.i("@Transworld", MeIUrl.URL_LEDGER_STATEMENT + companyMasterId + "/" + customerCode + "/" + toDate + "/" + fromDate);
            Log.i("@Transworld", "urlOfLedger - " + jsonMap);
            Map<String, Object> mapEntity = JsonMan.parseAnything(jsonMap, new TypeReference<Map<String, Object>>() {
            });
            Log.i("@Transworld", "Map Entity -" + mapEntity);
            return mapEntity;
        }
    }

    private class MeTaskLedgerAccountDetails extends AsyncTask<Void, Void, List<Map<String, Object>>> {
        ProgressDialog progressDialog;
        Integer companyMasterId, customerCode;
        String toDate, fromDate;
        private MeConnectorLedgerDetails meConnectorLedgerDetails;

        public MeTaskLedgerAccountDetails(Integer companyMasterId, Integer customerCode, String toDate, String fromDate) {
            this.companyMasterId = companyMasterId;
            this.customerCode = customerCode;
            this.toDate = toDate;
            this.fromDate = fromDate;
            meConnectorLedgerDetails = new MeConnectorLedgerDetails();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(parentActivity, "Ledger Details", "Loading...");

        }

        @Override
        protected List<Map<String, Object>> doInBackground(Void... params) {
            Map<String, Object> mapEntity = null;
            try {
                mapEntity = meConnectorLedgerDetails.listOfLedgerAccountDetails(companyMasterId, customerCode, toDate, fromDate);
                Log.i("@Transworld","mapEntity-"+mapEntity);
                if (mapEntity != null) {
                    mapLedgerAccountDetails = (List<Map<String, Object>>) mapEntity.get("transaction");
                    Log.i("@Transworld", "mapLedgerAccountDetails in doInBackground-" + mapLedgerAccountDetails);
                    if(mapLedgerAccountDetails!=null)
                        myFileLedger = meItextPdf.initConfigLedger(mapLedgerAccountDetails);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return mapLedgerAccountDetails;
        }

        @Override
        protected void onPostExecute(List<Map<String, Object>> maps) {
            super.onPostExecute(maps);
            progressDialog.dismiss();
            if (maps != null)
                getLedgerRecords(mapLedgerAccountDetails);
            textCompanyNameLedger.setText(companyName);
            textDuration.setText("From " + date1 + " to " + date2);

        }
    }


}
