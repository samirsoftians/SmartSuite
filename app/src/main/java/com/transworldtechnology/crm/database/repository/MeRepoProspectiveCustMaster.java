package com.transworldtechnology.crm.database.repository;

import com.transworldtechnology.crm.domain.MeProspectiveCustMaster;

import java.util.List;

/**
 * Created by root on 18/2/16.
 */
public interface MeRepoProspectiveCustMaster {
    //  void saveProspectiveCustMaster();

    void saveListCompanies(List<MeProspectiveCustMaster> prospectiveCustMasterList);
    //void saveSearchCompanyInfo(Integer opportunityCode,  String companyName, Integer customerCode, Integer salesCustomerCode,Integer flag) throws Exception;

    void deleteTableData();

    void saveCompanyName(String companyName) throws Exception;


    Integer getOpportunityCode() throws Exception;

    List<String> getCompanyInfo() throws Exception;

    String getCompanyName() throws Exception;

    Integer getCustomerCode() throws Exception;

    Integer getSalesCustomerCode() throws Exception;

    Integer getFlag()throws Exception;
}
