package com.transworldtechnology.crm.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.transworldtechnology.crm.R;
import com.transworldtechnology.crm.activity.MainActivity;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by root on 25/2/16.
 */
public class MeFragmentSelectDate extends Fragment {
    public static final String KEY_CUSTOMER_CODE = "cust_code";
    public static final String KEY_FRAGMENT = "name";
    public static final String KEY_COMPANY_NAME = "company_name";
    public static final String KEY_SALES_CUSTOMER_CODE = "sales_cust_code";
    public static final String KEY_OPPORTUNITY_CODE = "opp_code";
    private MainActivity parentActivity;
    private View rootView;
    private TextView textStartDate, textEndDate;
    private MeClick meClick;
    private Integer position = -1;
    private Button btnGo;
    private Integer customerCode, opportunityCode, salesCustomerCode;
    private String companyName;
    private String startDate, endDate, currentdate, date;
    private Boolean flag = false;
    private TextView textCustomerName;
    private Calendar calendar;
//    public static MeFragmentSelectDate getInstance(String companyName, String fragmentName) {
//        MeFragmentSelectDate fragmentLedgerDetails = new MeFragmentSelectDate();
//        Bundle args = new Bundle();
//        args.putString(MainActivity.KEY_FRAGMENT_NAME, "MeFragmentSelectDate");
//        args.putString(KEY_COMPANY_NAME, companyName);
//        args.putString(KEY_FRAGMENT, fragmentName);
//        fragmentLedgerDetails.setArguments(args);
//        return fragmentLedgerDetails;
//    }

    public static MeFragmentSelectDate newInstance(String companyName, Integer customerCode, Integer salesCustomerCode, Integer opportunityCode) {
        MeFragmentSelectDate fragmentLedgerDetails = new MeFragmentSelectDate();
        Bundle args = new Bundle();
        args.putString(MainActivity.KEY_FRAGMENT_NAME, "MeFragmentSelectDate");
        args.putString(KEY_COMPANY_NAME, companyName);
        args.putInt(KEY_CUSTOMER_CODE, customerCode);
        args.putInt(KEY_SALES_CUSTOMER_CODE, salesCustomerCode);
        args.putInt(KEY_OPPORTUNITY_CODE, opportunityCode);
        fragmentLedgerDetails.setArguments(args);
        return fragmentLedgerDetails;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        meClick = new MeClick();
        calendar = Calendar.getInstance();
        parentActivity = (MainActivity) getActivity();
        parentActivity.setTitle("Ledger Account Details");
        parentActivity.showSearchView(false);
        parentActivity.isVisibleFab(false);
        parentActivity.isToolbarClickable();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentActivity.getSupportActionBar().show();
        parentActivity.isToolbarClickable();
        rootView = inflater.inflate(R.layout.fragment_select_date, container, false);
        parentActivity.setTitle("Ledger Account Details");
        opportunityCode = getArguments().getInt(KEY_OPPORTUNITY_CODE);
        salesCustomerCode = getArguments().getInt(KEY_SALES_CUSTOMER_CODE);
        companyName = getArguments().getString(KEY_COMPANY_NAME);
        customerCode = getArguments().getInt(KEY_CUSTOMER_CODE);
        companyName = getArguments().getString(KEY_COMPANY_NAME);
        initDate();
        initTextView();
        initButton();
        return rootView;
    }

    private void initDate() {
        date = calendar.get(Calendar.DAY_OF_MONTH) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.YEAR);
        Format formatter = new SimpleDateFormat("dd-MMM-yyyy");
        date = formatter.format(new Date());
        Log.i("@Transworld", "Today's Date -" + date);
    }

    private void initTextView() {
        textCustomerName = (TextView) rootView.findViewById(R.id.textCustomerName);
        textCustomerName.setText(companyName + "[" + customerCode + "]" + "[" + salesCustomerCode + "]" + "[" + opportunityCode + "]");
        textStartDate = (TextView) rootView.findViewById(R.id.textStartDate);
        textStartDate.setOnClickListener(meClick);
        textEndDate = (TextView) rootView.findViewById(R.id.textEndDate);
        textEndDate.setOnClickListener(meClick);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.MONTH, -1);  // after 1 month
        String date2 = sdf.format(c.getTime());
        textStartDate.setText(date2);
        textEndDate.setText(date);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = simpleDateFormat.format(c.getTime());
        startDate = date;
    }

    private void initButton() {
        btnGo = (Button) rootView.findViewById(R.id.btnGo);
        btnGo.setOnClickListener(meClick);
    }

    private final class MeDateListener implements DatePickerDialog.OnDateSetListener {
        private TextView textView;
        private String dateToWeb, date, month;
        private String args[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

        public void setTextView(TextView textView) {
            this.textView = textView;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            month = args[monthOfYear];
            date = "" + (dayOfMonth > 9 ? dayOfMonth : "0" + dayOfMonth) + "-" + month + "-" + year;
            dateToWeb = "" + year + "-" + ((monthOfYear + 1) > 9 ? (monthOfYear + 1) : "0" + (monthOfYear + 1)) + "-" + (dayOfMonth > 9 ? dayOfMonth : "0" + dayOfMonth);
            Log.i("@Transworld", "dateToWeb -" + dateToWeb);
            currentdate = dateToWeb;
            Log.i("@Transworld", "currentdate -" + currentdate);
            textView.setText(date);
            if (flag == true) {
                startDate = currentdate;
                Log.i("@Transworld", "startDate -" + startDate);
            } else {
                endDate = currentdate;
                Log.i("@Transworld", "endDate -" + endDate);
            }
        }
    }

    private class MeClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.getId() == btnGo.getId()) {
                Log.i("@Transworld", "In Go");
                if (startDate == null) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    date = simpleDateFormat.format(new Date());
                    startDate = date;
                }
                if (endDate == null) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    date = simpleDateFormat.format(new Date());
                    endDate = date;
                }
                if (isTimeValid()) {
                    parentActivity.runFragmentTransaction(R.id.frameMainContainer, MeFragmentLedgerTable.getInstance(companyName, customerCode, salesCustomerCode, startDate, endDate));
                } else {
                    if (getView() != null) {
                        parentActivity.snack(rootView, "Please Select Proper Date");
                    }
                }
            }
            if (v.getId() == textStartDate.getId()) {
                flag = true;
                MeDateListener dateListener = new MeDateListener();
                dateListener.setTextView(textStartDate);
                new DatePickerDialog(parentActivity, dateListener, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
            if (v.getId() == textEndDate.getId()) {
                flag = false;
                MeDateListener dateListener = new MeDateListener();
                dateListener.setTextView(textEndDate);
                DatePickerDialog datePicker = new DatePickerDialog(parentActivity, dateListener, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                datePicker.getDatePicker().setMaxDate(new Date().getTime());
                datePicker.show();
             /*   new DatePickerDialog(parentActivity, dateListener, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();*/
            }
        }
    }

    private final Boolean isTimeValid() {
        //    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            Date startDate = dateFormat.parse(textStartDate.getText().toString());
            Date endDate = dateFormat.parse(textEndDate.getText().toString());
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Log.i("@Transworld", "Compare To " + startDate.compareTo(endDate));
            if (dateFormat.format(startDate).compareTo(dateFormat.format(endDate)) > 0) {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }
}
