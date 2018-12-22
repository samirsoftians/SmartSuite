package com.transworldtechnology.crm.database.repository;

import java.util.List;
import java.util.Map;

/**
 * Created by root on 22/1/16.
 */
public interface MeRepoLogin {
    void saveUserCrediantials(String userName, String password, Integer employeeCode, Integer companyMasterId, String role, Integer userTypeCode, String activeStatus, String userLevel, String companyCode, String empEmailId,String imei,String empName) throws Exception;

    void saveSpecialEmails(List<Map<String, Object>> specialEmailsList);

    List<String> getEmployeeNames() throws Exception;

    String getEmpName()throws Exception;

    String getUserName() throws Exception;

    String getPassword() throws Exception;

    Integer getEmployeeCode() throws Exception;

    Integer getCompanyMasterId() throws Exception;

    String getRole() throws Exception;

    Integer getUserTypeCode() throws Exception;

    String getActiveStatus() throws Exception;

    String getUserLevel() throws Exception;

    String getImei()throws Exception;
    String getCompanyCode() throws Exception;

    String getEmpEmailId() throws  Exception;

    Integer getFlag() throws Exception;

}
