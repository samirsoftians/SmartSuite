package com.transworldtechnology.crm.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by root on 7/3/16.
 */
public class MeFragmentTableInvoice extends Fragment {
    public static final String KEY_COMPANY_NAME = "company_name";
    public static final String KEY_CUSTOMER_CODE = "cust_code";
    public static final String KEY_SALES_CUSTOMER_CODE = "sales_cust_code";
    public static final String KEY_OPPORTUNITY_CODE = "opp_code";
    public static final String KEY_START_DATE = "start";
    public static final String KEY_END_DATE = "end";

    private MainActivity parentActivity;
    private MeItextPdf meItextPdf;
    private View rootView;
    private TableLayout tableLayoutInvoice;
    private TextView textCustomerNameTableIn;
    private TextView textSrNo, textInvoiceRefNo, textTotal, textGrandTotal, textInvoiceDate, textCurrency, textGroup, textStatus, textCompanyName, textSign;
    private TextView textCustomerNameTable, textSrNoIn, textInvoiceRefNoIn, textInvoiceDateIn, textTotalIn, textGrandTotalIn, textCurrentIn, textGroupIn, textStatusIn, textCompanyNameIn, textSignIn;
    private TextView textHeader;
    private String companyName, date1, date2;
    private Integer salesCustomerCode,customerCode,opportunityCode;
    private List<Map<String, Object>> listOfInvoice = new ArrayList<>();
    private File myFileInvoice;


