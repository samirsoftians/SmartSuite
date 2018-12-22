package com.transworldtechnology.crm.fragment;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.transworldtechnology.crm.R;
import com.transworldtechnology.crm.activity.MainActivity;

import java.io.File;
import java.net.URI;
import java.util.List;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.property.FormattedName;
import ezvcard.property.Telephone;


public class MeFragmentBottomSheetDialog extends BottomSheetDialogFragment {


    private MainActivity parentActivity;
    public static final Integer INTENT_REQUEST_GET_CONTACT = 1501;
    public static final Integer PICKFILE_RESULT_CODE = 1402;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentActivity = (MainActivity) getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_bottom_sheet_dialog, container, false);

        initUI(rootView);

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        dismiss();


        if(resultCode == Activity.RESULT_OK){
            if(requestCode == INTENT_REQUEST_GET_CONTACT){
                if (data != null) {
                    Uri uriContact = data.getData();

                    if (uriContact != null) {
                        Cursor cursor = getContext().getContentResolver().query(uriContact, new String[]{
                                ContactsContract.PhoneLookup.DISPLAY_NAME,
                                ContactsContract.CommonDataKinds.Phone.DATA
                        }, null, null, null);


                        if (cursor != null && cursor.moveToFirst()) {

                            String fullName = cursor.getString(0);
                            String name[] = new String[0];
                            String mobno = cursor.getString(1);

                            parentActivity.runFragmentTransaction(R.id.frameMainContainer,MeFragmentAddContacts.newInstance(fullName,mobno,"BottomDialog"));
                        }
                    }
                }
            }
            if (requestCode == PICKFILE_RESULT_CODE) {
                if (data != null) {
                    Uri uriContact = data.getData();

                    if (uriContact != null) {
                        String filePath = uriContact.toString();

                        readContact(filePath);
                    }
                }
            }
        }
    }
    private void initUI(final View rootView) {

        rootView.findViewById(R.id.linearLayoutAddContacts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                startActivityForResult(intent,INTENT_REQUEST_GET_CONTACT);
            }
        });
        rootView.findViewById(R.id.linearLayoutAddVcs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setDataAndType(Uri.fromFile(new File(""+Environment.getExternalStorageDirectory())),"text/x-vcard");
                startActivityForResult(intent,PICKFILE_RESULT_CODE);
            }
        });

    }

    private void readContact(String vcfFile){
        try{
            String fullName ="",mobNo ="";

            List<VCard> vcards = Ezvcard.parse(new File(new URI(vcfFile))).all();

            for (VCard vcard : vcards){

                for (FormattedName name : vcard.getFormattedNames()){
                    Log.i("", "Name in for: " + name.toString());
                }
                fullName = vcard.getFormattedName().getValue();

                for (Telephone tel : vcard.getTelephoneNumbers()){
                    Log.i(MainActivity.TAG,"Telephone no: " + tel.getText());
                    mobNo = tel.getText();
                }
            }
            dismiss();
            Log.i(MainActivity.TAG,"onActivityResult MeFragmentAddContacts getInstance vcf");
            parentActivity.runFragmentTransaction(R.id.frameMainContainer,MeFragmentAddContacts.newInstance(fullName,mobNo,"BottomDialog"));

        }catch(Exception e){
            e.printStackTrace();}
    }

}
