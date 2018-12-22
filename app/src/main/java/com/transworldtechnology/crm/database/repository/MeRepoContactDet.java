package com.transworldtechnology.crm.database.repository;

import com.transworldtechnology.crm.domain.MeAddContact;

import java.util.List;



/**
 * Created by root on 27/1/16.
 */
public interface MeRepoContactDet {
    void saveContacts(List<MeAddContact> addContactList) throws Exception;

    Integer checkDataContacts() throws Exception;
    void deleteTableData() throws Exception;
    void saveCode(Integer salesCustCode,Integer custCode) throws Exception;
    void saveContactPerson(String contactPerson)throws Exception;

    void addContactsLocally(MeAddContact meAddContact) throws Exception;

    List<MeAddContact> uploadContactsToServer() throws Exception;

    void updateFlag() throws Exception;

    List<String> getContactPerson() throws Exception;
    String getCity() throws Exception;
    String getDesignation() throws Exception;
    String getAddress() throws Exception;
    Integer getFlag() throws Exception;

    Integer getContactId() throws Exception;
}
