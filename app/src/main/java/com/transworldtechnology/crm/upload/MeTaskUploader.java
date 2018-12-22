package com.transworldtechnology.crm.upload;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.transworldtechnology.crm.R;
import com.transworldtechnology.crm.activity.MainActivity;
import com.transworldtechnology.crm.database.repository.MeRepoContactDet;
import com.transworldtechnology.crm.database.repository.MeRepoImplContactDet;
import com.transworldtechnology.crm.database.repository.MeRepoImplSaveFollowUp;
import com.transworldtechnology.crm.database.repository.MeRepoSaveFollowUp;
import com.transworldtechnology.crm.domain.MeAddContact;
import com.transworldtechnology.crm.web.JsonMan;
import com.transworldtechnology.crm.web.MeConnectable;
import com.transworldtechnology.crm.web.MeIUrl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by root on 8/4/16.
 */
public class MeTaskUploader extends AsyncTask<Void, Void, Boolean> {
    private Context context;
    private MainActivity parentActivity;
    private Integer companyMasterId;
    private MeUploadable uploadable;
    private MeOkHttpUploadable meOkHttpUploadable;
    private File[] multipartDocs;
    private String from;


    public MeTaskUploader(Context context,String from) {
        this.context = context;
        this.from = from;
        this.uploadable = new MeOkHttpUploadable();
        parentActivity = (MainActivity) context;
        meOkHttpUploadable = new MeOkHttpUploadable();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            MeRepoContactDet repoContactDet = new MeRepoImplContactDet(parentActivity.getDbHelper());
            MeRepoSaveFollowUp repoSaveFollowUp = new MeRepoImplSaveFollowUp(parentActivity.getDbHelper());
            Log.i("@Transworld", "Check " + repoContactDet.checkDataContacts());
            if (!repoContactDet.checkDataContacts().equals(0)) {
                meOkHttpUploadable.saveContactsToServer();
            }
            if(!repoSaveFollowUp.checkDataFollowUp().equals(0)){

                meOkHttpUploadable.saveFollowUpsToServer();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
      //  parentActivity.snack(parentActivity.getCurrentFocus(),"Data Uploaded");
        MeRepoContactDet repoContactDet = new MeRepoImplContactDet(parentActivity.getDbHelper());
        MeRepoSaveFollowUp repoSaveFollowUp = new MeRepoImplSaveFollowUp(parentActivity.getDbHelper());
      //  try {
         //   if (!repoContactDet.checkDataContacts().equals(0) && !repoSaveFollowUp.checkDataFollowUp().equals(0)) {
        Log.i("@Transworld","From " + from);
        if (from.equals("forcefully"))
                showNotification();
       //     }
            /*else {
                showNotificationNoData();
            }*/
     /*   } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    private final List<MeAddContact> uploadContactToServer() throws Exception {
        List<MeAddContact> addContactList = new ArrayList<>();
        MeRepoContactDet repoContactDet = new MeRepoImplContactDet(parentActivity.getDbHelper());
        addContactList = repoContactDet.uploadContactsToServer();
        repoContactDet.updateFlag();
        //  }
        Log.i("@Transworld", "Contact List : " + addContactList);
        if (addContactList == null && addContactList.isEmpty()) {
            return null;
        } else
            return addContactList;
    }

    public void showNotification() {
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent intent = new Intent(parentActivity, MeTaskUploader.class);
        PendingIntent pIntent = PendingIntent.getActivity(parentActivity, 0, intent, 0);
        Notification mNotification = new Notification.Builder(parentActivity)
                .setContentTitle("SmartSuite!")
                .setContentText("Data successfully Uploaded To Server!")
                .setSmallIcon(R.mipmap.ic_transworld)
                .setContentIntent(pIntent)
                .setSound(soundUri)
                .build();
        NotificationManager notificationManager = (NotificationManager) parentActivity.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, mNotification);
    }

    public void showNotificationNoData() {
        // define sound URI, the sound to be played when there's a notification
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        // intent triggered, you can add other intent for other actions
        Intent intent = new Intent(parentActivity, MeTaskUploader.class);
        PendingIntent pIntent = PendingIntent.getActivity(parentActivity, 0, intent, 0);
        Notification mNotification = new Notification.Builder(parentActivity)
                .setContentTitle("TransworldCRM!")
                .setContentText("No Data Available To Upload!")
                .setSmallIcon(R.mipmap.ic_transworld)
                .setContentIntent(pIntent)
                .setSound(soundUri)
                .build();
        NotificationManager notificationManager = (NotificationManager) parentActivity.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, mNotification);
    }

    public void cancelNotification(int notificationId) {
        if (Context.NOTIFICATION_SERVICE != null) {
            String ns = Context.NOTIFICATION_SERVICE;
            NotificationManager nMgr = (NotificationManager) parentActivity.getSystemService(ns);
            nMgr.cancel(notificationId);
        }
    }

    private final class MeOkHttpUploadable implements MeUploadable {
        @Override
        public Map<String, Object> saveContactsToServer() throws Exception {
            Map<String, Object> mapEntity = null;
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), JsonMan.fromObject(uploadContactToServer()));
            Log.i("@Transworld", "Body : " + body);
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(MeIUrl.URL_UPLOAD_CONTACTS_TO_SERVER)
                    .post(body)
                    .addHeader("Accept", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            String responsJson = response.body().string();
            Log.i("@Transworld", "Response Json Add Contact- " + responsJson);
            mapEntity = JsonMan.parseAnything(responsJson, new TypeReference<Map<String, Object>>() {
            });
            Log.i("@Transworld", "Response Entity - " + mapEntity);
            String status = mapEntity.get("status").toString();
            Log.i("@Transworld", "Status :  " + status);
            if (status.equals("added contact successfully")) {
                Log.i("@Transworld", "Status 2:  " + status);
                Log.i("@Transworld", "Fragment followup loaded");
            }
            return mapEntity;
        }

        @Override
        public Map<String, Object> saveFollowUpsToServer() throws Exception {
            MeConnectorFollowUp connector = new MeConnectorFollowUp();
            Map<String, Object> mapEntity = null;
            MeRepoSaveFollowUp repoSaveFollowUp = new MeRepoImplSaveFollowUp(parentActivity.getDbHelper());
            for (Map<String, Object> map : repoSaveFollowUp.getFollowUpData()) {
                if (map != null) {
                    mapEntity = connector.addNewFollowUpToServer(map);
                } else {
                    showNotificationNoData();
                }
            }
            repoSaveFollowUp.updateFlag();
            return mapEntity;
        }

        private final class MeConnectorFollowUp implements MeConnectable {
            public Map<String, Object> addNewFollowUpToServer(Map<String, Object> followUp) throws Exception {
                Log.i("@Transworld", "Connector Followup - " + followUp.toString());
                Map<String, Object> mapEntity = null;
                MultipartBody.Builder builder = new MultipartBody.Builder();
                builder.addFormDataPart("prospCustCode", "" + followUp.get("prospCustCode"));
                builder.addFormDataPart("remarks", "" + followUp.get("remarks"));
                builder.addFormDataPart("companyMasterId", "" + followUp.get("companyMasterId"));
                builder.addFormDataPart("followUpDate", "" + followUp.get("followUpDate"));
                builder.addFormDataPart("followUpTime", "" + followUp.get("followUpTime"));
                builder.addFormDataPart("status", "" + followUp.get("status"));
                builder.addFormDataPart("followUpType", "" + followUp.get("followUpType"));
                builder.addFormDataPart("followUpInOut", "" + followUp.get("followUpInOut"));
                builder.addFormDataPart("comments", "" + followUp.get("comments"));
                builder.addFormDataPart("nextFollowUpDate", "" + followUp.get("nextFollowUpDate"));
                builder.addFormDataPart("nextFollowUpTime", "" + followUp.get("nextFollowUpTime"));
                builder.addFormDataPart("nextFollowUpType", "" + followUp.get("nextFollowUpType"));
                builder.addFormDataPart("preparation", "" + followUp.get("preparation"));
                builder.addFormDataPart("marketingRepCode", "" + followUp.get("marketingRepCode"));
                builder.addFormDataPart("city", "" + followUp.get("city"));
                builder.addFormDataPart("contactId", "" + followUp.get("contactId"));
                builder.addFormDataPart("contactPerson", "" + followUp.get("contactPerson"));
                builder.addFormDataPart("designation", "" + followUp.get("designation"));
                builder.addFormDataPart("address", "" + followUp.get("address"));
                builder.addFormDataPart("prospCustName", "" + followUp.get("prospCustName"));
                builder.addFormDataPart("paymentFollowUp", "" + followUp.get("paymentFollowUp"));
                builder.addFormDataPart("spokenTo", "" + followUp.get("spokenTo"));
                if (followUp.get("paymentFollowUp").equals("YES")) {
                    builder.addFormDataPart("amountExpected", "" + (followUp.get("amountExpected")));
                    builder.addFormDataPart("amountExpectedOn", "" + followUp.get("amountExpectedOn"));
                }
                builder.addFormDataPart("locSource", "" + followUp.get("locSource"));
                builder.addFormDataPart("locLat", "" + followUp.get("locLat"));
                builder.addFormDataPart("locLong", "" + followUp.get("locLong"));
                builder.addFormDataPart("timeZone", "" + System.currentTimeMillis());
                List<File> fileLocal = new ArrayList<>();
                fileLocal.add((File) followUp.get("docPath1"));
                fileLocal.add((File) followUp.get("docPath2"));
                fileLocal.add((File) followUp.get("docPath3"));
                Log.i("@Transworld", "File Path :" + fileLocal);
                for (File file : fileLocal) {
                    if (file.exists()) {
                        builder.addFormDataPart("multipartFile", "" + (file.getName()), RequestBody.create(MediaType.parse("image"), file));
                    }
                }
                builder.addFormDataPart("assignToUserName", "" + followUp.get("assignToUserName"));
                builder.addFormDataPart("assignToUserId", "" + followUp.get("assignToUserId"));
                builder.setType(MultipartBody.FORM);
                RequestBody body = builder.build();
                Request request = new Request.Builder()
                        .url(MeIUrl.URL_ADD_NEW_FOLLOW_UP)
                        .post(body)
                        .addHeader("Accept", "application/json")
                        .build();
                Log.i("@Transworld", "req Body " + builder.toString());
                OkHttpClient client = new OkHttpClient();
                Response response = client.newCall(request).execute();
                String responseJson = response.body().string();
                Log.i("@Transworld", "Response Json Follow Up" + responseJson);
                if (response.code() == 200) {
                    mapEntity = JsonMan.parseAnything(responseJson, new TypeReference<Map<String, Object>>() {
                    });
                    Log.i("@Transworld", "Map Entity Follow Up" + mapEntity);
                }
                return mapEntity;
            }
        }
    }
}
