package com.transworldtechnology.crm.send;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.transworldtechnology.crm.R;
import com.transworldtechnology.crm.activity.MainActivity;
import com.transworldtechnology.crm.database.MeDatabase;
import com.transworldtechnology.crm.database.MeHelper;
import com.transworldtechnology.crm.database.repository.MeRepoImplUserTracker;
import com.transworldtechnology.crm.database.repository.MeRepoUserTracker;
import com.transworldtechnology.crm.domain.MeUserTracker;
import com.transworldtechnology.crm.web.JsonMan;
import com.transworldtechnology.crm.web.MeIUrl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by root on 18/4/16.
 */
public class MeTaskSender extends AsyncTask<Void, Void, Boolean>
{
    private Context context;
    private MainActivity parentActivity;
    private MeSendable sendable;
    private MeOkHttpSendable meOkHttpSendable;
    private MeHelper helper;

    public MeTaskSender(Context context)
    {
        this.context = context;
        this.sendable = new MeOkHttpSendable();
        parentActivity = (MainActivity) context;
        meOkHttpSendable = new MeOkHttpSendable();
    }
    @Override
    protected Boolean doInBackground(Void... params)
    {
        try
        {
            Log.i("SS"," IN THE BG OF ME TASK SENDER  ");
            helper=new MeHelper(context, MeDatabase.DB_NAME,null , MeDatabase.DB_VERSION);
            meOkHttpSendable.sendLocation();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
       // Log.i("SS"," RETURN TRUE ");

        return true;
    }

    private final List<MeUserTracker> sendLocationsToServer() throws Exception
    {
        List<MeUserTracker> userTrackers = new ArrayList<>();
        MeRepoUserTracker repoUserTracker = new MeRepoImplUserTracker(parentActivity.getDbHelper());
        userTrackers = repoUserTracker.sendLocations();
        Log.i("@Transworld", "Contact List : " + userTrackers);
        return userTrackers;
    }
    public void showNotification()
    {

        Log.i("SS"," NOTIFICATIION ON ");
        // define sound URI, the sound to be played when there's a notification
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        // intent triggered, you can add other intent for other actions
        Intent intent = new Intent(parentActivity, MeTaskSender.class);
        PendingIntent pIntent = PendingIntent.getActivity(parentActivity, 0, intent, 0);
        Notification mNotification = new Notification.Builder(parentActivity)
                .setContentTitle("SmartSuite!")
                .setContentText("Locations uploaded successfully!")
                .setSmallIcon(R.mipmap.ic_transworld)
                .setContentIntent(pIntent)
                .setSound(soundUri)
                .build();
        NotificationManager notificationManager = (NotificationManager) parentActivity.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, mNotification);
    }
    public void showNotificationFailed()
    {

        Log.i("SS"," NOTIFICATIION OFF ");

        // define sound URI, the sound to be played when there's a notification
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        // intent triggered, you can add other intent for other actions
        Intent intent = new Intent(parentActivity, MeTaskSender.class);
        PendingIntent pIntent = PendingIntent.getActivity(parentActivity, 0, intent, 0);
        Notification mNotification = new Notification.Builder(parentActivity)
                .setContentTitle("SmartSuite!")
                .setContentText("Failed to Upload Locations!")
                .setSmallIcon(R.mipmap.ic_suite)
                .setContentIntent(pIntent)
                .setSound(soundUri)
                .build();
        NotificationManager notificationManager = (NotificationManager) parentActivity.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, mNotification);
    }

    private class MeOkHttpSendable implements MeSendable
    {
        @Override
        public Map<String, Object> sendLocation() throws Exception
        {

            Log.i("SS"," IN THE ME send able method  ");

            Map<String, Object> mapEntity = null;
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), JsonMan.fromObject(sendLocationsToServer()));
            OkHttpClient client = new OkHttpClient();
            client.readTimeoutMillis();
            client.connectTimeoutMillis();
            Log.i("@Transworld", "TimeOut " + client.connectTimeoutMillis());

            Request request = new Request.Builder()
                    .url(MeIUrl.URL_UPLOAD_LOCATIONS_TO_SERVER)
                    .post(body)
                    .addHeader("Accept", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            Log.i("@Transworld","url"+String.valueOf(request));
            String responsJson = response.body().string();
            Log.i("@Transworld", "Response Json  " + responsJson);
            mapEntity = JsonMan.parseAnything(responsJson, new TypeReference<Map<String, Object>>()
            {


            });
            Log.i("@Transworld", "Response Entity - " + mapEntity);
            String status = mapEntity.get("status").toString();
            Log.i("@Transworld", "Status :  " + status);
            if (status.equals("success"))
            {
                Log.i("SS"," LOCATION SEND   ");
                helper.updateNameStatus("updated");
                Toast.makeText(context, "Updating Locations...", Toast.LENGTH_SHORT).show();
                MeRepoUserTracker repoUserTracker = new MeRepoImplUserTracker(parentActivity.getDbHelper());
               // repoUserTracker.deleteTableData();
                showNotification();
                Toast.makeText(context, "Location Sent to the Server ", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Log.i("SS"," LOCATION SEND FAILED ");

                showNotificationFailed();
            }
            return mapEntity;
        }
    }
}
