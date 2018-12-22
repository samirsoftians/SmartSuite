package com.transworldtechnology.crm.sync;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.kevinsawicki.http.HttpRequest;
import com.transworldtechnology.crm.activity.MainActivity;
import com.transworldtechnology.crm.database.MeRepoFactory;
import com.transworldtechnology.crm.database.repository.MeRepoContactDet;
import com.transworldtechnology.crm.database.repository.MeRepoImplContactDet;
import com.transworldtechnology.crm.database.repository.MeRepoImplLogin;
import com.transworldtechnology.crm.database.repository.MeRepoImplMarketingRepMaster;
import com.transworldtechnology.crm.database.repository.MeRepoImplProspectiveCustMaster;
import com.transworldtechnology.crm.database.repository.MeRepoImplSaveFollowUp;
import com.transworldtechnology.crm.database.repository.MeRepoLogin;
import com.transworldtechnology.crm.database.repository.MeRepoMarketingRepMaster;
import com.transworldtechnology.crm.database.repository.MeRepoProspectiveCustMaster;
import com.transworldtechnology.crm.database.repository.MeRepoSaveFollowUp;
import com.transworldtechnology.crm.domain.MeAddContact;
import com.transworldtechnology.crm.domain.MeFollowUp;
import com.transworldtechnology.crm.domain.MeProspectiveCustMaster;
import com.transworldtechnology.crm.web.JsonMan;
import com.transworldtechnology.crm.web.MeIUrl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import nl.changer.polypicker.utils.AsyncTask;

/**
 * Created by root on 17/3/16.
 */
public class MeTaskSyncer extends AsyncTask<Void, Void, Boolean> {
    private Context context;
    private ProgressDialog progressDialog;
    private MainActivity parentActivity;
    private MeSyncable syncable;
    private Integer companyMasterId;
    private Integer marketingRepCode;
    private String currentDate;
    private MeOkHttpSyncer meOkHttpSyncer;

