package com.transworldtechnology.crm.database;

import com.transworldtechnology.crm.database.repository.MeRepoCheckIn;
import com.transworldtechnology.crm.database.repository.MeRepoContactDet;
import com.transworldtechnology.crm.database.repository.MeRepoImplCheckIn;
import com.transworldtechnology.crm.database.repository.MeRepoImplContactDet;
import com.transworldtechnology.crm.database.repository.MeRepoImplLogin;
import com.transworldtechnology.crm.database.repository.MeRepoImplMarketingRepMaster;
import com.transworldtechnology.crm.database.repository.MeRepoImplProspectiveCustMaster;
import com.transworldtechnology.crm.database.repository.MeRepoImplSaveFollowUp;
import com.transworldtechnology.crm.database.repository.MeRepoImplUserTracker;
import com.transworldtechnology.crm.database.repository.MeRepoLogin;
import com.transworldtechnology.crm.database.repository.MeRepoMarketingRepMaster;
import com.transworldtechnology.crm.database.repository.MeRepoProspectiveCustMaster;
import com.transworldtechnology.crm.database.repository.MeRepoSaveFollowUp;
import com.transworldtechnology.crm.database.repository.MeRepoUserTracker;

/**
 * Created by root on 22/1/16.
 */
public final class MeRepoFactory {
    public static MeRepoLogin getLoginRepository(MeHelper helper) {

        return new MeRepoImplLogin(helper);
    }

    public static MeRepoCheckIn getCheckInRepository(MeHelper helper) {

        return new MeRepoImplCheckIn(helper);

    }

    public static MeRepoProspectiveCustMaster getSearchCompanyInfo(MeHelper helper) {
        return new MeRepoImplProspectiveCustMaster(helper);
    }

    public static MeRepoContactDet getContactList(MeHelper helper) {
        return new MeRepoImplContactDet(helper);
    }

    public static MeRepoSaveFollowUp getSavedFollowUpData(MeHelper helper) {
        return new MeRepoImplSaveFollowUp(helper);
    }
    //MeRepoFollowUpDocs

    public static MeRepoMarketingRepMaster getMarketingRepMaster(MeHelper helper) {
        return new MeRepoImplMarketingRepMaster(helper);
    }
    public static MeRepoUserTracker getUserTrackerRepository(MeHelper helper){
        return new MeRepoImplUserTracker(helper);

    }
}