    public static MeFragmentTableInvoice getInstance(String companyName, Integer customerCode, Integer salesCustomerCode, Integer opportunityCode) {
        MeFragmentTableInvoice fragmentTableInvoice = new MeFragmentTableInvoice();
        Bundle args = new Bundle();
        args.putString(MainActivity.KEY_FRAGMENT_NAME, "MeFragmentTableInvoice");
        args.putString(KEY_COMPANY_NAME, companyName);
        args.putInt(KEY_CUSTOMER_CODE, customerCode);
        args.putInt(KEY_SALES_CUSTOMER_CODE,salesCustomerCode);
        args.putInt(KEY_OPPORTUNITY_CODE,opportunityCode);
        Log.i("@Transworld", "companyName -" + customerCode);
        //    args.putString(KEY_START_DATE, startDate);
        //     args.putString(KEY_END_DATE, endDate);
        fragmentTableInvoice.setArguments(args);
        return fragmentTableInvoice;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentActivity = (MainActivity) getActivity();
        parentActivity.setTitle("Last 3 Invoices");
        parentActivity.showSearchView(false);
        parentActivity.isVisibleFab(false);
        parentActivity.isToolbarClickable();
        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main2, menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        try {
            Log.i("@Transwold","listOfInvoice -"+listOfInvoice);
            if(listOfInvoice.size()>0) {
                if (item.getItemId() == R.id.action_send_pdf) {
                    attachPdfToMail(myFileInvoice);
                }
            }
            else {
                if(getView()!=null)
                parentActivity.snack(rootView, "No Invoice Records Found :(");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentActivity.getSupportActionBar().show();
        rootView = inflater.inflate(R.layout.custom_table_invoice, container, false);
        parentActivity.isToolbarClickable();
        initTableLayout();
        opportunityCode = getArguments().getInt(KEY_OPPORTUNITY_CODE);
        customerCode = getArguments().getInt(KEY_CUSTOMER_CODE);
        salesCustomerCode = getArguments().getInt(KEY_SALES_CUSTOMER_CODE);
        companyName = getArguments().getString(KEY_COMPANY_NAME);
        initTextViews();
        //   toDate = getArguments().getString(KEY_START_DATE);
        //   fromDate = getArguments().getString(KEY_END_DATE);
        Integer companyMasterId = null;
        try {
            companyMasterId = MeRepoFactory.getLoginRepository(parentActivity.getDbHelper()).getCompanyMasterId();


        } catch (Exception e) {
            e.printStackTrace();
        }
        String headerText = textHeader.getText().toString();
        String text = headerText + "\n" + companyName;
        meItextPdf = new MeItextPdf(text);
        Log.i("@Transworld", "" + text);
        if (parentActivity.isNetworkAvailable()) {
            if(!salesCustomerCode.equals("0")){
                new MeTask(companyMasterId, salesCustomerCode).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
            else {
                new MeTask(companyMasterId, customerCode).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }

        }
        /*else
            parentActivity.snack(rootView, "No Invoice for this customers:(");*/
        return rootView;
    }

    private void initTextViews() {
        textCustomerNameTable = (TextView) rootView.findViewById(R.id.textCustomerNameTableIn);
        textCustomerNameTable.setText(companyName+"["+customerCode+"]"+"["+salesCustomerCode+"]"+"["+opportunityCode+"]");
        textHeader = (TextView) rootView.findViewById(R.id.textHeader);
        textHeader.setPaintFlags(textHeader.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        textSrNoIn = (TextView) rootView.findViewById(R.id.textSrNoIn);
        textInvoiceRefNoIn = (TextView) rootView.findViewById(R.id.textInvoiceRefNoIn);
        textInvoiceDateIn = (TextView) rootView.findViewById(R.id.textInvoiceDateIn);
        textTotalIn = (TextView) rootView.findViewById(R.id.textTotalIn);
        textGrandTotalIn = (TextView) rootView.findViewById(R.id.textGrandTotalIn);
        textCurrentIn = (TextView) rootView.findViewById(R.id.textCurrentIn);
        textGroupIn = (TextView) rootView.findViewById(R.id.textGroupIn);
        textStatusIn = (TextView) rootView.findViewById(R.id.textStatusIn);
        textCompanyNameIn = (TextView) rootView.findViewById(R.id.textCompanyNameIn);
        textSignIn = (TextView) rootView.findViewById(R.id.textSignIn);

    }

    private void initTableLayout() {
        tableLayoutInvoice = (TableLayout) rootView.findViewById(R.id.tableLayoutInvoice);
    }

    private void attachPdfToMail(File myFile) {
        Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:")); // it's not ACTION_SEND
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(myFile));
        intent .putExtra(Intent.EXTRA_SUBJECT, "Invoice_Report");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
        startActivity(intent);
    }

    private void addTableData(List<Map<String, Object>> maps) {
        Integer i = 1;
        Log.i(MainActivity.TAG,"Map Size Invoices "+maps.size());
        if (maps.size() > 1) {
            for (Map<String, Object> mapItem : maps) {
                textSrNo = new TextView(parentActivity);
                textInvoiceRefNo = new TextView(parentActivity);
                textInvoiceDate = new TextView(parentActivity);
                textTotal = new TextView(parentActivity);
                textGrandTotal = new TextView(parentActivity);
                textCurrency = new TextView(parentActivity);
                textGroup = new TextView(parentActivity);
                textStatus = new TextView(parentActivity);
                textCompanyName = new TextView(parentActivity);
                textSign = new TextView(parentActivity);
                TableRow tableRowValue = new TableRow(parentActivity);
                tableRowValue.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT));
                TableLayout.LayoutParams tableRowParams =
                        new TableLayout.LayoutParams
                                (TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);

                textSrNo.setText("" + i);
                textInvoiceRefNo.setText("" + mapItem.get("invoiceRefNo"));
                textInvoiceDate.setText("" + mapItem.get("invoiceDate"));
                textTotal.setText(String.format("%.2f",mapItem.get("total")));
                textGrandTotal.setText(String.format("%.2f",mapItem.get("grandTotal")));
                textCurrency.setText("" + mapItem.get("currType"));
                textGroup.setText("" + mapItem.get("theGroup"));
                textSign.setText("" + mapItem.get("TWEmpName"));
                textCompanyName.setText("" + mapItem.get("companyName"));
                //dont know what is this col
                textStatus.setText("" + mapItem.get("invoiceCancelled"));

                textSrNo.setGravity(Gravity.RIGHT);
                textInvoiceRefNo.setGravity(Gravity.LEFT);
                textInvoiceDate.setGravity(Gravity.RIGHT);
                textTotal.setGravity(Gravity.RIGHT);
                textGrandTotal.setGravity(Gravity.RIGHT);
                textCurrency.setGravity(Gravity.LEFT);
                textGroup.setGravity(Gravity.LEFT);
                textSign.setGravity(Gravity.LEFT);
                textCompanyName.setGravity(Gravity.LEFT);
                textSrNo.setPadding(10, 0, 10, 0);
                textInvoiceRefNo.setPadding(10, 0, 10, 0);
                textInvoiceDate.setPadding(10, 0, 10, 0);
                textTotal.setPadding(10, 0, 10, 0);
                textGrandTotal.setPadding(10, 0, 10, 0);
                textCurrency.setPadding(10, 0, 10, 0);
                textGroup.setPadding(10, 0, 10, 0);
                textSign.setPadding(10, 0, 10, 0);
                textCompanyName.setPadding(10, 0, 10, 0);

                textStatus.setGravity(Gravity.LEFT);
                textSrNo.setTextColor(ContextCompat.getColor(parentActivity, R.color.black));
                textInvoiceRefNo.setTextColor(ContextCompat.getColor(parentActivity, R.color.black));
                textInvoiceRefNo.setTypeface(Typeface.DEFAULT_BOLD);
                textInvoiceDate.setTextColor(ContextCompat.getColor(parentActivity, R.color.black));
                textTotal.setTextColor(ContextCompat.getColor(parentActivity, R.color.black));
                textGrandTotal.setTextColor(ContextCompat.getColor(parentActivity, R.color.black));
                textCurrency.setTextColor(ContextCompat.getColor(parentActivity, R.color.black));
                textGroup.setTextColor(ContextCompat.getColor(parentActivity, R.color.black));
                textSign.setTextColor(ContextCompat.getColor(parentActivity, R.color.black));
                textCompanyName.setTextColor(ContextCompat.getColor(parentActivity, R.color.black));
                textStatus.setTextColor(ContextCompat.getColor(parentActivity, R.color.black));
               // tableRowValue.setPadding(10, 10, 10, 10);
                tableRowValue.addView(textSrNo);

                tableRowValue.addView(textInvoiceRefNo);

                tableRowValue.addView(textInvoiceDate);
                tableRowValue.addView(textTotal);
                tableRowValue.addView(textGrandTotal);
                tableRowValue.addView(textCurrency);
                tableRowValue.addView(textGroup);
                tableRowValue.addView(textStatus);
                tableRowValue.addView(textCompanyName);
                tableRowValue.setPadding(10, 10, 10, 10);
                tableRowValue.addView(textSign);
                tableRowParams.setMargins(0, 0, 10, 0);
                tableRowValue.setLayoutParams(tableRowParams);
                tableLayoutInvoice.addView(tableRowValue, new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.WRAP_CONTENT,
                        TableLayout.LayoutParams.MATCH_PARENT));
                i++;
            }

        } else {
            if(getView()!=null)
            parentActivity.snack(rootView, "No Invoice for this customers:(");
        }

    }

