package com.transworldtechnology.crm.database.repository;

import com.transworldtechnology.crm.domain.MeFollowUp;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by root on 18/2/16.
 */
public interface MeRepoSaveFollowUp {
    void saveFollowUpLocally(Map<String, Object> mapEntity) throws Exception;

    Integer checkDataFollowUp() throws Exception;
    void saveFollowUpLocallyServer(Map<String, Object> mapEntity) throws Exception;

    void saveListAppointment(List<MeFollowUp> appointmentList) throws Exception;

    void getFollowUpType(String followUpType) throws Exception;

    List<Map<String, Object>> getAppointmentsListToday(String date1, String followUpType) throws Exception;

    List<Map<String, Object>> getAppointmentsListWeekly(String date1, String followUpType) throws Exception;

    List<Map<String, Object>> getAppointmentsListMonthly(String date1, String followUpType) throws Exception;

    List<HashMap<String, Object>> getAppointmentDetails(Integer theSrno) throws Exception;

    List<Map<String, Object>> getFollowUpData();

    void updateFlag() throws Exception;

    List<File> getFileList();

    void deleteTableData();

    Integer getFlag() throws Exception;
}
