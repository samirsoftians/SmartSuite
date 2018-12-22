package com.transworldtechnology.crm.database.repository;


/**
 * Created by twtech on 21/12/17.
 */

    public interface MeRepoCheckIn {

        void saveUserDetails(String userName, Integer uId, String clientName, String comments, String status, String dateTime, Double lat, Double lng, String updateStatus) throws Exception;
       // List<MeCheckIn> sendLocations() throws Exception;
        String getuserName() throws Exception;

        Integer getuId() throws Exception;

        String getclientName() throws Exception;

        String getcomments() throws Exception;

        String getstatus() throws Exception;

        String getdateTime() throws Exception;

        Double getlat() throws Exception;

        Double getlng() throws Exception;

        String updateStatus() throws Exception;

    }
