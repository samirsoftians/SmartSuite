package com.transworldtechnology.crm.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.transworldtechnology.crm.R;
import com.transworldtechnology.crm.activity.MainActivity;

import java.util.HashMap;

/**
 * Created by root on 13/2/16.
 */
public class MeDialogFragment extends DialogFragment
{
    public static final String TAG_APPOINTMENT_LIST = "appointment";
    public static final String KEY_MAP_CLICK_INFO = "clickInfo";
    public static final String KEY_CONTACT_ID = "contactId";
    private MainActivity parentActivity;
    private Dialog rootDialog;
    private TextView txtCompanyName, txtAppointmentDateDialog, txtContactPerson, txtAppointmentTimeDialog, txtNextFollowUpTypeApp, txtPreparation, txtPayment, txtRemarks, txtExpectedOn;
    private TextView txtFollowUpTypePrev, txtStatusPrev, txtCommentsPrev, txtFollowUpDateTime;
    private Button btnOk;
    private MeDialogDissmissListener meDialogDissmissListener;
    public static MeDialogFragment getInstanseWithContact(HashMap<String, Object> mapFollowUpData)
    {
        MeDialogFragment dialogFragment = new MeDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_MAP_CLICK_INFO, mapFollowUpData);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }
    public void setMeDialogDissmissListener(MeDialogDissmissListener meDialogDissmissListener)
    {
        if (meDialogDissmissListener != null)
            this.meDialogDissmissListener = meDialogDissmissListener;
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        parentActivity = (MainActivity) getActivity();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        super.onCreateDialog(savedInstanceState);

        if (getTag().equals(TAG_APPOINTMENT_LIST))
        {
            rootDialog = new Dialog(getContext());
            rootDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            rootDialog.setContentView(R.layout.dialog_detailed_appointment);

            initTextView();
            initButton();
            showDialog();

        }
        return rootDialog;

    }

    private void initButton() {
        btnOk = (Button) rootDialog.findViewById(R.id.btnOk);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootDialog.dismiss();
            }
        });
    }

    private void initTextView()
    {
        txtCompanyName = (TextView) rootDialog.findViewById(R.id.txtCompanyName);
        txtContactPerson = (TextView) rootDialog.findViewById(R.id.txtContactPerson);
        txtAppointmentDateDialog = (TextView) rootDialog.findViewById(R.id.txtAppointmentDateDialog);
        txtAppointmentTimeDialog = (TextView) rootDialog.findViewById(R.id.txtAppointmentTimeDialog);
        txtNextFollowUpTypeApp = (TextView) rootDialog.findViewById(R.id.txtNextFollowUpTypeApp);
        txtPreparation = (TextView) rootDialog.findViewById(R.id.txtPreparation);
        txtPayment = (TextView) rootDialog.findViewById(R.id.txtPayment);
        txtFollowUpTypePrev = (TextView) rootDialog.findViewById(R.id.txtFollowUpTypePrev);
        txtFollowUpDateTime = (TextView) rootDialog.findViewById(R.id.txtFollowUpDateTime);
        txtStatusPrev = (TextView) rootDialog.findViewById(R.id.txtStatusPrev);
        txtCommentsPrev = (TextView) rootDialog.findViewById(R.id.txtCommentsPrev);
        txtRemarks = (TextView) rootDialog.findViewById(R.id.txtRemarks);
        txtExpectedOn = (TextView) rootDialog.findViewById(R.id.txtExpectedOn);
    }
    private void showDialog()
    {
        HashMap<String, Object> map = (HashMap<String, Object>) getArguments().getSerializable(KEY_MAP_CLICK_INFO);
        txtCompanyName.setText("" + map.get("prospCustName"));
        txtAppointmentDateDialog.setText("" + map.get("nextFollowUpDate"));
        txtAppointmentTimeDialog.setText("" + map.get("nextFollowUpTime"));
        txtCommentsPrev.setText(Html.fromHtml("<b>Comments : </b>" + map.get("comments")));
        txtContactPerson.setText((String) map.get("contactPerson"));
        txtFollowUpDateTime.setText(Html.fromHtml("<b>Date/Time : </b>" + map.get("followUpDate") + ", " + map.get("followUpTime")));
        txtFollowUpTypePrev.setText(Html.fromHtml("<b>FollowUp Type : </b>" + map.get("followUpType") + "(" + map.get("followUpInOut") + ")"));
        txtNextFollowUpTypeApp.setText(Html.fromHtml("<b>Next FollowUp Type : </b>" + map.get("nextFollowUpType")));
        txtStatusPrev.setText(Html.fromHtml("<b>Status : </b>" + map.get("status")));

        if (map.get("preparation").equals(""))
            txtPreparation.setText(Html.fromHtml("<b>Preparation : </b>" + "-"));
        else
            txtPreparation.setText(Html.fromHtml("<b>Preparation : </b>" + map.get("preparation")));

        if (map.get("paymentFollowUp").equals("YES")) {
            txtPayment.setText(Html.fromHtml("<b>Payment : </b>" + "Rs." + map.get("amountExpected")));
            txtExpectedOn.setText(Html.fromHtml("<b>Expected On : </b>" + map.get("amountExpectedOn")));
        }
        else
            txtPayment.setText(Html.fromHtml("<b>Payment : </b>" + "-"));
        txtExpectedOn.setText(Html.fromHtml("<b>Expected On : </b>" + "-"));

        if (map.get("remarks").equals(""))
            txtRemarks.setText(Html.fromHtml("<b>Remarks : </b>" + "-"));
        else
            txtRemarks.setText(Html.fromHtml("<b>Remarks : </b>" + map.get("remarks")));
    }
    public static interface MeDialogDissmissListener
    {
        void onDismiss(String tag, Integer btn);
    }
}
