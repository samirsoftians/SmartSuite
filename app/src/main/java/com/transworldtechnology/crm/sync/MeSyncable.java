package com.transworldtechnology.crm.sync;

import com.transworldtechnology.crm.domain.MeAddContact;
import com.transworldtechnology.crm.domain.MeFollowUp;
import com.transworldtechnology.crm.domain.MeProspectiveCustMaster;

import java.util.List;
import java.util.Map;

/**
 * Created by root on 17/3/16.
 */
public interface MeSyncable {
    List<MeProspectiveCustMaster> syncCompanies(Integer companyMasterId, String currentDate) throws Exception;

    List<MeAddContact> syncContacts(Integer companyMasterId, String currentDate) throws Exception;

    List<MeFollowUp> syncAppointment(Integer companyMasterId, Integer marketingRepCode, String currentDate) throws Exception;

    List<Map<String, Object>> syncAssignTo(Integer companyMasterId) throws Exception;

    List<Map<String, Object>> syncSpecialEmails() throws Exception;
}