    private class MeTask extends AsyncTask<Void, Void, List<Map<String, Object>>> {
        private ProgressDialog progressDialog;
        private Integer customerCode;
        private String companyName;
        //private String invoiceFromDate;
        //    private String invoiceToDate;
        private Integer companyMasterId;
        private MeConnectorInvoice connectorInvoice;

        public MeTask(Integer companyMasterId, Integer customerCode) {
            this.customerCode = customerCode;
            //     this.invoiceFromDate = invoiceFromDate;
            //     this.invoiceToDate = invoiceToDate;
            this.companyMasterId = companyMasterId;
            connectorInvoice = new MeConnectorInvoice();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(parentActivity, "Invoice Info.", "Loading...");
        }

        @Override
        protected List<Map<String, Object>> doInBackground(Void... params) {
            try {
                Map<String, Object> mapEntity = connectorInvoice.getLastInvoice(companyMasterId, customerCode);
                if (mapEntity != null) {
                    listOfInvoice = (List<Map<String, Object>>) mapEntity.get("invoice");
                    Log.i("@Transworld","listOfInvoice -"+listOfInvoice);
                    if(listOfInvoice.size()>0)
                        myFileInvoice = meItextPdf.initConfigInvoice(listOfInvoice);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listOfInvoice;
        }

        @Override
        protected void onPostExecute(List<Map<String, Object>> maps) {
            super.onPostExecute(maps);
            progressDialog.dismiss();
            if (maps != null && !maps.equals("")) {
                addTableData(maps);
                //     textCustomerNameTable.setText(""+companyName);
            }
        }
    }

    private final class MeConnectorInvoice implements MeConnectable {
        private String jsonMap;

        public Map<String, Object> getLastInvoice(Integer companyMasterId, Integer customerCode) throws Exception {
            Map<String, Object> mapEntity = new HashMap<>();
            jsonMap = HttpRequest.get(MeIUrl.URL_LAST_INVOICE + companyMasterId + "/" + customerCode)
                    .body();
            Log.i("@Transworld", MeIUrl.URL_LAST_INVOICE + companyMasterId + "/" + customerCode);
            Log.i("@Transworld", "Last Invoice" + jsonMap);
            mapEntity = JsonMan.parseAnything(jsonMap, new TypeReference<Map<String, Object>>() {
            });
            return mapEntity;
        }
    }
}