    public MeTaskSyncer(Context context) {
        this.context = context;
        this.syncable = new MeOkHttpSyncer();
        parentActivity = (MainActivity) context;
        meOkHttpSyncer = new MeOkHttpSyncer();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Long currentTime = System.currentTimeMillis();
        Log.i("@Transworld", "Current Time : " + currentTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(currentTime);
        Log.i("@Transworld", "Timezone After Format : " + sdf.format(date).toString() + " Date" + date);
        currentDate = sdf.format(date).toString();
        progressDialog = ProgressDialog.show(context, "Sync Data", "Loading...");
//        progressDialog.setMessage("Data syncing");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setIndeterminate(true);
        try {
            companyMasterId = MeRepoFactory.getLoginRepository(parentActivity.getDbHelper()).getCompanyMasterId();
            // customerCode = MeRepoFactory.getSearchCompanyInfo(parentActivity.getDbHelper()).getCustomerCode();
            //salesCustomerCode = MeRepoFactory.getSearchCompanyInfo(parentActivity.getDbHelper()).getSalesCustomerCode();
            marketingRepCode = MeRepoFactory.getLoginRepository(parentActivity.getDbHelper()).getEmployeeCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            meOkHttpSyncer.syncCompanies(companyMasterId, currentDate);
            meOkHttpSyncer.syncContacts(companyMasterId, currentDate);
            meOkHttpSyncer.syncAppointment(companyMasterId, marketingRepCode, currentDate);
            meOkHttpSyncer.syncAssignTo(companyMasterId);
            meOkHttpSyncer.syncSpecialEmails();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        progressDialog.dismiss();
    }

    private final void saveSearchCompanyInfo(List<MeProspectiveCustMaster> custMasterList) {
        Integer flag = 1;
        MeRepoProspectiveCustMaster repoSearchCompany = new MeRepoImplProspectiveCustMaster(parentActivity.getDbHelper());
        Log.i("@Transworld ", "Map List Save " + custMasterList);
        if (custMasterList != null) {
            try {
                repoSearchCompany.deleteTableData();
                repoSearchCompany.saveListCompanies(custMasterList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private final void saveSpokenTo(List<MeAddContact> addContactList) {
        Integer flag = 1;
        MeRepoContactDet repoContactDet = new MeRepoImplContactDet(parentActivity.getDbHelper()) {
        };
        Log.i("@Transworld ", "Map List Contacts " + addContactList);
        if (addContactList != null) {
            try {
                repoContactDet.deleteTableData();
                repoContactDet.saveContacts(addContactList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private final void saveAppointments(List<MeFollowUp> appointmentList) throws Exception {
        MeRepoSaveFollowUp repoSaveFollowUp = new MeRepoImplSaveFollowUp(parentActivity.getDbHelper());
        Log.i("@Transworld", "List Appointment " + appointmentList);
        if (appointmentList != null) {
            repoSaveFollowUp.deleteTableData();
            repoSaveFollowUp.saveListAppointment(appointmentList);
        }
    }

    private final void saveAssignTo(List<Map<String, Object>> assignToList) throws Exception {
        MeRepoMarketingRepMaster repoMarketingRepMaster = new MeRepoImplMarketingRepMaster(parentActivity.getDbHelper());
        Log.i("@Transworld", "List Assign To " + assignToList);
        if (assignToList != null) {
            repoMarketingRepMaster.deleteTableData();
            repoMarketingRepMaster.saveAssignToLocally(assignToList);
        }
    }

    private final void saveSpecialEmails(List<Map<String, Object>> specialEmailList) throws Exception {
        MeRepoLogin repoLogin = new MeRepoImplLogin(parentActivity.getDbHelper());
        if (specialEmailList != null) {
            repoLogin.saveSpecialEmails(specialEmailList);
        }
    }

    private final class MeOkHttpSyncer implements MeSyncable {
        @Override
        public List<MeProspectiveCustMaster> syncCompanies(Integer companyMasterId, String currentDate) throws Exception {
            List<MeProspectiveCustMaster> prospectiveCustMasterList = null;
            String urlWithPathVariable = MeIUrl.URL_SYNC_SEARCH_COMPANY + companyMasterId + "/" + currentDate;
            Log.i("@Transworld", MeIUrl.URL_SYNC_SEARCH_COMPANY + companyMasterId + "/" + currentDate);
            String jsonList = HttpRequest.get(urlWithPathVariable).accept("application/json").body();
            Log.i("@Transworld", "JSON - " + jsonList);
            Map<String, List<MeProspectiveCustMaster>> custMasterList = JsonMan.parseAnything(jsonList, new TypeReference<Map<String, List<MeProspectiveCustMaster>>>() {
            });
            if (custMasterList != null) {
                prospectiveCustMasterList = custMasterList.get("companies");
                Log.i("@Transworld", "CustMasterObject" + custMasterList);
                Log.i("@Transworld", "prospectiveCustMasterList" + prospectiveCustMasterList);
                saveSearchCompanyInfo(prospectiveCustMasterList);
            }
            return prospectiveCustMasterList;
        }

        @Override
        public List<MeAddContact> syncContacts(Integer companyMasterId, String currentDate) throws Exception {
            List<MeAddContact> addContactList = null;
            String urlWithPathVariable = MeIUrl.URL_SYNC_ADD_CONTACT_SPOKEN_TO + companyMasterId + "/" + currentDate;
            Log.i("@Transworld", MeIUrl.URL_SYNC_ADD_CONTACT_SPOKEN_TO + companyMasterId + "/" + currentDate);
            String jsonList = HttpRequest.get(urlWithPathVariable).accept("application/json").body();
            Log.i("@Transworld", "JSON - " + jsonList);
            Map<String, List<MeAddContact>> list = JsonMan.parseAnything(jsonList, new TypeReference<Map<String, List<MeAddContact>>>() {
            });
            if (list != null) {
                addContactList = list.get("spokenTo");
                Log.i("@Transworld", "CustMasterObject" + list);
                Log.i("@Transworld", "prospectiveCustMasterList" + addContactList);
                saveSpokenTo(addContactList);
            }
            return addContactList;
        }

        @Override
        public List<MeFollowUp> syncAppointment(Integer companyMasterId, Integer marketingRepCode, String currentDate) throws Exception {
            List<MeFollowUp> appointmentList = null;
            String urlWithPathVariable = MeIUrl.URL_SYNC_APPOINTMENT + companyMasterId + "/" + marketingRepCode + "/" + currentDate;
            Log.i("@Transworld", MeIUrl.URL_SYNC_APPOINTMENT + companyMasterId + "/" + marketingRepCode + "/" + currentDate);
            String jsonList = HttpRequest.get(urlWithPathVariable).accept("application/json").body();
            Log.i("@Transworld", "JSON - " + jsonList);
            Map<String, List<MeFollowUp>> list = JsonMan.parseAnything(jsonList, new TypeReference<Map<String, List<MeFollowUp>>>() {
            });
            if (list != null) {
                appointmentList = list.get("appointment");
                Log.i("@Transworld", "Follow Up Object " + list);
                Log.i("@Transworld", "Appointment List " + appointmentList);
                saveAppointments(appointmentList);
            }
            return appointmentList;
        }

        @Override
        public List<Map<String, Object>> syncAssignTo(Integer companyMasterId) throws Exception {
            List<Map<String, Object>> listAssignToMap = new ArrayList<>();
            String urlWithPathVariable = MeIUrl.URL_SYNC_ASSIGN_TO + companyMasterId;
            Log.i("@Transworld", MeIUrl.URL_SYNC_ASSIGN_TO + companyMasterId);
            String jsonMap = HttpRequest.get(urlWithPathVariable).accept("application/json").body();
            Log.i("@Transworld", "Assign JSON - " + jsonMap);
            Map<String, Object> mapEntity = JsonMan.parseAnything(jsonMap, new TypeReference<Map<String, Object>>() {
            });
            Log.i("@Transworld", "Map Entity - " + mapEntity);
            listAssignToMap = (List<Map<String, Object>>) mapEntity.get("AssignTo");
            Log.i("@Transworld", "List Assign To  To - " + listAssignToMap);
            saveAssignTo(listAssignToMap);
            return listAssignToMap;
        }

        @Override
        public List<Map<String, Object>> syncSpecialEmails() throws Exception {
            List<Map<String, Object>> listSpecialEmailsList = new ArrayList<>();
            String url = MeIUrl.URL_SYNC_SPECIAL_EMAILS;
            Log.i("@Transworld", MeIUrl.URL_SYNC_SPECIAL_EMAILS);
            String jsonMap = HttpRequest.get(url).accept("application/json").body();
            Log.i("@Transworld", "Special Email JSON - " + jsonMap);
            Map<String, Object> mapEntity = JsonMan.parseAnything(jsonMap, new TypeReference<Map<String, Object>>() {
            });
            Log.i("@Transworld", "MapEntity - " + mapEntity);
            listSpecialEmailsList = (List<Map<String, Object>>) mapEntity.get("specialEmails");
            Log.i("@Transworld", "List SpecialEmails - " + listSpecialEmailsList);
            saveSpecialEmails(listSpecialEmailsList);
            return null;
        }
    }
}
